package org.jamutils.apiversion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.AfterEach;
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

    // Reset the pom after each test
    @AfterEach
    void tearDown() throws IOException {
        MavenProjectWriter writer = new MavenProjectWriter(project.getFile());
        writer.writeVersion("0.1.0");
    }

    @Test
    void testSetMultiPomVersion() throws MojoExecutionException, MojoFailureException {
        var setterMojo = new SetApiVersionMojo();
        setterMojo.setProject(project);
        setterMojo.setApiFilePath("src/test/resources/api.yaml");

        setterMojo.execute();

        var mavenReader = new MavenXpp3Reader();
        try (var in = new FileInputStream(project.getFile().toPath().getParent().resolve("client").resolve("pom.xml").toFile())) {
            var model = mavenReader.read(in);

            assertEquals("1.0.0", model.getVersion(), "Version is not 0.1.0");
        } catch (Exception e) {
            throw new MojoExecutionException("Error reading pom.xml", e);
        }
    }

    @Test
    void testSinglePomVersion() throws MojoExecutionException, MojoFailureException {
        var setterMojo = new SetApiVersionMojo();
        project = new MavenProject();
        project.setFile(new File("src/test/resources/client/pom.xml"));

        setterMojo.setProject(project);
        setterMojo.setApiFilePath("src/test/resources/api.yaml");

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
