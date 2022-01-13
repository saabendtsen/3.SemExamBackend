package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.AuctionDTO;
import facades.AuctionFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/auction")
public class AuctionResource {


    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final AuctionFacade facade = AuctionFacade.getAuctionFacade(EMF);

    @Context
    SecurityContext securityContext;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }


    @POST
    @Path("/createAuction")
    @RolesAllowed("admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAuction(String newAction){
        AuctionDTO auctionDTO = gson.fromJson(newAction,AuctionDTO.class);
       return Response.ok(gson.toJson(facade.createAuction(auctionDTO)),"application/json").build();
    }

    @GET
    @Path("/allAuction")
    @RolesAllowed({"admin","user"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAuctions(){
        return Response.ok(gson.toJson(facade.getAllAuction()),"application/json").build();
    }

}
