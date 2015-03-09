package se.nrm.bio.mediaserver.business;

import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

    public T getVechicle(String uuid) {
        Query namedQuery = em.createNamedQuery("Vehicle.findByUuid");
        namedQuery.setParameter("uuid", uuid);

        T media = null;
        try {
            media = (T) namedQuery.getSingleResult();
        } catch (Exception ex) {

        }
        return media;
    }

    public T getTitle(String title) {
        Query namedQuery = em.createNamedQuery("Dummy.FindByTitle");
        namedQuery.setParameter("title", title);

        T y = null;
        try {
            y = (T) namedQuery.getSingleResult();
        } catch (Exception ex) {

        }
        return y;
    }
}
