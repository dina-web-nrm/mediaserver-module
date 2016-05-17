package se.nrm.bio.mediaserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ingimar
 */
@Entity
@Table(name = "LIC")
@NamedQueries({
    @NamedQuery(name = Lic.FIND_ALL, query = "SELECT l FROM Lic l"),
    @NamedQuery(name = "Lic.findById", query = "SELECT l FROM Lic l WHERE l.id = :id"),
    @NamedQuery(name = Lic.FIND_BY_ABBREV, query = "SELECT l FROM Lic l WHERE l.abbrev = :abbrev"),
    @NamedQuery(name = Lic.FIND_BY_ABBREV_AND_VERSION,
            query = "SELECT l FROM Lic l WHERE l.abbrev = :abbrev and l.version=:version"),
    @NamedQuery(name = "Lic.findByIssuer", query = "SELECT l FROM Lic l WHERE l.issuer = :issuer"),
    @NamedQuery(name = "Lic.findByUri", query = "SELECT l FROM Lic l WHERE l.uri = :uri"),
    @NamedQuery(name = "Lic.findByName", query = "SELECT l FROM Lic l WHERE l.name = :name")})
@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlRootElement
public class Lic implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String FIND_ALL = "Lic.findAll";

    public static final String FIND_BY_ABBREV = "Lic.findByAbbrev";

    public static final String FIND_BY_ABBREV_AND_VERSION = "Lic.findByAbbrevAndVersion";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    @JsonIgnore
    private Integer id;

    @Column(name = "ABBREV")
    private String abbrev;

    @Column(name = "VERSION")
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Column(name = "ISSUER")
    private String issuer;

    @Column(name = "URI")
    private String uri;

    @Column(name = "NAME")
    private String name;

//    @ManyToMany(mappedBy = "lics", fetch = FetchType.LAZY)
//    @XmlTransient
//    private Set<Image> media = new HashSet<>();
    public Lic() {
    }

//    public Lic(String abbrev) {
//        this.abbrev = abbrev;
//    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAbbrev() {
        return abbrev;
    }

    public void setAbbrev(String abbrev) {
        this.abbrev = abbrev;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Lic)) {
            return false;
        }
        Lic other = (Lic) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "se.nrm.mediaserver.media3.domain.Lic[ id=" + id + " ]";
    }

}
