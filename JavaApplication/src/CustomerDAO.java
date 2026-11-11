
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {

    // Add a new customer
    public void addCustomer(String name, String email, String phone) {

        String sql = "INSERT INTO customers " +
                     "(customer_name, email, phone) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);

            int result = ps.executeUpdate();

            if (result > 0) {
                System.out.println("Customer registered successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Customer Error: " + e.getMessage());
        }
    }

    // Display all customers
    public void displayCustomers() {

        String sql = "SELECT * FROM customers";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n===== CUSTOMER DETAILS =====");

            while (rs.next()) {

                System.out.println("Customer ID: " +
                        rs.getInt("customer_id"));

                System.out.println("Name: " +
                        rs.getString("customer_name"));

                System.out.println("Email: " +
                        rs.getString("email"));

                System.out.println("Phone: " +
                        rs.getString("phone"));

                System.out.println("----------------------");
            }

        } catch (SQLException e) {
            System.out.println("Display Error: " + e.getMessage());
        }
    }

    // Search customer by ID
    public void searchCustomer(int customerId) {

        String sql = "SELECT * FROM customers WHERE customer_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, customerId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    System.out.println("\nCustomer Found!");
                    System.out.println("ID: " +
                            rs.getInt("customer_id"));
                    System.out.println("Name: " +
                            rs.getString("customer_name"));
                    System.out.println("Email: " +
                            rs.getString("email"));
                    System.out.println("Phone: " +
                            rs.getString("phone"));

                } else {
                    System.out.println("Customer not found!");
                }
            }

        } catch (SQLException e) {
            System.out.println("Search Error: " + e.getMessage());
        }
    }
}
