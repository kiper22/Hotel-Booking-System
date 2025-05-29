package hotelService;

import java.io.Serializable;
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

    public String info() {
        return "Booking{" +
                "ID=" + bookingId +
                ", room=" + roomId +
                ", client=" + customerName + " " + customerSurname +
                ", phone=" + customerPhone +
                ", from=" + startDate +
                ", to=" + endDate +
                ", total=" + totalPrice +
                '}';
    }
}
