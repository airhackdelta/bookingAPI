package com.hackathon.api.workflow;

import com.hackathon.api.mappers.OrderAPIMapper;
import com.hackathon.model.*;
import com.hackathon.ndc.order.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by 424047 on 4/5/2019.
 */
@Component
public class BookingsWorkflow {

    @Autowired
    OrderAPIMapper orderAPIMapper;

    public BookingOfferResponse getBookings(BookingSearchRequest bookingRequest){
        BookingOfferResponse bookingOfferResponse = new BookingOfferResponse();

        //allows for defaults for UI development
        if ("TEST".equalsIgnoreCase(bookingRequest.getSourceType())){
            bookingOfferResponse.addBookingOffersItem(getCannedBookingOffer());
        } else {
            AirShoppingRQ request = orderAPIMapper.getRequest(bookingRequest);

            //make the call
            AirShoppingRS response = new AirShoppingRS();

            bookingOfferResponse = orderAPIMapper.getResponse(response);
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
