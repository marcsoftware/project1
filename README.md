
# HOW TO RUN
```
// This will create a war file (target/App.war).
mvn clean install
```
I used the extension "Tomcat for Java - Wei Shen" for vscode to deploy the war file.

# how to setup the extension
```
1. https://tomcat.apache.org/download-80.cgi 
2. download the windows64.zip
3.  then unzip the folder (can be unzipped to anywhere)
4.  point the extension to the unzipped folder. 

5. in vsocde right click tomcat server to run
6. right click war file to run
```
## this prevents the drive not found error during runtime
 mvn install:install-file -Dfile=c:/db/postgresql-42.2.6.jar -DgroupId=org.postgresql -DartifactId=postgresql -Dversion=42.2.6 -Dpackaging=jar
##copy past it into tomcat/lib as well. with the rest of the jar files

#install the database
psql -U postgres 
none
 \ir src/main/resources/schema.sql

 psql -U postgres 
none
 \ir test.sql
 