import com.Services.ReservationServices;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ReservationServices hotel = new ReservationServices();
        hotel.hotelReservationApp(10);
        String clientName = "";

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Create reservation");
            System.out.println("2. Cancel reservation");
            System.out.println("3. Display reservations");
            System.out.println("4. Edit reservations");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    LocalDate maxCheckInDate = LocalDate.of(2025, 9, 15);
                    LocalDate maxCheckOutDate = LocalDate.of(2025, 9, 15);
                    while(clientName.isEmpty()) {
                        System.out.print("Client name: ");
                         clientName = scanner.nextLine();
                    }
                    LocalDate checkInDate = null;
                    while (checkInDate == null) {
                        System.out.print("Check-in date (yyyy-MM-dd): ");
                        String checkInDateString = scanner.nextLine();
                        try {
                            checkInDate = LocalDate.parse(checkInDateString, DATE_FORMATTER);
                            if (checkInDate.isBefore(LocalDate.now())) {
                                System.out.println("The check-in date cannot be in the past. Please enter a future date.");
                                checkInDate = null;
                            }
                            if (checkInDate != null && checkInDate.isAfter(maxCheckInDate)) {
                                System.out.println("The check-in date cannot be after " + maxCheckInDate + ". Please enter an earlier date.");
                                checkInDate = null;
                            }
                        } catch (DateTimeParseException e) {
                            System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
                        }
                    }

                    LocalDate checkOutDate = null;
                    while (checkOutDate == null) {
                        System.out.print("Check-out date (yyyy-MM-dd): ");
                        String checkOutDateString = scanner.nextLine();
                        try {
                            checkOutDate = LocalDate.parse(checkOutDateString, DATE_FORMATTER);
                            if (checkOutDate.isBefore(LocalDate.now())) {
                                System.out.println("The check-out date cannot be in the past. Please enter a future date.");
                                checkOutDate = null;
                            }
                            else if(checkOutDate.isBefore(checkInDate)){
                                System.out.println("The check-out date cannot be before the check-in date. Please enter a valid date.");
                                checkOutDate = null;
                            }
                            else if (checkOutDate != null && checkOutDate.isAfter(maxCheckOutDate)) {
                                System.out.println("The check-out date cannot be after " + maxCheckOutDate + ". Please enter an earlier date.");
                                checkOutDate = null;
                            }
                        } catch (DateTimeParseException e) {
                            System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
                        }
                    }

                    hotel.createReservation(clientName, checkInDate.toString(), checkOutDate.toString());
                    break;
                case 2:
                    String cancelClientName = "";
                    while(cancelClientName.isEmpty()) {
                        System.out.print("Client name: ");
                        cancelClientName = scanner.nextLine();
                    }
                    hotel.cancelReservation(cancelClientName);
                    break;
                case 3:
                    hotel.displayReservations();
                    break;
                case 4:
                    hotel.editReservations();
                    break;
                case 5:
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
