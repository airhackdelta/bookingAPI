package com.hackathon.api.mappers;

import com.hackathon.api.utils.DateUtils;
import com.hackathon.model.BookingOfferResponse;
import com.hackathon.model.BookingSearchRequest;
import com.hackathon.ndc.order.model.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        OriginDestType originDestType = new OriginDestType();
        originDestType.setDestArrivalRequest(new DestArrivalRequestType());
        originDestType.getDestArrivalRequest().setIatalocationCode("MYR"); //TODO: get from nearest airport
        originDestType.getDestArrivalRequest().setDate(DateUtils.getDate(bookingSearchRequest.getDepartureDate()));
        originDestType.setOriginDepRequest(new OriginDepRequestType());
        originDestType.getOriginDepRequest().setIatalocationCode("ATL"); //TODO: get from nearest airport
        originDestType.getDestArrivalRequest().setDate(DateUtils.getDate(bookingSearchRequest.getDepartureDate()));
        request.getRequest().getFlightRequest().addOriginDestRequestItem(originDestType);

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

        System.out.println(response.toString());
        for (AirlineOffersType airlineOffersType : response.getResponse().getOffersGroup().getCarrierOffers()){

        }
        return new BookingOfferResponse();
    }

    public String getXMLRequest(BookingSearchRequest bookingSearchRequest){
        String xml = "<AirShoppingRQ xmlns=\"http://www.iata.org/IATA/2015/00/2018.1/AirShoppingRQ\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "\t<Party>\n" +
                "\t\t<Sender>\n" +
                "\t\t\t<TravelAgency>\n" +
                "\t\t\t\t<AgencyID>HACK-TST</AgencyID>\n" +
                "\t\t\t\t<IATA_Number>1234</IATA_Number>\n" +
                "\t\t\t\t<Name>Hackathon</Name>\n" +
                "\t\t\t\t<PseudoCityID>BJS</PseudoCityID>\n" +
                "\t\t\t</TravelAgency>\n" +
                "\t\t</Sender>\n" +
                "\t</Party>\n" +
                "\t<Request>\n" +
                "\t\t<FlightRequest>\n" +
                "\t\t\t<OriginDestRequest>\n" +
                "\t\t\t\t<OriginDestID/>\n" +
                "\t\t\t\t<ConnectionCriteriaRefID/>\n" +
                "\t\t\t\t<DestArrivalRequest>\n" +
                "\t\t\t\t\t<IATA_LocationCode>REPLACE_DESTINATION</IATA_LocationCode>\n" +
                "\t\t\t\t\t<Date>REPLACE_DEPARTURE_DATE</Date>\n" +
                "\t\t\t\t</DestArrivalRequest>\n" +
                "\t\t\t\t<OriginDepRequest>\n" +
                "\t\t\t\t\t<IATA_LocationCode>REPLACE_ORIGIN</IATA_LocationCode>\n" +
                "\t\t\t\t\t<Date>REPLACE_DEPARTURE_DATE</Date>\n" +
                "\t\t\t\t</OriginDepRequest>\n" +
                "\t\t\t</OriginDestRequest>\n" +
                "\t\t</FlightRequest>\n" +
                "\t\t<Paxs>\n" +
                "\t\t\t<Pax>\n" +
                "\t\t\t\t<PaxID>PaxRefID1</PaxID>\n" +
                "\t\t\t\t<PTC>ADT</PTC>\n" +
                "\t\t\t</Pax>\n" +
                "\t\t</Paxs>\n" +
                "\t\t<ResponseParameters>\n" +
                "\t\t\t<CurParameter>\n" +
                "\t\t\t\t<CurCode>USD</CurCode>\n" +
                "\t\t\t</CurParameter>\n" +
                "\t\t</ResponseParameters>\n" +
                "\t\t<ShoppingCriteria/>\n" +
                "\t</Request>\n" +
                "</AirShoppingRQ>";

        String destinationAirport = getAirpotByAddress(bookingSearchRequest.getDestinationId());
        xml = xml.replace("REPLACE_DESTINATION", destinationAirport);
        String originAirport = getAirportByCoordinates(bookingSearchRequest.getOriginLat(), bookingSearchRequest.getOriginLong());
        System.out.println(originAirport);
        xml = xml.replace("REPLACE_ORIGIN", originAirport);
        xml = xml.replace("REPLACE_DEPARTURE_DATE", bookingSearchRequest.getDepartureDate());
        return xml;
    }

    public BookingOfferResponse getXMLResponse(String xmlResponse){

        BookingOfferResponse bookingOfferResponse = new BookingOfferResponse();

        InputSource inputXML = new InputSource( new StringReader( xmlResponse.replaceAll("xmlns=", "xmlns:not-used=") ) );

        XPath xPath = XPathFactory.newInstance().newXPath();

        try {
            String offerId = xPath.evaluate("/AirShoppingRS/Response/OffersGroup/CarrierOffers/Offer[1]/OfferID", inputXML);
            System.out.println(offerId);
        } catch (Exception e){
            e.printStackTrace();
        }

        return bookingOfferResponse;
    }

    private String getAirportByCoordinates(String lati, String longi) {

        DefaultHttpClient httpClient = new DefaultHttpClient();

        try {
            HttpGet getRequest = new HttpGet("https://airport-qa.api.aero/airport/v2/nearest/"+lati+"/"+longi+"?maxAirports=1&user_key=3035d833bb6e531654a3cce03e6b1fde");

            HttpResponse response = httpClient.execute(getRequest);
            HttpEntity responseEntity = response.getEntity();
            if(responseEntity!=null) {
                String responseString = EntityUtils.toString(responseEntity);
                System.out.println(responseString);

                Pattern p=Pattern.compile("\"iatacode\":\"([A-Z]{3})\"");
                Matcher matcher = p.matcher(responseString);
                if(matcher.find())
                    return matcher.group(1);
            }

        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    private String getAirpotByAddress(String address) {

        DefaultHttpClient httpClient = new DefaultHttpClient();

        try {
            address=address.replaceAll(" ", "+");
            HttpGet getRequest = new HttpGet("https://maps.googleapis.com/maps/api/geocode/json?address="+address+"&key=AIzaSyADktCH4LN8oCUDekSpPFZ_EQlKgA2fy0w");
            getRequest.addHeader("Content-type", "application/xml");
            getRequest.addHeader("Accept", "");

            HttpResponse response = httpClient.execute(getRequest);
            HttpEntity responseEntity = response.getEntity();
            if(responseEntity!=null) {
                String responseString = EntityUtils.toString(responseEntity);
                System.out.println(responseString);

                JSONObject jsonObject = new JSONObject(responseString);
                JSONArray arry=jsonObject.getJSONArray("results");
                JSONObject obj=(JSONObject)arry.get(0);

                JSONObject loc=obj.getJSONObject("geometry").getJSONObject("location");
                String lat=String.valueOf(loc.getFloat("lat"));
                String lng=String.valueOf(loc.getFloat("lng"));

                System.out.println(lat+" "+lng);
                return getAirportByCoordinates(lat, lng);

            }

        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return null;

    }
}
