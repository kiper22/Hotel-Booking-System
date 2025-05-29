package hotelServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hotelService.Booking;
import hotelService.Hotel;
import hotelService.HotelService;
import hotelService.Room;
import hotelService.enums.RoomType;

public class HotelServiceImpl extends UnicastRemoteObject implements HotelService {

    private List<Hotel> hotels;
    private List<Booking> bookings;
    private int bookingCounter = 0;

    public HotelServiceImpl() throws RemoteException {
        super();
        this.hotels = new ArrayList<>();
        this.bookings = new ArrayList<>();

        // dane wejściowe
        Hotel hotel = new Hotel(1, "Hotel RMI", "ul. Przykładowa 1");

        hotel.addRoom(new Room(101, RoomType.SINGLE_BED, true, "Pokój jednoosobowy", 1, 100.0));
        hotel.addRoom(new Room(102, RoomType.DOUBLE_BED, true, "Pokój dwuosobowy", 2, 180.0));
        hotel.addRoom(new Room(103, RoomType.FAMILY, true, "Pokój rodzinny", 4, 300.0));

        hotels.add(hotel);
    }

    @Override
    public List<Room> getAvailableRooms() throws RemoteException {
        List<Room> availableRooms = new ArrayList<>();
        for (Hotel hotel : hotels) {
            availableRooms.addAll(hotel.findAvaliableRooms());
        }
        return availableRooms;
    }


    private boolean isRoomBooked(int roomId, Date start, Date end) {
        for (Booking booking : bookings) {
            if (booking.getRoomId() == roomId && booking.getEndDate().after(start) && booking.getStartDate().before(end)){
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Room> getAvailableRooms(Date start, Date end, RoomType type, int guests) throws RemoteException {
        List<Room> availableRooms = new ArrayList<>();
        for (Hotel hotel : hotels) {
            for (Room room : hotel.getRooms()){
                boolean isFree = room.isAvaliable() && room.getRoomType() == type && room.getMaxOccupancy() >= guests && !isRoomBooked(room.getRoomId(), start, end);
                if (isFree) {
                    availableRooms.add(room);
                }
            }
        }
        return availableRooms;
    }


    @Override
    public boolean bookRoom(int roomId, String name, String surname, String phoneNumber, Date start, Date stop) throws RemoteException {
        if (isRoomBooked(roomId, start, stop)) {
            return false;
        }

        double pricePerNight = 0.0;

        for (Hotel hotel : hotels) {
            for (Room room : hotel.getRooms()) {
                if (room.getRoomId() == roomId) {
                    pricePerNight = room.getPricePerNight();
                    room.setAvaliable(false);
                }
            }
        }

        long milliseconds = stop.getTime() - start.getTime();
        int numberOfDays = (int) Math.ceil(milliseconds / (1000.0 * 60 * 60 * 24));
        double totalPrice = pricePerNight * numberOfDays;

        Booking newBooking = new Booking(bookingCounter++, roomId, name, surname, phoneNumber, start, stop, totalPrice);
        bookings.add(newBooking);

        return true;
    }

    @Override
    public boolean cancelBooking(int bookingId, String name, String surname, String phoneNumber) throws RemoteException {
        for (int i = 0; i < bookings.size(); i++) {
            Booking b = bookings.get(i);
            if (b.getBookingId() == bookingId && b.getCustomerName().equals(name) && b.getCustomerSurname().equals(surname) && b.getCustomerPhone().equals(phoneNumber)) {
                for (Hotel hotel : hotels) {
                    for (Room room : hotel.getRooms()) {
                        if (room.getRoomId() == b.getRoomId()) {
                            room.setAvaliable(true);
                        }
                    }
                }
                bookings.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Booking> getBookings() throws RemoteException {
        return bookings;
    }

    @Override
    public List<Booking> getBookingsForUser(String name, String surname, String phoneNumber) throws RemoteException {
        List<Booking> result = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getCustomerName().equals(name) && b.getCustomerSurname().equals(surname) && b.getCustomerPhone().equals(phoneNumber)){
                result.add(b);
            }
        }
        return result;
    }


    @Override
    public List<Room> sortRoomsByPrice(boolean ascending) throws RemoteException {
        List<Room> allRooms = new ArrayList<>();
        for (Hotel hotel : hotels) {
            allRooms.addAll(hotel.getRooms());
        }

        allRooms.sort((a, b) -> {
            return ascending ?
                Double.compare(a.getPricePerNight(), b.getPricePerNight()) :
                Double.compare(b.getPricePerNight(), a.getPricePerNight());
        });
        return allRooms;
    }

    @Override
    public List<Room> filterRoomsByOccupancy(int minOccupancy, int maxOccupancy) throws RemoteException {
        List<Room> filtered = new ArrayList<>();
        for (Hotel hotel : hotels) {
            for (Room room : hotel.getRooms()) {
                int occ = room.getMaxOccupancy();
                if (occ >= minOccupancy && occ <= maxOccupancy) {
                    filtered.add(room);
                }
            }
        }
        return filtered;
    }

}
