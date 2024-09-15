package UI;

import Models.Reservation;
import Models.Room;
import Repository.ReservationRepository;
import Repository.RoomRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class ReservationUI {
    public static void updateMenu(int roomNumber) {
        int choice = 0;
        Scanner scanner = new Scanner(System.in);
        int reservationId = ReservationRepository.getReservationIdByRoomNumber(roomNumber);
        final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate maxCheckOutDate = LocalDate.of(2025, 9, 15);
        LocalDate maxCheckInDate = LocalDate.of(2025, 9, 15);
        LocalDate checkInDate = null;
        LocalDate checkOutDate = null;


        while (choice == 0) {
            System.out.println("What do you want to update?");
            System.out.println("1. Update client name");
            System.out.println("2. Update check-in date");
            System.out.println("3. Update check-out date");
            System.out.println("4. Update number room");
            System.out.println("5. Update all info");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
            switch (choice) {
                case 1:
                    String ClientName = "";
                    while (ClientName.isEmpty()) {
                        System.out.println("Enter the new client name: ");
                        ClientName = scanner.nextLine();
                    }
                    ReservationRepository.updateClientName(ClientName, reservationId);
                    break;
                case 2:
                    while (checkInDate == null) {
                        System.out.println("Enter the new check-in date (yyyy-MM-dd): ");
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
                    ReservationRepository.updateCheckInDate(checkInDate, roomNumber);


                    break;
                case 3:
                    while (checkOutDate == null) {
                        System.out.println("Enter the new check-out date (yyyy-MM-dd): ");
                        String checkOutDateString = scanner.nextLine();
                        try {
                            checkOutDate = LocalDate.parse(checkOutDateString, DATE_FORMATTER);
                            LocalDate checkInDateReservation = ReservationRepository.getCheckInDateByRoomNumber(roomNumber);
                            if (checkOutDate.isBefore(LocalDate.now())) {
                                System.out.println("The check-out date cannot be in the past. Please enter a future date.");
                                checkOutDate = null;
                            } else if (checkOutDate.isBefore(checkInDateReservation)) {
                                System.out.println("The check-out date cannot be before the check-in date. Please enter a valid date.");
                                checkOutDate = null;
                            } else if (checkOutDate != null && checkOutDate.isAfter(maxCheckOutDate)) {
                                System.out.println("The check-out date cannot be after " + maxCheckOutDate + ". Please enter an earlier date.");
                                checkOutDate = null;
                            }
                        } catch (DateTimeParseException e) {
                            System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
                        }
                    }
                    ReservationRepository.updateCheckOutDate(checkOutDate, roomNumber);

                    break;
                case 4:
                    List<Room> rooms = RoomRepository.getAllRooms();
                    System.out.println("|------ All Rooms ------|");
                    for (Room room : rooms) {
                        System.out.println("Room number: " + room.getRoomNumber());
                        System.out.println("Room Type: " + room.getRoomType());
                    }
                    System.out.println("|-----------------------|");
                    int newRoomNumber = 0;
                    while (newRoomNumber == 0){
                        System.out.println("Chose the new room number: ");
                        newRoomNumber = Integer.parseInt(scanner.nextLine());
                        if (!RoomRepository.checkRoomExistByRoomNumber(newRoomNumber)){
                            System.out.println("Invalid room number. Please enter a valid room number.");
                            newRoomNumber = 0;
                        }
                    }
                    ReservationRepository.updateRoomNumber(newRoomNumber, roomNumber);

                    break;
                case 5:
                    String clientName = "";
                    while(clientName.isEmpty()) {
                        System.out.print("Client name: ");
                        clientName = scanner.nextLine();
                    }
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
                    if (RoomRepository.checkRoomExistByRoomNumber(roomNumber)){
                        new ReservationRepository().updateReservation(new Reservation(clientName, checkInDate, checkOutDate, roomNumber), roomNumber);
                    }
                    else {
                        System.out.println("Room number not found. Please enter a valid room number.");
                    }
                    break;
                case 6:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please enter a number between 1 and 4.");
                    choice = 0;
            }
        }
    }
}
