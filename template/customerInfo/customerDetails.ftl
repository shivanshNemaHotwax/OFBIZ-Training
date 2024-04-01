<#assign serviceName = "getAllPartiesDetails">
<#assign context = {}>
<#assign serviceResult = service.runSync(serviceName, context)>


<head>
    <title>All Parties Details</title>
</head>
<body>
    <h1>All Parties Details</h1>


    <table border="1">
        <thead>
            <tr>
                <th>Party ID</th>
                <th>First Name</th>
                <th>Last Name</th>
            </tr>
        </thead>
        <tbody>

            <#if serviceResult?has_content && serviceResult.get("partyList")?has_content>
                <#list serviceResult.partyList as party>
                    <tr>
                        <td>${party.partyId}</td>
                        <td>${party.firstName}</td>
                        <td>${party.lastName}</td>
                    </tr>
                </#list>
            <#else>
                <tr>
                    <td colspan="3">Failed to fetch party details.</td>
                </tr>
            </#if>

        </tbody>
    </table>
</body>

