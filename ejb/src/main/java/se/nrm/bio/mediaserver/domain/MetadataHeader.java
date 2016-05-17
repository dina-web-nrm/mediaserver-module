/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.bio.mediaserver.domain;

import java.net.URL;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * According to :
 * https://github.com/DINA-Web/dina-api-standard/blob/master/DINA-Web-API-Guidelines.md#api-response-metadata
 *
 * @author ingimar
 */
@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlRootElement
public class MetadataHeader {

    private URL callEndpoint;

    private URL next;

    private URL previous;

    private int limit;

    private long offset;

    private String callDate;

    private int statusCode;

    private String apiVersion;

    private long resultCount;

    private String[] orderBy;

    // asc or desc
    private String sortOrder;

    private String[] resultLanguages;

    private String[] supportedLanguages;

    private String[] contentLicenses;

    private String message;

    private String maintenanceContact;

    private MetadataHeader() {
        this.callDate = this.setIsoDate();
    }

    public MetadataHeader(String apiVersion, int statusCode) {
        this();
        this.apiVersion = apiVersion;
        this.statusCode = statusCode;
    }

    public MetadataHeader(String apiVersion, int statusCode, String[] contentLicenses) {
        this();
        this.apiVersion = apiVersion;
        this.statusCode = statusCode;
        this.contentLicenses = contentLicenses;
    }
    
    public MetadataHeader(String apiVersion, int statusCode, String[] contentLicenses, String[] resultLanguages) {
        this();
        this.apiVersion = apiVersion;
        this.statusCode = statusCode;
        this.contentLicenses = contentLicenses;
        this.resultLanguages = resultLanguages;
        
    }

    public URL getCallEndpoint() {
        return callEndpoint;
    }

    public void setCallEndpoint(URL callEndpoint) {
        this.callEndpoint = callEndpoint;
    }

    public URL getNext() {
        return next;
    }

    public void setNext(URL next) {
        this.next = next;
    }

    public URL getPrevious() {
        return previous;
    }

    public void setPrevious(URL previous) {
        this.previous = previous;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public String getCallDate() {
        return callDate;
    }

    public void setCallDate(String callDate) {
        this.callDate = callDate;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public long getResultCount() {
        return resultCount;
    }

    public void setResultCount(long resultCount) {
        this.resultCount = resultCount;
    }

    public String[] getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String[] orderBy) {
        this.orderBy = orderBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String[] getResultLanguages() {
        return resultLanguages;
    }

    public void setResultLanguages(String[] resultLanguages) {
        this.resultLanguages = resultLanguages;
    }

    public String[] getSupportedLanguages() {
        return supportedLanguages;
    }

    public void setSupportedLanguages(String[] supportedLanguages) {
        this.supportedLanguages = supportedLanguages;
    }

    public String[] getContentLicenses() {
        return contentLicenses;
    }

    public void setContentLicenses(String[] contentLicenses) {
        this.contentLicenses = contentLicenses;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMaintenanceContact() {
        return maintenanceContact;
    }

    public void setMaintenanceContact(String maintenanceContact) {
        this.maintenanceContact = maintenanceContact;
    }

    private String setIsoDate() {
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
        String nowAsISO = fmt.print(dt);
        return nowAsISO;
    }
}
