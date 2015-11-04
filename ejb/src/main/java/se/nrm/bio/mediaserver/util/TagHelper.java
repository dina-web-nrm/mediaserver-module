package se.nrm.bio.mediaserver.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import se.nrm.bio.mediaserver.domain.Media;
import se.nrm.bio.mediaserver.domain.Tag;

/**
 *
 * @author ingimar
 */
public class TagHelper {

    private final static Logger logger = Logger.getLogger(TagHelper.class);

    public static final String getSplitter() {
        return "&";
    }
    
    public static final String getDelimiter(){
        return ":";
    }

    public List<String> addingTags(Media media, final String tags) {
        final String delim = getDelimiter();
        final String splitter = getSplitter();

        final boolean keyWithoutValue = (tags.indexOf(delim) == tags.length() - 1);
        final boolean firstCharacterIsDelim = tags.indexOf(delim) == 0;

        List<String> tagList = Collections.EMPTY_LIST;

        if (tags.contains(delim) && !keyWithoutValue && !firstCharacterIsDelim) {

            tagList = Arrays.asList(tags.split(splitter));
            for (String pair : tagList) {
                if (pair.equals("")) {
                    tagList = Collections.EMPTY_LIST;
                    break;
                }
                String[] split = pair.split(delim);
                Tag tag = new Tag(split[0], split[1], media);
                media.addTag(tag);
            }

            media.setTaggar(tags);
        } else {
            logger.info("no tagging delimiter of sort " + delim);
        }
        return tagList;
    }

    public String sqlUsageParseWithRegexp(String tags) {
        String delimiter = getSplitter();

        String prefix = "AND m.TAGS REGEXP ('";
        String suffix = "') ";

        LinkedList<String> listan = new LinkedList(Arrays.asList(tags.split(delimiter)));
        String firstElement = "m.TAGS REGEXP ('" + listan.pop() + "') ";

        String filter = "";
        for (String pair : listan) {
            String block = prefix.concat(pair).concat(suffix);
            filter = filter.concat(block);
        }
        return firstElement + filter.trim();
    }

    public String sqlUsageParseWithLike(String tags) {
        String splitter = getSplitter();
        String prefix = "AND m.taggar LIKE '%";
        String suffix = "%' ";

        LinkedList<String> listan = new LinkedList(Arrays.asList(tags.split(splitter)));
        String firstElement = "m.taggar LIKE '%" + listan.pop() + "%' ";

        String filter = "";
        for (String pair : listan) {
            String block = prefix.concat(pair).concat(suffix);
            filter = filter.concat(block);
        }
        return firstElement + filter.trim();
    }

}
