package com.hackathon.api.workflow;

import com.hackathon.model.BookingConfirmation;
import com.hackathon.model.BookingOffer;
import com.hackathon.model.BookingPurchaseRequest;
import com.hackathon.model.BookingSearchRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 424047 on 4/5/2019.
 */
@Component
public class BookingsWorkflow {

    public List<BookingOffer> getBookings(BookingSearchRequest bookingRequest){
        List<BookingOffer> bookings = new ArrayList<>();

        //allows for defaults for UI development
        if ("TEST".equalsIgnoreCase(bookingRequest.getSourceType())){
            bookings.add(getCannedBookingOffer());
        }

        return bookings;
    }

    public BookingConfirmation purchase(BookingPurchaseRequest bookingPurchaseRequest){
        BookingConfirmation bookingConfirmation = null;

        //allows for defaults for UI development
        if ("TEST".equalsIgnoreCase(bookingPurchaseRequest.getSourceType())){
            bookingConfirmation = getCannedBookingConfirmation();
        }

        return bookingConfirmation;
    }

    private BookingOffer getCannedBookingOffer(){
        BookingOffer bookingOffer = new BookingOffer();
        bookingOffer.setDepartureDate("Apr 30, 2019");
        bookingOffer.setDepartureTime("02:17pm");
        bookingOffer.setOriginAirport("ATL");
        bookingOffer.setDestinationAirport("MYR");
        bookingOffer.setFlightNumber("0045");
        bookingOffer.setOfferId("offer_id_12345");
        bookingOffer.setPrice("240.50");
        return bookingOffer;
    }

    private BookingConfirmation getCannedBookingConfirmation(){
        BookingConfirmation bookingConfirmation = new BookingConfirmation();
        bookingConfirmation.setOrderId("ABC123");
        return bookingConfirmation;
    }
}
