package se.nrm.mediaserver.resteasy.util;

import java.io.Serializable;
import javax.ws.rs.FormParam;

/**
 *
 * @author ingimar
 */
public class LinkUploadForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @FormParam("typeOfSystem")
    private String typeOfSystem;

    @FormParam("taxonUUID")
    private String taxonUUID;

    @FormParam("nameOfSystem")
    private String nameOfSystem;

    @FormParam("systemURL")
    private String systemURL;

    @FormParam("mediaList")
    private String[] mediaList;

    public String getTypeOfSystem() {
        return typeOfSystem;
    }

    public void setTypeOfSystem(String typeOfSystem) {
        this.typeOfSystem = typeOfSystem;
    }

    public String getTaxonUUID() {
        return taxonUUID;
    }

    public void setTaxonUUID(String taxonUUID) {
        this.taxonUUID = taxonUUID;
    }

    public String getNameOfSystem() {
        return nameOfSystem;
    }

    public void setNameOfSystem(String nameOfSystem) {
        this.nameOfSystem = nameOfSystem;
    }

    public String getSystemURL() {
        return systemURL;
    }

    public void setSystemURL(String systemURL) {
        this.systemURL = systemURL;
    }

    public String[] getMediaList() {
        return mediaList;
    }

    public void setMediaList(String[] mediaList) {
        this.mediaList = mediaList;
    }

}
