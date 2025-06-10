package Client;

import hotelService.Booking;
import hotelService.HotelService;
import hotelService.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.Naming;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ClientTemp extends JFrame {

    private HotelService hotelService;
    private JTextArea output;
    private JTable roomTable;
    private DefaultTableModel tableModel;
    private JScrollPane tableScrollPane;
    private JPanel mainPanel;

    public ClientTemp() {
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
        } catch (Exception e) {
            showError("Nie można połączyć z serwerem: " + e.getMessage());
        }

        btnRooms.addActionListener(e -> showFilterRoomsDialog());
        btnBook.addActionListener(e -> bookRoom());
        btnBookings.addActionListener(e -> showUserBookings());
        btnCancel.addActionListener(e -> cancelBooking());
        btnExit.addActionListener(e -> System.exit(0));
    }

    private void showFilterRoomsDialog() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
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

            int guests = 0;
            if (guestsStr != null && !guestsStr.trim().isEmpty()) {
                guests = Integer.parseInt(guestsStr.trim());
            }

            updateRoomTable(start, end, guests);

        } catch (Exception ex) {
            showError("Błąd filtrowania pokoi: " + ex.getMessage());
        }
    }

    private void updateRoomTable(Date start, Date end, int guests) {
        try {
            List<Room> rooms = hotelService.getAvailableRooms(start, end, guests);

            if (tableScrollPane != null) {
                mainPanel.remove(tableScrollPane);
            }

            String[] columnNames = {"ID", "Room type", "Description", "Max occupancy", "Price per night"};
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
                for (Room room : rooms) {
                    tableModel.addRow(new Object[]{
                            room.getRoomId(),
                            room.getRoomType(),
                            room.getDescription(),
                            room.getMaxOccupancy(),
                            room.getPricePerNight()
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

            String[] columnNames = {"ID", "ID pokoju", "Od", "Do", "Cena całkowita"};
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
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
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
        SwingUtilities.invokeLater(() -> new ClientTemp().setVisible(true));
    }
}