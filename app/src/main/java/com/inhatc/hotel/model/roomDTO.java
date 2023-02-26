package com.inhatc.hotel.model;

import java.util.ArrayList;

public class roomDTO {
    private String room;
    private String roomPrice;
    private String roomEa;
    private String roomExplane;
    private int roomImage;
    private String roomResCheck;
    private String resDate;




    public void setRoom(String room) {
        this.room = room;
    }

    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }

    public void setRoomImage(int roomImage) { this.roomImage = roomImage; }

    public void setRoomResCheck(String roomResCheck) { this.roomResCheck = roomResCheck; }

    public void setRoomEa(String roomEa) {this.roomEa = roomEa;}

    public void setRoomExplane(String roomExplane) {this.roomExplane = roomExplane; }

    public void setResDate(String resDate) {this.resDate = resDate;}

    /////////////////////////////////////////////////////////////////////////////////////

    public String getRoom() { return room; }

    public String getRoomPrice() { return roomPrice; }

    public int getRoomImage() { return roomImage; }

    public String getRoomResCheck() {
        return roomResCheck;
    }

    public String getRoomEa() {return roomEa;}

    public String getRoomExplane() {return roomExplane;}

    public String getResDate() {return resDate;}
}
