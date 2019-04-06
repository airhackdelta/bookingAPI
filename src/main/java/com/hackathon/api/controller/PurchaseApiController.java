package com.hackathon.api.controller;

import com.hackathon.api.PurchaseApi;
import com.hackathon.api.workflow.BookingsWorkflow;
import com.hackathon.model.BookingConfirmation;
import com.hackathon.model.BookingPurchaseRequest;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by 424047 on 4/5/2019.
 */
@RestController
public class PurchaseApiController implements PurchaseApi {

    @Autowired
    BookingsWorkflow bookingWorkflow;

    public ResponseEntity<BookingConfirmation> purchaseByPost(@ApiParam(value = "BookingPurchaseRequest" ,required=true )  @Valid @RequestBody BookingPurchaseRequest body) {
        return new ResponseEntity<>(bookingWorkflow.purchase(body), HttpStatus.OK);
    }

}
