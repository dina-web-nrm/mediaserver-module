package se.nrm.bio.mediaserver.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import se.nrm.bio.mediaserver.domain.Media;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-03-09T15:54:20")
@StaticMetamodel(Determination.class)
public class Determination_ { 

    public static volatile SingularAttribute<Determination, Integer> id;
    public static volatile SingularAttribute<Determination, String> tagKey;
    public static volatile SingularAttribute<Determination, Integer> sortOrder;
    public static volatile SingularAttribute<Determination, String> tagValue;
    public static volatile SingularAttribute<Determination, String> externalSystem;
    public static volatile SingularAttribute<Determination, String> externalSystemUrl;
    public static volatile SingularAttribute<Determination, Media> media;

}