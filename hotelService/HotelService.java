// Co może wywołać klient zdalnie na serwerze
package hotelService;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

public interface HotelService extends Remote {
    // na podstawie albo daty albo lokalizacji albo obu
    List<Room> getAvailableRooms() throws RemoteException;
    
    boolean bookRoom(int roomId, String name, String surname, String phoneNumber, Date start, Date stop) throws RemoteException;

    boolean cancelBooking(int bookingId, String name, String surname, String phoneNumber) throws RemoteException;

    // moje rezerwacje
    List<Booking> getBookings() throws RemoteException;

}
