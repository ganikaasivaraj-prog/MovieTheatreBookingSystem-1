
import java.sql.*;
import java.math.BigDecimal;

public class CancellationDAO {

    public void cancelBooking(int bookingId, String reason) {

        String checkQuery =
            "SELECT b.booking_status, p.amount, p.payment_status " +
            "FROM bookings b " +
            "JOIN payments p ON b.booking_id = p.booking_id " +
            "WHERE b.booking_id = ?";

        String updateQuery =
            "UPDATE bookings SET booking_status = 'Cancelled' " +
            "WHERE booking_id = ? AND booking_status = 'Confirmed'";

        String insertQuery =
            "INSERT INTO cancellations " +
            "(booking_id, reason, refund_amount, refund_status) " +
            "VALUES (?, ?, ?, 'Pending')";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try {
                BigDecimal refundAmount;

                // Check booking and payment
                try (PreparedStatement ps = con.prepareStatement(checkQuery)) {

                    ps.setInt(1, bookingId);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()) {
                            System.out.println(
                                "Booking or payment record not found!");
                            con.rollback();
                            return;
                        }

                        String status = rs.getString("booking_status");
                        String paymentStatus = rs.getString("payment_status");

                        if (!"Confirmed".equalsIgnoreCase(status)) {
                            System.out.println(
                                "Booking is already cancelled or not confirmed!");
                            con.rollback();
                            return;
                        }

                        if (!"Paid".equalsIgnoreCase(paymentStatus)) {
                            System.out.println(
                                "Only paid bookings can be cancelled!");
                            con.rollback();
                            return;
                        }

                        refundAmount = rs.getBigDecimal("amount");
                    }
                }

                // Update booking status
                try (PreparedStatement ps = con.prepareStatement(updateQuery)) {

                    ps.setInt(1, bookingId);

                    if (ps.executeUpdate() == 0) {
                        System.out.println("Booking cancellation failed!");
                        con.rollback();
                        return;
                    }
                }

                // Insert cancellation record
                try (PreparedStatement ps = con.prepareStatement(insertQuery)) {

                    ps.setInt(1, bookingId);
                    ps.setString(2, reason);
                    ps.setBigDecimal(3, refundAmount);

                    ps.executeUpdate();
                }

                con.commit();

                System.out.println("\n===== CANCELLATION SUCCESSFUL =====");
                System.out.println("Booking ID: " + bookingId);
                System.out.println("Reason: " + reason);
                System.out.println("Refund Amount: Rs. " + refundAmount);
                System.out.println("Refund Status: Pending");

            } catch (SQLException e) {
                con.rollback();
                throw e;
            }

        } catch (SQLException e) {
            System.out.println("Cancellation error: " + e.getMessage());
        }
    }
}
