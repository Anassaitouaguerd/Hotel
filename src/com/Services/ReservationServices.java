package com.Services;

import com.Rooms.Room;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.Scanner;

public class ReservationServices {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static class Reservation {
        public String clientName;
        public Room room;
        public String checkInDate;
        public String checkOutDate;

        public Reservation(String clientName, Room room, String checkInDate, String checkOutDate) {
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
    public void hotelReservationApp(int numberOfRooms) {
        rooms = new ArrayList<>(numberOfRooms);
        for (int i = 0; i < numberOfRooms; i++) {
            rooms.add(new Room(i + 1));
        }
        reservations = new ArrayList<>();
    }
    public ArrayList<Room> rooms;
    public ArrayList<Reservation> reservations;
    public int reservationCount = 0;



    public void createReservation(String clientName, String checkInDate, String checkOutDate) {
        for (Room room : rooms) {
            if (room.available) {
                reservations.add(new Reservation(clientName, room, checkInDate, checkOutDate));
                reservationCount++;
                System.out.println("com.Rooms.Reservation created: " + reservations.get(reservationCount - 1));
                return;
            }
        }
        System.out.println("No available rooms.");
    }

    public void cancelReservation(String clientName) {
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).clientName.equals(clientName)) {
                reservations.get(i).room.available = true ;
                reservations.remove(i);
                reservationCount--;
                System.out.println("Reservation cancelled for " + clientName);
                return;
            }
        }
        System.out.println("Reservation not found.");
    }

    public int displayReservations() {
        if (reservationCount == 0) {
            System.out.println("No reservations.");
            return 0;
        } else {
            for (int i = 0; i < reservationCount; i++) {
                System.out.println(reservations.get(i));
            }
            return 1;
        }
    }
    public void editReservations(){
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
            if (reservations.get(i).clientName.equals(clientName)){
                reservationFind = i;
            }
        }

        System.out.println("what do u need to edit :");
        System.out.println("1: name client ");
        System.out.println("2: date check in ");
        System.out.println("3: date check out ");
        System.out.println("4: edit all info ");
        int choice = Integer.parseInt(scanner.nextLine());
        switch (choice){
            case 1:
                System.out.println("entre the new name client :");
                String newClientName = scanner.nextLine();
                reservations.get(reservationFind).clientName = newClientName;
                System.out.println("the name is updated");
                break;
            case 2:
                System.out.println("New Check-in date (yyyy-MM-dd):");
                String checkInDateString  = scanner.nextLine();
                try {
                    checkInDate = LocalDate.parse(checkInDateString, DATE_FORMATTER);
                    if (checkInDate.isBefore(LocalDate.now())) {
                        System.out.println("The check-in date cannot be in the past. Please enter a future date.");
                        System.exit(0);
                    }
                    reservations.get(reservationFind).checkInDate = checkInDate.toString();
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
                    reservations.get(reservationFind).checkOutDate = checkOutDate.toString();
                    System.out.println("the date check-out is updated");
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
                }
                break;
            case 4:
                System.out.println("entre new name client :");
                String newNameClient = scanner.nextLine();
                System.out.println("entre new Check-in date : ");
                String newCheckInDateString = scanner.nextLine();
                System.out.println("entre new Check-out date : ");
                String newCheckOutDateString = scanner.nextLine();
                // Edit name client
                reservations.get(reservationFind).clientName = newNameClient;
                // Edit Check - in date
                try {
                    checkInDate = LocalDate.parse(newCheckInDateString, DATE_FORMATTER);
                    if (checkInDate.isBefore(LocalDate.now())) {
                        System.out.println("The check-in date cannot be in the past. Please enter a future date.");
                        System.exit(0);
                    }
                    reservations.get(reservationFind).checkInDate = checkInDate.toString();
                    System.out.println("the date check-in is updated");


                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
                }
                // Edit Check - out date
                try {
                    if (checkOutDate.isBefore(LocalDate.now())) {
                        System.out.println("The check-out date cannot be in the past. Please enter a future date.");
                        System.exit(0);
                    }
                    reservations.get(reservationFind).checkOutDate = checkOutDate.toString();
                    System.out.println("the date check-out is updated");
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
                }
                break;
        }

    }
}
