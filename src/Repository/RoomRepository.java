// RoomRepository.java
package Repository;

import Models.DatabaseConnection;
import Models.Reservation;
import Models.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RoomRepository {
    public static List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String querySQL = "SELECT * FROM rooms WHERE isAvailable = true";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(querySQL)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int roomNumber = resultSet.getInt("room_number");
                String roomType = resultSet.getString("room_type");
                boolean isAvailable = resultSet.getBoolean("isAvailable");
                rooms.add(new Room(id, roomNumber,roomType, isAvailable));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }
    public static int getRoomsAvailable() {
        String querySQL = "SELECT id FROM rooms WHERE isAvailable = true ORDER BY id ASC LIMIT 1";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(querySQL)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean checkRoomHasReservation(int roomId, LocalDate checkOutDate) {
        String querySQL = "SELECT check_in_date FROM reservations WHERE room = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(querySQL)) {
            statement.setInt(1, roomId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                LocalDate checkInDate = resultSet.getDate("check_in_date").toLocalDate();
                if (checkInDate.isAfter(LocalDate.now()) && checkOutDate.isBefore(checkInDate)) {
                    return false;
                }
            }
            else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean checkAvailability(int roomId, LocalDate checkOutDate) {
        return !checkRoomHasReservation(roomId, checkOutDate);
    }

    public static int getRoomNumber(int roomId){
        String querySQL = "SELECT room_number FROM rooms WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(querySQL)) {
            statement.setInt(1, roomId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("room_number");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //        TODO:  CREATE NEW METHODE FOR CHECKING ROOM AVAILABILITY WITH USING DATES CHECK - IN AND CHECK - OUT
    //        TODO : CREATE METHODE FOR CRUD ROOMS IN DATABASE

}