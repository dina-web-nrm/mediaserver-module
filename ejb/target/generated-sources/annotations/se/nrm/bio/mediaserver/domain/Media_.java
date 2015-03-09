package se.nrm.bio.mediaserver.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import se.nrm.bio.mediaserver.domain.Determination;
import se.nrm.bio.mediaserver.domain.Lic;
import se.nrm.bio.mediaserver.domain.MediaText;
import se.nrm.bio.mediaserver.domain.Tag;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-03-09T15:54:20")
@StaticMetamodel(Media.class)
public abstract class Media_ { 

    public static volatile CollectionAttribute<Media, Tag> tags;
    public static volatile SetAttribute<Media, MediaText> texts;
    public static volatile CollectionAttribute<Media, Determination> determinations;
    public static volatile SingularAttribute<Media, String> mimetype;
    public static volatile SingularAttribute<Media, String> taggar;
    public static volatile SingularAttribute<Media, String> hash;
    public static volatile SingularAttribute<Media, String> visibility;
    public static volatile SingularAttribute<Media, String> alt;
    public static volatile SetAttribute<Media, Lic> lics;
    public static volatile SingularAttribute<Media, String> mediaURL;
    public static volatile SingularAttribute<Media, String> owner;
    public static volatile SingularAttribute<Media, String> filename;
    public static volatile SingularAttribute<Media, String> uuid;

}