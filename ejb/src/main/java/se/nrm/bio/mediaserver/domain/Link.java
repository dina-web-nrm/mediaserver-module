package se.nrm.bio.mediaserver.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ingimar
 */
@Entity
@Table(name = "DETERMINATION")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Link implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "TAG_KEY")
    private String tagKey;

    @Column(name = "TAG_VALUE")
    private String tagValue;

    @Column(name = "EXTERNAL_SYSTEM")
    private String externalSystem;

    @Column(name = "EXTERNAL_SYSTEM_URL")
    private String externalSystemUrl;

    @Column(name = "MEDIA_UUID")
    private String mediaUUID;

    @Column(name = "SORT_ORDER")
    private Integer sortOrder;

    @Transient
    private Date dateCreated;

    public Link() {
        dateCreated = null;
    }

    /**
     *
     * @param typeOfSystem : i.e 'taxon'
     * @param taxonUUID : i.e the UUID in your taxon-system
     * @param nameOfSystem : i.e 'Dyntaxa' or 'Naturforskaren-internal-taxa'
     * @param systemURL : i.e base-url to your taxon-service
     * @param storedMediaUUID : the UUID that you have stored in this system
     */
    public Link(String typeOfSystem, String taxonUUID,
            String nameOfSystem, String systemURL, String storedMediaUUID) {
        this();

        this.tagKey = typeOfSystem;
        this.tagValue = taxonUUID;
        this.externalSystem = nameOfSystem;
        this.externalSystemUrl = systemURL;
        this.mediaUUID = storedMediaUUID;
    }

    public static Link newInstanceWithSortOrder(String typeOfSystem, String taxonUUID,
            String nameOfSystem, String systemURL, String storedMediaUUID,
            Integer sortOrder) {
        Link link = new Link(typeOfSystem, taxonUUID, nameOfSystem, systemURL, storedMediaUUID, sortOrder);
        return link;
    }

    private Link(String typeOfSystem, String taxonUUID,
            String nameOfSystem, String systemURL, String storedMediaUUID,
            Integer sortOrder) {
        this();
        
        this.tagKey = typeOfSystem;
        this.tagValue = taxonUUID;
        this.externalSystem = nameOfSystem;
        this.externalSystemUrl = systemURL;
        this.mediaUUID = storedMediaUUID;
        this.sortOrder = sortOrder;
    }

    public static Link newInstanceWithoutMedia(String typeOfSystem, String taxonUUID, String nameOfSystem, String systemURL) {
        return new Link(typeOfSystem, taxonUUID, nameOfSystem, systemURL);
    }

    public Link(String typeOfSystem, String taxonUUID, String nameOfSystem, String systemURL) {
        this();

        this.tagKey = typeOfSystem;
        this.tagValue = taxonUUID;
        this.externalSystem = nameOfSystem;
        this.externalSystemUrl = systemURL;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTagKey() {
        return tagKey;
    }

    public void setTagKey(String tagKey) {
        this.tagKey = tagKey;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public String getExternalSystem() {
        return externalSystem;
    }

    public void setExternalSystem(String externalSystem) {
        this.externalSystem = externalSystem;
    }

    public String getExternalSystemUrl() {
        return externalSystemUrl;
    }

    public void setExternalSystemUrl(String externalSystemUrl) {
        this.externalSystemUrl = externalSystemUrl;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getMediaUUID() {
        return mediaUUID;
    }

    public void setMediaUUID(String mediaUUID) {
        this.mediaUUID = mediaUUID;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public String toString() {
        return "" + this.id;
    }

}
