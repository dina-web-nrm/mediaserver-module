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
@Table(name = "SOUND")
@NamedQueries({
    @NamedQuery(name = Sound.FIND_ALL, query = "SELECT s FROM Sound s"), //    @NamedQuery(name = "Sound.findByUuid", query = "SELECT s FROM Sound s WHERE s.uuid = :uuid"),
//    @NamedQuery(name = "Sound.findByIsExported", query = "SELECT s FROM Sound s WHERE s.isExported = :isExported")
})
@XmlRootElement
public class Sound extends Stream implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String FIND_ALL = "Sound.findAll";

    public Sound() {
    }

    public Sound(String owner) {
        super(owner);
    }

    public Sound(String owner, String visibility, String filename, String mimetype) {
        super(owner, visibility, filename, mimetype);
    }
}