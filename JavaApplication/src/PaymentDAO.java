
import java.sql.*;
import java.math.BigDecimal;
import java.util.Scanner;

public class PaymentDAO {

    public void makePayment(int bookingId, String method) {

        String bookingQuery =
            "SELECT number_of_tickets, show_id FROM bookings WHERE booking_id = ?";

        String priceQuery =
            "SELECT ticket_price FROM shows WHERE show_id = ?";

        String paymentQuery =
            "INSERT INTO payments (booking_id, amount, payment_method) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            BigDecimal totalAmount;

            // Check booking
            try (PreparedStatement ps = con.prepareStatement(bookingQuery)) {

                ps.setInt(1, bookingId);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        System.out.println("Booking ID not found!");
                        return;
                    }

                    int tickets = rs.getInt("number_of_tickets");
                    int showId = rs.getInt("show_id");

                    // Get ticket price
                    try (PreparedStatement pricePs =
                            con.prepareStatement(priceQuery)) {

                        pricePs.setInt(1, showId);

                        try (ResultSet priceRs = pricePs.executeQuery()) {

                            if (!priceRs.next()) {
                                System.out.println("Show not found!");
                                return;
                            }

                            BigDecimal price =
                                priceRs.getBigDecimal("ticket_price");

                            totalAmount =
                                price.multiply(BigDecimal.valueOf(tickets));
                        }
                    }
                }
            }

            // Insert payment
            try (PreparedStatement ps = con.prepareStatement(
                    paymentQuery, Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, bookingId);
                ps.setBigDecimal(2, totalAmount);
                ps.setString(3, method);

                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {

                    if (rs.next()) {
                        System.out.println("\n===== PAYMENT SUCCESSFUL =====");
                        System.out.println("Payment ID: " + rs.getInt(1));
                        System.out.println("Booking ID: " + bookingId);
                        System.out.println("Amount Paid: Rs. " + totalAmount);
                        System.out.println("Payment Method: " + method);
                        System.out.println("Payment Status: Paid");
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Payment error: " + e.getMessage());
        }
    }
}
