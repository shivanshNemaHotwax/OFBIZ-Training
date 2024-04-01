<div style="margin: 20px;">
    <table style="border-collapse: collapse; width: 100%; border: 1px solid #dddddd;">
        <tr style="background-color: #f2f2f2;">
            <th style="border: 1px solid #dddddd; text-align: left; padding: 8px;">Customer Id</th>
            <th style="border: 1px solid #dddddd; text-align: left; padding: 8px;">First Name</th>
            <th style="border: 1px solid #dddddd; text-align: left; padding: 8px;">Last Name</th>
            <th style="border: 1px solid #dddddd; text-align: left; padding: 8px;">Email</th>
            <th style="border: 1px solid #dddddd; text-align: left; padding: 8px;">Address</th>
            <th style="border: 1px solid #dddddd; text-align: left; padding: 8px;">Postal Code</th>
            <th style="border: 1px solid #dddddd; text-align: left; padding: 8px;">City</th>
            <th style="border: 1px solid #dddddd; text-align: center; padding: 8px;">Actions</th>

        </tr>
        <tr>
            <td style="border: 1px solid #dddddd; text-align: left; padding: 8px;">${parameters.partyId}</td>
            <td style="border: 1px solid #dddddd; text-align: left; padding: 8px;">${parameters.firstName}</td>
            <td style="border: 1px solid #dddddd; text-align: left; padding: 8px;">${parameters.lastName}</td>
            <td style="border: 1px solid #dddddd; text-align: left; padding: 8px;">${parameters.email}</td>
            <td style="border: 1px solid #dddddd; text-align: left; padding: 8px;">${parameters.genAddress}</td>
            <td style="border: 1px solid #dddddd; text-align: left; padding: 8px;">${parameters.postalCode}</td>
            <td style="border: 1px solid #dddddd; text-align: left; padding: 8px;">${parameters.city}</td>
            <td style="border: 1px solid #dddddd; text-align: center; padding: 8px;">
             <a href="<@ofbizUrl>updateCustomerInfo?partyId=${parameters.partyId}&firstName=${parameters.firstName}&lastName=${parameters.lastName}&email=${parameters.email}&genAddress=${parameters.genAddress}&postalCode=${parameters.postalCode}&city=${parameters.city}</@ofbizUrl>">
                <button style="padding: 5px 10px; background-color: #4CAF50; color: white; border: none; border-radius: 4px; cursor: pointer;">Update</button>
             </a>
            </td>
        </tr>
    </table>
</div>
