

# run this command and make sure the jar file is downloaded to that location

## this prevents the drive not found error during runtime
 mvn install:install-file -Dfile=c:/db/postgresql-42.2.6.jar -DgroupId=org.postgresql -DartifactId=postgresql -Dversion=42.2.6 -Dpackaging=jar
##copy past it into tomcat/lib as well. with the rest of the jar files

#install the database
psql -U postgres 
none
 \ir src/main/resources/schema.sql