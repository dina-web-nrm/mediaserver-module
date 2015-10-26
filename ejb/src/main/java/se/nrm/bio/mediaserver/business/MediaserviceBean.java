package se.nrm.bio.mediaserver.business;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import se.nrm.bio.mediaserver.domain.Lic;
import se.nrm.bio.mediaserver.domain.Media;
import org.apache.log4j.Logger;
import se.nrm.bio.mediaserver.domain.Image;
import se.nrm.bio.mediaserver.domain.MediaText;
import se.nrm.bio.mediaserver.util.TagHelper;

/**
 *
 * @author ingimar
 */
@Stateless
public class MediaserviceBean<T> implements Serializable {

    private final static Logger logger = Logger.getLogger(MediaserviceBean.class);

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "MysqlPU")
    private EntityManager em;

    public T save(T entity) {
        T saved = em.merge(entity);
        return saved;
    }

    public T get(String uuid) {
        Query namedQuery = em.createNamedQuery(Media.FIND_BY_UUID);
        namedQuery.setParameter("uuid", uuid);

        T media = null;
        try {
            media = (T) namedQuery.getSingleResult();
        } catch (Exception ex) {

        }
        return media;
    }

    public T delete(T t) {
        T media = null;

        return media;
    }

    public void delete(MediaText iText) {
        MediaText text = em.find(MediaText.class, iText.getUuid());
        em.remove(text);
    }

    /**
     * obs: Removes the media from 'DETERMINATION'-table as well. ?
     *
     * @param mediaUUID
     * @return
     */
    public boolean deleteMediaMetadata(final String mediaUUID) {
        boolean deleted = false;
        T fetchedEntity;
        try {
            fetchedEntity = get(mediaUUID);
        } catch (Exception e) {
            return deleted;
        }

        try {
            em.remove(fetchedEntity);
        } catch (Exception e) {
            return deleted;
        }
        deleted = true;

        return deleted;
    }

    public boolean isMediaUUIDCoupled(final String mediaUUID) {
        boolean isCoupled = false;
        Query q = em.createNativeQuery("select TAG_VALUE from DETERMINATION where MEDIA_UUID='" + mediaUUID + "'");
        try {
            String taxonUUID = (String) q.getSingleResult();
            isCoupled = true;
        } catch (Exception ex) {
        }

        return isCoupled;
    }

    public T getLicenseByAbbr(String abbrevation) {
        Query namedQuery = em.createNamedQuery(Lic.FIND_BY_ABBREV);
        namedQuery.setParameter("abbrev", abbrevation);
        T licence = null;
        try {
            licence = (T) namedQuery.getSingleResult();
        } catch (Exception ex) {
            logger.info("no license linked to '" + abbrevation + "' : \n" + ex);
            return null;
        }
        return licence;
    }

    /**
     * @Todo , duplicate in AdminBean Adjusted to fit a table with version,
     * defaults to version 3.0
     * @param abbrevAndLicense
     * @return
     */
    public T getNewLicenseByAbbr(String abbrevAndLicense) {
        Query namedQuery = em.createNamedQuery(Lic.FIND_BY_ABBREV_AND_VERSION);
        int indexOfVersion = abbrevAndLicense.indexOf('v');
        if (indexOfVersion == -1) {
            String defaultLicense = "v3.0";
            String concat = abbrevAndLicense.concat(" ").concat(defaultLicense);
            return getNewLicenseByAbbr(concat);
        }
        String version = abbrevAndLicense.substring(indexOfVersion + 1);
        String licenseType = abbrevAndLicense.substring(0, indexOfVersion);
        namedQuery.setParameter("abbrev", licenseType);
        namedQuery.setParameter("version", version);

        T licence = null;
        try {
            licence = (T) namedQuery.getSingleResult();
        } catch (Exception ex) {
            logger.info("no license linked to '" + abbrevAndLicense + "' : \n" + ex);
            return null;
        }

        return licence;
    }

    public List<Image> getMetadataByTags_MEDIA(String tags) {
        TagHelper tagHelper = new TagHelper();
        String parsedTags = tagHelper.sqlUsageParseWithLike(tags);
        String sql = "SELECT m from Media m where " + parsedTags;
        Query query = em.createQuery(sql);
        List<Image> images = query.getResultList();
        return images;
    }

    public List<Image> getAll() {
        Query query = em.createNamedQuery(Media.FIND_ALL);
        List<Image> images = query.getResultList();
        return images;
    }

    public List getXImages(String in, int limit) {
        Query query = em.createNamedQuery(in);
        List<Media> images = query.setMaxResults(limit).getResultList();
        return images;
    }

    public List<T> findRange(Class<T> entityClass, int[] range) {
        List list = Collections.EMPTY_LIST;
        CriteriaQuery<Object> criteriaQ = em.getCriteriaBuilder().createQuery();
        criteriaQ.select(criteriaQ.from(entityClass));
        TypedQuery<Object> typedQ = em.createQuery(criteriaQ);
        typedQ.setMaxResults(range[1] - range[0] + 1);
        typedQ.setFirstResult(range[0]);
        list = typedQ.getResultList();

        return list;
    }

    public int count(Class<T> entityClass) {
        CriteriaQuery criteriaQ = em.getCriteriaBuilder().createQuery();
        Root<T> rt = criteriaQ.from(entityClass);
        criteriaQ.select(em.getCriteriaBuilder().count(rt));
        Query query = em.createQuery(criteriaQ);
        int value = ((Long) query.getSingleResult()).intValue();
        return value;
    }

}
