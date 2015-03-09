package se.nrm.mediaserver.resteasy.util;

import java.io.Serializable;
import javax.ws.rs.FormParam;

/**
 *
 * @author ingimar
 */
public class SingleLinkUploadForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @FormParam("typeOfSystem")
    private String typeOfSystem;

    @FormParam("taxonUUID")
    private String taxonUUID;

    @FormParam("nameOfSystem")
    private String nameOfSystem;

    @FormParam("systemURL")
    private String systemURL;

    @FormParam("mediaUUID")
    private String mediaUUID;
    
    @FormParam("sortOrder")
    private String sortOrder;

    public SingleLinkUploadForm() {
    }

    public SingleLinkUploadForm(String typeOfSystem, String taxonUUID, String nameOfSystem, String systemURL, String mediaUUID, String sortOrder) {
        this.typeOfSystem = typeOfSystem;
        this.taxonUUID = taxonUUID;
        this.nameOfSystem = nameOfSystem;
        this.systemURL = systemURL;
        this.mediaUUID = mediaUUID;
        this.sortOrder = sortOrder;
    }

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

    public String getMediaUUID() {
        return mediaUUID;
    }

    public void setMediaUUID(String mediaUUID) {
        this.mediaUUID = mediaUUID;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
    
}
