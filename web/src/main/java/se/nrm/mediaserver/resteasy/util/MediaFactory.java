package se.nrm.mediaserver.resteasy.util;

import se.nrm.bio.mediaserver.domain.Attachment;
import se.nrm.bio.mediaserver.domain.Image;
import se.nrm.bio.mediaserver.domain.Media;
import se.nrm.bio.mediaserver.domain.Sound;
import se.nrm.bio.mediaserver.domain.Video;



/**
 *
 * @author ingimar
 */
public class MediaFactory {

    
//    public static Media createMedia(MediaMimeType type) {
//        try {
//            switch (type) {
//                case IMAGE:
//                    return new Image();
//                case VIDEO:
//                    return new Video();
//                case SOUND:
//                    return new Sound();
//                case ATTACHMENT:
//                    return new Attachment();
//                default:
//                    throw new IllegalArgumentException();
//            }
//        } catch (Exception e) {
//            return null;
//        }
//    }

    public static Media createImage(boolean isExported) {
        Image image = new Image();
        image.setIsExported(isExported);
        return image;
    }

    public static Media createImage2(boolean isExported, String exif) {
        Image image = new Image();
        image.setIsExported(isExported);
        image.setExif(exif);
        return image;
    }

    public static Media createVideo(int startTime, int endTime) {
        Video video = new Video();
        video.setStartTime(startTime);
        video.setEndTime(endTime);
        return video;
    }

    public static Media createSound(int startTime, int endTime) {
        Sound sound = new Sound();
        sound.setStartTime(startTime);
        sound.setEndTime(endTime);
        return sound;
    }

    public static Media createAttachement() {
        Attachment attach = new Attachment();
        return attach;
    }
}
