
FROM tomcat:latest

COPY main-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/app.war

CMD ["catalina.sh", "run"]
