package se.nrm.bio.mediaserver.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-03-09T15:54:20")
@StaticMetamodel(AdminMedia.class)
public class AdminMedia_ { 

    public static volatile SingularAttribute<AdminMedia, Integer> id;
    public static volatile SingularAttribute<AdminMedia, String> pathToFiles;
    public static volatile SingularAttribute<AdminMedia, String> environment;
    public static volatile SingularAttribute<AdminMedia, Boolean> isExif;

}