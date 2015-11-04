package se.nrm.mediaserver.resteasy.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import se.nrm.bio.mediaserver.domain.Attachment;
import se.nrm.bio.mediaserver.domain.Image;
import se.nrm.bio.mediaserver.domain.Media;
import se.nrm.bio.mediaserver.domain.Sound;
import se.nrm.bio.mediaserver.domain.Video;



/**
 *
 * @author ingimar
 */
public enum SupportedMedia {

    IMAGE("image/jpeg", "image/gif"),
    VIDEO("video/mp4"),
    SOUND("audio/ogg"),
    ATTACHMENT("application/pdf");

    private final List<String> mediaList = new ArrayList<>();

    private final Map<String, String> map = new HashMap<>();

    private SupportedMedia() {
    }

    private SupportedMedia(String... mime) {
        List<String> asList = Arrays.asList(mime);
        mediaList.addAll(asList);
        for (String m : asList) {
            map.put(m, this.toString());
        }
    }

    public List<String> getMediaList() {
        return mediaList;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public Media getMedia(String discriminator) {
        Media media = null;
        switch (discriminator) {
            case "image/jpeg":
                media = new Image();
                break;
            case "image/gif":
                media = new Image();
                break;
            case "video/mp4":
                media = new Video();
                break;
            case "audio/ogg":
                media = new Sound();
                break;
            case "application/pdf":
                media = new Attachment();
                break;
            default: 
                media=null;
        }
        return media;
    }

}
