package se.nrm.bio.mediaserver.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ingimar
 */
@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlRootElement
public class ErrorHeader {

    private String id;

    private String links;

    private String status;

    private String code;

    private String title;

    private String detail;

    private String source;

    private String meta;

    public ErrorHeader() {
        this.title = "Resource not found";
        this.detail = "There is no API hosted at this URL.\n"
                + "For information about our API's, please refer to the documentation at:  https://github.com/DINA-Web/mediaserver-module";
    }

    public ErrorHeader(String message, String detail) {
        this.title = message;
        this.detail = detail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }
    
    

}
