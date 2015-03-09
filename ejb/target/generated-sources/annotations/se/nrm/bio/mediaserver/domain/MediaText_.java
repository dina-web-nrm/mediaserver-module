package se.nrm.bio.mediaserver.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import se.nrm.bio.mediaserver.domain.Media;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-03-09T15:54:20")
@StaticMetamodel(MediaText.class)
public class MediaText_ { 

    public static volatile SingularAttribute<MediaText, String> legend;
    public static volatile SingularAttribute<MediaText, Integer> uuid;
    public static volatile SingularAttribute<MediaText, String> comment;
    public static volatile SingularAttribute<MediaText, String> lang;
    public static volatile SingularAttribute<MediaText, Media> media;

}