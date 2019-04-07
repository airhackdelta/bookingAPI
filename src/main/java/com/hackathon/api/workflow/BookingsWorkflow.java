package com.hackathon.api.workflow;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.hackathon.api.mappers.OrderAPIMapper;
import com.hackathon.model.*;
import com.hackathon.ndc.order.model.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

/**
 * Created by 424047 on 4/5/2019.
 */
@Component
public class BookingsWorkflow {

    @Autowired
    OrderAPIMapper orderAPIMapper;

    public BookingOfferResponse getBookings(BookingSearchRequest bookingSearchRequest){
        BookingOfferResponse bookingOfferResponse = new BookingOfferResponse();

        //allows for defaults for UI development
        if ("TEST".equalsIgnoreCase(bookingSearchRequest.getSourceType())){
            bookingOfferResponse.addBookingOffersItem(getCannedBookingOffer());
        } else {
            //AirShoppingRQ request = orderAPIMapper.getRequest(bookingSearchRequest);
            String xmlRequest = orderAPIMapper.getXMLRequest(bookingSearchRequest);

            //AirShoppingRS response = call(request, bookingSearchRequest.getDepartureDate());
            //System.out.println(xmlRequest);
            String xmlResponse = call(xmlRequest);
            //System.out.println(xmlResponse);

            bookingOfferResponse = orderAPIMapper.getXMLResponse(xmlResponse);
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

    private String call(String xmlRequest){
        String xmlResponse = null;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost("https://stage-apigateway.delta.com/NDC/v18.1/AirShopping");
            //postRequest.addHeader("Content-type", "application/xml");
            //postRequest.addHeader("Accept", "");
            postRequest.addHeader("Authorization", "Bearer eVqqc2AjpqR6CMq7CTfATPqIitQG");

            StringEntity userEntity = new StringEntity(xmlRequest);
            postRequest.setEntity(userEntity);

            HttpResponse httpResponse = httpClient.execute(postRequest);
            HttpEntity responseEntity = httpResponse.getEntity();
            if(responseEntity!=null) {
                xmlResponse = EntityUtils.toString(responseEntity);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return xmlResponse;
    }

    private AirShoppingRS call(AirShoppingRQ request, String dateOverride){
        AirShoppingRS response = null;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost("https://stage-apigateway.delta.com/NDC/v18.1/AirShopping");
            postRequest.addHeader("Content-type", "application/xml");
            postRequest.addHeader("Accept", "");
            postRequest.addHeader("Authorization", "Bearer mGGR6GNIhGO8svLXysYa6NVYsAxH");

            String xml = toXML(request);
            xml = xml.replace("<AirShoppingRQ>", "<AirShoppingRQ xmlns=\"http://www.iata.org/IATA/2015/00/2018.1/AirShoppingRQ\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
            //<date>1565236800000</date>
            xml = xml.replaceAll("(?<=<date>)(.*)(?=</date>)", dateOverride);
            xml = xml.replaceAll("(</?)(\\pL)", "$1\\u$2");
            xml = xml.replaceAll("iatanumber", "IATA_Number");
            xml = xml.replaceAll("iatalocation", "IATA_Location");

            StringEntity userEntity = new StringEntity(xml);
            postRequest.setEntity(userEntity);

            HttpResponse httpResponse = httpClient.execute(postRequest);
            HttpEntity responseEntity = httpResponse.getEntity();
            if(responseEntity!=null) {
                String responseString = EntityUtils.toString(responseEntity);
                response = fromXML(responseString);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private String toXML(AirShoppingRQ request){
        String xml = null;
        try {
            /*
            JAXBContext jaxbContext = JAXBContext.newInstance(AirShoppingRQ.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            jaxbMarshaller.marshal(request, baos);
            xml = baos.toString();
            */
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.setDefaultUseWrapper(false);
            xmlMapper.registerModule(new JodaModule());
            xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            xmlMapper.writeValue(baos, request);
            xml = baos.toString();
        } catch (Exception e){
            e.printStackTrace();
        }
        return xml;
    }

    private AirShoppingRS fromXML(String xml){
        AirShoppingRS response = null;
        try {
            /*
            JAXBContext jaxbContext = JAXBContext.newInstance(AirShoppingRS.class);
            Unmarshaller jaxbMarshaller = jaxbContext.createUnmarshaller();

            ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());
            response = (AirShoppingRS)jaxbMarshaller.unmarshal(bais);
            */
            XmlMapper xmlMapper = new XmlMapper();
            response = xmlMapper.readValue(xml, AirShoppingRS.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }


}
