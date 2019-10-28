/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.bio.mediaserver.clients;

/**
 *
 * @author ingimar
 */
public class Util {

    public final static String prefixProd = "https://api.nrm.se";
    public final static String prefixLocal = "http://127.0.0.1:8080";
    public final static String suffix = "/MediaServerResteasy/media";

    public static String getProdURL() {
        String URL = prefixProd.concat(suffix);
        return URL;
    }
    
    public static String getLocalURL() {
        String URL = prefixLocal.concat(suffix);
        return URL;
    }

}
