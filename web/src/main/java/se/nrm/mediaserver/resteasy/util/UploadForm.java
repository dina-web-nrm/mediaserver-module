/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.mediaserver.resteasy.util;

/**
 *
 * @author ingimar
 */
public interface UploadForm {

    public void setFileData(byte[] fileData);

    public byte[] getFileData();

    public String getTags();

    public void setTags(String tags);
    
      public String[] getTaglist();

    public void setTaglist(String[] taglist);
}
