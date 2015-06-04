package se.nrm.bio.mediaserver.domain;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ingimar
 */
@Entity
@Table(name = "LIC_VERSIONS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = LicVersions.FIND_ALL, query = "SELECT l FROM LicVersions l"),
    @NamedQuery(name = "LicVersions.findById", query = "SELECT l FROM LicVersions l WHERE l.id = :id"),
    @NamedQuery(name = "LicVersions.findByAbbrev", query = "SELECT l FROM LicVersions l WHERE l.abbrev = :abbrev"),
    @NamedQuery(name = LicVersions.FIND_BY_ABBREV_AND_VERSION,
            query = "SELECT l FROM LicVersions l WHERE l.abbrev = :abbrev and l.version=:version"),
    @NamedQuery(name = "LicVersions.findByVersion", query = "SELECT l FROM LicVersions l WHERE l.version = :version"),
    @NamedQuery(name = "LicVersions.findByUri", query = "SELECT l FROM LicVersions l WHERE l.uri = :uri"),
    @NamedQuery(name = "LicVersions.findByIssuer", query = "SELECT l FROM LicVersions l WHERE l.issuer = :issuer"),
    @NamedQuery(name = "LicVersions.findByName", query = "SELECT l FROM LicVersions l WHERE l.name = :name")})
public class LicVersions implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String FIND_ALL = "LicVersions.findAll";

    public static final String FIND_BY_ABBREV_AND_VERSION = "LicVersions.findByAbbrevAndVersion";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;

    @Size(max = 255)
    @Column(name = "ABBREV")
    private String abbrev;

    @Size(max = 255)
    @Column(name = "VERSION")
    private String version;

    @Size(max = 255)
    @Column(name = "URI")
    private String uri;

    @Size(max = 255)
    @Column(name = "ISSUER")
    private String issuer;

    @Size(max = 255)
    @Column(name = "NAME")
    private String name;

    public LicVersions() {
    }

    public LicVersions(Integer id) {
        this.id = id;
    }

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
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
        if (!(object instanceof LicVersions)) {
            return false;
        }
        LicVersions other = (LicVersions) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "se.nrm.bio.mediaserver.domain.LicVersions[ id=" + id + " ]";
    }

}
