package hotelService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Hotel implements Serializable {
    private int hotelId;
    private String name;
    private String address;
    private List<Room> rooms;

    public Hotel(int hotelId, String name, String address){
        this.hotelId = hotelId;
        this.name = name;
        this.address = address;
        this.rooms = new ArrayList<>();
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void addRoom(Room room){
        rooms.add(room);
    }

    public boolean removeRoom(int roomId){
        for (int i = 0; i < rooms.size(); i++) {
            if(rooms.get(i).getRoomId() == roomId){
                rooms.remove(i);
                return true;
            }
        }
        return false;
    }

    public List<Room> findAvaliableRooms(){
        List<Room> avaliableRooms = new ArrayList<>();
        for (Room room : rooms){
            if(room.isAvaliable()){
                avaliableRooms.add(room);
            }
        }
        return  avaliableRooms;
    }
}