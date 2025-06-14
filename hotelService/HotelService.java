// Co może wywołać klient zdalnie na serwerze
package hotelService;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

public interface HotelService extends Remote {
    // return all rooms or with specified filters
    List<Room> getAvailableRooms() throws RemoteException;
    List<Room> getAvailableRooms(Date start, Date end, int guests) throws RemoteException; // , RoomType type
    
    boolean bookRoom(int roomId, String name, String surname, String phoneNumber, Date start, Date stop) throws RemoteException;
    boolean cancelBooking(int bookingId, String name, String surname, String phoneNumber) throws RemoteException;

    // booking info
    List<Booking> getBookings() throws RemoteException;
    List<Booking> getBookingsForUser(String name, String surname, String phoneNumber) throws RemoteException;
    List<Room> sortRoomsByPrice(boolean ascending, Date start, Date end, int guests) throws RemoteException;

    void registerListener(RoomUpdateListener listener) throws RemoteException;
    void unregisterListener(RoomUpdateListener listener) throws RemoteException;
}