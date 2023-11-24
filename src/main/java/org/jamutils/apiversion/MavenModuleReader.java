package org.jamutils.apiversion;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;

/**
 * Checks if there is a <modules> section in the pom.xml file.
 * If yes, it will return a list of the modules.
 */
public class MavenModuleReader {

    MavenProject project;

    public MavenModuleReader(MavenProject project) {
        this.project = project;
    }
    public List<String> readModules() throws IOException {
        var mavenReader = new MavenXpp3Reader();
        try (var in = new FileInputStream(project.getFile())) {
            var model = mavenReader.read(in);

            var modules = model.getModules();
            return Objects.requireNonNullElseGet(modules, ArrayList::new);
        } catch (Exception e) {
            throw new IOException("Error reading pom.xml", e);
        }
    }
}
