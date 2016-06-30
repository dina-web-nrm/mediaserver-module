/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.bio.mediaserver.domain;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;

/**
 *
 * @author ingimar
 */
public class ErrorListWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlAttribute(name = "error")
    private final ErrorHeader metadata;

    public ErrorListWrapper(ErrorHeader metadata) {
        this.metadata = metadata;
    }

    public ErrorHeader getMetadata() {
        return metadata;
    }

}
