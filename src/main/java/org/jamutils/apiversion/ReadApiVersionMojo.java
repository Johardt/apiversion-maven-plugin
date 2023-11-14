package org.jamutils.apiversion;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Reads the version from the API file and writes it to a temporary file.
 * Obsolete. Use {@link org.jamutils.apiversion.SetApiVersionMojo} instead.
 * @deprecated Use {@link org.jamutils.apiversion.SetApiVersionMojo} instead.
 */
@Deprecated(since = "2.1.0", forRemoval = true)
@Mojo(name = "read-version")
public class ReadApiVersionMojo extends AbstractMojo {

    @Parameter(property = "file.path", defaultValue = "api.yaml")
    private String filePath;

    @Parameter(defaultValue = "${project}", readonly = true)
    MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        var apiVersionReader = new ApiVersionReader(filePath);

        try {
            var version = apiVersionReader.readVersion();
            getLog().info("Writing version " + version + " to version.tmp");
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
