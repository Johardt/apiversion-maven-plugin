package org.jamutils.apiversion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

class ReadApiVersionMojoTest {

    @AfterEach
    public void tearDown() {
        File outputTmpFile = new File("version.tmp");
        if (outputTmpFile.exists()) {
            outputTmpFile.delete();
        }
    }

    @Test
    void testReadApiVersionYaml() throws Exception {
        MavenProject mockProject = Mockito.mock(MavenProject.class);
        ReadApiVersionMojo readApiVersionMojo = new ReadApiVersionMojo();
        readApiVersionMojo.project = mockProject;
        readApiVersionMojo.setFilePath("src/test/resources/api.yaml");

        readApiVersionMojo.execute();

        File outputTmpFile = new File("version.tmp");

        assertTrue(outputTmpFile.exists(), "Temporary file doesn't exist");

        try (BufferedReader br = new BufferedReader(new FileReader(outputTmpFile))) {
            String version = br.readLine();
            assertEquals("1.0.0", version, "Version doesn't match");
        }
    }

    @Test
    void testReadApiVersionJson() throws Exception {
        MavenProject mockProject = Mockito.mock(MavenProject.class);
        ReadApiVersionMojo readApiVersionMojo = new ReadApiVersionMojo();
        readApiVersionMojo.project = mockProject;
        readApiVersionMojo.setFilePath("src/test/resources/api.json");

        readApiVersionMojo.execute();

        File outputTmpFile = new File("version.tmp");

        assertTrue(outputTmpFile.exists(), "Temporary file doesn't exist");

        try (BufferedReader br = new BufferedReader(new FileReader(outputTmpFile))) {
            String version = br.readLine();
            assertEquals("1.0.0", version, "Version doesn't match");
        }
    }

    @ParameterizedTest
    @CsvSource({"src/test/resources/api2.yaml", "src/test/resources/api.txt", "src/test/resources/invalidApi.yaml"})
    void testInvalidApiSpec(String apiFile) {
        MavenProject mockProject = Mockito.mock(MavenProject.class);
        ReadApiVersionMojo readApiVersionMojo = new ReadApiVersionMojo();
        readApiVersionMojo.project = mockProject;
        readApiVersionMojo.setFilePath(apiFile);

        assertThrows(MojoExecutionException.class, () -> {
            try {
                readApiVersionMojo.execute();
            } catch (MojoFailureException e) {
                fail("MojoFailureException thrown");
            }
        }, "Exception not thrown");
    }
}