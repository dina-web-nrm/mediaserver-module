package se.nrm.bio.mediaserver.business;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import org.apache.log4j.Logger;
import se.nrm.bio.mediaserver.domain.AdminConfig;

/**
 * http://stackoverflow.com/questions/1149737/on-using-enum-based-singleton-to-cache-large-objects-java 
 * obs. control the hostname of the machine ?
 * boolean isProduction = true;
        try {
            String result = InetAddress.getLocalHost().getHostName();
            if (StringUtils.isNotEmpty(result) && result.contains("as")) {
                isProduction = true;
            }
        } catch (UnknownHostException e) {
           
        }
        request.setAttribute("isProduktion", isProduction);
 * @author ingimar
 */
@Singleton
@Startup
public class StartupBean {

    private final static Logger logger = Logger.getLogger(StartupBean.class);
    
    @EJB
    AdminBean bean;

    private static final String ENVIRONMENT = "development";
    
    private ConcurrentHashMap map = null;

    @PostConstruct
//    public void postConstruct() throws IOException {
    public void postConstruct()  {
        this.map = new ConcurrentHashMap();
        List<AdminConfig> config = bean.getAdminConfig(ENVIRONMENT);

        for (AdminConfig conf : config) {
                map.put(conf.getAdminKey(), conf.getAdminValue());
            }
    }

    public ConcurrentHashMap getEnvironment()  {
        return map;
    }

    @PreDestroy
    private void shutdown() {
    }
}


