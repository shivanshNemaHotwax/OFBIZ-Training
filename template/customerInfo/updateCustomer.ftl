<div style="margin: 20px; font-family: Arial, sans-serif;">
    <form id="updateForm" method="post" action="<@ofbizUrl>updateCustomer</@ofbizUrl>">
        <div style="margin-bottom: 10px;">
            <label for="partyId" style="display: inline-block; width: 120px;">PartyId:</label>
            <input type="text" id="partyId" name="partyId" value="${parameters.partyId}" style="padding: 5px; width: 200px; border: 1px solid #ccc; border-radius: 4px;" /><br/>
        </div>
        <div style="margin-bottom: 10px;">
            <label for="firstName" style="display: inline-block; width: 120px;">First Name:</label>
            <input type="text" id="firstName" name="updatedFirstName" value="${parameters.firstName}"  style="padding: 5px; width: 200px; border: 1px solid #ccc; border-radius: 4px;" /><br/>
        </div>
<div style="margin-bottom: 10px;">
            <label for="firstName" style="display: inline-block; width: 120px;">Last Name:</label>
            <input type="text" id="lastName" name="updatedLastName" value="${parameters.lastName}"  style="padding: 5px; width: 200px; border: 1px solid #ccc; border-radius: 4px;" /><br/>
        </div>
        <div style="margin-bottom: 10px;">
                    <label for="email" style="display: inline-block; width: 120px;">Email:</label>
                    <input type="email" id="email" name="updatedEmail" value="${parameters.email}"  style="padding: 5px; width: 200px; border: 1px solid #ccc; border-radius: 4px;" /><br/>
                </div>
                <div style="margin-bottom: 10px;">
                            <label for="genAddress" style="display: inline-block; width: 120px;">General Address:</label>
                            <input type="text" id="genAddress" name="updatedGenAddress" value="${parameters.genAddress}" style="padding: 5px; width: 200px; border: 1px solid #ccc; border-radius: 4px;" /><br/>
                        </div>
                        <div style="margin-bottom: 10px;">
                                    <label for="city" style="display: inline-block; width: 120px;">City:</label>
                                    <input type="text" id="city" name="updatedCity" value="${parameters.city}"  style="padding: 5px; width: 200px; border: 1px solid #ccc; border-radius: 4px;" /><br/>
                                </div>

                                <div style="margin-bottom: 10px;">
                                            <label for="postalCode" style="display: inline-block; width: 120px;">postalCode:</label>
                                            <input type="text" id="postalCode" name="updatedPostalCode" value="${parameters.postalCode}"  style="padding: 5px; width: 200px; border: 1px solid #ccc; border-radius: 4px;" /><br/>
                                        </div>

        <input type="submit" id="updateButton" value="Update" style="padding: 8px 20px; background-color: #4CAF50; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 16px;" />
    </form>
</div>


