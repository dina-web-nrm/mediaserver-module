package se.nrm.mediaserver.resteasy.util;

import java.io.File;

/**
 * @author ingimar
 */
public class PathHelper {

    private PathHelper() {
    }

    /**
     * Creates directory. Fetches the absolute path to the file
     *
     * @param uuid
     * @param mediaPath
     * @return
     */
    public static String getEmptyOrAbsolutePathToFile(String uuid, String mediaPath) {
        String mediaPathWithUUID = "";
        String pathen = getPath(uuid, mediaPath);
        File directory = new File(pathen);

        boolean dirNotExisting = !directory.exists();

        if (dirNotExisting) {
            directory.mkdirs();
        }
        mediaPathWithUUID = pathen.concat(uuid);
        return mediaPathWithUUID;
    }

    public static String getDynamicPathToFile(String uuid, String mediaPath) {
        final String pathen = getPath(uuid, mediaPath);
        return pathen;
    }

    private static String getPath(String uuid, String mediaPath) {
        final int PATH_DEPTH = 3;
        StringBuilder tmpPath = new StringBuilder(mediaPath);
        for (int i = 0; i < PATH_DEPTH; i++) {
            tmpPath.append(uuid.charAt(i)).append("/");
        }
        String pathen = tmpPath.toString();
        return pathen;
    }
}
