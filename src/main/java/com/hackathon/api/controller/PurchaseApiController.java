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

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import javax.validation.Valid;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 424047 on 4/5/2019.
 */
@RestController
public class PurchaseApiController implements PurchaseApi {

    private static String request_XML="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<p:OrderCreateRQ xmlns:p=\"http://www.iata.org/IATA/2015/00/2018.1/OrderCreateRQ\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
            "   <p:PayloadAttributes>\n" +
            "      <p:TrxID>CRR-NO-GBP-0318-07</p:TrxID>\n" +
            "   </p:PayloadAttributes>\n" +
            "   <p:PointOfSale>\n" +
            "      <p:City>\n" +
            "         <p:IATA_LocationCode>BJS</p:IATA_LocationCode>\n" +
            "      </p:City>\n" +
            "   </p:PointOfSale>\n" +
            "   <p:Party>\n" +
            "      <p:Sender>\n" +
            "         <p:TravelAgency>\n" +
            "            <p:AgencyID>HACK-TST</p:AgencyID>\n" +
            "            <p:IATA_Number>1234</p:IATA_Number>\n" +
            "            <p:Name>Hackathon</p:Name>\n" +
            "            <p:PseudoCityID>BJS</p:PseudoCityID>\n" +
            "         </p:TravelAgency>\n" +
            "      </p:Sender>\n" +
            "   </p:Party>\n" +
            "   <p:Request>\n" +
            "      <p:CreateOrder>\n" +
            "         <p:SelectedOffer>\n" +
            "            <p:OfferID>TestOfferID</p:OfferID>\n" +
            "            <p:OwnerCode>DL</p:OwnerCode>\n" +
            "            <p:ShoppingResponseRefID>44F21F6CBF604617B21F6CBF60261781</p:ShoppingResponseRefID>\n" +
            "            <p:TotalOfferPriceAmount CurCode=\"USD\">TestPrice</p:TotalOfferPriceAmount>\n" +
            "            <p:SelectedOfferItem>\n" +
            "               <p:OfferItemID>1-1</p:OfferItemID>\n" +
            "               <p:PaxRefID>1</p:PaxRefID>\n" +
            "               <!--p:PaxRefID>2</p:PaxRefID>\n" +
            "               <p:PaxRefID>3</p:PaxRefID>\n" +
            "               <p:PaxRefID>4</p:PaxRefID>\n" +
            "               <p:PaxRefID>5</p:PaxRefID-->\n" +
            "            </p:SelectedOfferItem>\n" +
            "         </p:SelectedOffer>\n" +
            "      </p:CreateOrder>\n" +
            "      <p:Commission />\n" +
            "      <p:DataLists>\n" +
            "         <p:PaxList>\n" +
            "            <p:Pax>\n" +
            "               <p:PaxID>1</p:PaxID>\n" +
            "               <p:PTC>ADT</p:PTC>\n" +
            "               <!--p:PaxRefID>2</p:PaxRefID-->\n" +
            "               <p:ContactInfo>\n" +
            "                  <p:ContactInfoID>1</p:ContactInfoID>\n" +
            "                  <p:Phone>\n" +
            "                     <p:LabelText>HOME</p:LabelText>\n" +
            "                     <p:CountryDialingCode>1</p:CountryDialingCode>\n" +
            "                     <p:AreaCodeNumber>404</p:AreaCodeNumber>\n" +
            "                     <p:PhoneNumber>1211212</p:PhoneNumber>\n" +
            "                  </p:Phone>\n" +
            "                  <p:EmailAddress>\n" +
            "                     <p:EmailAddressText>john.doe@test.com</p:EmailAddressText>\n" +
            "                  </p:EmailAddress>\n" +
            "               </p:ContactInfo>\n" +
            "               <p:Individual>\n" +
            "                  <p:GenderCode>F</p:GenderCode>\n" +
            "                  <p:TitleName>Ms</p:TitleName>\n" +
            "                  <p:GivenName>Dcom</p:GivenName>\n" +
            "                  <p:MiddleName>I</p:MiddleName>\n" +
            "                  <p:Surname>Testacct</p:Surname>\n" +
            "                  <p:Birthdate>1988-02-17</p:Birthdate>\n" +
            "               </p:Individual>\n" +
            "               <!--p:RedressProgram>\n" +
            "                  <p:RedressID>0123456789012</p:RedressID>\n" +
            "                  <p:ProgramName />\n" +
            "                  <p:CountryCode>\n" +
            "                     <p:value>US</p:value>\n" +
            "                  </p:CountryCode>\n" +
            "               </p:RedressProgram-->\n" +
            "               <p:LoyaltyProgramAccount>\n" +
            "            	<p:ProgramCode>SM</p:ProgramCode>\n" +
            "            	<p:AccountNumber>2384393167</p:AccountNumber>\n" +
            "               </p:LoyaltyProgramAccount>\n" +
            "            </p:Pax>\n" +
            "            <!--p:Pax>\n" +
            "               <p:PaxID>2</p:PaxID>\n" +
            "               <p:PTC>INF</p:PTC>\n" +
            "               <p:Individual>\n" +
            "                  <p:GenderCode>F</p:GenderCode>\n" +
            "                  <p:TitleName />\n" +
            "                  <p:GivenName>BabyInlap</p:GivenName>\n" +
            "                  <p:MiddleName />\n" +
            "                  <p:Surname>Member</p:Surname>\n" +
            "                  <p:SuffixName>Jr</p:SuffixName>\n" +
            "                  <p:Birthdate>2018-03-22</p:Birthdate>\n" +
            "               </p:Individual>\n" +
            "            </p:Pax>\n" +
            "            <p:Pax>\n" +
            "               <p:PaxID>3</p:PaxID>\n" +
            "               <p:PTC>GBE</p:PTC>\n" +
            "               <p:Individual>\n" +
            "                  <p:GenderCode>F</p:GenderCode>\n" +
            "                  <p:TitleName />\n" +
            "                  <p:GivenName>BabyInseat</p:GivenName>\n" +
            "                  <p:MiddleName />\n" +
            "                  <p:Surname>Member</p:Surname>\n" +
            "                  <p:SuffixName />\n" +
            "                  <p:Birthdate>2004-03-22</p:Birthdate>\n" +
            "               </p:Individual>\n" +
            "            </p:Pax>\n" +
            "            <p:Pax>\n" +
            "               <p:PaxID>4</p:PaxID>\n" +
            "               <p:PTC>CNN</p:PTC>\n" +
            "               <p:Individual>\n" +
            "                  <p:GenderCode>F</p:GenderCode>\n" +
            "                  <p:TitleName />\n" +
            "                  <p:GivenName>BabyCNN</p:GivenName>\n" +
            "                  <p:MiddleName />\n" +
            "                  <p:Surname>Member</p:Surname>\n" +
            "                  <p:SuffixName />\n" +
            "                  <p:Birthdate>2008-03-22</p:Birthdate>\n" +
            "               </p:Individual>\n" +
            "            </p:Pax>\n" +
            "            <p:Pax>\n" +
            "               <p:PaxID>5</p:PaxID>\n" +
            "               <p:PTC>ADT</p:PTC>\n" +
            "               <p:Individual>\n" +
            "                  <p:GenderCode>M</p:GenderCode>\n" +
            "                  <p:TitleName />\n" +
            "                  <p:GivenName>Christa</p:GivenName>\n" +
            "                  <p:MiddleName />\n" +
            "                  <p:Surname>Test</p:Surname>\n" +
            "                  <p:SuffixName />\n" +
            "                  <p:Birthdate>1970-03-22</p:Birthdate>\n" +
            "               </p:Individual>\n" +
            "               <p:LoyaltyProgramAccount>\n" +
            "                  <p:ProgramCode>SM</p:ProgramCode>\n" +
            "                  <p:AccountNumber>8007312856</p:AccountNumber>\n" +
            "                </p:LoyaltyProgramAccount>\n" +
            "            </p:Pax-->\n" +
            "         </p:PaxList>\n" +
            "      </p:DataLists>\n" +
            "      <p:OrderCreateParameters references=\"token\" />\n" +
            "      <p:PaymentInfo>\n" +
            "          <p:PaymentInfoID>01</p:PaymentInfoID>\n" +
            "         <p:Amount CurCode=\"USD\">TestPurchasePrice</p:Amount>\n" +
            "         <p:TypeCode>Fliggy</p:TypeCode>\n" +
            "         <p:PayerContactInfo>\n" +
            "         <p:Individual>\n" +
            "         <p:GivenName>Chammak</p:GivenName>\n" +
            "         <p:Surname>Chameli</p:Surname>\n" +
            "         </p:Individual>\n" +
            "         </p:PayerContactInfo>\n" +
            "         <p:PaymentMethod>\n" +
            "            <!--p:OtherPaymentMethod>\n" +
            "               <p:Remark>\n" +
            "                  <p:RemarkText>1234567899876543210</p:RemarkText>\n" +
            "                  <p:CreationDateTime>2002-05-30T09:00:00</p:CreationDateTime>\n" +
            "               </p:Remark>\n" +
            "               <p:Remark>\n" +
            "                  <p:RemarkText>1234567899876</p:RemarkText>\n" +
            "               </p:Remark>\n" +
            "            </p:OtherPaymentMethod-->\n" +
            "            <p:PaymentCard>\n" +
            "               <p:CardNumber>371010000000000</p:CardNumber>\n" +
            "               <p:SeriesCode>1234</p:SeriesCode>\n" +
            "               <p:CreditCardVendorCode>AX</p:CreditCardVendorCode>\n" +
            "               <p:ExpirationDate>0120</p:ExpirationDate>\n" +
            "                    <p:CardholderContactInfo>\n" +
            "                          <p:StreetText>100 Main Street</p:StreetText>\n" +
            "                          <p:StreetText>Sengupta Mansion</p:StreetText>\n" +
            "                          <p:BuildingRoomText>Apt 100</p:BuildingRoomText>\n" +
            "                          <p:PostalCode>30306</p:PostalCode>\n" +
            "                          <p:CityName>Atlanta</p:CityName>\n" +
            "                          <p:CountrySubDivisionName>GA</p:CountrySubDivisionName>\n" +
            "                          <p:CountryCode>US</p:CountryCode>\n" +
            "                    </p:CardholderContactInfo>\n" +
            "              </p:PaymentCard>\n" +
            "         </p:PaymentMethod>\n" +
            "      </p:PaymentInfo>\n" +
            "   </p:Request>\n" +
            "</p:OrderCreateRQ>";

    @Autowired
    BookingsWorkflow bookingWorkflow;

    public ResponseEntity<BookingConfirmation> purchaseByPost(@ApiParam(value = "BookingPurchaseRequest" ,required=true )  @Valid @RequestBody BookingPurchaseRequest body) {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        System.out.println(body.getOfferId());
        request_XML=request_XML.replaceAll("TestOfferID", body.getOfferId());
        request_XML=request_XML.replaceAll("TestPrice", body.getPrice());
        request_XML=request_XML.replaceAll("TestPurchasePrice", body.getPrice());
        System.out.println(request_XML);

        try {
            HttpPost postRequest = new HttpPost("https://stage-apigateway.delta.com/NDC/v18.1/OrderCreate");
            postRequest.addHeader("Content-type", "application/xml");
            postRequest.addHeader("Accept", "");
            postRequest.addHeader("Authorization", "Bearer WpaYasAvJflSZlahNGrF8ozf6AlR");

            StringEntity userEntity = new StringEntity(request_XML);
            postRequest.setEntity(userEntity);

            HttpResponse response = httpClient.execute(postRequest);
            HttpEntity responseEntity = response.getEntity();
            if(responseEntity!=null) {
                String responseString = EntityUtils.toString(responseEntity);
                System.out.println(responseString);

                BookingConfirmation bc = new BookingConfirmation();
                //Pattern p = Pattern.compile("^<OrderID>([A-Z0-9]*)<$");
                //Pattern p = Pattern.compile("(?<=<OrderID>)(.*)(?=</OrderID>)");
                //Matcher m = p.matcher(responseString);

                int index = responseString.indexOf("<OrderID>");
                bc.setOrderId(responseString.substring(index+9, index+15));

                //bc.setOrderId(m.group(1));
                System.out.println(bc.getOrderId());

                return new ResponseEntity(bc, HttpStatus.OK);
            }

        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}