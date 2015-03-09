package se.nrm.bio.mediaserver.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ingimar
 */
@Entity
@Table(name = "IMAGE")
//@PrimaryKeyJoinColumn(name="UUID")
@NamedQueries({
    @NamedQuery(name = Image.FIND_ALL, query = "SELECT i FROM Image i")
})
@XmlRootElement
public class Image extends Media implements Serializable {

    private static final long serialVersionUID = 6L;

    public static final String FIND_ALL = "Image.findAll";
    
    @Column(name = "IS_EXPORTED")
    private Boolean isExported;
    
    @Column(name = "EXIF")
    private String exif;

    public Image() {
    }

    public Image(String owner) {
        super(owner);
    }

    public Image(String owner, String visibility, String filename, String mimetype) {
        super(owner, visibility, filename, mimetype);
    }

    public Boolean isIsExported() {
        return isExported;
    }

    public void setIsExported(Boolean isExported) {
        this.isExported = isExported;
    }

    public String getExif() {
        return exif;
    }

    public void setExif(String exif) {
        this.exif = exif;
    }
}
