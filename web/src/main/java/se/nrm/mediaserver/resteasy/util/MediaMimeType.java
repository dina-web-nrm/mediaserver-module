package se.nrm.mediaserver.resteasy.util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ingimar
 */
@Deprecated
public enum MediaMimeType {

    IMAGE("image/jpeg", "image/gif"),
    VIDEO("video/mp4"),
    SOUND("audio/ogg"),
    ATTACHMENT("application/pdf");

    private final List<String> mediaList = new ArrayList<>();

    private final Map<String, String> map = new HashMap<>();

    private MediaMimeType() {
    }

    private MediaMimeType(String... mime) {
        List<String> asList = Arrays.asList(mime);
        mediaList.addAll(asList);
        for (String m : asList) {
            map.put(this.toString(), m);
        }
    }
}
