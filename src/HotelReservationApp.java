import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;


public class HotelReservationApp {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    static class Room {
        int number;
        boolean available = true;

        Room(int number) {
            this.number = number;
        }
    }

    static class Reservation {
        String clientName;
        Room room;
        String checkInDate;
        String checkOutDate;

        Reservation(String clientName, Room room, String checkInDate, String checkOutDate) {
            this.clientName = clientName;
            this.room = room;
            this.checkInDate = checkInDate;
            this.checkOutDate = checkOutDate;
            room.available = false;
        }

        @Override
        public String toString() {
            return clientName + " in room " + room.number + " from " + checkInDate + " to " + checkOutDate;
        }
    }

    Room[] rooms;
    Reservation[] reservations;
    int reservationCount = 0;

    public HotelReservationApp(int numberOfRooms, int maxReservations) {
        rooms = new Room[numberOfRooms];
        for (int i = 0; i < numberOfRooms; i++) {
            rooms[i] = new Room(i + 1);
        }
        reservations = new Reservation[maxReservations];
    }

    void createReservation(String clientName, String checkInDate, String checkOutDate) {
        for (Room room : rooms) {
            if (room.available) {
                reservations[reservationCount++] = new Reservation(clientName, room, checkInDate, checkOutDate);
                System.out.println("Reservation created: " + reservations[reservationCount - 1]);
                return;
            }
        }
        System.out.println("No available rooms.");
    }

    void cancelReservation(String clientName) {
        for (int i = 0; i < reservationCount; i++) {
            if (reservations[i].clientName.equals(clientName)) {
                reservations[i].room.available = true;
                for (int j = i; j < reservationCount - 1; j++) {
                    reservations[j] = reservations[j + 1];
                }
                reservations[--reservationCount] = null;
                System.out.println("Reservation cancelled for " + clientName);
                return;
            }
        }
        System.out.println("Reservation not found.");
    }

    int displayReservations() {
        if (reservationCount == 0) {
            System.out.println("No reservations.");
            return 0;
        } else {
            for (int i = 0; i < reservationCount; i++) {
                System.out.println(reservations[i]);
            }
            return 1;
        }
    }
    void editReservations(){
        int reservationFind = 0;
        Scanner scanner = new Scanner(System.in);
        LocalDate checkInDate = null;
        LocalDate checkOutDate = null;
       int checkResultOfDisplay = this.displayReservations();
       if (checkResultOfDisplay == 0){
           scanner.close();
           System.exit(0);
       }
       System.out.println("entre the name of costmer do you need to edit:");
        String clientName = scanner.nextLine();
        for (int i = 0 ; i < reservationCount ; i++)
        {
            if (reservations[i].clientName.equals(clientName)){
                reservationFind = i;
            }
        }

        System.out.println("what do u need to edit :");
        System.out.println("1: name client ");
        System.out.println("2: date check in ");
        System.out.println("3: date check out ");
        int choice = Integer.parseInt(scanner.nextLine());
        switch (choice){
            case 1:
                System.out.println("entre the new name client :");
                String newClientName = scanner.nextLine();
                reservations[reservationFind].clientName = newClientName;
                System.out.println("the name is updated");
                break;
            case 2:
                System.out.println("New Check-in date (yyyy-MM-dd):");
                String checkInDateString = scanner.nextLine();
                try {
                    checkInDate = LocalDate.parse(checkInDateString, DATE_FORMATTER);
                    if (checkInDate.isBefore(LocalDate.now())) {
                        System.out.println("The check-in date cannot be in the past. Please enter a future date.");
                        System.exit(0);
                    }
                    reservations[reservationFind].checkInDate = checkInDate.toString();
                    System.out.println("the date check-in is updated");


                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
                }
                break;
            case 3:
                System.out.println("New Check-out date (yyyy-MM-dd):");
                String checkOutDateString = scanner.nextLine();
                try {
                    checkOutDate = LocalDate.parse(checkOutDateString, DATE_FORMATTER);
                    if (checkOutDate.isBefore(LocalDate.now())) {
                        System.out.println("The check-out date cannot be in the past. Please enter a future date.");
                        System.exit(0);
                    }
                    reservations[reservationFind].checkOutDate = checkOutDate.toString();
                    System.out.println("the date check-out is updated");
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
                }
                break;
        }

    }

    public static void main(String[] args) {
        HotelReservationApp hotel = new HotelReservationApp(10, 10);
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
                    System.out.print("Client name: ");
                    String clientName = scanner.nextLine();

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
                        } catch (DateTimeParseException e) {
                            System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
                        }
                    }

                    hotel.createReservation(clientName, checkInDate.toString(), checkOutDate.toString());
                    break;
                case 2:
                    System.out.print("Client name: ");
                    clientName = scanner.nextLine();
                    hotel.cancelReservation(clientName);
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
