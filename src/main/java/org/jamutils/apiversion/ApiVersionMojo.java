package org.jamutils.apiversion;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "read-api-version", defaultPhase = LifecyclePhase.VALIDATE)
public class ApiVersionMojo extends AbstractMojo {

    @Parameter(property = "file.path", defaultValue = "api.yaml")
    private String filePath;

    @Parameter(defaultValue = "${project}", readonly = true)
    MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        ObjectMapper mapper;

        // Determine the format by file extension
        if (filePath.endsWith(".json")) {
            mapper = new ObjectMapper(new JsonFactory());
        } else if (filePath.endsWith(".yaml") || filePath.endsWith(".yml")) {
            mapper = new ObjectMapper(new YAMLFactory());
        } else {
            throw new MojoExecutionException("Unsupported file format");
        }

        try {
            // Load the file into a Map
            Map<String, Object> data = mapper.readValue(new File(filePath), new TypeReference<Map<String, Object>>() {
            });

            // Get version from the Map
            Map<String, Object> info = (Map<String, Object>) data.get("info");
            String version = (String) info.get("version");

            // Log and set the property
            getLog().info("Writing version " + version + " to version.tmp");

            // Write to version.tmp at the project root
            File versionFile = new File(project.getBasedir(), "version.tmp");
            try (PrintWriter out = new PrintWriter(versionFile)) {
                out.println(version);
            }
        } catch (IOException e) {
            throw new MojoExecutionException("Error reading API file", e);
        }
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
