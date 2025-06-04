package Client;

import hotelService.Booking;
import hotelService.HotelService;
import hotelService.Room;

import java.rmi.Naming;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            HotelService hotelService = (HotelService) Naming.lookup("//localhost:5555/HotelService");
            Scanner sc = new Scanner(System.in);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

            while (true){
                System.out.println("Wybierz jedną z dostępnych opcji:");
                System.out.println("Menu\n" +
                                "1. Wyświetl dostępne pokoje. \n" +
                                "2. Rezerwuj pokój. \n" +
                                "3. Anuluj rezerwacje. \n" +
                                "4. Pokaż moje rezerwacje. \n" +
                                "5. Sortuj pokoje po cenie. \n" +
                                "6. Sortuj po liczbie gości. \n"+
                                "7. Wyjście. \n");

                int choose = sc.nextInt();
                sc.nextLine();

                switch (choose){
                    case 1:
                        List<Room> rooms = hotelService.getAvailableRooms();
                        if(rooms.isEmpty()){
                            System.out.println("Brak dostępnych pokoi.");
                        } else {
                            for(Room room : rooms){
//                                System.out.println(room);
                                System.out.println(room.info());
                            }
                        }
                        break;
                    case 2:
                        System.out.println("Podaj ID pokoju:");
                        int roomID = sc.nextInt();
                        sc.nextLine();
                        System.out.println("Podaj imię:");
                        String name = sc.nextLine();
                        System.out.println("Podaj nazwisko:");
                        String surname = sc.nextLine();
                        System.out.println("Podaj numer telefonu:");
                        String phoneNumber = sc.nextLine();
                        System.out.println("Podaj date początkową (format dd-mm-yyyy):");
                        Date start = dateFormat.parse(sc.nextLine());
                        System.out.println("Podaj date końcową (format dd-mm-yyyy):");
                        Date end = dateFormat.parse(sc.nextLine());
                        boolean isBooked = hotelService.bookRoom(roomID, name, surname, phoneNumber, start, end);
                        if(isBooked){
                            System.out.println("Rezerwacja zakończona sukcesem");
                        } else {
                            System.out.println("Pokój jest już zarezerwowany");
                        }
                        break;
                    case 3:
                        System.out.println("Podaj ID rezerwacji:");
                        int bookingID = sc.nextInt();
                        sc.nextLine();
                        System.out.println("Podaj imię:");
                        String firstName = sc.nextLine();
                        System.out.println("Podaj nazwisko:");
                        String lastName = sc.nextLine();
                        System.out.println("Podaj numer telefonu:");
                        String phoneNum = sc.nextLine();

                        boolean isCanceled = hotelService.cancelBooking(bookingID, firstName, lastName, phoneNum);
                        if(isCanceled){
                            System.out.println("Rejestracja anulowana.");
                        } else {
                            System.out.println("Nie znaleziono rezerwacji.");
                        }
                        break;
                    case 4:
                        System.out.println("Podaj imię:");
                        String userName = sc.nextLine();
                        System.out.println("Podaj nazwisko:");
                        String userSurname = sc.nextLine();
                        System.out.println("Podaj numer telefonu:");
                        String userPhoneNumber = sc.nextLine();
                        List<Booking> bookings = hotelService.getBookingsForUser(userName, userSurname, userPhoneNumber);
                        if(bookings.isEmpty()){
                            System.out.println("Nie znaleziono dokonanych rejestracji.");
                        } else {
                            for (Booking booking : bookings){
                                System.out.println(booking.info());
                            }
                        }
                        break;
                    case 5:
                        System.out.println("Sortowanie po cenie rosnąco (true/false):");
                        boolean asc = sc.nextBoolean();
                        List<Room> sorted = hotelService.sortRoomsByPrice(asc);
                        if(sorted.isEmpty()){
                            System.out.println("Brak dostępnych pokoi.");
                        } else {
                            for(Room room : sorted){
                                System.out.println(room.info());
                            }
                        }
                        break;
                    case 6:
                        System.out.print("Minimalna liczba gości: ");
                        int min = sc.nextInt();
                        System.out.print("Maksymalna liczba gości: ");
                        int max = sc.nextInt();
                        List<Room> filtered = hotelService.filterRoomsByOccupancy(min, max);
                        if(filtered.isEmpty()){
                            System.out.println("Brak dostępnych pokoi.");
                        } else {
                            for (Room room : filtered){
                                System.out.println(room.info());
                            }
                        }
                        break;
                    case 7:
                        System.out.println("Wyjście.");
                        return;
                    default:
                        System.out.println("Niepoprawna opcja");
                }
            }
        } catch (Exception e) {
//            System.out.println("Client error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
