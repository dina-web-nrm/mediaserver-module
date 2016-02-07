package se.nrm.bio.mediaserver.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ingimar
 */
@Entity
@Table(name = "DETERMINATION")
@NamedQueries({
    @NamedQuery(name = "Determination.findAll", query = "SELECT d FROM Determination d"),
    @NamedQuery(name = "Determination.findById", query = "SELECT d FROM Determination d WHERE d.id = :id"),
    @NamedQuery(name = "Determination.findByTagKey", query = "SELECT d FROM Determination d WHERE d.tagKey = :tagKey"),
    @NamedQuery(name = Determination.FIND_BY_EXTERNAL_TAG, query = "SELECT d FROM Determination d WHERE d.tagValue = :tagValue"),
    @NamedQuery(name = Determination.FIND_BY_TAXONUUID_AND_MEDIAUUID,
            query = "SELECT d FROM Determination d WHERE d.tagValue = :tagValue AND d.media.uuid= :mediaUUID"),
    @NamedQuery(name = Determination.FIND_ONE_BY_EXTERNAL_TAG, query = "SELECT DISTINCT d FROM Determination d WHERE d.tagValue = :tagValue"),
    @NamedQuery(name = "Determination.findByExternalSystem", query = "SELECT d FROM Determination d WHERE d.externalSystem = :externalSystem"),
    @NamedQuery(name = "Determination.findByExternalSystemUrl", query = "SELECT d FROM Determination d WHERE d.externalSystemUrl = :externalSystemUrl")
    //    @NamedQuery(name = "Determination.findByDateCreated", query = "SELECT d FROM Determination d WHERE d.dateCreated = :dateCreated")
})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Determination implements Comparable<Determination>, Serializable {

    public static final String FIND_BY_EXTERNAL_TAG = "Determination.findByTagValue";

    public static final String FIND_ONE_BY_EXTERNAL_TAG = "Determination.findOneTagValue";

    public static final String FIND_BY_TAXONUUID_AND_MEDIAUUID = "Determination.findBtTagValueAndMediaUUID";

    private static final long serialVersionUID = 123423432L;

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

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "MEDIA_UUID")
    @XmlTransient
    private Media media;

    @Transient
    private Date dateCreated;

    @Column(name = "SORT_ORDER")
    private int sortOrder;

    public Determination() {
    }

    public Determination(Integer id) {
        this.id = id;
    }

    public Determination(String tagKey, String tagValue, String externalSystem, String externalSystemUrl, Media media) {
        this.tagKey = tagKey;
        this.tagValue = tagValue;
        this.externalSystem = externalSystem;
        this.externalSystemUrl = externalSystemUrl;
        this.media = media;
    }

    public Determination(String tagValue, int sortOrder, Media media) {
        this.tagKey = "NF_Taxon";
        this.tagValue = tagValue;
        this.externalSystem = "NF_SYSTEM";
        this.externalSystemUrl = "http://naturforskaren.se";
        this.sortOrder = sortOrder;
        this.media = media;
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

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append("Determination:");
        sb.append(", id='").append(id).append('\'');
        sb.append(", tagKey='").append(tagKey).append('\'');
        sb.append(", tagValue='").append(tagValue).append('\'');
        sb.append(", externalSystem='").append(externalSystem).append('\'');
        sb.append(", externalSystemUrl='").append(externalSystemUrl).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int compareTo(Determination d) {
        Integer mySortOrder = this.getSortOrder();
        return mySortOrder.compareTo(d.getSortOrder());
    }

}
