Repository Check Maven Plugin
=============================
This plugin checks the connectivity to maven repositories before the build starts.

Usage
=====
Example pom.xml:
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.test</groupId>
    <artifactId>test-project</artifactId>
    <version>1.0-SNAPSHOT</version>
    <properties>
        <repository-check.artifactCoords>org.apache.maven:maven-parent:pom:30</repository-check.artifactCoords>
    </properties>

    <build>
        <plugins>
            <plugin>
                <groupId>com.t-systems.sbs</groupId>
                <artifactId>repository-check-maven-plugin</artifactId>
                <version>1.0</version>
                <executions>
                    <execution>
                        <id>check-repository</id>
                        <goals>
                            <goal>check-repository</goal>
                        </goals>
                        <phase>validate</phase>
                    </execution>
                </executions>
                <configuration>
                    <artifactCoords>${repository-check.artifactCoords}</artifactCoords>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```    

