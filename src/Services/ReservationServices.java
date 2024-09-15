package Services;

import Models.Reservation;
import Models.Room;
import Repository.ReservationRepository;
import Repository.RoomRepository;
import UI.ReservationUI;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ReservationServices {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void createReservation(String clientName, LocalDate checkInDate, LocalDate checkOutDate) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select a room type:");
        System.out.println("1. Single");
        System.out.println("2. Double");
        System.out.println("3. Suite");

        int choice = 0;
        String roomType = null;
        while (roomType == null) {
            try {
                choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        roomType = "Single";
                        break;
                    case 2:
                        roomType = "Double";
                        break;
                    case 3:
                        roomType = "Suite";
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        int roomId = RoomRepository.getAvailableRoomByType(roomType, checkInDate, checkOutDate);
        if (roomId == 0) {
            System.out.println("No " + roomType + " rooms available for the selected dates.");
            return;
        }

        new ReservationRepository().addNewReservation(new Reservation(clientName, checkInDate, checkOutDate, roomId));
    }

    public void displayReservations() {
        ReservationRepository reserv = new ReservationRepository();
        for(Reservation res : reserv.getAllReservations()){
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
                if (choice == res.getRoomNumber()) {
                    check = 1;
                    ReservationUI.updateMenu(choice);
                }
            }
            if (check == 0) {
                System.out.println("Room number not found. Please enter a valid room number.");
                choice = 0;
            }
        }

    }
    public void deleteReservation() {
        List<Reservation> reservations = new ReservationRepository().getAllReservations();

        // Display all reservations
        System.out.println("Current Reservations:");
        for (Reservation res : reservations) {
            System.out.println("Room Number: " + res.getRoomNumber() + ", Client: " + res.getClientName() +
                    ", Check-in: " + res.getCheckInDate() + ", Check-out: " + res.getCheckOutDate());
        }

        Scanner scanner = new Scanner(System.in);
        int roomNumber = 0;
        boolean validInput = false;

        while (!validInput) {
            System.out.println("Enter the room number of the reservation you want to delete: ");
            try {
                roomNumber = Integer.parseInt(scanner.nextLine());
                validInput = true;

                // Check if the room number exists in reservations
                int finalRoomNumber = roomNumber;
                boolean roomFound = reservations.stream().anyMatch(res -> res.getRoomNumber() == finalRoomNumber);
                if (!roomFound) {
                    System.out.println("No reservation found for room number " + roomNumber);
                    validInput = false;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid room number.");
            }
        }

        // Get the reservation ID using the room number
        int reservationId = ReservationRepository.getReservationIdByRoomNumber(roomNumber);

        // Call the delete method from ReservationRepository
        ReservationRepository.deleteReservation(reservationId);

        System.out.println("Reservation for room " + roomNumber + " has been deleted.");
    }

    public Map<String, Double> getStatistics() {
        List<Reservation> allReservations = new ReservationRepository().getAllReservations();
        List<Room> allRooms = RoomRepository.getAllRooms();

        long totalReservations = allReservations.size();

        double totalRevenue = calculateTotalRevenue(allReservations);

        double occupancyRate = calculateOccupancyRate(allReservations, allRooms);

        Map<String, Double> stats = new HashMap<>();
        stats.put("Total Reservations", (double) totalReservations);
        stats.put("Total Revenue", totalRevenue);
        stats.put("Occupancy Rate", occupancyRate);
        return stats;
    }

    private double calculateTotalRevenue(List<Reservation> reservations) {
        return reservations.stream()
                .mapToDouble(this::calculateReservationPrice)
                .sum();
    }

    private double calculateReservationPrice(Reservation reservation) {
        Room room = RoomRepository.getRoomByNumber(reservation.getRoomNumber());
        return calculateTotalPrice(room, reservation.getCheckInDate(), reservation.getCheckOutDate());
    }

    private double calculateOccupancyRate(List<Reservation> reservations, List<Room> rooms) {
        long totalRoomNights = rooms.size() * 365; // Assuming we're calculating for a year
        long occupiedNights = reservations.stream()
                .mapToLong(res -> ChronoUnit.DAYS.between(res.getCheckInDate(), res.getCheckOutDate()))
                .sum();

        return (double) occupiedNights / totalRoomNights * 100;
    }

    private double calculateTotalPrice(Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        double basePrice = room.getPrice();
        return basePrice * nights * getSeasonMultiplier(checkInDate);
    }

    private double getSeasonMultiplier(LocalDate date) {
        int month = date.getMonthValue();
        if (month >= 6 && month <= 8) return 1.5; // Summer season
        if (month == 12 || month <= 2) return 1.2; // Winter season
        return 1.0; // Regular season
    }

    public void displayStatistics() {
        Map<String, Double> stats = getStatistics();
        System.out.println("Statistics:");
        stats.forEach((key, value) -> System.out.println(key + ": " + value));
    }


}
