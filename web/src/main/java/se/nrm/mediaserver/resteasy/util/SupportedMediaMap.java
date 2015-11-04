package se.nrm.mediaserver.resteasy.util;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ingimar
 */
public class SupportedMediaMap {

    public Map<String, String> getSupported() {
        Map<String, String> map = new HashMap<>();
        SupportedMedia[] values = SupportedMedia.values();
        for (SupportedMedia media : values) {
            map.putAll(media.getMap());
        }
        return map;
    }

}
