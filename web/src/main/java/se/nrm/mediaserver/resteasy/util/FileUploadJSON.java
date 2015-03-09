package se.nrm.mediaserver.resteasy.util;

/**
 * For the media-migration from naturforskaren.se to the mediaserver
 * 
 * @author ingimar
 */
public class FileUploadJSON {

    private String mediaUUID;

    private String fileDataBase64;

    private String owner;

    private String access;

    private String tags;

    private String legend;

    private String language;

    private String licenseType;

    private String fileName;

    private String alt;

    private Boolean export;

    private String startTime;

    private String endTime;

    private String comment;

    private String displayOrder;

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

    /**
     * i.e: private or public
     */
    public void setAccess(String access) {
        this.access = access;
    }

    public String getTags() {
        return tags;
    }

    /**
     * '&' is delimiter between key:value
     *
     * i.e : country:sweden&test:true
     */
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

    public Boolean getExport() {
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
