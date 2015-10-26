package se.nrm.bio.mediaserver.rs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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

    @GET
    @Path("/all/{mediaType}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Media> getMediaType(@PathParam("mediaType") String mediaType) {
        int limit = 10;
        return this.getMediaTypeWithLimit(mediaType, limit);
    }

    // 2015-10-26
    @GET
    @Path("/all/{mediaType}/limit/{limit}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Media> getMediaTypeWithLimit(@PathParam("mediaType") String mediaType, @PathParam("limit") int limit) {
        List<Media> mediaList = new ArrayList<>();
        if (limit < 0 || limit > 100) {
            limit = 10;
        }
        String jpql = mediaType.concat(".findAll");
        try {
            mediaList = service.getXImages(jpql, limit);
        } catch (Exception ex) {
            logger.info(ex);
            return Collections.EMPTY_LIST;
        }
        return mediaList;
    }
    /**
     * Returning list in a 'Response' : 
     *  http://www.adam-bien.com/roller/abien/entry/jax_rs_returning_a_list
     * @param from
     * @param to
     * @return 
     */
    @GET
    @Path("/images/{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public Response getRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        if (from > to || (to-from) > 1000) {
           return Response.status(Response.Status.NOT_FOUND).build();
        }
        List<Media> range = service.findRange(Image.class, new int[]{from, to});
        GenericEntity<List<Media>> list = new GenericEntity<List<Media>>(range) {};
        Response build = Response.ok(list).build();
        return build;
    }
}
