package Client;

import hotelService.Booking;
import hotelService.HotelService;
import hotelService.Room;
import hotelService.RoomUpdateListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Client extends JFrame implements RoomUpdateListener {

    private HotelService hotelService;
    private JTextArea output;
    private JTable roomTable;
    private DefaultTableModel tableModel;
    private JScrollPane tableScrollPane;
    private JPanel mainPanel;
    private String priceSortOption = "brak";
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private Date lastStartDate;
    private Date lastEndDate;
    private int lastGuestCount;

    public Client() {
        setTitle("Hotel Booking System");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        mainPanel = new JPanel(new BorderLayout());
        output = new JTextArea();
        output.setEditable(false);
        output.setFont(new Font("Dialog", Font.PLAIN, 14));
        mainPanel.add(new JScrollPane(output), BorderLayout.CENTER);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        JButton btnRooms = new JButton("Dostępne pokoje");
        JButton btnBook = new JButton("Rezerwuj pokój");
        JButton btnBookings = new JButton("Moje rezerwacje");
        JButton btnCancel = new JButton("Anuluj rezerwację");
        JButton btnExit = new JButton("Wyjdź");

        panel.add(btnRooms);
        panel.add(btnBook);
        panel.add(btnBookings);
        panel.add(btnCancel);
        panel.add(btnExit);

        add(panel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);

        try {
            hotelService = (HotelService) Naming.lookup("//localhost:5555/HotelService");
            UnicastRemoteObject.exportObject(this, 0);
            hotelService.registerListener(this);
        } catch (RemoteException e) {
            showError("Błąd rejestracji listenera: " + e.getMessage());
        } catch (Exception e) {
            showError("Nie można połączyć z serwerem: " + e.getMessage());
        }

        btnRooms.addActionListener(e -> showFilterRoomsDialog());
        btnBook.addActionListener(e -> bookRoom());
        btnBookings.addActionListener(e -> showUserBookings());
        btnCancel.addActionListener(e -> cancelBooking());
        btnExit.addActionListener(e -> System.exit(0));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    hotelService.unregisterListener(Client.this);
                } catch (RemoteException ex) {
                    System.err.println("Błąd przy wyrejestrowywaniu listenera: " + ex.getMessage());
                }
            }
        });
    }

    private void showFilterRoomsDialog() {
        try {
            Date today = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(today);
            String todayStr = sdf.format(today);

            cal.add(Calendar.DAY_OF_MONTH, 1);
            String tomorrowStr = sdf.format(cal.getTime());

            String startStr = JOptionPane.showInputDialog(this, "Data początkowa (dd-MM-yyyy):", todayStr);
            String endStr = JOptionPane.showInputDialog(this, "Data końcowa (dd-MM-yyyy):", tomorrowStr);
            String guestsStr = JOptionPane.showInputDialog(this, "Liczba gości:");

            if (startStr == null || endStr == null) {
                showError("Podanie daty jest wymagane.");
                return;
            }

            Date start = sdf.parse(startStr.trim());
            Date end = sdf.parse(endStr.trim());

            lastStartDate = start;
            lastEndDate = end;

            int guests = 0;
            lastGuestCount = guests;
            if (guestsStr != null && !guestsStr.trim().isEmpty()) {
                guests = Integer.parseInt(guestsStr.trim());
                lastGuestCount = guests;
            }

            JRadioButton brak = new JRadioButton("Brak", true);
            JRadioButton rosnaco = new JRadioButton("Rosnąco");
            JRadioButton malejaco = new JRadioButton("Malejąco");

            ButtonGroup group = new ButtonGroup();
            group.add(brak);
            group.add(rosnaco);
            group.add(malejaco);

            JPanel sortPanel = new JPanel();
            sortPanel.add(new JLabel("Sortowanie po cenie: "));
            sortPanel.add(brak);
            sortPanel.add(rosnaco);
            sortPanel.add(malejaco);

            int result = JOptionPane.showConfirmDialog(this, sortPanel, "Wybierz sortowanie", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                if (rosnaco.isSelected()) {
                    priceSortOption = "rosnąco";
                } else if (malejaco.isSelected()) {
                    priceSortOption = "malejąco";
                } else {
                    priceSortOption = "brak";
                }

                updateRoomTable(start, end, guests);
            }

        } catch (Exception ex) {
            showError("Błąd filtrowania pokoi: " + ex.getMessage());
        }
    }


    private void updateRoomTable(Date start, Date end, int guests) {
        try {
            List<Room> rooms;

            if ("rosnąco".equals(priceSortOption)) {
                rooms = hotelService.sortRoomsByPrice(true, start, end, guests);
            } else if ("malejąco".equals(priceSortOption)) {
                rooms = hotelService.sortRoomsByPrice(false, start, end, guests);
            } else {
                rooms = hotelService.getAvailableRooms(start, end, guests);
            }

            if (tableScrollPane != null) {
                mainPanel.remove(tableScrollPane);
            }

            String[] columnNames = {"ID", "Room type", "Description", "Max occupancy", "Price per night", "Total price"};
            tableModel = new DefaultTableModel(columnNames, 0);
            roomTable = new JTable(tableModel);
            roomTable.setEnabled(false);
            roomTable.setRowHeight(24);
            roomTable.getTableHeader().setReorderingAllowed(false);
            tableScrollPane = new JScrollPane(roomTable);
            mainPanel.add(tableScrollPane, BorderLayout.NORTH);

            revalidate();
            repaint();

            tableModel.setRowCount(0);
            output.setText("");

            if (rooms.isEmpty()) {
                output.append("Brak dostępnych pokoi.\n");
            } else {
                long diff = end.getTime() - start.getTime();
                int days = (int) Math.ceil(diff / (1000.0 * 60 * 60 * 24));

                for (Room room : rooms) {
                    double total = room.getPricePerNight() * days;
                    tableModel.addRow(new Object[]{
                            room.getRoomId(),
                            room.getRoomType(),
                            room.getDescription(),
                            room.getMaxOccupancy(),
                            room.getPricePerNight(),
                            total
                    });
                }
            }

        } catch (Exception e) {
            showError("Błąd aktualizacji tabeli: " + e.getMessage());
        }
    }


    private void showUserBookings() {
        try {
            String name = JOptionPane.showInputDialog(this, "Imię:");
            String surname = JOptionPane.showInputDialog(this, "Nazwisko:");
            String phone = JOptionPane.showInputDialog(this, "Telefon:");

            List<Booking> bookings = hotelService.getBookingsForUser(name, surname, phone);

            if (tableScrollPane != null) {
                mainPanel.remove(tableScrollPane);
            }

            String[] columnNames = {"ID", "room ID", "From", "To", "Total price"};
            tableModel = new DefaultTableModel(columnNames, 0);
            JTable bookingTable = new JTable(tableModel);
            bookingTable.setEnabled(false);
            bookingTable.setRowHeight(24);
            bookingTable.getTableHeader().setReorderingAllowed(false);
            tableScrollPane = new JScrollPane(bookingTable);
            mainPanel.add(tableScrollPane, BorderLayout.NORTH);

            output.setText("");
            if (bookings.isEmpty()) {
                output.append("Brak rezerwacji.\n");
            } else {
                for (Booking booking : bookings) {
                    tableModel.addRow(new Object[]{
                            booking.getBookingId(),
                            booking.getRoomId(),
                            sdf.format(booking.getStartDate()),
                            sdf.format(booking.getEndDate()),
                            booking.getTotalPrice()
                    });
                }
            }

            revalidate();
            repaint();

        } catch (Exception ex) {
            showError("Błąd pobierania rezerwacji: " + ex.getMessage());
        }
    }

    private void bookRoom() {
        try {
            String idStr = JOptionPane.showInputDialog(this, "ID pokoju:");
            String name = JOptionPane.showInputDialog(this, "Imię:");
            String surname = JOptionPane.showInputDialog(this, "Nazwisko:");
            String phone = JOptionPane.showInputDialog(this, "Telefon:");
            String startDateStr = JOptionPane.showInputDialog(this, "Data początkowa (dd-MM-yyyy):");
            String endDateStr = JOptionPane.showInputDialog(this, "Data końcowa (dd-MM-yyyy):");

            int roomId = Integer.parseInt(idStr);
            Date start = new SimpleDateFormat("dd-MM-yyyy").parse(startDateStr);
            Date end = new SimpleDateFormat("dd-MM-yyyy").parse(endDateStr);

            boolean booked = hotelService.bookRoom(roomId, name, surname, phone, start, end);

            if (booked) {
                showInfo("Rezerwacja zakończona sukcesem.");
            } else {
                showInfo("Pokój jest już zarezerwowany.");
            }

        } catch (Exception ex) {
            showError("Błąd rezerwacji: " + ex.getMessage());
        }
    }

    private void cancelBooking() {
        try {
            String bookingIdStr = JOptionPane.showInputDialog(this, "ID rezerwacji:");
            String name = JOptionPane.showInputDialog(this, "Imię:");
            String surname = JOptionPane.showInputDialog(this, "Nazwisko:");
            String phone = JOptionPane.showInputDialog(this, "Telefon:");

            int bookingId = Integer.parseInt(bookingIdStr);

            boolean cancelled = hotelService.cancelBooking(bookingId, name, surname, phone);

            if (cancelled) {
                showInfo("Rezerwacja została anulowana.");
            } else {
                showInfo("Nie znaleziono rezerwacji lub dane są nieprawidłowe.");
            }

        } catch (Exception ex) {
            showError("Błąd anulowania rezerwacji: " + ex.getMessage());
        }
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Informacja", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Błąd", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Client().setVisible(true));
    }

    @Override
    public void onRoomChanged() throws RemoteException {
        SwingUtilities.invokeLater(() -> {
            output.append("Aktualizacja dostępnych pokoi.\n");

            if (lastStartDate != null && lastEndDate != null) {
                updateRoomTable(lastStartDate, lastEndDate, lastGuestCount);
            }
        });
    }
}