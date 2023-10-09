package org.jamutils.apiversion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.junit.Test;
import org.mockito.Mockito;

public class ApiVersionMojoTest {

    @Test
    public void testReadApiVersionYaml() throws Exception {
        MavenProject mockProject = Mockito.mock(MavenProject.class);
        ApiVersionMojo apiVersionMojo = new ApiVersionMojo();
        apiVersionMojo.project = mockProject;
        apiVersionMojo.setFilePath("src/test/resources/api.yaml");

        apiVersionMojo.execute();

        File outputTmpFile = new File("version.tmp");

        assertTrue("Temporary file doesn't exist", outputTmpFile.exists());

        try (BufferedReader br = new BufferedReader(new FileReader(outputTmpFile))) {
            String version = br.readLine();
            assertEquals("Version doesn't match", "1.0.0", version);
        }
    }

    @Test
    public void testReadApiVersionJson() throws Exception {
        MavenProject mockProject = Mockito.mock(MavenProject.class);
        ApiVersionMojo apiVersionMojo = new ApiVersionMojo();
        apiVersionMojo.project = mockProject;
        apiVersionMojo.setFilePath("src/test/resources/api.json");

        apiVersionMojo.execute();

        File outputTmpFile = new File("version.tmp");

        assertTrue("Temporary file doesn't exist", outputTmpFile.exists());

        try (BufferedReader br = new BufferedReader(new FileReader(outputTmpFile))) {
            String version = br.readLine();
            assertEquals("Version doesn't match", "1.0.0", version);
        }
    }

    @Test
    public void testReadNonExistingFile() throws Exception {
        MavenProject mockProject = Mockito.mock(MavenProject.class);
        ApiVersionMojo apiVersionMojo = new ApiVersionMojo();
        apiVersionMojo.project = mockProject;
        apiVersionMojo.setFilePath("src/test/resources/api2.yaml");

        assertThrows("Exception not thrown", MojoExecutionException.class, apiVersionMojo::execute);
    }

    @Test
    public void testUnsupportedFileExtension() throws Exception {
        MavenProject mockProject = Mockito.mock(MavenProject.class);
        ApiVersionMojo apiVersionMojo = new ApiVersionMojo();
        apiVersionMojo.project = mockProject;
        apiVersionMojo.setFilePath("src/test/resources/api.txt");

        assertThrows("Exception not thrown", MojoExecutionException.class, apiVersionMojo::execute);
    }
}