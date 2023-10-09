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

class ApiVersionMojoTest {

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
        ApiVersionMojo apiVersionMojo = new ApiVersionMojo();
        apiVersionMojo.project = mockProject;
        apiVersionMojo.setFilePath("src/test/resources/api.yaml");

        apiVersionMojo.execute();

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
        ApiVersionMojo apiVersionMojo = new ApiVersionMojo();
        apiVersionMojo.project = mockProject;
        apiVersionMojo.setFilePath("src/test/resources/api.json");

        apiVersionMojo.execute();

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
        ApiVersionMojo apiVersionMojo = new ApiVersionMojo();
        apiVersionMojo.project = mockProject;
        apiVersionMojo.setFilePath(apiFile);

        assertThrows(MojoExecutionException.class, () -> {
            try {
                apiVersionMojo.execute();
            } catch (MojoFailureException e) {
                fail("MojoFailureException thrown");
            }
        }, "Exception not thrown");
    }
}