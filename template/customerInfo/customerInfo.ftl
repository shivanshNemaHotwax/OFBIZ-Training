<div style="margin: 20px; font-family: Arial, sans-serif;">
    <form method="post" action="<@ofbizUrl>createPartyAndContactServices</@ofbizUrl>" onsubmit="return validateForm()">
        <div style="margin-bottom: 10px;">
            <label for="partyId" style="display: inline-block; width: 120px;">Customer ID :</label>
            <input type="text" id="partyId" name="partyId" style="padding: 5px; width: 200px; border: 1px solid #ccc; border-radius: 4px;" /><br/>
        </div>

        <div style="margin-bottom: 10px;">
            <label for="firstName" style="display: inline-block; width: 120px;">First Name <span style ="color:red">*</span> :</label>
            <input type="text" id="firstName" name="firstName" style="padding: 5px; width: 200px; border: 1px solid #ccc; border-radius: 4px;" /><br/>
        </div>

        <div style="margin-bottom: 10px;">
            <label for="lastName" style="display: inline-block; width: 120px;">Last Name <span style ="color:red">*</span> :</label>
            <input type="text" id="lastName" name="lastName" style="padding: 5px; width: 200px; border: 1px solid #ccc; border-radius: 4px;" /><br/>
        </div>

        <div style="margin-bottom: 10px;">
            <label for="email" style="display: inline-block; width: 120px;">Email :</label>
            <input type="email" id="email" name="email" style="padding: 5px; width: 200px; border: 1px solid #ccc; border-radius: 4px;" /><br/>
        </div>

        <div style="margin-bottom: 10px;">
            <label for="phone" style="display: inline-block; width: 120px;">Phone :</label>
            <input type="number" id="phone" name="phone" style="padding: 5px; width: 200px; border: 1px solid #ccc; border-radius: 4px;" /><br/>
        </div>

        <div style="margin-bottom: 10px;">
            <label for="genAddress" style="display: inline-block; width: 120px;">General Address <span style ="color:red">*</span> :</label>
            <input type="text" id="genAddress" name="genAddress" style="padding: 5px; width: 200px; border: 1px solid #ccc; border-radius: 4px;" /><br/>
        </div>

        <div style="margin-bottom: 10px;">
            <label for="city" style="display: inline-block; width: 120px;">City <span style ="color:red">*</span> :</label>
            <input type="text" id="city" name="city" style="padding: 5px; width: 200px; border: 1px solid #ccc; border-radius: 4px;" /><br/>
        </div>

        <div style="margin-bottom: 10px;">
            <label for="postalCode" style="display: inline-block; width: 120px;">Postal Code <span style ="color:red">*</span> :</label>
            <input type="text" id="postalCode" name="postalCode" style="padding: 5px; width: 200px; border: 1px solid #ccc; border-radius: 4px;" /><br/>
        </div>

        <input type="submit" value="Submit" style="padding: 8px 20px; background-color: #4CAF50; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 16px;" />
    </form>
</div>

<script>
    function validateForm() {
        var firstName = document.getElementById("firstName").value;
        var lastName = document.getElementById("lastName").value;
        var email = document.getElementById("email").value;
        var phone = document.getElementById("phone").value;
        var genAddress = document.getElementById("genAddress").value;
        var city = document.getElementById("city").value;
        var postalCode = document.getElementById("postalCode").value;

         if(email==="" && phone===""){
           alert("Either email or phone number should not be empty");
           return false;
         }

        if ( firstName === "" || lastName === "" || genAddress === "" || city === "" || postalCode === "") {
            alert("All fields are required");
            return false;
        }

        return true;
    }
</script>