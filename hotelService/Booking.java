package hotelService;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;

public class Booking implements Serializable{
    private int bookingId;
    private int roomId;
    private String customerName;
    private String customerSurname;
    private String customerPhone;
    private Date startDate;
    private Date endDate;
    private double totalPrice;
    private LocalTime checkInTime = LocalTime.of(16, 0);
    private LocalTime checkOutTime = LocalTime.of(10, 0);

    public Booking(int bookingId, int roomId, String customerName, String customerSurname, String customerPhone,
            Date startDate, Date endDate, double totalPrice) {
        this.bookingId = bookingId;
        this.roomId = roomId;
        this.customerName = customerName;
        this.customerSurname = customerSurname;
        this.customerPhone = customerPhone;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;

    }

    public int getBookingId() {
        return bookingId;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getCustomerName (){
        return customerName;
    }

    public String getCustomerSurname() {
        return customerSurname;
    }

    public String getCustomerPhone (){
        return customerPhone;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate (){
        return endDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public LocalTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public LocalTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(LocalTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String info() {
        return "Booking{" +
                "ID=" + bookingId + '\n' +
                ", room=" + roomId + '\n' +
                ", client=" + customerName + " " + customerSurname + '\n' +
                ", phone=" + customerPhone + '\n' +
                ", from=" + startDate + '\n' +
                ", to=" + endDate + '\n' +
                ", total=" + totalPrice +
                '}';
    }
}
