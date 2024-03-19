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

//public class CustomerServices {
//
//    public static Map<String, Object> CreatePartyAndContactMech(DispatchContext ctx, Map<String, ? extends Object> context) {
//        // Extracting parameters from the context
//        String partyId = (String) context.get("partyId");
//        String firstName = (String) context.get("firstName");
//        String lastName = (String) context.get("lastName");
//        String email = (String) context.get("email");
//        String phoneNumber = (String) context.get("phone");
//        String generalAddress = (String) context.get("genAddress");
//
//        // Getting the dispatcher
//        LocalDispatcher dispatcher = ctx.getDispatcher();
//        Delegator delegator = ctx.getDelegator();
//
//        try {
//            // Creating party
//            Map<String, Object> createPersonResult = dispatcher.runSync("createPerson", UtilMisc.toMap("partyId", partyId, "firstName", firstName, "lastName", lastName));
//            if (ServiceUtil.isError(createPersonResult)) {
//                return ServiceUtil.returnError(ServiceUtil.getErrorMessage(createPersonResult));
//            }
//            GenericValue userLogin = (GenericValue) context.get("userLogin");
//            // Creating contact mechanism for email
////            Map<String, Object> emailContactMechResult = dispatcher.runSync("createContactMech", UtilMisc.toMap("partyId", partyId, "contactMechTypeId", "EMAIL_ADDRESS", "infoString", email, "userLogin", userLogin));
////            if (ServiceUtil.isError(emailContactMechResult)) {
////                return ServiceUtil.returnError(ServiceUtil.getErrorMessage(emailContactMechResult));
////            }
////            String emailConMechId = (String) emailContactMechResult.get("contactMechId");
//
//            // Creating contact mechanism for telephone number
////            Map<String, Object> teleContactMechResult = dispatcher.runSync("createContactMech", UtilMisc.toMap("partyId", partyId, "contactMechTypeId", "TELECOM_NUMBER", "infoString", phoneNumber,"userLogin", userLogin));
////            if (ServiceUtil.isError(teleContactMechResult)) {
////                return ServiceUtil.returnError(ServiceUtil.getErrorMessage(teleContactMechResult));
////            }
////            String teleConMechId = (String) teleContactMechResult.get("contactMechId");
//
////             Creating contact mechanism for postal address
//            Map<String, Object> createPostalAddressResult = dispatcher.runSync("createPostalAddress", UtilMisc.toMap("address1", generalAddress, "city", "Indore", "postalCode", "123456","userLogin",userLogin));
//            if (ServiceUtil.isError(createPostalAddressResult)) {
//                return ServiceUtil.returnError(ServiceUtil.getErrorMessage(createPostalAddressResult));
//            }
//            String postalAddressContactMechId = (String) createPostalAddressResult.get("contactMechId");
//
//            try {
//                // Create a new GenericValue object for PartyContactMech entity
//                GenericValue partyContactMech = delegator.makeValue("PartyContactMech", UtilMisc.toMap(
//                        "partyId", partyId,
//                        "contactMechId", postalAddressContactMechId,
//                        "fromDate", UtilDateTime.nowTimestamp(),
//                        "roleTypeId", "GENERAL_LOCATION"
//                ));
//                // Store the PartyContactMech entity in the database
//                delegator.create(partyContactMech);
//            } catch (GenericEntityException e) {
////                Debug.logError("Error creating PartyContactMech: " + e.getMessage(), MODULE);
//                return ServiceUtil.returnError("Error creating PartyContactMech");
//            }
//
//
//            // Defining purpose of postal address
////            Map<String, Object> createPartyContactMechPurposeResult = dispatcher.runSync("createPartyContactMechPurpose", UtilMisc.toMap("partyId", partyId, "contactMechId", addressConMechId, "contactMechPurposeTypeId", "GENERAL_LOCATION","userLogin", userLogin));
////            if (ServiceUtil.isError(createPartyContactMechPurposeResult)) {
////                return ServiceUtil.returnError(ServiceUtil.getErrorMessage(createPartyContactMechPurposeResult));
////            }
//
//            // Return success message
//            return ServiceUtil.returnSuccess();
//
//        } catch (Exception e) {
//            return ServiceUtil.returnError("Error occurred while creating party and contact mechanism: " + e.getMessage());
//        }
//    }
//}


public class CustomerServices {

    public static Map<String, Object> CreatePartyAndContactMech(DispatchContext ctx, Map<String, ? extends Object> context) {
        // Extracting parameters from the context
        String partyId = (String) context.get("partyId");
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
            Map<String, Object> createPersonResult = dispatcher.runSync("createPerson", UtilMisc.toMap("partyId", partyId, "firstName", firstName, "lastName", lastName));
            if (ServiceUtil.isError(createPersonResult)) {
                return ServiceUtil.returnError(ServiceUtil.getErrorMessage(createPersonResult));
            }

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

           //Creating telecom number
            Map<String, Object> createTeleNumberResult = dispatcher.runSync("createTelecomNumber", UtilMisc.toMap("contactNumber",phoneNumber, "userLogin",userLogin));
            if (ServiceUtil.isError(createTeleNumberResult)) {
                return ServiceUtil.returnError(ServiceUtil.getErrorMessage(createTeleNumberResult));
            }
            String telecomNumberContactMechId = (String) createTeleNumberResult.get("contactMechId");

            // Defining purpose of Telecom Number
            Map<String, Object> createPartyContactMechPurposeResult_TC = dispatcher.runSync("createPartyContactMechPurpose", UtilMisc.toMap("partyId", partyId, "contactMechId", telecomNumberContactMechId, "contactMechPurposeTypeId", "PRIMARY_PHONE","userLogin", userLogin));
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

            //Creating email address
            Map<String, Object> createEmailAddressResult = dispatcher.runSync("createEmailAddress", UtilMisc.toMap("emailAddress",email,"userLogin",userLogin));
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
            Map<String, Object> createPartyContactMechPurposeResult_EM = dispatcher.runSync("createPartyContactMechPurpose", UtilMisc.toMap("partyId", partyId, "contactMechId", emailAddressContactMechId, "contactMechPurposeTypeId", "PRIMARY_EMAIL","userLogin", userLogin));
            if (ServiceUtil.isError(createPartyContactMechPurposeResult_EM)) {
                return ServiceUtil.returnError(ServiceUtil.getErrorMessage(createPartyContactMechPurposeResult_EM));
            }
            // Return success message
            return ServiceUtil.returnSuccess();

        } catch (Exception e) {
            return ServiceUtil.returnError("Error occurred while creating party and contact mechanism: " + e.getMessage());
        }
    }
}