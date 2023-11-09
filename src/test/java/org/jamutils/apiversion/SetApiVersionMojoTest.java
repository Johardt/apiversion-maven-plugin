package org.jamutils.apiversion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SetApiVersionMojoTest {

    MavenProject project;

    @BeforeEach
    void setUp() {
        project = new MavenProject();
        var baseDir = new File("src/test/resources");
        project.setFile(new File(baseDir, "pom.xml"));
    }

    @Test
    void testSetVersion() throws MojoExecutionException, MojoFailureException {
        var setterMojo = new SetApiVersionMojo();
        setterMojo.setProject(project);
        setterMojo.setFilePath("src/test/resources/api.yaml");

        setterMojo.execute();

        var mavenReader = new MavenXpp3Reader();
        try (var in = new FileInputStream(project.getFile())) {
            var model = mavenReader.read(in);

            assertEquals("1.0.0", model.getVersion(), "Version is not 0.1.0");
        } catch (Exception e) {
            throw new MojoExecutionException("Error reading pom.xml", e);
        }
    }
}
