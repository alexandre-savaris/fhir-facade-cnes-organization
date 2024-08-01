FROM tomcat:10.1.26-jdk17-temurin-noble

COPY target/fhir-facade-cnes-organization.war $CATALINA_HOME/webapps/ROOT.war

RUN mkdir -p "/data"
RUN mkdir -p "/logs"

EXPOSE 8080:8080
