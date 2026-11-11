
import java.sql.*;
import java.math.BigDecimal;

public class BookingDAO {

    public void bookTickets(int customerId, int showId, int quantity) {

        if (quantity <= 0) {
            System.out.println("Invalid ticket quantity!");
            return;
        }

        String customerQuery =
            "SELECT customer_id FROM customers WHERE customer_id = ?";

        String showQuery =
            "SELECT ticket_price FROM shows WHERE show_id = ?";

        String seatQuery =
            "SELECT available_seats FROM show_seat_availability WHERE show_id = ?";

        String bookingQuery =
            "INSERT INTO bookings (customer_id, show_id, number_of_tickets) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            // Check customer
            try (PreparedStatement ps = con.prepareStatement(customerQuery)) {
                ps.setInt(1, customerId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("Customer ID not found!");
                        return;
                    }
                }
            }

            // Get ticket price
            BigDecimal ticketPrice;

            try (PreparedStatement ps = con.prepareStatement(showQuery)) {
                ps.setInt(1, showId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("Show ID not found!");
                        return;
                    }

                    ticketPrice = rs.getBigDecimal("ticket_price");
                }
            }

            // Check available seats
            try (PreparedStatement ps = con.prepareStatement(seatQuery)) {
                ps.setInt(1, showId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("Seat availability information not found!");
                        return;
                    }

                    int availableSeats = rs.getInt("available_seats");

                    if (quantity > availableSeats) {
                        System.out.println("Booking failed!");
                        System.out.println("Available seats: " + availableSeats);
                        return;
                    }
                }
            }

            // Calculate total amount
            BigDecimal totalAmount =
                ticketPrice.multiply(BigDecimal.valueOf(quantity));

            // Insert booking
            try (PreparedStatement ps = con.prepareStatement(
                    bookingQuery, Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, customerId);
                ps.setInt(2, showId);
                ps.setInt(3, quantity);

                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        System.out.println("\n===== BOOKING CONFIRMED =====");
                        System.out.println("Booking ID: " + keys.getInt(1));
                        System.out.println("Customer ID: " + customerId);
                        System.out.println("Show ID: " + showId);
                        System.out.println("Number of Tickets: " + quantity);
                        System.out.println("Ticket Price: Rs. " + ticketPrice);
                        System.out.println("Total Amount: Rs. " + totalAmount);
                        System.out.println("Booking Status: Confirmed");
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Booking error: " + e.getMessage());
        }
    }
}

