package hotelServer;

import hotelService.HotelService;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Server {
    public static void main(String[] args) {
        try{
            HotelService remoteObject = new HotelServiceImpl();
            LocateRegistry.createRegistry(5555);
            Naming.bind("//localhost:5555/HotelService", remoteObject);
            System.out.println("Server ready");
        } catch (Exception e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}