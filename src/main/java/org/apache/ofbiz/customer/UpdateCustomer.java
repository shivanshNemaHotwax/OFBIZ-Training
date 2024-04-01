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

public class UpdateCustomer {

    public static Map<String, Object> updateCustomerInfo(DispatchContext ctx, Map<String, ? extends Object> context) {
        // Extracting parameters from the context
        String partyId = (String) context.get("partyId");
        String firstName = (String) context.get("updatedFirstName");
        String lastName = (String) context.get("updatedLastName");
        String emailAddress = (String) context.get("updatedEmail");
        String phoneNumber = (String) context.get("updatedPhone");
        String generalAddress = (String) context.get("updatedGenAddress");
        String city = (String) context.get("updatedCity");
        String postalCode = (String) context.get("updatedPostalCode");

        // Getting the dispatcher
        LocalDispatcher dispatcher = ctx.getDispatcher();
        Delegator delegator = ctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        try {
            // Fetch contact mechanism IDs
            String contactMechIdEmail = getContactMechIdForParty(delegator, partyId,"EMAIL_ADDRESS");
            String contactMechIdPa = getContactMechIdForParty(delegator, partyId,"POSTAL_ADDRESS");
            String contactMechIdTn = getContactMechIdForParty(delegator, partyId,"TELECOM_NUMBER");

            // Update person
            Map<String, Object> updatePerson = dispatcher.runSync("updatePerson", UtilMisc.toMap("partyId", partyId, "firstName", firstName, "lastName", lastName, "userLogin", userLogin));
            if (ServiceUtil.isError(updatePerson)) {
                return ServiceUtil.returnError(ServiceUtil.getErrorMessage(updatePerson));
            }

            // Update Email Address
            Map<String, Object> updateEmailAddress = dispatcher.runSync("updateEmailAddress", UtilMisc.toMap("partyId", partyId, "emailAddress", emailAddress, "contactMechId", contactMechIdEmail, "userLogin", userLogin));
            if (ServiceUtil.isError(updateEmailAddress)) {
                return ServiceUtil.returnError(ServiceUtil.getErrorMessage(updateEmailAddress));
            }

            // Update Postal Address
            Map<String, Object> updatePostalAddress = dispatcher.runSync("updatePostalAddress", UtilMisc.toMap(
                    "partyId", partyId,
                    "address1", generalAddress,
                    "city", city,
                    "postalCode", postalCode,
                    "contactMechId", contactMechIdPa,
                    "userLogin", userLogin));
            if (ServiceUtil.isError(updatePostalAddress)) {
                return ServiceUtil.returnError(ServiceUtil.getErrorMessage(updatePostalAddress));
            }

            // Update Telecom Number
            Map<String, Object> updateTelecomNumber = dispatcher.runSync("updateTelecomNumber", UtilMisc.toMap(
                    "partyId", partyId,
                    "contactNumber", phoneNumber,
                    "contactMechId", contactMechIdTn,
                    "userLogin", userLogin));
            if (ServiceUtil.isError(updateTelecomNumber)) {
                return ServiceUtil.returnError(ServiceUtil.getErrorMessage(updateTelecomNumber));
            }


            return ServiceUtil.returnSuccess();
        } catch (Exception e) {
            // Handle any unexpected exceptions
            return ServiceUtil.returnError("Error occurred while updating customer information: " + e.getMessage());
        }
    }

    private static String getContactMechIdForParty(Delegator delegator, String partyId, String contactMechTypeId) {
        List<GenericValue> partyContactMechs = null;
        try {
            partyContactMechs = EntityQuery.use(delegator)
                    .from("PartyContactMech")
                    .where(EntityCondition.makeCondition(
                            EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId)))
                    .queryList();
        } catch (GenericEntityException e) {
            // Handle the exception
        }

        if (UtilValidate.isNotEmpty(partyContactMechs)) {
            for (GenericValue partyContactMech : partyContactMechs) {
                String contactMechId = partyContactMech.getString("contactMechId");
                // Check if the contact mechanism matches the given type
                if (isContactMechType(delegator, contactMechId, contactMechTypeId)) {
                    return contactMechId;
                }
            }
        }

        return null;
    }

    // Method to check if the contact mechanism matches the given type
    private static boolean isContactMechType(Delegator delegator, String contactMechId, String contactMechTypeId) {
        GenericValue contactMech = null;
        try {
            contactMech = EntityQuery.use(delegator)
                    .from("ContactMech")
                    .where("contactMechId", contactMechId)
                    .queryOne();
        } catch (GenericEntityException e) {
            // Handle the exception
        }

        return contactMech != null && contactMechTypeId.equals(contactMech.getString("contactMechTypeId"));
    }


}