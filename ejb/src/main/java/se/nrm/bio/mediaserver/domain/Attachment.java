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
@Table(name = "ATTACHMENTS")
@NamedQueries({
    @NamedQuery(name = Attachment.FIND_ALL, query = "SELECT a FROM Attachment a")
})
@XmlRootElement
public class Attachment extends Media implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String FIND_ALL = "Attachment.findAll";

    public Attachment() {
    }

    public Attachment(String owner) {
        super(owner);
    }

    public Attachment(String owner, String visibility, String filename, String mimetype) {
        super(owner, visibility, filename, mimetype);
    }
}
