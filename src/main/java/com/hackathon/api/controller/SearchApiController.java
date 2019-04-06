package com.hackathon.api.controller;

import com.hackathon.api.SearchApi;
import com.hackathon.api.workflow.BookingsWorkflow;
import com.hackathon.model.BookingOffer;
import com.hackathon.model.BookingOfferResponse;
import com.hackathon.model.BookingSearchRequest;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by 424047 on 4/5/2019.
 */
@RestController
public class SearchApiController implements SearchApi {

    @Autowired
    BookingsWorkflow bookingWorkflow;

    public ResponseEntity<BookingOfferResponse> searchByGet(@NotNull@ApiParam(value = "", required = true) @RequestParam(value = "originLat", required = true) String originLat, @NotNull@ApiParam(value = "", required = true) @RequestParam(value = "originLong", required = true) String originLong, @NotNull@ApiParam(value = "", required = true) @RequestParam(value = "destinationId", required = true) String destinationId, @NotNull@ApiParam(value = "", required = true) @RequestParam(value = "departureDate", required = true) String departureDate, @NotNull@ApiParam(value = "", required = true) @RequestParam(value = "returnDate", required = true) String returnDate, @NotNull@ApiParam(value = "", required = true) @RequestParam(value = "sourceType", required = true) String sourceType){
        BookingSearchRequest bookingSearchRequest = new BookingSearchRequest();
        bookingSearchRequest.setOriginLat(originLat);
        bookingSearchRequest.setOriginLong(originLong);
        bookingSearchRequest.setDestinationId(destinationId);
        bookingSearchRequest.setSourceType(sourceType);
        bookingSearchRequest.setDepartureDate(departureDate);
        bookingSearchRequest.setReturnDate(returnDate);
        return new ResponseEntity<BookingOfferResponse>(bookingWorkflow.getBookings(bookingSearchRequest), HttpStatus.OK);
    }

    public ResponseEntity<BookingOfferResponse> searchByPost(@ApiParam(value = "BookingSearchRequest" ,required=true )  @Valid @RequestBody BookingSearchRequest body){
        return new ResponseEntity<BookingOfferResponse>(bookingWorkflow.getBookings(body), HttpStatus.OK);
    }
}
