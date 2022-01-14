package rest;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import dtos.AuctionDTO;
import dtos.BoatDTO;
import entities.Role;
import entities.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;
import utils.SetupTestUsers;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;


class AuctionResourceTest {


    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static HttpServer httpServer;
    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static EntityManagerFactory emf;
    private static String securityToken;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    private static void login(String role, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");
//        System.out.println("TOKEN ---> " + securityToken);
    }


    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        SetupTestUsers.setupUsers(emf);

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }


    public static void closeTestServer() {
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        httpServer.shutdownNow();
    }

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void serverIsRunning() {
        given().when().get("/auction").then().statusCode(200);
    }


    //@Test
    public void createAuction() {
        login("admin", "admin1");

        JSONObject requestParams = new JSONObject();
        requestParams.put("time", "10:25:00");
        requestParams.put("date", "2023-06-14");
        requestParams.put("name", "The Big Thing");
        requestParams.put("location", "New york");
        System.out.println(requestParams);

        given()
                .contentType("application/json")
                .header("x-access-token",securityToken)
                .body(requestParams.toString())
                .when()
                .post("/auction/createAuction")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", greaterThan(0));
    }


    //@Test
    public void getAllAuctions() {
        login("admin", "admin1");
        createAuction();
        createAuction();

        given()
                .contentType("application/json")
                .header("x-access-token",securityToken)
                .get("/auction/allAuction").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("all", greaterThan(1));
    }

    //TODO : Need to be implemented.
    @Test
    public void editAuction(){

    }

}
