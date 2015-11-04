package se.nrm.bio.mediaserver.business;

import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import se.nrm.bio.mediaserver.domain.Determination;
import se.nrm.bio.mediaserver.domain.Image;
import se.nrm.bio.mediaserver.domain.Media;
import se.nrm.bio.mediaserver.util.TagHelper;

/**
 *
 * @author ingimar
 */
@Stateless
public class MediaCouplingBean<T> implements Serializable {

    private final static Logger logger = Logger.getLogger(MediaCouplingBean.class);

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "MysqlPU")
    private EntityManager em;

    public T save(T entity) {
        T saved = em.merge(entity);
        return saved;
    }

    public List<Media> getMetaDataForMedia(String externalUUID) {
        final String tags = "";
        return this.getMetaDataForMedia(externalUUID, tags);
    }

    public List<Media> getMetaDataForMedia(String externalUUID, String tags) {
        String sql;
        if (!tags.isEmpty()) {
            TagHelper h = new TagHelper();
            String parsedTags = h.sqlUsageParseWithLike(tags);
            sql = "SELECT DISTINCT d.media FROM Determination d, Media m where d.media.uuid = m.uuid "
                    + "AND d.tagValue= '" + externalUUID + "' AND " + parsedTags;
        } else {
            sql = "SELECT DISTINCT d.media FROM Determination d, Media m where d.media.uuid = m.uuid "
                    + "AND d.tagValue= '" + externalUUID + "'";
        }

        Query query = em.createQuery(sql);
        List<Media> mediaList = query.getResultList();
        return mediaList;
    }
    
    public List<Determination> getListByDetermination(String externalUUID){
         Query namedQuery = em.createNamedQuery(Determination.FIND_BY_EXTERNAL_TAG);
        namedQuery.setParameter("tagValue", externalUUID);
        List<Determination>  res = (List<Determination> )namedQuery.getSingleResult();
        return res;
    }

    public Media getMetaMedia(String externalUUID, String mediaUUID) {
        String sql = "SELECT d FROM Determination d WHERE WHERE d.tagValue = :tagValue and d.media_uuid = :media_uuid ";

        Query namedQuery = em.createNamedQuery(sql);
        namedQuery.setParameter("tagValue", externalUUID);
        namedQuery.setParameter("media_uuid", mediaUUID);

        Media singleResult = (Media) namedQuery.getSingleResult();
        return singleResult;
    }

    // 2015-02-23 beror på Mediatext, avvakta
    public List<Image> getMetadataByLanguage(String externalUUID, String language) {
        String sql;
        sql = "SELECT DISTINCT d.media FROM Determination d, Media m, MediaText t"
                + " WHERE d.media.uuid = m.uuid AND m.uuid=t.media.uuid"
                + " AND d.tagValue= '" + externalUUID + "'"
                + " AND t.lang= '" + language + "' ORDER BY d.sortOrder";

        Query query = em.createQuery(sql);
        List<Image> images = query.getResultList();
        return images;
    }

    public List<Image> getMetadataByLanguageAndTags(String externalUUID, String language, String tags) {
        String sql;
        TagHelper tagHelper = new TagHelper();
        String parsedTags = tagHelper.sqlUsageParseWithLike(tags);
        sql = "SELECT DISTINCT d.media FROM Determination d, Media m, MEDIA_TEXT t"
                + " WHERE d.media.uuid = m.uuid AND m.uuid=t.media.uuid"
                + " AND d.tagValue= '" + externalUUID + "'"
                + " AND t.lang= '" + language + "' AND " + parsedTags;

        Query query = em.createQuery(sql);
        List<Image> images = query.getResultList();
        return images;
    }

    public List<Image> getMetadataByTags_MEDIA(String tags) {
        TagHelper tagHelper = new TagHelper();
        String parsedTags = tagHelper.sqlUsageParseWithLike(tags);
        String sql = "SELECT m from Media m where " + parsedTags;
        Query query = em.createQuery(sql);
        List<Image> images = query.getResultList();
        return images;
    }

    public Boolean isTagPresentInDetermination(String tagValue) {
        boolean existence = false;

        String sql = "SELECT count(d.tagValue) FROM Determination d WHERE d.tagValue = '" + tagValue + "'";
        Query query = em.createQuery(sql);
        Long singleResult = (Long) query.getSingleResult();

        if (singleResult > 0) {
            existence = true;
        }

        return existence;
    }

    public List<T> getDeterminationsByTagValue(String externalUUID) {
        Query query = em.createNamedQuery(Determination.FIND_BY_EXTERNAL_TAG);
        query.setParameter("tagValue", externalUUID);
        List<T> list = query.getResultList();

        return list;
    }
    public Determination getDeterminationsByTagValueAndMediaUUID(String externalUUID,String mediaUUID) {
        Query query = em.createNamedQuery(Determination.FIND_BY_TAXONUUID_AND_MEDIAUUID);
        query.setParameter("tagValue", externalUUID);
        query.setParameter("mediaUUID", mediaUUID);
        Determination res = (Determination)query.getSingleResult();

        return res;
    }

    public List<String> getMediaUUID(String externalUUID) {
        final String tags = "";
        return this.getMediaUUID(externalUUID, tags);
    }

    /**
     * Using Native SQL-Query
     *
     * @param externalUUID
     * @param tags
     * @return
     */
    public List<String> getMediaUUID(String externalUUID, String tags) {
        String result_media_uuid;
        if (!tags.isEmpty()) {
            TagHelper h = new TagHelper();
            String parsedTags = h.sqlUsageParseWithRegexp(tags);
            result_media_uuid = "SELECT MEDIA_UUID FROM DETERMINATION d,"
                    + "MEDIA m WHERE d.TAG_VALUE = '" + externalUUID + "' "
                    + "AND d.MEDIA_UUID=m.UUID AND " + parsedTags;
        } else {
            result_media_uuid = "SELECT MEDIA_UUID FROM DETERMINATION d,"
                    + "MEDIA m WHERE d.TAG_VALUE = '" + externalUUID + "' "
                    + "AND d.MEDIA_UUID=m.UUID";
        }

        Query query = em.createNativeQuery(result_media_uuid);

        List<String> listOfUUID = query.getResultList();

        return listOfUUID;
    }
}
