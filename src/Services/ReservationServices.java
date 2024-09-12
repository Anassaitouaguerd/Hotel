package Services;

import Models.Reservation;
import Repository.ReservationRepository;
import Repository.RoomRepository;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class ReservationServices {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");





    public void createReservation(String clientName,LocalDate checkInDate, LocalDate checkOutDate) {
           int roomId = RoomRepository.getRoomsAvailable();
           new ReservationRepository().addNewReservation(new Reservation(clientName, checkInDate, checkOutDate , roomId));
        }

    public void displayReservations() {
        ReservationRepository reserv = new ReservationRepository();
        for(Reservation res : reserv.getAllReservations()){
//            System.out.println("id: " + res.getId());
            System.out.println("client name: " + res.getClientName());
            System.out.println("check in date : " + res.getCheckInDate());
            System.out.println("check out date : " + res.getCheckOutDate());
            System.out.println("number room : " + res.getRoomNumber());
        }
    }
    public void updateReservation(){
        // 1 - get all id and number rooms of all reservations
        List<Reservation> reservations = new ReservationRepository().getAllReservations();
        for (Reservation res : reservations){
            System.out.println("Reservation room number: " + res.getRoomNumber());
        }
        int choice = 0;
        int check = 0;
        while (choice == 0) {
            System.out.println("Enter the room number you want to update: ");
            Scanner scanner = new Scanner(System.in);
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
            for (Reservation res : reservations) {
                if (res.getRoomNumber() == choice) {
                    System.out.println("Enter the new client name: ");
                    String clientName = scanner.nextLine();
                    System.out.println("Enter the new check-in date (yyyy-MM-dd): ");
                    String checkInDateString = scanner.nextLine();
                    LocalDate checkInDate = LocalDate.parse(checkInDateString, DATE_FORMATTER);
                    System.out.println("Enter the new check-out date (yyyy-MM-dd): ");
                    String checkOutDateString = scanner.nextLine();
                    LocalDate checkOutDate = LocalDate.parse(checkOutDateString, DATE_FORMATTER);
                    new ReservationRepository().updateReservation(new Reservation(clientName, checkInDate, checkOutDate, choice), choice);
                    check = 1;
                    break;
                }
            }
            if (check == 0) {
                System.out.println("Room number not found. Please enter a valid room number.");
                choice = 0;
            }
        }

    }
//    public void getOneReservation(int reservationId){
//
//    }
//    public void editReservation(String clientName , LocalDate checkInDate, LocalDate checkOutDate , int roomId){
//        ReservationRepository updateReservation = new ReservationRepository();
//        updateReservation.updateReservation(new Reservation(clientName , checkInDate,  checkOutDate) , roomId);
//    }
}
