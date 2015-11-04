package se.nrm.bio.mediaserver.rs;


import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


@ApplicationPath("webresources")
public class MediaResourceApplication extends Application {
    
     @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        // following code can be used to customize Jersey 1.x JSON provider:
        try {
            Class jacksonProvider = Class.forName("org.codehaus.jackson.jaxrs.JacksonJsonProvider");
            resources.add(jacksonProvider);
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(se.nrm.bio.mediaserver.rs.AdministrationResource.class);
        resources.add(se.nrm.bio.mediaserver.rs.MediaResource.class);
        resources.add(se.nrm.bio.mediaserver.rs.MediaResourceFetchBinary.class);
        resources.add(se.nrm.bio.mediaserver.rs.MediaResourceForm.class);
        resources.add(se.nrm.bio.mediaserver.rs.NewMediaResource.class);
        resources.add(se.nrm.bio.mediaserver.rs.NewMediaResourceForm.class);
        resources.add(se.nrm.bio.mediaserver.rs.coupling.DeterminationPostForm.class);
        resources.add(se.nrm.bio.mediaserver.rs.coupling.DeterminationResourceFetch.class);
    }
}
