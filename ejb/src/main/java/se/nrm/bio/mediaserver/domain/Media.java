package se.nrm.bio.mediaserver.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Will wait to implement hashcode/equals. hascode and equals ...
 * http://stackoverflow.com/questions/5031614/the-jpa-hashcode-equals-dilemma
 * http://michalorman.com/2012/07/do-not-override-equals-and-hashcode-for-entities/
 *
 * @author ingimar
 */
@Entity
@Table(name = "MEDIA")
@NamedQueries({
    @NamedQuery(name = Media.FIND_BY_UUID, query = "SELECT c FROM Media c where c.uuid = :uuid"),
    @NamedQuery(name = Media.FIND_ALL, query = "SELECT m FROM Media m")
})
@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlRootElement
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Media implements Serializable {

    private static final long serialVersionUID = -863604661852460140L;

    public static final String FIND_BY_UUID = "Media.FindByUuid";

    public static final String FIND_ALL = "Media.findAll";

    @Id
    @Column(name = "UUID")
    private String uuid;

    @Column(name = "OWNER", length = 255, table = "MEDIA")
    private String owner;

    @Column(name = "VISIBILITY", length = 50, table = "MEDIA")
    private String visibility;

    @Column(name = "FILENAME", length = 255, table = "MEDIA")
    private String filename;

    @Column(name = "MIME_TYPE", length = 50, table = "MEDIA")
    private String mimetype; // anv. Enum

    @Column(name = "RESTFUL_STREAM", length = 255, table = "MEDIA")
    private String mediaURL;

    @Column(name = "TAGS", length = 255, table = "MEDIA")
    private String taggar;

    @Column(name = "ALT", length = 255, table = "MEDIA")
    private String alt;

    @Column(name = "HASH", length = 255, table = "MEDIA")
    private String hash;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "media",
            targetEntity = Tag.class, fetch = FetchType.EAGER)
    @XmlElementWrapper(name = "tags")
    @XmlElement(name = "tag", required = true)
    private Collection<Tag> tags;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "media",
            targetEntity = Determination.class, fetch = FetchType.EAGER)
    @XmlElementWrapper(name = "externalsystems")
    @XmlElement(name = "system", required = true)
    private Collection<Determination> determinations;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "media",
            targetEntity = MediaText.class, fetch = FetchType.EAGER)
    @XmlElementWrapper(name = "descriptions")
    @XmlElement(name = "description", required = true)
    private Set<MediaText> texts; //= new HashSet<>(0);

    @ManyToMany(fetch = FetchType.EAGER) // cascade = {CascadeType.ALL},
    @JoinTable(name = "MEDIA_X_LIC",
            joinColumns = {
                @JoinColumn(name = "MEDIA_ID")},
            inverseJoinColumns = {
                @JoinColumn(name = "LIC_ID")})
    @XmlElementWrapper(name = "licos")
    @XmlElement(name = "lico", required = true)
    private Set<Lic> lics;

    public Set<Lic> getLics() {
        return lics;
    }

    public void setLics(Set<Lic> lics) {
        this.lics = lics;
    }

    public Media() {
        lics = new HashSet<>();
        texts = new HashSet<>(0);
    }

    public Media(String owner) {
        this();
        this.owner = owner;
    }

    /**
     * Testing with JSON from curl.
     *
     * @param owner
     * @param visibility
     * @param filename
     * @param mimetype
     */
    public Media(String owner, String visibility, String filename, String mimetype) {
        this();
        this.setOwner(owner);
        this.visibility = visibility;
        this.filename = filename;
        this.mimetype = mimetype;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        if (owner == null) {
            throw new IllegalArgumentException();
        }
        this.owner = owner;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getMediaURL() {
        return mediaURL;
    }

    public void setMediaURL(String mediaURL) {
        this.mediaURL = mediaURL;
    }

    public Set<MediaText> getTexts() {
        return texts;
    }

    public void setTexts(Set<MediaText> texts) {
        this.texts = texts;
    }

    public void addMediaText(MediaText text) {
        this.texts.add(text);
    }

    public Collection<Tag> getTags() {
        if (tags != null) {
            return Collections.unmodifiableCollection(tags);
        } else {
            return Collections.<Tag>emptyList();
        }
    }

    public void setTags(Collection<Tag> tags) {
        this.tags = tags;
    }

    public boolean addTag(Tag tag) {
        if (tags == null) {
            tags = new LinkedList<Tag>();
        }
        if (tag != null && !tags.contains(tag)) {
            tags.add(tag);
            return true;
        }
        return false;
    }

    public boolean removeTag(Tag tag) {
        return (tags != null && !tags.isEmpty() && tags.remove(tag));
    }

    public Collection<Determination> getDeterminations() {
        if (determinations != null) {
            return Collections.unmodifiableCollection(determinations);
        } else {
            return Collections.<Determination>emptyList();
        }
    }

    public Determination getFirstDetermination() {
        Collection<Determination> collection = this.determinations;
        Determination determination = collection.iterator().next();
        return determination;
    }

    public void setDeterminations(Collection<Determination> determinations) {
        this.determinations = determinations;
    }

    public boolean addDetermination(Determination determination) {
        if (determinations == null) {
            determinations = new LinkedList<>();
        }
        if (determination != null && !determinations.contains(determination)) {
            determinations.add(determination);
            return true;
        }
        return false;
    }

    public boolean removeDetermination(Determination determination) {
        return (determinations != null && !determinations.isEmpty() && determinations.remove(determination));
    }

    public String getTaggar() {
        return taggar;
    }

    public void setTaggar(String taggar) {
        this.taggar = taggar;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public String toString() {
        return this.uuid;
    }
//    @Override
//    public String toString() {
//        final StringBuilder sb = new StringBuilder();
//        sb.append('{');
//        sb.append("\"uuid\":\"").append(uuid).append("\"");
//        sb.append(",\"owner\":\"").append(owner).append("\"");
//        sb.append(",\"visibility\":\"").append(visibility).append("\"");
//        sb.append(",\"filename\":\"").append(filename).append("\"");
//        sb.append(",\"mimetype\":\"").append(mimetype).append("\"");
//        sb.append(",\"taggar\":\"").append(taggar).append("\"");
//        // for the sake of testing
//        if (tags != null) {
//            sb.append(",\"tag size\":\"").append(tags.size()).append("\"");
//        }
//        sb.append('}');
//        return sb.toString();
//    }
}
