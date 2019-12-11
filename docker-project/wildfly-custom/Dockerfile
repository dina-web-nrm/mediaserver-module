FROM jboss/wildfly:8.2.1.Final

WORKDIR /opt/jboss/wildfly

ADD modules /opt/jboss/wildfly/modules/
ADD customization /opt/jboss/wildfly/customization/

# standalone.conf increased Java-Heap size
ADD standalone.conf /opt/jboss/wildfly/bin

USER root
RUN chmod +x /opt/jboss/wildfly/customization/execute.sh
RUN /opt/jboss/wildfly/customization/execute.sh

# Fix for Error: Could not rename /opt/jboss/wildfly/standalone/configuration/standalone_xml_history/current 
RUN rm -rf /opt/jboss/wildfly/standalone/configuration/standalone_xml_history
RUN chown -R jboss:jboss /opt/jboss/wildfly/

# admin-interface
RUN bin/add-user.sh -s -u media -p passw0rd12  -e

ADD mediaserver.ear /opt/jboss/wildfly/standalone/deployments/
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]


