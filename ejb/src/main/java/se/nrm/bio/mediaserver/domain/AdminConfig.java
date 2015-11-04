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
@Table(name = "ADMIN_CONFIG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = AdminConfig.FIND_ALL, query = "SELECT a FROM AdminConfig a"),
    @NamedQuery(name = "AdminConfig.findById", query = "SELECT a FROM AdminConfig a WHERE a.id = :id"),
    @NamedQuery(name = AdminConfig.FIND_BY_ENV, query = "SELECT a FROM AdminConfig a WHERE a.environment = :environment"),
    @NamedQuery(name = "AdminConfig.findByAdminKey", query = "SELECT a FROM AdminConfig a WHERE a.adminKey = :adminKey"),
    @NamedQuery(name = "AdminConfig.findByAdminValue", query = "SELECT a FROM AdminConfig a WHERE a.adminValue = :adminValue")})
public class AdminConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String FIND_ALL = "AdminConfig.findAll";

    public static final String FIND_BY_ENV = "AdminConfig.findByEnvironment";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "environment")
    private String environment;

    @Basic(optional = false)
    @Column(name = "admin_key")
    private String adminKey;

    @Column(name = "admin_value")
    private String adminValue;

    public AdminConfig() {
    }

    public AdminConfig(Integer id) {
        this.id = id;
    }

    public AdminConfig(Integer id, String environment, String adminKey) {
        this.id = id;
        this.environment = environment;
        this.adminKey = adminKey;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getAdminKey() {
        return adminKey;
    }

    public void setAdminKey(String adminKey) {
        this.adminKey = adminKey;
    }

    public String getAdminValue() {
        return adminValue;
    }

    public void setAdminValue(String adminValue) {
        this.adminValue = adminValue;
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
        if (!(object instanceof AdminConfig)) {
            return false;
        }
        AdminConfig other = (AdminConfig) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "se.nrm.mediaserver.media3.domain.AdminConfig[ id=" + id + " ]";
    }

}
