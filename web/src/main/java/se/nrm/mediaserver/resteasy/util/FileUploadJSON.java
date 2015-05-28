package se.nrm.mediaserver.resteasy.util;

import java.io.Serializable;
import java.util.List;
import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ingimar
 */
@XmlRootElement
public class FileUploadJSON implements Serializable {

    private static final long serialVersionUID = 5008278244961718845L;

    private String mediaUUID;

    private String fileDataBase64;

    private String owner;

    /**
     * i.e: private or public
     */
    private String access;

    /**
     * '&' is delimiter between key:value
     *
     * i.e : country:sweden&test:true
     */
    @FormParam("tags")
    private String tags;

    @FormParam("taggar")
    public String[] taggar;

    @FormParam("legends")
    public LegendLang[] legends;

    private String legend;

    private String language;

    private String licenseType;

    private String fileName;

    private String alt;

    private String startTime;

    private String endTime;

    private String comment;

    private String displayOrder;

    private Boolean export = false;

    public FileUploadJSON() {
    }

    public String getMediaUUID() {
        return mediaUUID;
    }

    public void setMediaUUID(String mediaUUID) {
        this.mediaUUID = mediaUUID;
    }

    public String getFileDataBase64() {
        return fileDataBase64;
    }

    public void setFileDataBase64(String fileDataBase64) {
        this.fileDataBase64 = fileDataBase64;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String[] getTaggar() {
        return taggar;
    }

    public void setTaggar(String[] taggar) {
        this.taggar = taggar;
    }

    public LegendLang[] getLegends() {
        return legends;
    }

    public void setLegends(LegendLang[] legends) {
        this.legends = legends;
    }

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getExport() {
        return export;
    }

    public void setExport(Boolean export) {
        this.export = export;
    }

    @Override
    public String toString() {
        return this.mediaUUID;
    }
}
