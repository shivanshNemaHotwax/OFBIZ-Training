package org.apache.ofbiz.customer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVFormat.Builder;
import org.apache.commons.csv.CSVRecord;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.base.util.UtilGenerics;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.DelegatorFactory;
import org.apache.ofbiz.entity.GenericEntity;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityConditionList;
import org.apache.ofbiz.entity.condition.EntityExpr;
import org.apache.ofbiz.entity.condition.EntityFunction;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.model.DynamicViewEntity;
import org.apache.ofbiz.entity.model.ModelKeyMap;
import org.apache.ofbiz.entity.util.EntityListIterator;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.entity.util.EntityTypeUtil;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ModelService;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.service.GenericDispatcherFactory;
import javax.servlet.http.HttpServletRequest;
import org.apache.ofbiz.base.util.Debug;

public class CustomerServices {

    public static Map<String, Object> CreatePartyAndContactMech(DispatchContext ctx, Map<String, ? extends Object> context) {
        // Extracting parameters from the context
        String partyIdUser = (String) context.get("partyId");
        String firstName = (String) context.get("firstName");
        String lastName = (String) context.get("lastName");
        String email = (String) context.get("email");
        String phoneNumber = (String) context.get("phone");
        String generalAddress = (String) context.get("genAddress");
        String city = (String) context.get("city");
        String postalCode = (String) context.get("postalCode");


        // Getting the dispatcher
        LocalDispatcher dispatcher = ctx.getDispatcher();
        Delegator delegator = ctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        try {
            // Creating party
            Map<String, Object> createPersonResult;
            if(partyIdUser==""){
                 createPersonResult = dispatcher.runSync("createPerson", UtilMisc.toMap("firstName", firstName, "lastName", lastName));
            }
            else {
                 createPersonResult = dispatcher.runSync("createPerson", UtilMisc.toMap("partyId", partyIdUser, "firstName", firstName, "lastName", lastName));
            }
            if (ServiceUtil.isError(createPersonResult)) {
                return ServiceUtil.returnError(ServiceUtil.getErrorMessage(createPersonResult));
            }
            String partyId =(String) createPersonResult.get("partyId");
            // Creating a role for the party
            GenericValue partyRole = delegator.makeValue("PartyRole", UtilMisc.toMap("partyId", partyId, "roleTypeId", "CUSTOMER"));
            delegator.create(partyRole);

            // Creating postal address
            Map<String, Object> createPostalAddressResult = dispatcher.runSync("createPostalAddress", UtilMisc.toMap("address1", generalAddress,"city",city,"postalCode",postalCode,"userLogin",userLogin));
            if (ServiceUtil.isError(createPostalAddressResult)) {
                return ServiceUtil.returnError(ServiceUtil.getErrorMessage(createPostalAddressResult));
            }

            String postalAddressContactMechId = (String) createPostalAddressResult.get("contactMechId");

            //Party Contact Mech
            try {
                // Create a new GenericValue object for PartyContactMech entity
                GenericValue partyContactMech = delegator.makeValue("PartyContactMech", UtilMisc.toMap(
                        "partyId", partyId,
                        "contactMechId", postalAddressContactMechId,
                        "fromDate", UtilDateTime.nowTimestamp(),
                        "roleTypeId", "CUSTOMER"
                ));
                // Store the PartyContactMech entity in the database
                delegator.create(partyContactMech);
            } catch (GenericEntityException e) {
                return ServiceUtil.returnError("Error creating PartyContactMech");
            }

            // Defining purpose of postal address
            Map<String, Object> createPartyContactMechPurposeResult = dispatcher.runSync("createPartyContactMechPurpose", UtilMisc.toMap("partyId", partyId, "contactMechId", postalAddressContactMechId, "contactMechPurposeTypeId", "GENERAL_LOCATION","userLogin", userLogin));
           if (ServiceUtil.isError(createPartyContactMechPurposeResult)) {
               return ServiceUtil.returnError(ServiceUtil.getErrorMessage(createPartyContactMechPurposeResult));
           }

           if(phoneNumber!=null) {
               //Creating telecom number
               Map<String, Object> createTeleNumberResult = dispatcher.runSync("createTelecomNumber", UtilMisc.toMap("phoneNumber", phoneNumber, "userLogin", userLogin));
               if (ServiceUtil.isError(createTeleNumberResult)) {
                   return ServiceUtil.returnError(ServiceUtil.getErrorMessage(createTeleNumberResult));
               }
               String telecomNumberContactMechId = (String) createTeleNumberResult.get("contactMechId");

               // Defining purpose of Telecom Number
               Map<String, Object> createPartyContactMechPurposeResult_TC = dispatcher.runSync("createPartyContactMechPurpose", UtilMisc.toMap("partyId", partyId, "contactMechId", telecomNumberContactMechId, "contactMechPurposeTypeId", "PRIMARY_PHONE", "userLogin", userLogin));
               if (ServiceUtil.isError(createPartyContactMechPurposeResult_TC)) {
                   return ServiceUtil.returnError(ServiceUtil.getErrorMessage(createPartyContactMechPurposeResult_TC));
               }

               //Party Contact Mech
               try {
                   // Create a new GenericValue object for PartyContactMech entity
                   GenericValue partyContactMech = delegator.makeValue("PartyContactMech", UtilMisc.toMap(
                           "partyId", partyId,
                           "contactMechId", telecomNumberContactMechId,
                           "fromDate", UtilDateTime.nowTimestamp(),
                           "roleTypeId", "CUSTOMER"
                   ));

                   // Store the PartyContactMech entity in the database
                   delegator.create(partyContactMech);

               } catch (GenericEntityException e) {
                   return ServiceUtil.returnError("Error creating PartyContactMech");
               }
           }

            if(email!=null) {
                //Creating email address
                Map<String, Object> createEmailAddressResult = dispatcher.runSync("createEmailAddress", UtilMisc.toMap("emailAddress", email, "userLogin", userLogin));
                if (ServiceUtil.isError(createEmailAddressResult)) {
                    return ServiceUtil.returnError(ServiceUtil.getErrorMessage(createEmailAddressResult));
                }
                String emailAddressContactMechId = (String) createEmailAddressResult.get("contactMechId");

                //Party Contact Mech
                try {
                    // Create a new GenericValue object for PartyContactMech entity
                    GenericValue partyContactMech = delegator.makeValue("PartyContactMech", UtilMisc.toMap(
                            "partyId", partyId,
                            "contactMechId", emailAddressContactMechId,
                            "fromDate", UtilDateTime.nowTimestamp(),
                            "roleTypeId", "CUSTOMER"
                    ));

                    // Store the PartyContactMech entity in the database
                    delegator.create(partyContactMech);

                } catch (GenericEntityException e) {
                    return ServiceUtil.returnError("Error creating PartyContactMech");
                }
                // Defining purpose of email address
                Map<String, Object> createPartyContactMechPurposeResult_EM = dispatcher.runSync("createPartyContactMechPurpose", UtilMisc.toMap("partyId", partyId, "contactMechId", emailAddressContactMechId, "contactMechPurposeTypeId", "PRIMARY_EMAIL", "userLogin", userLogin));
                if (ServiceUtil.isError(createPartyContactMechPurposeResult_EM)) {
                    return ServiceUtil.returnError(ServiceUtil.getErrorMessage(createPartyContactMechPurposeResult_EM));
                }
            }
            // Create a map to hold the customer information
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("partyId", partyId);
            responseData.put("firstName", firstName);
            responseData.put("lastName", lastName);
            responseData.put("email", email);
            responseData.put("phone", phoneNumber);
            responseData.put("genAddress", generalAddress);
            responseData.put("city", city);
            responseData.put("postalCode", postalCode);
            // Return success message
//            return ServiceUtil.returnSuccess();
            // Return responseData along with the success message
            Map<String, Object> resultMap = ServiceUtil.returnSuccess();
            resultMap.put("data", responseData);
            return resultMap;

        } catch (Exception e) {
            return ServiceUtil.returnError("Error occurred while creating party and contact mechanism: " + e.getMessage());
        }
    }

    public static final String MODULE = CustomerServices.class.getName();
    public static Map<String, Object> getAllPartiesDetails(DispatchContext dctx, Map<String, ? extends Object> context) {
        Delegator delegator = dctx.getDelegator();

        try {
            List<GenericValue> personList = EntityQuery.use(delegator)
                    .from("Person")
                    .select("partyId", "firstName", "lastName")
                    .queryList();

            // Fetch partyId from the Party entity
            List<GenericValue> partyList = EntityQuery.use(delegator)
                    .from("Party")
                    .select("partyId")
                    .queryList();

            List<Map<String, Object>> combinedList = new ArrayList<>();
            for (GenericValue person : personList) {
                String partyId = person.getString("partyId");
                for (GenericValue party : partyList) {
                    if (partyId.equals(party.getString("partyId"))) {
                        Map<String, Object> combinedEntry = new HashMap<>();
                        combinedEntry.put("partyId", partyId);
                        combinedEntry.put("firstName", person.getString("firstName"));
                        combinedEntry.put("lastName", person.getString("lastName"));
                        combinedList.add(combinedEntry);
                        break;
                    }
                }
            }

            for (Map<String, Object> entry : combinedList) {
                Debug.log("Party ID: " + entry.get("partyId"));
                Debug.log("First Name: " + entry.get("firstName"));
                Debug.log("Last Name: " + entry.get("lastName"));
            }

            Map<String, Object> result = new HashMap<>();
            result.put("partyList", combinedList);
            return result;
        } catch (GenericEntityException e) {
            Debug.logError("Error occurred while fetching party details: " + e.getMessage(), MODULE);
            return ServiceUtil.returnError("Error occurred while fetching party details");
        }
    }

}