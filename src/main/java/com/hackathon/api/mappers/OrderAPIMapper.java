package com.hackathon.api.mappers;

import com.hackathon.api.utils.DateUtils;
import com.hackathon.model.BookingOfferResponse;
import com.hackathon.model.BookingSearchRequest;
import com.hackathon.ndc.order.model.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by 424047 on 4/6/2019.
 */
@Component
public class OrderAPIMapper {

    public AirShoppingRQ getRequest(BookingSearchRequest bookingSearchRequest){
        AirShoppingRQ request = new AirShoppingRQ();

        request.setParty(new PartyType());
        request.getParty().setSender(new SenderType());
        request.getParty().getSender().setTravelAgency(new TravelAgencyType());
        request.getParty().getSender().getTravelAgency().setAgencyID("HACK-TEST");
        request.getParty().getSender().getTravelAgency().setIatanumber(new BigDecimal(1234));
        request.getParty().getSender().getTravelAgency().setName("Hackathon");
        request.getParty().getSender().getTravelAgency().setPseudoCityID("BJS");

        request.setRequest(new RequestType());
        request.getRequest().setFlightRequest(new FlightRequestType());
        request.getRequest().getFlightRequest().addOriginDestRequestItem(new OriginDestType());
        request.getRequest().getFlightRequest().getOriginDestRequest().get(0).setDestArrivalRequest(new DestArrivalRequestType());
        request.getRequest().getFlightRequest().getOriginDestRequest().get(0).getDestArrivalRequest().setIatalocationCode("MYR"); //TODO: get from nearest airport
        request.getRequest().getFlightRequest().getOriginDestRequest().get(0).getDestArrivalRequest().setDate(DateUtils.getDate(bookingSearchRequest.getDepartureDate()));
        request.getRequest().getFlightRequest().getOriginDestRequest().get(0).setOriginDepRequest(new OriginDepRequestType());
        request.getRequest().getFlightRequest().getOriginDestRequest().get(0).getOriginDepRequest().setIatalocationCode("ATL"); //TODO: get from nearest airport
        request.getRequest().getFlightRequest().getOriginDestRequest().get(0).getDestArrivalRequest().setDate(DateUtils.getDate(bookingSearchRequest.getDepartureDate()));

        request.getRequest().setPaxs(new PaxsType());
        request.getRequest().getPaxs().addPaxItem(new PaxType());
        request.getRequest().getPaxs().getPax().get(0).setPaxID("1");
        request.getRequest().getPaxs().getPax().get(0).setPtc("ADT");

        request.getRequest().setResponseParameters(new ResponseParametersType());
        request.getRequest().getResponseParameters().addCurParameterItem(new CurParameterType());
        request.getRequest().getResponseParameters().getCurParameter().get(0).setCurCode("USD");

        return request;
    }

    public BookingOfferResponse getResponse(AirShoppingRS response){
        //TODO:
        return new BookingOfferResponse();
    }
}
