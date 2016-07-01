# SoftwareConstruction
Team 7: Repository indicated for the Software Construction CS 5374 course Summer 2016.
This repository is used to store documentation, along with source code, and other project requirements.

## Dependency Management

Maven project so the following dependancies are being used. You can find them in the pom.xml

Current Release

```xml
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>

    <dependency>
      <groupId>org.apache.commons</groupId>
      <artifactId>commons-lang3</artifactId>
      <version>3.4</version>
    </dependency>

    <dependency>
      <groupId>com.github.javaparser</groupId>
      <artifactId>javaparser-core</artifactId>
      <version>2.4.0</version>
    </dependency>
```

## How To Compile Sources

If you have checkout the project from GitHub you can build the project with maven using:

```
mvn clean install
```

## Neatbeans

######At netbens menu bar: Team->Git->Clone
######Paste github project in the 'Repository URL' field
######Follow the wizard and click next
######Right click in the project (at the left panel) and select 'Clean and Build'
######Main class located in Source Packege... You can use the Test package as well
