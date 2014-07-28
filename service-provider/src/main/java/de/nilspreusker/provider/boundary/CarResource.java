package de.nilspreusker.provider.boundary;

import de.nilspreusker.provider.entity.Car;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

/**
 * @author Nils Preusker - n.preusker@gmail.com - http://www.nilspreusker.de
 */
@Stateless
@Path("car")
public class CarResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCar(@Context UriInfo uriInfo, Car car) {
        return Response.created(URI.create(uriInfo.getRequestUri().toString() + "/1")).build();
    }
}
