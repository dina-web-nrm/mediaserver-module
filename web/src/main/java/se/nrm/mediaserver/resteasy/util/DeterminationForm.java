package se.nrm.mediaserver.resteasy.util;

import javax.ws.rs.FormParam;

/**
 *
 * @author ingimar
 */
public class DeterminationForm {
   

    @FormParam("fileName")
    private String fileName;

    @FormParam("owner")
    private String owner;

    @FormParam("access")
    private String access;

    @FormParam("tags")
    private String tags;

    @FormParam("legend")
    private String legend;

    @FormParam("language")
    private String language;
}
