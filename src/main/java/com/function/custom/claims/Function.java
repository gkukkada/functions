package com.function.custom.claims;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Function {

    @FunctionName("HttpTriggerJava")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context){

        context.getLogger().info("Java HTTP trigger function processed a request.");

        // Read the request body
        String requestBody;
        try {
            requestBody = request.getBody().orElse(null);
        } catch (Exception e) {
            context.getLogger().severe("Error reading request body: " + e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        context.getLogger().info(" requestBody ." + requestBody);


        // Deserialize JSON manually
        // Object dataObject = parseJson(requestBody);


        // Extract correlation ID
        // String correlationId = extractCorrelationId(dataObject);

        // Claims to return to Azure AD
        ResponseContent r = new ResponseContent();
        // r.getData().getActions().get(0).getClaims().setCorrelationId(correlationId);
        r.getData().getActions().get(0).getClaims().setApiVersion("1.0.0");
        r.getData().getActions().get(0).getClaims().setDateOfBirth("01/01/2000");
        r.getData().getActions().get(0).getClaims().getCustomRoles().add("Writer");
        r.getData().getActions().get(0).getClaims().getCustomRoles().add("Editor");

        return request.createResponseBuilder(HttpStatus.OK).body(r).build();
    }

    // private String readRequestBody(String inputStream) throws IOException {
    //     StringBuilder stringBuilder = new StringBuilder();
    //     try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
    //         String line;
    //         while ((line = bufferedReader.readLine()) != null) {
    //             stringBuilder.append(line);
    //         }
    //     }
    //     return stringBuilder.toString();
    // }

    private Object parseJson(String jsonString) {
        // Manually parse JSON without using a library (you might need a more robust parser for complex JSON)
        // This is just a simple example for illustration purposes.
        return jsonString; // Assuming the JSON is a simple String for this example.
    }

    private String extractCorrelationId(Object dataObject) {
        // Manually extract correlationId, you need to implement the logic based on your JSON structure.
        // This is just a placeholder and might not work for all JSON structures.
        return dataObject.toString();
    }
}

class ResponseContent {

    private Data data;

    public ResponseContent() {
        this.data = new Data();
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}

class Data {

    private String odatatype;
    private List<Action> actions;

    public Data() {
        this.odatatype = "microsoft.graph.onTokenIssuanceStartResponseData";
        this.actions = new ArrayList<>();
        this.actions.add(new Action());
    }

    public String getOdatatype() {
        return odatatype;
    }

    public void setOdatatype(String odatatype) {
        this.odatatype = odatatype;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }
}

class Action {

    private String odatatype;
    private Claims claims;

    public Action() {
        this.odatatype = "microsoft.graph.tokenIssuanceStart.provideClaimsForToken";
        this.claims = new Claims();
    }

    public String getOdatatype() {
        return odatatype;
    }

    public void setOdatatype(String odatatype) {
        this.odatatype = odatatype;
    }

    public Claims getClaims() {
        return claims;
    }

    public void setClaims(Claims claims) {
        this.claims = claims;
    }
}

class Claims {

    private String correlationId;
    private String dateOfBirth;
    private String apiVersion;
    private List<String> customRoles;

    public Claims() {
        this.customRoles = new ArrayList<>();
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public List<String> getCustomRoles() {
        return customRoles;
    }

    public void setCustomRoles(List<String> customRoles) {
        this.customRoles = customRoles;
    }
}