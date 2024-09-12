package Models;

import java.time.LocalDate;

public class Reservation {
    private String clientName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int roomNumber;

    public Reservation(String clientName, LocalDate checkInDate, LocalDate checkOutDate , int roomNumber) {
        this.clientName = clientName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.roomNumber = roomNumber;
    }


    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
    public void setRoomNumber(int room){
        this.roomNumber = room;
    }
    public int getRoomNumber(){
        return roomNumber;
    }
}
