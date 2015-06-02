/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.bio.mediaserver.business;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import se.nrm.bio.mediaserver.domain.AdminConfig;
import se.nrm.bio.mediaserver.domain.Lic;
import se.nrm.bio.mediaserver.domain.LicVersions;

/**
 *
 * @author ingimar
 */
@Stateless
public class AdminBean {

    @PersistenceContext(unitName = "MysqlPU")
    private EntityManager em;

    public List<AdminConfig> getAdminConfig(String env) {
        Query query = em.createNamedQuery(AdminConfig.FIND_BY_ENV);
        query.setParameter("environment", env);
        List<AdminConfig> configs = query.getResultList();
        return configs;
    }

    public List<Lic> getLicenses() {
        Query query = em.createNamedQuery(Lic.FIND_ALL);
        List<Lic> resultList = query.getResultList();
        return resultList;
    }

    public Lic getLicense(String abbrev) {
        Query query = em.createNamedQuery(Lic.FIND_BY_ABBREV);
        query.setParameter("abbrev", abbrev);
        Lic license = (Lic) query.getSingleResult();
        return license;
    }

    public List<LicVersions> getLicensesWithVersion() {
        Query query = em.createNamedQuery(LicVersions.FIND_ALL);
        List<LicVersions> resultList = query.getResultList();
        return resultList;
    }
    
    public LicVersions getLicensesWithAbbrevAndVersion(String abbrev,String version) {
        Query query = em.createNamedQuery(LicVersions.FIND_BY_ABBREV_AND_VERSION);
        query.setParameter("abbrev", abbrev);
        query.setParameter("version", version);
        LicVersions license = (LicVersions) query.getSingleResult();
        return license;
    }
}
