package se.nrm.bio.mediaserver.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-03-09T15:54:20")
@StaticMetamodel(Link.class)
public class Link_ { 

    public static volatile SingularAttribute<Link, Integer> id;
    public static volatile SingularAttribute<Link, String> tagKey;
    public static volatile SingularAttribute<Link, String> mediaUUID;
    public static volatile SingularAttribute<Link, Integer> sortOrder;
    public static volatile SingularAttribute<Link, String> tagValue;
    public static volatile SingularAttribute<Link, String> externalSystem;
    public static volatile SingularAttribute<Link, String> externalSystemUrl;

}