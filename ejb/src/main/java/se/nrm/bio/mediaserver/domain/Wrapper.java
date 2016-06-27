/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.bio.mediaserver.domain;

import java.io.Serializable;
import java.util.List;
import javax.ws.rs.core.GenericEntity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ingimar
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "header")
public class Wrapper implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlAttribute(name = "metadata")
    private final MetadataHeader metadata;

    @XmlAttribute(name = "data")
    private Media media;

    public Wrapper(MetadataHeader metadata, Media media) {
        this.metadata = metadata;
        this.media = media;
    }

    public Media getMedia() {
        return media;
    }

    public MetadataHeader getMetadata() {
        return metadata;
    }
}
