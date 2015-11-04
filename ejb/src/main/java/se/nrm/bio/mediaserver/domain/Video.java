package se.nrm.bio.mediaserver.domain;

import java.io.Serializable;
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
@Table(name = "VIDEO")
@NamedQueries({
    @NamedQuery(name = Video.FIND_ALL, query = "SELECT v FROM Video v"), 
//    @NamedQuery(name = "Video.findByUuid", query = "SELECT v FROM Video v WHERE v.uuid = :uuid"),
//    @NamedQuery(name = "Video.findByIsExported", query = "SELECT v FROM Video v WHERE v.isExported = :isExported")
})
@XmlRootElement
public class Video extends Stream implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String FIND_ALL = "Video.findAll";

    public Video() {
    }

    public Video(String owner) {
        super(owner);
    }

    public Video(String owner, String visibility, String filename, String mimetype) {
        super(owner, visibility, filename, mimetype);
    }
}
