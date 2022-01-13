package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.BoatDTO;
import dtos.UserDTO;
import facades.BoatFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/boat")
public class BoatResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final BoatFacade facade = BoatFacade.getBoatFacade(EMF);
    @Context
    SecurityContext securityContext;



    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }



    @POST
    @Path("/createBoat")
    @RolesAllowed("user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createBoat(String newBoat){
        String thisuser = securityContext.getUserPrincipal().getName();
        BoatDTO boatDTO = gson.fromJson(newBoat, BoatDTO.class);
        boatDTO.getOwners().add(new UserDTO(thisuser));
        boatDTO = facade.createBoat(boatDTO);
        return gson.toJson(boatDTO);
    }


    @GET
    @Path("/myBoats")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyBoats(){
        String thisuser = securityContext.getUserPrincipal().getName();
        return Response.ok(gson.toJson(facade.getAllBoatsByUser(thisuser)),"application/json").build();
    }


}