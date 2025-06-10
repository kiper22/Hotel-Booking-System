package hotelServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Calendar;
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

        hotel.addRoom(new Room(101, RoomType.SINGLE_BED, "Pokój jednoosobowy", 1, 100.0));
        hotel.addRoom(new Room(102, RoomType.DOUBLE_BED, "Pokój dwuosobowy", 2, 180.0));
        hotel.addRoom(new Room(103, RoomType.FAMILY, "Pokój rodzinny", 4, 300.0));

        hotels.add(hotel);
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


    private boolean isRoomBooked(int roomId, Date start, Date end) {
        for (Booking booking : bookings) {
            if (booking.getRoomId() == roomId && datesOverlap(booking.getStartDate(), booking.getEndDate(), start, end)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Room> getAvailableRooms() throws RemoteException {
        List<Room> availableRooms = new ArrayList<>();
        for (Hotel hotel : hotels) {
            availableRooms.addAll(hotel.getRooms());
        }
        return availableRooms;
    }

    @Override
    public List<Room> getAvailableRooms(Date start, Date end, int guests) throws RemoteException {
        List<Room> availableRooms = new ArrayList<>();

        for (Hotel hotel : hotels) {
            for (Room room : hotel.getRooms()) {

                boolean fitsGuests = guests <= 0 || room.getMaxOccupancy() >= guests;
                boolean isFree = true;

                if (start != null && end != null) {
                    isFree = !isRoomBooked(room.getRoomId(), start, end);
                }

                if (fitsGuests && isFree) {
                    availableRooms.add(room);
                }
            }
        }

        return availableRooms;
    }

    @Override
    public List<Room> getAvailableRoomsWithoutAnyBooking() throws RemoteException {
        List<Room> availableRooms = new ArrayList<>();

        for (Hotel hotel : hotels) {
            for (Room room : hotel.getRooms()) {
                boolean hasAnyBooking = false;

                for (Booking booking : bookings) {
                    if (booking.getRoomId() == room.getRoomId()) {
                        hasAnyBooking = true;
                        break;
                    }
                }

                if (!hasAnyBooking) {
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
