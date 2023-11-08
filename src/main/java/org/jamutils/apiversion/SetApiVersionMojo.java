package org.jamutils.apiversion;

import java.io.IOException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "set-version")
public class SetApiVersionMojo extends AbstractMojo {

    @Parameter(property = "file.path", defaultValue = "api.yaml")
    private String filePath;

    @Parameter(defaultValue = "${project}")
    MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        var reader = new ApiVersionReader(filePath);
        String version;
        try {
            version = reader.readVersion();
        } catch (IOException e) {
            throw new MojoExecutionException("Error reading API file", e);
        }

        var writer = new MavenProjectWriter(project.getFile());
        try {
            writer.writeVersion(version);
        } catch (IOException e) {
            throw new MojoExecutionException("Error writing version to pom.xml", e);
        }
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setProject(MavenProject project) {
        this.project = project;
    }
}
