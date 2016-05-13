/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.bio.mediaserver.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 *
 * @author ingimar
 */
@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlRootElement
public class Metadata {

    private String apiVersion;

    private String callDate;

    private String statusCode;

    private String supportedLanguages;

    private String contentLicenses;

    private String maintenanceContact;

    private Metadata() {
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
        String nowAsISO = fmt.print(dt);

        this.callDate = nowAsISO;
    }

    protected Metadata(String apiVersion) {
        this();
        this.apiVersion = apiVersion;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getCallDate() {
        return callDate;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getSupportedLanguages() {
        return supportedLanguages;
    }

    public void setSupportedLanguages(String supportedLanguages) {
        this.supportedLanguages = supportedLanguages;
    }

    public String getContentLicenses() {
        return contentLicenses;
    }

    public void setContentLicenses(String contentLicenses) {
        this.contentLicenses = contentLicenses;
    }

    public String getMaintenanceContact() {
        return maintenanceContact;
    }

    public void setMaintenanceContact(String maintenanceContact) {
        this.maintenanceContact = maintenanceContact;
    }

}
