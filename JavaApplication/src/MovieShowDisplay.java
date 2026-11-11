
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MovieShowDisplay {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/movie_theatre_db";
        String user = "root";
        String password = "Gani@2726";

        String query = "SELECT m.movie_id, m.title, m.genre, " +
                "m.language, m.duration_minutes, s.show_id, " +
                "s.screen_id, s.show_date, s.show_time, s.ticket_price " +
                "FROM movies m JOIN shows s " +
                "ON m.movie_id = s.movie_id";

        try {
            Connection con = DriverManager.getConnection(
                    url, user, password);

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            System.out.println("===== MOVIE THEATRE DETAILS =====");

            while (rs.next()) {

                System.out.println("\nMovie ID: " +
                        rs.getInt("movie_id"));

                System.out.println("Movie Name: " +
                        rs.getString("title"));

                System.out.println("Genre: " +
                        rs.getString("genre"));

                System.out.println("Language: " +
                        rs.getString("language"));

                System.out.println("Duration: " +
                        rs.getInt("duration_minutes") + " minutes");

                System.out.println("Show ID: " +
                        rs.getInt("show_id"));

                System.out.println("Screen ID: " +
                        rs.getInt("screen_id"));

                System.out.println("Show Date: " +
                        rs.getDate("show_date"));

                System.out.println("Show Time: " +
                        rs.getTime("show_time"));

                System.out.println("Ticket Price: Rs. " +
                        rs.getBigDecimal("ticket_price"));
            }

            rs.close();
            stmt.close();
            con.close();

        } catch (Exception e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }
}
