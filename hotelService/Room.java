package hotelService;
import hotelService.enums.RoomType;

import java.io.Serializable;

public class Room implements Serializable {
    private int roomId;
    private RoomType roomType;
    private boolean isAvaliable;
    private String description;
    private int maxOccupancy;
    private double pricePerNight;

    public Room(int roomId, RoomType roomType, boolean isAvaliable, String description, int maxOccupancy, double pricePerNight){
        this.roomId = roomId;
        this.roomType = roomType;
        this.isAvaliable = isAvaliable;
        this.description = description;
        this.maxOccupancy = maxOccupancy;
        this.pricePerNight = pricePerNight;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public boolean isAvaliable(){
        return isAvaliable;
    }

    public void setAvaliable(boolean avaliable) {
        isAvaliable = avaliable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }

    public void setMaxOccupancy(int maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNightByPerson) {
        this.pricePerNight = pricePerNightByPerson;
    }


    public String info(){
        return "Room: \n" +
                "ID: " + roomId + '\n' +
                "Room type: " + roomType + '\n' +
                "Avaliable: " + isAvaliable + '\n' +
                "Description: " + description + '\n' +
                "Max occupacy: " + maxOccupancy + '\n' +
                "Price per night" + pricePerNight + '\n';
    }
}