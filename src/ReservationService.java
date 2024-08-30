import java.time.LocalDate;
import java.util.Scanner;
public class ReservationService {
    Scanner scanner = new Scanner(System.in);
    public void createReservation(){
        System.out.print("Enter check-in date (YYYY-MM-DD): ");
        LocalDate checkInDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter check-out date (YYYY-MM-DD): ");
        LocalDate checkOutDate = LocalDate.parse(scanner.nextLine());

    }
    public void modifyReservation(){
        System.out.println("Creating a edit reservation...");
    }
    public void cancelReservation(){
        System.out.println("Creating a cancel reservation...");
    }
    public void viewReservationDetails()
    {
        System.out.println("Creating a view reservation...");
    }
    public void checkRoomAvailability(){
        System.out.println("Creating a check reservation...");
    }
}
