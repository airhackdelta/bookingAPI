package com.hackathon.api.workflow;

import com.hackathon.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 424047 on 4/5/2019.
 */
@Component
public class BookingsWorkflow {

    public BookingOfferResponse getBookings(BookingSearchRequest bookingRequest){
        BookingOfferResponse bookingOfferResponse = new BookingOfferResponse();

        //allows for defaults for UI development
        if ("TEST".equalsIgnoreCase(bookingRequest.getSourceType())){
            bookingOfferResponse.addBookingOffersItem(getCannedBookingOffer());
        }

        return bookingOfferResponse;
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
        bookingOffer.setReturnDate("May 1, 2019");
        bookingOffer.setReturnTime("02:28pm");
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
