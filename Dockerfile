# Use the official Tomcat base image
FROM tomcat:8.0.37-jre8

# Copy the wait-for-it script to /usr/local/bin
COPY wait-for-it.sh /usr/local/bin/wait-for-it.sh
RUN chmod +x /usr/local/bin/wait-for-it.sh

# Remove the default webapps (optional, for a clean environment)
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the WAR file to the Tomcat webapps directory
COPY target/inbetween-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps//ROOT.war

# Expose the default Tomcat port
EXPOSE 8080

# Start Tomcat server
CMD ["wait-for-it.sh", "mysql8:3306", "--timeout=60", "--", "catalina.sh", "run"]


