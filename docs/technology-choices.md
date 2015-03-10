#Versionhistory
Date | Version | Author | Description
------------- | ------------- | ------------- | -------------
2013-03-21  |  1  | ingimar.erlingsson@nrm.se  | original
<date>  | <version>  | <email>  | <desc>

# Technologies 
This document describes the technology choices that were made <p>
Documenting the technology makes it easier for others to get the mediaserver running <p>

##  Technology choices
JavaSE6 or higher, JEE6 or higher. <p>
- EJB 3.x is beeing used ( need , application server such as glassfish or wildfly).<p>
- relational database is used .<p>

- JEE6-stack or higher (stack includes EJB 3.x, JPA 2.x , JAX-RS and soforth)<p>
-- database agnostic, gained by using liquibase <p>
- tested with MySQL 5.5, PostgreSQL 9.x<p>
- Application Server agnostic<p>
-- tested with Wildfly 8.1, 8.2<p>

## used during development

1. Operating System : GNU/Linux ( Ubuntu 14.04.1 LTS)
2. JDK 1.7 <p>
2.1. java version "1.7.0_75" <p>
2.2. OpenJDK Runtime Environment (IcedTea 2.5.4) (7u75-2.5.4-1~trusty1)<p>
3. JEE6-stack / JEE7-stack <p>
3.1 EJB 3.x<p>
3.2 JPA ( implemented using Eclipselink ) <p>
3.3 JAX-RS ( implemented using RESTeasy)<p>
4. Databases (liquibase for database agnostic)<p>
4.1 MySQL 5.5 (5.5.41-0ubuntu0.14.04.1)<p>
4.2 PostgreSQL 9.x <p>
5. maven <p>
 Apache Maven 3.0.5  <p>
6. IDE <p><p>
 Netbeans 7.x and 8.x has been used <p>

7 utils <p>
7.1 metadata extractor : https://drewnoakes.com/code/exif/ <p>
7.2 for resizing images : <p>
7.2.1  http://projects.coobird.net/thumbnailator/   <p>
7.2.3 https://code.google.com/p/thumbnailator/ <p>

## technologies
- liquibase : http://www.liquibase.org/ 
- RESTeasy : http://resteasy.jboss.org/ 