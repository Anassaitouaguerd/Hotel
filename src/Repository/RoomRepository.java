// RoomRepository.java
package Repository;

import Models.DatabaseConnection;
import Models.Room;

import java.sql.*;
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
                rooms.add(new Room(id, roomNumber,roomType, isAvailable , resultSet.getInt("price")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }
    public static int getAvailableRoomByType(String roomType, LocalDate checkInDate, LocalDate checkOutDate) {
        String querySQL = "SELECT r.id FROM rooms r " +
                "LEFT JOIN reservations res ON r.id = res.room " +
                "WHERE r.room_type = ? AND r.isAvailable = true " +
                "AND (res.id IS NULL OR " +
                "(res.check_out_date <= ? OR res.check_in_date >= ?)) " +
                "ORDER BY r.id ASC LIMIT 1";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(querySQL)) {

            statement.setString(1, roomType);
            statement.setDate(2, Date.valueOf(checkInDate));
            statement.setDate(3, Date.valueOf(checkOutDate));

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
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
    public static int getRoomId(int roomNumber){
        String querySQL = "SELECT id FROM rooms WHERE room_number = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(querySQL)) {
            statement.setInt(1, roomNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static boolean checkRoomExistByRoomNumber(int roomNumber){
        String querySQL = "SELECT * FROM rooms WHERE room_number = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(querySQL)) {
            statement.setInt(1, roomNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static Room getRoomByNumber(int roomNumber) {
        String querySQL = "SELECT * FROM rooms WHERE room_number = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(querySQL)) {
            statement.setInt(1, roomNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Room(
                        resultSet.getInt("id"),
                        resultSet.getInt("room_number"),
                        resultSet.getString("room_type"),
                        resultSet.getBoolean("isAvailable"),
                        resultSet.getInt("price")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //        TODO:  CREATE NEW METHODE FOR CHECKING ROOM AVAILABILITY WITH USING DATES CHECK - IN AND CHECK - OUT
    //        TODO : CREATE METHODE FOR CRUD ROOMS IN DATABASE

}