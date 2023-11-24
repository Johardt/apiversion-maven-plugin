package org.jamutils.apiversion;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;

/**
 * Used to read the modules from a Maven project.
 */
public class MavenModuleReader {

    MavenProject project;

    public MavenModuleReader(MavenProject project) {
        this.project = project;
    }

    /**
     * Reads the modules from the pom.xml file. If there are no modules, an empty list is returned.
     * @return A list of modules
     * @throws IOException If there is an error reading the pom.xml file
     */
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
