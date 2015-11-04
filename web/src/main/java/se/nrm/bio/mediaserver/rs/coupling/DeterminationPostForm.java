package se.nrm.bio.mediaserver.rs.coupling;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import se.nrm.bio.mediaserver.business.MediaCouplingBean;
import se.nrm.bio.mediaserver.domain.Determination;

/**
 *
 * @author ingimar
 */
@Path("/link")
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class DeterminationPostForm {

    @EJB
    private MediaCouplingBean bean;

    @POST
    @Path("/post")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("text/plain")
    public Response createNewCoupling(Determination d) {
        writeToDatabase(d);
        String responseOutput = Response.Status.OK.toString();
        return Response.status(200).entity(responseOutput).build();
    }

    private void writeToDatabase(Determination d) {
        bean.save(d);
    }
}
