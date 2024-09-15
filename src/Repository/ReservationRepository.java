package Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Models.DatabaseConnection;
import Models.Reservation;
import java.sql.*;

public class ReservationRepository {

    // Fetch all reservations
    public List<Reservation> getAllReservations(){
        List<Reservation> reservations = new ArrayList<>();

        String querySQL = "SELECT * FROM reservations";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(querySQL)) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String clientName = resultSet.getString("client_name");

                // Parsing check-in and check-out dates as LocalDate
                LocalDate checkInDate = resultSet.getDate("check_in_date").toLocalDate();
                LocalDate checkOutDate = resultSet.getDate("check_out_date").toLocalDate();

                int roomNumber = RoomRepository.getRoomNumber(resultSet.getInt("room"));

                // Creating a new reservation object
                Reservation reservation = new Reservation(clientName, checkInDate, checkOutDate, roomNumber);
                reservations.add(reservation);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    // Add a new reservation
    public void addNewReservation(Reservation reservation) {
        String querySQL = "INSERT INTO reservations (client_name, check_in_date, check_out_date, room) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(querySQL)) {

            statement.setString(1, reservation.getClientName());
            statement.setDate(2, Date.valueOf(reservation.getCheckInDate()));
            statement.setDate(3, Date.valueOf(reservation.getCheckOutDate()));
            statement.setInt(4, reservation.getRoomNumber());

            // Execute the SQL query
            int rowsInserted = statement.executeUpdate();

            // Check if the insertion was successful
            if (rowsInserted > 0) {
                System.out.println("A new reservation was added successfully!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
// TODO: UPDATE THIS METHODE TO NEW VERSION OF CODE
    public void updateReservation(Reservation reservation , int roomId){

        String querySQL = "UPDATE reservations SET client_name = ?, check_in_date = ?, check_out_date = ?, room = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(querySQL)) {

            statement.setString(1, reservation.getClientName());
            statement.setDate(2, Date.valueOf(reservation.getCheckInDate()));
            statement.setDate(3, Date.valueOf(reservation.getCheckOutDate()));
            statement.setInt(4, reservation.getRoomNumber());
            statement.setInt(5, roomId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Reservation updated successfully!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateClientName(String clientName, int reservationId){
        String querySQL = "UPDATE reservations SET client_name = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(querySQL)) {

            statement.setString(1, clientName);
            statement.setInt(2, reservationId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Client name updated successfully!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public static void updateCheckInDate(LocalDate checkInDate, int roomNumber){
        int roomId = RoomRepository.getRoomId(roomNumber);
        String querySQL = "UPDATE reservations SET check_in_date = ? WHERE room = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(querySQL)) {

            statement.setDate(1, Date.valueOf(checkInDate));
            statement.setInt(2, roomId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Check-in date updated successfully!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateCheckOutDate(LocalDate checkOutDate, int roomNumber){
        int roomId = RoomRepository.getRoomId(roomNumber);
        String querySQL = "UPDATE reservations SET check_out_date = ? WHERE room = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(querySQL)) {

            statement.setDate(1, Date.valueOf(checkOutDate));
            statement.setInt(2, roomId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Check-out date updated successfully!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateRoomNumber(int newRoomNumber, int roomNumber){
        int roomId = RoomRepository.getRoomId(roomNumber);
        int newRoomNumberId = RoomRepository.getRoomId(newRoomNumber);
        String querySQL = "UPDATE reservations SET room = ? WHERE room = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(querySQL)) {
            statement.setInt(1, newRoomNumberId);
            statement.setInt(2, roomId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Room number updated successfully!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static int getReservationIdByRoomNumber(int roomNumber){
        int roomId = RoomRepository.getRoomId(roomNumber);
        String querySQL = "SELECT id FROM reservations WHERE room = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(querySQL)) {
            statement.setInt(1, roomId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static LocalDate getCheckInDateByRoomNumber(int roomNumber) {
        int roomId = RoomRepository.getRoomId(roomNumber);
        String querySQL = "SELECT check_in_date FROM reservations WHERE room = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(querySQL)) {
            statement.setInt(1, roomId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDate("check_in_date").toLocalDate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

//    TODO: CREATE METHODE FOR DELETE RESERVATION
public static void deleteReservation(int reservationId) {
    String querySQL = "DELETE FROM reservations WHERE id = ?";
    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(querySQL)) {

        statement.setInt(1, reservationId);

        int rowsDeleted = statement.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("Reservation deleted successfully!");
        } else {
            System.out.println("No reservation found with the given ID.");
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

}
