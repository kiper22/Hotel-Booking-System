package hotelService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;

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

    public boolean removeRoom(int roomId) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getRoomId() == roomId) {
                rooms.remove(i);
                return true;
            }
        }
        return false;
    }


    private boolean datesOverlap(Date start1, Date end1, Date start2, Date end2) {
        start1 = stripTime(start1);
        end1 = stripTime(end1);
        start2 = stripTime(start2);
        end2 = stripTime(end2);

        return start1.before(end2) && start2.before(end1);
    }

    private Date stripTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }


    public List<Room> findAvailableRooms(Date start, Date end, List<Booking> bookings) {
        List<Room> availableRooms = new ArrayList<>();

        for (Room room : rooms) {
            boolean isBooked = false;

            for (Booking booking : bookings) {
                if (booking.getRoomId() == room.getRoomId()) {
                    if (datesOverlap(booking.getStartDate(), booking.getEndDate(), start, end)) {
                        isBooked = true;
                        break;
                    }
                }
            }

            if (!isBooked) {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }

}