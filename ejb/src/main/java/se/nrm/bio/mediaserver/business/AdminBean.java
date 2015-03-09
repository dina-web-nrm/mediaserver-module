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
    
    public List<Lic> getLicenses(){
        Query query = em.createNamedQuery(Lic.FIND_ALL);
        List<Lic> resultList = query.getResultList();
        return resultList;
    }
    public Lic getLicense(String abbrev){
        Query query = em.createNamedQuery(Lic.FIND_BY_ABBREV);
        query.setParameter("abbrev", abbrev);
        Lic license = (Lic) query.getSingleResult();
        return license;
    }
}
