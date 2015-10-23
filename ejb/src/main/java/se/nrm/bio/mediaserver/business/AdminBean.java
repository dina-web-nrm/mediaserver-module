/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.bio.mediaserver.business;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import se.nrm.bio.mediaserver.domain.AdminConfig;
import se.nrm.bio.mediaserver.domain.Lic;

/**
 *
 * @author ingimar
 */
@Stateless
public class AdminBean {
    
    private final static Logger logger = Logger.getLogger(AdminBean.class);

    @EJB
    private StartupBean envBean;

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

    public Lic fetchNewLicenseFromDB(String abbrevAndLicense) {
        ConcurrentHashMap envMap = envBean.getEnvironment();

        Query namedQuery = em.createNamedQuery(Lic.FIND_BY_ABBREV_AND_VERSION);

        String trimmedAbbrevation = abbrevAndLicense.trim();
        String version = "";
        String licenseType = "";

        int indexOfVersion = trimmedAbbrevation.indexOf('v');
        if (indexOfVersion == -1) {
            version = (String) envMap.get("default_CC_license");
            version = version.substring(1);
            licenseType=abbrevAndLicense;
        } else {
            version = abbrevAndLicense.substring(indexOfVersion + 1);
            licenseType = abbrevAndLicense.substring(0, indexOfVersion);
        }

        namedQuery.setParameter("abbrev", licenseType);
        namedQuery.setParameter("version", version);

        Lic licence = null;
        try {
            licence = (Lic) namedQuery.getSingleResult();
        } catch (Exception ex) {
            logger.info("no license linked to '" + abbrevAndLicense + "' : \n" + ex);
            return null;
        }

        return licence;

    }
}
