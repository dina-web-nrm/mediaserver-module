package se.nrm.mserver.rs;

import java.io.Serializable;
import javax.ws.rs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

/**
 *
 * @author ingimar
 */
public class FileUploadForm implements Serializable{

    public FileUploadForm() {
    }

    @FormParam("filename")
    private String filename;

    @FormParam("owner")
    private String owner;

    @FormParam("licenseType")
    private String licenseType;

    private byte[] data;

    public byte[] getData() {
        return data;
    }

    @FormParam("content")
    @PartType("application/octet-stream")
    public void setData(byte[] data) {
        this.data = data;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }
    
    

}
