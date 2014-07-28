package de.nilspreusker.consumer.boundary;

import de.nilspreusker.consumer.control.CarOrderService;
import de.nilspreusker.consumer.entity.CarOrder;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Nils Preusker - n.preusker@gmail.com - http://www.nilspreusker.de
 */
@Path("/car-order")
public class CarOrderResource {

    @Inject
    CarOrderService carOrderService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCarOrder(CarOrder carOrder) {
        carOrderService.placeCarOrder(carOrder);
        return Response.ok().build();
    }

}
