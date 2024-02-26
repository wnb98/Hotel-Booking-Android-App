package com.example.hotelbooking;

public class accountDataClass {

    private String hotelName;
    private int totalAmount;
    private String bookingDate;
    private int nights;



    public String getHotelName() {
        return hotelName ;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public String getBookingDate() {
        return bookingDate;
    }
    public int getNights() {
        return nights;
    }




    public accountDataClass (String hotelName, int totalAmount, int nights, String bookingDate){
        this.nights = nights;
        this.hotelName = hotelName ;
        this.totalAmount = totalAmount;
        this.bookingDate = bookingDate;
    }


}
