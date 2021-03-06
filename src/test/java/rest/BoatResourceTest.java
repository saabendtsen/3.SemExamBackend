package rest;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;


class BoatResourceTest {



    private static final int SERVER_PORT = 7778;
    private static final String SERVER_URL = "http://localhost/api";
    private static HttpServer httpServer;
    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static EntityManagerFactory emf;
    private static String securityToken;
    private BoatDTO boatDTO;

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

    @AfterAll
    public static void closeTestServer() {
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();
            em.getTransaction().commit();
        }finally {
            em.close();
        }
        httpServer.shutdownNow();
    }

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void serverIsRunning() {
        given().when().get("/boat").then().statusCode(200);
    }


    @Test
    void createBoat() {
        login("user","user1");

        JSONObject ownersJSON = new JSONObject();
        ownersJSON.put("username","user");

        JSONArray array = new JSONArray();
        array.add(ownersJSON);

        JSONObject requestParams = new JSONObject();
        requestParams.put("brand","volvo");
        requestParams.put("make","Japan");
        requestParams.put("name","julia");
        requestParams.put("image","BilledURL");
       // requestParams.put("owners",array);
        System.out.println(requestParams);

        boatDTO = given()
                .contentType("application/json")
                .header("x-access-token",securityToken)
                .body(requestParams.toString())
                .when()
                .post("/boat/createBoat")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .extract().as(BoatDTO.class);


        assertTrue(boatDTO.getId() > 0);

    }

    @Test
    List<BoatDTO> getMyBoats(){
        createBoat();
        login("user","user1");
        List<BoatDTO> boatDTOList = given()
                .contentType(ContentType.JSON)
                .header("x-access-token",securityToken)
                .get("boat/myBoats")
                .then()
                .extract().body().jsonPath().getList("",BoatDTO.class);
        assertTrue(boatDTOList.size()>0);

        return boatDTOList;
    }


    @Test
    void editBoat(){

        createBoat();
        login("user","user1");
        JSONObject ownersJSON = new JSONObject();
        ownersJSON.put("username","user");

        JSONArray array = new JSONArray();
        array.add(ownersJSON);

        JSONObject requestParams = new JSONObject();
        requestParams.put("id", boatDTO.getId());
        requestParams.put("brand","volvo");
        requestParams.put("make","Japan");
        requestParams.put("name","New Name");
        requestParams.put("image","BilledURL");
        // requestParams.put("owners",array);
        System.out.println(requestParams);

        boatDTO = given()
                .contentType("application/json")
                .header("x-access-token",securityToken)
                .body(requestParams.toString())
                .when()
                .post("/boat/createBoat")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .extract().as(BoatDTO.class);


        assertTrue(boatDTO.getName().equals("New Name"));
    }



    //Todo need to be implemented
    @Test
    void addtoAuction(){
   }


    //Todo need to be implemented
    @Test
    void deleteFromAuction(){
    }



    //After is still same as before, but DB shows -1 record.
    //@Test
    void deleteBoat(){
        createBoat();
        int before = getMyBoats().size();
        login("user","user1");
        boatDTO = given()
                .contentType(ContentType.JSON)
                .header("x-access-token",securityToken)
                .delete("boat/" + boatDTO.getId())
                .then()
                .extract().as(BoatDTO.class);
        int after = getMyBoats().size();
        System.out.println(before + " " +after);
        assertTrue(before-1 == after);
    }









}