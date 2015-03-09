package se.nrm.mediaserver.resteasy.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import se.nrm.bio.mediaserver.domain.Link;

/**
 *
 * @author ingimar
 */

public class Links implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Link link;
    
    private Collection<String> medias;

    public Links() {
        link = new Link();
        medias = new HashSet<String>();
    }

    public Links(Link link, Collection<String> medias) {
        this.link = Link.newInstanceWithoutMedia(link.getTagKey(), link.getTagValue(), link.getExternalSystem(), link.getExternalSystemUrl());
        this.medias = medias;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public Collection<String> getMedias() {
        return medias;
    }

    public void setMedias(Collection<String> medias) {
        this.medias = medias;
    }
    
}