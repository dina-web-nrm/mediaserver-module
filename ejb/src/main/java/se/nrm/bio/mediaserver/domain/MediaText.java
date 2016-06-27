package se.nrm.bio.mediaserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ingimar
 */
@Entity
@Table(name = "MEDIA_TEXT")
@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlRootElement
public class MediaText implements Serializable {

    @Transient
    public static final String DEFAULT_LANGUAGE = "sv_SE";

    private static final long serialVersionUID = 83434343L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "UUID")
    @JsonIgnore
    private Integer uuid;

    @Column(name = "LEGEND")
    private String legend="";

    @Column(name = "LANG")
    private String lang="";

    /**
     * Naturforskaren-specific
     */
    @Column(name = "COMMENT")
    private String comment="";

    @JoinColumn(name = "MEDIA_UUID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @XmlTransient
    private Media media;

    public MediaText() {
    }

    public MediaText(String legend, String lang) {
        this.legend = legend;
        this.setLang(lang);
    }
    public MediaText(String legend, String lang,String comment) {
        this.legend = legend;
        this.setLang(lang);
        this.comment = comment;
    }

    public MediaText(String legend, String lang, Media media) {
        this.legend = legend;
        this.setLang(lang);
        this.media = media;
    }

    public MediaText(String legend, String lang, Media media, String comment) {
        this.legend     = legend;
        this.setLang(lang);
        this.media      = media;
        this.comment    = comment;
    }

    public Integer getUuid() {
        return uuid;
    }

    public void setUuid(Integer uuid) {
        this.uuid = uuid;
    }

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        if (null == lang || lang.equals("")) {
            lang = MediaText.DEFAULT_LANGUAGE;
        }

        this.lang = lang;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append("MediaText:");
        sb.append(", uuid='").append(uuid).append('\'');
        sb.append(", legend='").append(legend).append('\'');
        sb.append(", lang='").append(lang).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
