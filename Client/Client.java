package Client;

import hotelService.Booking;
import hotelService.HotelService;
import hotelService.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.Naming;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Client extends JFrame {

    private HotelService hotelService;
    private JTextArea output;

    public Client() {
        setTitle("Hotel Booking System");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        output = new JTextArea();
        output.setFont(new Font("Dialog", Font.PLAIN, 14));
        output.setEditable(false);
        add(new JScrollPane(output), BorderLayout.CENTER);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

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

    private void showUserBookings() {
        try {
            String name = JOptionPane.showInputDialog(this, "Imię:");
            String surname = JOptionPane.showInputDialog(this, "Nazwisko:");
            String phone = JOptionPane.showInputDialog(this, "Telefon:");

            List<Booking> bookings = hotelService.getBookingsForUser(name, surname, phone);

            output.setText("");
            if (bookings.isEmpty()) {
                output.append("Brak rezerwacji.\n");
            } else {
                for (Booking booking : bookings) {
                    output.append(booking.info() + "\n");
                }
            }
        } catch (Exception ex) {
            showError("Błąd pobierania rezerwacji: " + ex.getMessage());
        }
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Informacja", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Błąd", JOptionPane.ERROR_MESSAGE);
    }

//    private void showFilterRoomsDialog() {
//        JDialog dialog = new JDialog(this, "Filtruj dostępne pokoje", true);
//        dialog.setSize(400, 300);
//        dialog.setLayout(new BorderLayout());
//
//        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
//
//        form.add(new JLabel("Data początkowa (dd-MM-yyyy):"));
//        JTextField startDateField = new JTextField();
//        form.add(startDateField);
//
//        form.add(new JLabel("Data końcowa (dd-MM-yyyy):"));
//        JTextField endDateField = new JTextField();
//        form.add(endDateField);
//
//        form.add(new JLabel("Liczba gości:"));
//        JTextField guestsField = new JTextField();
//        form.add(guestsField);
//
//        dialog.add(form, BorderLayout.CENTER);
//
//        JButton btnSearch = new JButton("Szukaj");
//        dialog.add(btnSearch, BorderLayout.SOUTH);
//
//        btnSearch.addActionListener(e -> {
//            try {
//                String startStr = startDateField.getText().trim();
//                String endStr = endDateField.getText().trim();
//                String guestsStr = guestsField.getText().trim();
//
//                Date start = null, end = null;
//                if (!startStr.isEmpty()) {
//                    start = new SimpleDateFormat("dd-MM-yyyy").parse(startStr);
//                }
//                if (!endStr.isEmpty()) {
//                    end = new SimpleDateFormat("dd-MM-yyyy").parse(endStr);
//                }
//
//                final int guests;
//                if (!guestsStr.isEmpty()) {
//                    guests = Integer.parseInt(guestsStr);
//                } else {
//                    guests = 0;
//                }
//
//                List<Room> rooms = hotelService.getAvailableRooms(start, end, guests);
//
//                output.setText("");
//                if (rooms.isEmpty()) {
//                    output.append("Brak dostępnych pokoi.\n");
//                } else {
//                    for (Room r : rooms) {
//                        output.append(r.info() + "\n");
//                    }
//                }
//
//                dialog.dispose();
//
//            } catch (Exception ex) {
//                showError("Błąd filtrowania pokoi: " + ex.getMessage());
//            }
//        });
//
//        dialog.setLocationRelativeTo(this);
//        dialog.setVisible(true);
//    }

    private void showFilterRoomsDialog() {
        JDialog dialog = new JDialog(this, "Filtruj dostępne pokoje", true);
        dialog.setSize(800, 400);  // większa szerokość na potrzeby tabeli
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));

        form.add(new JLabel("Data początkowa (dd-MM-yyyy):"));
        JTextField startDateField = new JTextField();
        form.add(startDateField);

        form.add(new JLabel("Data końcowa (dd-MM-yyyy):"));
        JTextField endDateField = new JTextField();
        form.add(endDateField);

        form.add(new JLabel("Liczba gości:"));
        JTextField guestsField = new JTextField();
        form.add(guestsField);

        dialog.add(form, BorderLayout.NORTH);

        // Tabela na wyniki
        String[] columnNames = {"ID", "Room type", "Description", "Max occupancy", "Price per night"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable roomTable = new JTable(tableModel);
        roomTable.setEnabled(false);  // tylko do odczytu
        roomTable.setRowHeight(24);
        roomTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(roomTable);
        dialog.add(scrollPane, BorderLayout.CENTER);

        JButton btnSearch = new JButton("Szukaj");
        dialog.add(btnSearch, BorderLayout.SOUTH);

        btnSearch.addActionListener(e -> {
            try {
                String startStr = startDateField.getText().trim();
                String endStr = endDateField.getText().trim();
                String guestsStr = guestsField.getText().trim();

                Date start = null, end = null;
                if (!startStr.isEmpty()) {
                    start = new SimpleDateFormat("dd-MM-yyyy").parse(startStr);
                }
                if (!endStr.isEmpty()) {
                    end = new SimpleDateFormat("dd-MM-yyyy").parse(endStr);
                }

                final int guests;
                if (!guestsStr.isEmpty()) {
                    guests = Integer.parseInt(guestsStr);
                } else {
                    guests = 0;
                }

                List<Room> rooms = hotelService.getAvailableRooms(start, end, guests);

                // Czyścimy poprzednie wyniki
                tableModel.setRowCount(0);

                if (rooms.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Brak dostępnych pokoi.", "Informacja", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    for (Room room : rooms) {
                        Object[] rowData = {
                                room.getRoomId(),
                                room.getRoomType(),
                                room.getDescription(),
                                room.getMaxOccupancy(),
                                room.getPricePerNight()
                        };
                        tableModel.addRow(rowData);
                    }
                }

            } catch (Exception ex) {
                showError("Błąd filtrowania pokoi: " + ex.getMessage());
            }
        });

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
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


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Client().setVisible(true));
    }
}
