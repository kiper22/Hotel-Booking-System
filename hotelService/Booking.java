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

    public int getbookingId() {
        return bookingId;
    }

    public int getroomId() {
        return roomId;
    }

    public String getcustomerName (){
        return customerName;
    }

    public String getcustomerSurname() {
        return customerSurname;
    }

    public String getcustomerPhone (){
        return customerPhone;
    }

    public Date getstartDate() {
        return startDate;
    }

    public Date getendDate (){
        return endDate;
    }

    public double gettotalPrice() {
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
