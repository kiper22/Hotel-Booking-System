package hotelService;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RoomUpdateListener extends Remote {
    void onRoomChanged() throws RemoteException;
}