FROM tomcat:9.0-jdk17

# Elimina apps default de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia tu WAR al directorio de despliegue de Tomcat
COPY target/ROOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080