package se.nrm.mediaserver.resteasy.util;

import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import java.util.LinkedHashMap;
import org.json.simple.JSONObject;

/**
 *
 * @author ingimar
 */
public class ExifExtraction {

    public JSONObject packageEXIF_IN_JSON(Metadata metadata, boolean isFiltered) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        for (Directory directory : metadata.getDirectories()) {
            for (com.drew.metadata.Tag tag : directory.getTags()) {
                String tagName = tag.getTagName();
                String tagDescription = "";
                if (isFiltered) {
                    if (!tagName.contains("Unknown")) {
                        tagDescription = tag.getDescription();
                        map.put(tagName, tagDescription);
                    }
                } else {
                    map.put(tagName, tagDescription);
                }
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.putAll(map);
        return jsonObject;
    }

}
