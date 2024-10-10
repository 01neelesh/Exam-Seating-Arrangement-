package org.seating;


public class Room {
    private String roomID;
    private int capacity;

    public Room(String roomID, int capacity) {
        this.roomID = roomID;
        this.capacity = capacity;

    }

    public String getRoomID() {
        return roomID;
    }

    public int getCapacity() {
        return capacity;
    }
}
