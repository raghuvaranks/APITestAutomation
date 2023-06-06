package com.fs.cust.teststeps;

import com.fs.cust.utils.CustomLogFilter;
import com.fs.cust.utils.TestDataReader;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class TestSteps extends BaseTest {
    public Response response;
    public ValidatableResponse json;
    public RequestSpecification request;
    public String resp;
    public String act_desc;
    public String act_aut;
    Properties prop;
    FileReader reader;
    String baseurl;
    private Filter logFilter;
    Map req_bparam = new HashMap();
    Map exp_res= new HashMap();
    String cust_response;
    TestDataReader rdr = new TestDataReader();
    private Scenario scenario;
    public TestSteps() throws IOException {
        prop= new Properties();
        try {
            reader = new FileReader("src//test//resources//api.properties");
            prop.load(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        baseurl=prop.getProperty("baseurl");
        }

        @Before
        public void setup (Scenario scenario) {
            this.scenario = scenario;
            RestAssured.useRelaxedHTTPSValidation();
            logFilter = new CustomLogFilter();
        }

    @Given("invoke the customer api endpt with input {string}")
    public void invoke_the_customer_api_endpt_with_input(String tcnm) {
     RestAssured.baseURI= setup (baseurl, "customer");
     System.out.println(RestAssured.baseURI);
     try {
         req_bparam= rdr.getrq_body(tcnm, "customer");
        } catch (IOException e){
         e.printStackTrace();}
     request = req_body(req_bparam);
    }


    @When("PUT method is called")
    public void put_method_is_called() {
        request.filter(logFilter);
        response = putreq(request);
        if (logFilter instanceof CustomLogFilter) {
            CustomLogFilter customLogFilter = (CustomLogFilter) logFilter;
            scenario.log("\n" + "API Request: " + customLogFilter.getRequsetBuilder() + "\n" + "API Response: "
                    + customLogFilter.getResponseBuilder());
        }
    }

    @Then("status should be {int}")
    public void status_should_be(Integer stcd) {
        validate_responsecd(response,stcd);
    }
    @Then("verify the expected response {string}")
    public void verify_the_expected_response(String tcnm) {
        try{
            exp_res=rdr.getexp_response(tcnm,"customer");
            //get expected value from excel
            String exp_msg=exp_res.get("EXPECTED").toString();
            //get actual value from response -- need to enhance the below line to extract value from json to map
            String act_msg=response.getBody().toString();
            validate_response_data(exp_msg,act_msg);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
