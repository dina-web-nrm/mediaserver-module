package se.nrm.mediaserver.resteasy.util;

/**
 *
 * @author ingimar
 */
import java.io.Serializable;
import javax.ws.rs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

public class FileUploadForm implements UploadForm, Serializable {

    private static final long serialVersionUID = 1L;

    @FormParam("mediaUUID")
    private String mediaUUID;

    @FormParam("selectedFile")
    @PartType("application/octet-stream")
    private byte[] fileData;

    @FormParam("owner")
    private String owner;

    /**
     * i.e: private or public
     */
    @FormParam("access")
    private String access;

    /**
     * '&' is delimiter between key:value
     *
     * i.e : country:sweden&test:true
     */
    @FormParam("tags")
    private String tags;

    @FormParam("legend")
    private String legend;

    @FormParam("legendLanguage")
    private String language;

    @FormParam("licenseType")
    private String licenseType;

    @FormParam("fileName")
    private String fileName;

    @FormParam("alt")
    private String alt;

    @FormParam("exportImage")
    private Boolean export;

    @FormParam("startTime")
    private String startTime;

    @FormParam("endTime")
    private String endTime;

    @FormParam("comment")
    private String comment;

    @FormParam("displayOrder")
    private String displayOrder;

    @FormParam("taglist")
    private String[] taglist;

    public FileUploadForm() {
        export = false;

//        String[] mock = {"tag1:value1", "tag2:value2", "tag3:value3"};
//        taglist = mock;
    }

    public String[] getTaglist() {
        return this.taglist;
    }

    public void setTaglist(String[] taglist) {
        this.taglist = taglist;
    }

    public String getMediaUUID() {
        return mediaUUID;
    }

    public void setMediaUUID(String mediaUUID) {
        this.mediaUUID = mediaUUID;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
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

    public String licenceType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public Boolean isExport() {
        return export;
    }

    public void setExport(Boolean export) {
        this.export = export;
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
}
