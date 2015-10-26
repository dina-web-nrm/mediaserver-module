package se.nrm.bio.mediaserver.rs;

import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import se.nrm.bio.mediaserver.business.MediaserviceBean;
import se.nrm.bio.mediaserver.domain.Media;

import org.apache.log4j.Logger;
import se.nrm.bio.mediaserver.domain.Image;

/**
 *
 * @author ingimar
 */
@Path("")
public class MediaResource {

    private final static Logger logger = Logger.getLogger(MediaResource.class);

    @EJB
    private MediaserviceBean service;

    /**
     * http://localhost:8080/wildfly-ejb-in-ear/media/meta/00036f71-465c-4443-88e3-370f71fe1d84
     * http://localhost:8080/wildfly-ejb-in-ear/rest/meta/00036f71-465c-4443-88e3-370f71fe1d84
     *
     * @param mediaUUID
     * @return
     */
    @GET
    @Path("/meta/{uuid}")
    @Produces({MediaType.APPLICATION_JSON})
    public Media getMetadata(@PathParam("uuid") String mediaUUID) {
        Media media = (Media) service.get(mediaUUID);
        return media;
    }

    @GET
    @Path("/search/{tags}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Media> getMediaMetadataByLangAndTags(@PathParam("tags") String tags) {
        List<Media> medLiaList = service.getMetadataByTags_MEDIA(tags);
        return medLiaList;
    }

    /**
     * Returning list in a 'Response' :
     * http://www.adam-bien.com/roller/abien/entry/jax_rs_returning_a_list
     *
     * @param minid
     * @param maxid
     * @return
     */
    @GET
    @Path("/images")
    @Produces({"application/xml", "application/json"})
    public Response getRangeOfImages(@QueryParam("minid") Integer minid, @QueryParam("maxid") Integer maxid) {
        final int defaultLimitSize = 20;

        if (minid == null || maxid == null) {
            minid = 0;
            maxid = defaultLimitSize;
        }

        if (minid > maxid || (maxid - minid) > 1000) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<Media> range = service.findRange(Image.class, new int[]{minid, maxid});
        GenericEntity<List<Media>> list = new GenericEntity<List<Media>>(range) {
        };

        Response build = Response.ok(list).build();
        return build;
    }
    
    @GET
    @Path("/images/count")
    @Produces("text/plain")
    public Response countImages() {
        int count = service.count(Image.class);
        return Response.ok(count).build();
    }
}
