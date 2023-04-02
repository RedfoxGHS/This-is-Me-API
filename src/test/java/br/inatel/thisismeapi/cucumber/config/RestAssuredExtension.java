package br.inatel.thisismeapi.cucumber.config;

import br.inatel.thisismeapi.services.impl.AdminServiceImpl;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.RequestSpecification;

public class RestAssuredExtension {

    public static final String BASE_URL = "https://timeapibyredfoxghs.herokuapp.com";

    public static RequestSpecification REQUEST;

    public static RequestSpecification REQUEST_ADMIN;


    public RestAssuredExtension() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri(BASE_URL);
        requestSpecBuilder.setContentType("application/json");
        var requestSpec = requestSpecBuilder.build();
        REQUEST = RestAssured.given().spec(requestSpec);

        RequestSpecBuilder requestSpecBuilderAdmin = new RequestSpecBuilder();
        requestSpecBuilderAdmin.setBaseUri(BASE_URL);
        requestSpecBuilderAdmin.setContentType("application/json");
        requestSpecBuilderAdmin.addCookie("token", this.LoginWithAdmin());
        var requestSpecAdmin = requestSpecBuilderAdmin.build();
        REQUEST_ADMIN = RestAssured.given().spec(requestSpecAdmin);
    }

    public static ResponseOptions<Response> GetOps(String url) {
        clearQueryParams();
        try {
            return REQUEST.get(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResponseOptions<Response> PostOps(String url, Object body) {
        try {
            var respose = REQUEST.body(body).post(url);
            var token = respose.getCookie("token");
            return REQUEST.body(body).post(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResponseOptions<Response> PutOps(String url, Object body) {
        try {
            return REQUEST.body(body).put(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResponseOptions<Response> DeleteOps(String url) {
        try {
            return REQUEST.delete(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResponseOptions<Response> PatchOps(String url, Object body) {
        try {
            return REQUEST.body(body).patch(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String LoginWithAdmin() {
        try {
            var response =  REQUEST.body("{\"email\":\"admin@admin.com\", " + "\"password\":\"admin456\"}").post("/user/login");
            return response.getCookie("token");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResponseOptions<Response> LoginWithUser(String url, Object body) {
        RequestSpecBuilder requestSpecBuilderLogged = new RequestSpecBuilder();
        requestSpecBuilderLogged.setBaseUri(BASE_URL);
        requestSpecBuilderLogged.setContentType("application/json");

        ResponseOptions<Response> response = null;
        try {
            response = REQUEST.body(body).post(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response != null) {
            requestSpecBuilderLogged.addCookie("token", response.getCookie("token"));
        }

        var requestSpecLogged = requestSpecBuilderLogged.build();
        REQUEST = RestAssured.given().spec(requestSpecLogged);

        return response;
    }

    public static void loggout() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri(BASE_URL);
        requestSpecBuilder.setContentType("application/json");
        var requestSpec = requestSpecBuilder.build();
        REQUEST = RestAssured.given().spec(requestSpec);
    }

    public static ResponseOptions<Response> DeleteAdminOps(String url) {
        clearQueryParams();

        try {
            return REQUEST_ADMIN.delete(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResponseOptions<Response> GetAdminOps(String url) {
        try {
            return REQUEST_ADMIN.get(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void clearQueryParams() {
        FilterableRequestSpecification request = (FilterableRequestSpecification) REQUEST_ADMIN;
        request.removeQueryParam("email");
    }
}
