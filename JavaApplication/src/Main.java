


import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        CustomerDAO customer = new CustomerDAO();
        BookingDAO booking = new BookingDAO();
        PaymentDAO payment = new PaymentDAO();
        CancellationDAO cancellation = new CancellationDAO();
        ReportDAO report = new ReportDAO();

        System.out.println("\n===== MOVIE THEATRE BOOKING SYSTEM =====");
        System.out.println("1. Add Customer");
        System.out.println("2. Display Customers");
        System.out.println("3. Search Customer");
        System.out.println("4. Book Movie Tickets");
        System.out.println("5. Make Payment");
        System.out.println("6. Cancel Booking");
        System.out.println("7. Box Office Reports");

        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {

            case 1:
                System.out.print("Enter customer name: ");
                String name = sc.nextLine();

                System.out.print("Enter email: ");
                String email = sc.nextLine();

                System.out.print("Enter phone: ");
                String phone = sc.nextLine();

                customer.addCustomer(name, email, phone);
                break;

            case 2:
                customer.displayCustomers();
                break;

            case 3:
                System.out.print("Enter customer ID: ");
                int id = sc.nextInt();

                customer.searchCustomer(id);
                break;

            case 4:
                System.out.print("Enter customer ID: ");
                int customerId = sc.nextInt();

                System.out.print("Enter show ID: ");
                int showId = sc.nextInt();

                System.out.print("Enter number of tickets: ");
                int quantity = sc.nextInt();

                booking.bookTickets(customerId, showId, quantity);
                break;

            case 5:
                System.out.print("Enter booking ID: ");
                int bookingId = sc.nextInt();
                sc.nextLine();

                System.out.println("Payment Methods:");
                System.out.println("1. Cash");
                System.out.println("2. UPI");
                System.out.println("3. Card");

                System.out.print("Choose payment method: ");
                int methodChoice = sc.nextInt();

                String method;

                switch (methodChoice) {
                    case 1:
                        method = "Cash";
                        break;
                    case 2:
                        method = "UPI";
                        break;
                    case 3:
                        method = "Card";
                        break;
                    default:
                        System.out.println("Invalid payment method!");
                        sc.close();
                        return;
                }

                payment.makePayment(bookingId, method);
                break;
                case 6:
    System.out.print("Enter booking ID: ");
    int cancelBookingId = sc.nextInt();
    sc.nextLine();

    System.out.print("Enter cancellation reason: ");
    String reason = sc.nextLine();

    cancellation.cancelBooking(cancelBookingId, reason);
    break;
    case 7:
    report.displayTotalRevenue();
    report.displayMovieRevenue();
    report.displayBookingCount();
    report.displayCancelledBookings();
    report.displayPendingRefunds();
    break;

            default:
                System.out.println("Invalid choice!");
        }

        sc.close();
    }
}
