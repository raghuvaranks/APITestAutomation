package com.fs.cust.teststeps;

import com.fs.cust.model.Customer;
import com.fs.cust.model.address;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.junit.Assert;

import java.util.Iterator;
import java.util.Map;

public class BaseTest {
    public Response response;
    public RequestSpecification request;
    public ValidatableResponse jsn;

    //method to add end point details to the base url
    public String setup(String baseurl, String endpt_nm){return baseurl+endpt_nm;}

    //method to build query paratmeters with given input values from map
    public RequestSpecification req_mapqryparam(Map req_qparam){
        request = RestAssured.given().queryParams(req_qparam);
        return request;
    }

    //method to build path paratmeters with given input values from map
    public RequestSpecification req_mappathparam(Map req_pparam){
        request = RestAssured.given().pathParams(req_pparam);
        return request;
    }


        //method to build request body with given input values from map
        public RequestSpecification req_body(Map req_body){
        Customer customer = new Customer();
        customer.setFirstName(req_body.get("firstName").toString());
        customer.setLastName(req_body.get("lastName").toString());
        customer.setPhoneNumber(req_body.get("phoneNumber").toString());

        address addr = new address();
        addr.setAddrLine1(req_body.get("addr1").toString());
        addr.setAddrLine2(req_body.get("addr2").toString());
        addr.setCity(req_body.get("city").toString());
        addr.setPostalCode(req_body.get("pstlcd").toString());
        addr.setCountry(req_body.get("country").toString());

        customer.setAddress(addr);

        JSONObject jsonCust = new JSONObject(customer);
        if(jsonCust!=null){
            Iterator<String> itr= jsonCust.keys();
            while(itr.hasNext()){
                String key=itr.next();
                Object obj=jsonCust.get(key);
                if(obj==null||obj==JSONObject.NULL||obj.equals("")||obj.equals("null")){
                    itr.remove();
                }
            }
        }


        request = RestAssured.given().contentType("application/json").accept("application/json").body(jsonCust);
        return request;
    }

    //method to build paratmeters with given input values from map
    public RequestSpecification req_mapparam(Map req_param){
        request = RestAssured.given().params(req_param);
        return request;
    }

    //method to build request body with given input values from java object
    public RequestSpecification req_objbody(Object req_body){
        request = RestAssured.given().body(req_body);
        return request;
    }

    //method to call PUT method of an API
    public Response putreq(RequestSpecification request){
        response = request.put();
        return response;
    }

    //method to call POST method of an API
    public Response postreq(RequestSpecification request){
        response = request.post();
        return response;
    }
    //method to call DELETE method of an API
    public Response deletereq(RequestSpecification request){
        response = request.delete();
        return response;
    }

    //method to call GET method of an API
    public Response getreq(RequestSpecification request){
        request.redirects().follow(false);
        response = request.get();
        return response;
    }

    //method to validate http status code
    public void validate_responsecd(Response response,Integer st_cd){
        jsn=response.then().statusCode(st_cd);
    }

    //method to validate response value
    public void validate_response_data(String expected,String actual){
        Assert.assertEquals(expected,actual);
    }

}
