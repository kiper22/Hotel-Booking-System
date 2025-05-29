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

    @Override
    public List<Room> getAvailableRooms(Date start, Date end, RoomType type, int guests) throws RemoteException {
        return ;
    }


    @Override
    public boolean bookRoom(int roomId, String name, String surname, String phoneNumber, Date start, Date stop) throws RemoteException {
        return ;
    }

    @Override
    public boolean cancelBooking(int bookingId, String name, String surname, String phoneNumber) throws RemoteException {
        return ;
    }

    @Override
    public List<Booking> getBookings() throws RemoteException {
        return ;
    }

    @Override
    public List<Booking> getBookingsForUser(String name, String surname, String phoneNumber) throws RemoteException {
        return ;
    }


    @Override
    public List<Room> sortRoomsByPrice(boolean ascending) throws RemoteException {
        return ;
    }

    @Override
    public List<Room> filterRoomsByOccupancy(int minOccupancy, int maxOccupancy) throws RemoteException {
        return ;
    }

}
