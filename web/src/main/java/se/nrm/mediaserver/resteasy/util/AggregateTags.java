package se.nrm.mediaserver.resteasy.util;

import se.nrm.bio.mediaserver.util.TagHelper;


/**
 *
 * @author ingimar
 */
public class AggregateTags {

    public String aggregateTags(UploadForm form) {
        String tagsConcatenated="";
        String tags = form.getTags();
        String newTaglist = getEmptyOrtransformed(form.getTaglist());

        if (tags != null && !newTaglist.isEmpty()) {
            tagsConcatenated = tags.concat(TagHelper.getSplitter()).concat(newTaglist);
        } else if (!newTaglist.isEmpty()) {
            tagsConcatenated = newTaglist;
        } else if ( tags != null ){
            tagsConcatenated = tags;
        }

        return tagsConcatenated;
    }

    private String getEmptyOrtransformed(String[] taglist) {
        if (taglist == null || taglist.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String keyvalue : taglist) {
            sb.append(keyvalue).append("&");
        }
        sb.setLength(sb.length() - 1);

        return sb.toString();
    }

}
