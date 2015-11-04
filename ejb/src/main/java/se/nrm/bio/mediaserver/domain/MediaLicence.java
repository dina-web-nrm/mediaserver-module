package se.nrm.bio.mediaserver.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ingimar
 */
@Entity
@Table(name = "MEDIA_X_LICENSE")
@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlRootElement
public class MediaLicence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "MEDIA_UUID", length = 255, table = "MEDIA_X_LICENSE")
    private String mediaUUID;

    @Id
    @Column(name = "LIC_ID", length = 255, table = "MEDIA_X_LICENSE")
    private int licID;

    public MediaLicence() {
    }

    public MediaLicence(String mediaUUID, int licID) {
        this.mediaUUID = mediaUUID;
        this.licID = licID;
    }

    public String getMediaUUID() {
        return mediaUUID;
    }

    public void setMediaUUID(String mediaUUID) {
        this.mediaUUID = mediaUUID;
    }

    public int getLicID() {
        return licID;
    }

    public void setLicID(int licID) {
        this.licID = licID;
    }

}
