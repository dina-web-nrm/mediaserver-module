/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ingimar
 */
@Entity
@Table(name = "ADMIN_MEDIA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = AdminMedia.FIND_ALL, query = "SELECT a FROM AdminMedia a"),
    @NamedQuery(name = AdminMedia.FIND_ALL_IN_ENV, query = "SELECT a FROM AdminMedia a WHERE a.environment = :environment"),
    @NamedQuery(name = "AdminMedia.findById", query = "SELECT a FROM AdminMedia a WHERE a.id = :id"),
    @NamedQuery(name = "AdminMedia.findByIsExif", query = "SELECT a FROM AdminMedia a WHERE a.isExif = :isExif"),
    @NamedQuery(name = "AdminMedia.findByPathToFiles", query = "SELECT a FROM AdminMedia a WHERE a.pathToFiles = :pathToFiles")
})
public class AdminMedia implements Serializable {

    private static final long serialVersionUID = 12343L;

//    public static final String Testing  = "/opt/data/mediaserver/newmedia/";
    public static final String FIND_ALL = "AdminMedia.findAll";

    public static final String FIND_ALL_IN_ENV = "AdminMedia.findAllInEnvironment";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "IS_EXIF")
    private Boolean isExif;

    @Column(name = "PATH_TO_FILES")
    private String pathToFiles;

    @Column(name = "ENVIRONMENT")
    private String environment;

    public AdminMedia() {
    }

    public AdminMedia(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getIsExif() {
        return isExif;
    }

    public void setIsExif(Boolean isExif) {
        this.isExif = isExif;
    }

    public String getPathToFiles() {
        return pathToFiles;
    }

    public void setPathToFiles(String pathToFiles) {
        this.pathToFiles = pathToFiles;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
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
        if (!(object instanceof AdminMedia)) {
            return false;
        }
        AdminMedia other = (AdminMedia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "se.nrm.mediaserver.media3.domain.AdminMedia[ id=" + id + " ]";
    }

}
