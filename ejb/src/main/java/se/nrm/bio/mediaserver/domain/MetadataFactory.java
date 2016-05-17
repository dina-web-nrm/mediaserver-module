/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.bio.mediaserver.domain;

/**
 *
 * @author ingimar
 */
public class MetadataFactory {
    public static MetadataHeader  getMetadata(String metadataType, String apiVersion, int statusCode) throws IllegalArgumentException {
       
        if (metadataType == null) {
            throw new IllegalArgumentException("null is not valid argument!");
        }

        if (metadataType.equalsIgnoreCase("ground")) {
            return new MetadataHeader(apiVersion,statusCode);

        } else if (metadataType.equalsIgnoreCase("medium")) {
//            return new Metadata();

        } else if (metadataType.equalsIgnoreCase("top")) { // with paging as well
//            return new Metadata();
        }
        
        return null;

    }

}
