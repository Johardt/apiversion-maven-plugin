## apiversion-maven-plugin

This is a simple plugin that will extract the version property from a valid OpenAPI-spec and write it to a temporary file at the root of the project.
This can be used in a multistep maven build to set the version of the maven package according to the version of the OpenAPI-spec.
The plugin runs at the validate phase of the maven build lifecycle.

## Installation
You need to add this to your <pluginRepositories> in your pom.xml:
```xml
<pluginRepositories>
        <pluginRepository>
            <id>gitlab-maven</id>
            <url>https://gitlab.codecentric.de/api/v4/groups/8234/-/packages/maven</url>
        </pluginRepository>
</pluginRepositories>
```

## Usage
Using it is very simple, just add this to your pom.xml:
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.jamutils</groupId>
            <artifactId>apiversion-maven-plugin</artifactId>
            <version>1.0.0</version>
            <configuration>
                <filePath>src/main/resources/api.yaml</filePath>
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>read-api-version</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

Running `mvn validate` will create a `version.tmp` file at the level of the pom file. This file contains the version of 
the OpenAPI-specification. You can use it with mvn versions:set to set the version of the maven package.