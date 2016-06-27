package se.nrm.bio.mediaserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@NamedQueries({
    @NamedQuery(name = "Tag.findAll", query = "SELECT t FROM Tag t"),
    @NamedQuery(name = "Tag.findById", query = "SELECT t FROM Tag t WHERE t.id = :id"),
    @NamedQuery(name = "Tag.findByTagKey", query = "SELECT t FROM Tag t WHERE t.tagKey = :tagKey"),
    @NamedQuery(name = Tag.FIND_KEY_VALUE, query = "SELECT t FROM Tag t WHERE t.tagKey = :tagKey and t.tagValue = :tagValue"),
    @NamedQuery(name = "Tag.findByTagValue", query = "SELECT t FROM Tag t WHERE t.tagValue = :tagValue")
})
@Table(name = "TAGS")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Tag implements Serializable {

    private static final long serialVersionUID = 434343L;

    public static final String FIND_KEY_VALUE = "Media.findByKeyValue";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    @JsonIgnore
    private Integer id;

    @Column(name = "TAG_KEY")
    private String tagKey;

    @Column(name = "TAG_VALUE")
    private String tagValue;

//    @Size(max = 255)
//    @Column(name = "tags")
//    private String tags;

    @Transient 
    @JsonIgnore
    private Date dateCreated;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "MEDIA_UUID")
    @XmlTransient
    private Media media;

    public Tag() {
    }

    public Tag(Integer id) {
        this.id = id;
    }
//
//    public Tag(Integer id, Date dateCreated) {
//        this.id = id;
//        this.dateCreated = dateCreated;
//    }

    public Tag(String tagKey, String tagValue, Media media) {
        this.tagKey = tagKey;
        this.tagValue = tagValue;
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

//    public String getTags() {
//        return tags;
//    }
//
//    public void setTags(String tags) {
//        this.tags = tags;
//    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append("Media:");
        sb.append(", id='").append(id).append('\'');
        sb.append(", tagKey='").append(tagKey).append('\'');
        sb.append(", tagValue='").append(tagValue).append('\'');
        sb.append(", dateCreated='").append(dateCreated).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
