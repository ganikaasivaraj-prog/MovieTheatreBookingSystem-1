
import java.sql.*;

public class ReportDAO {

    public void displayTotalRevenue() {

        String sql = "SELECT SUM(amount) AS total_revenue " +
                     "FROM payments WHERE payment_status = 'Paid'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n===== TOTAL BOX OFFICE REVENUE =====");

            if (rs.next()) {
                System.out.println("Revenue: Rs. " +
                    rs.getBigDecimal("total_revenue"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void displayMovieRevenue() {

        String sql =
            "SELECT m.title, SUM(p.amount) AS revenue " +
            "FROM payments p " +
            "JOIN bookings b ON p.booking_id = b.booking_id " +
            "JOIN shows s ON b.show_id = s.show_id " +
            "JOIN movies m ON s.movie_id = m.movie_id " +
            "WHERE p.payment_status = 'Paid' " +
            "GROUP BY m.movie_id, m.title";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n===== MOVIE-WISE REVENUE =====");

            while (rs.next()) {
                System.out.println(
                    rs.getString("title") + " : Rs. " +
                    rs.getBigDecimal("revenue"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void displayBookingCount() {

        String sql =
            "SELECT COUNT(*) AS total FROM bookings";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                System.out.println("\nTotal Bookings: " +
                    rs.getInt("total"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void displayCancelledBookings() {

        String sql =
            "SELECT COUNT(*) AS total FROM cancellations";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                System.out.println("\nCancelled Bookings: " +
                    rs.getInt("total"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void displayPendingRefunds() {

        String sql =
            "SELECT SUM(refund_amount) AS total " +
            "FROM cancellations WHERE refund_status = 'Pending'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                System.out.println("\nPending Refunds: Rs. " +
                    rs.getBigDecimal("total"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
