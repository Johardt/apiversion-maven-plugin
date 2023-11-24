package org.jamutils.apiversion;

import java.io.IOException;
import java.util.List;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Sets the version in the pom.xml file to the version in the API file.
 */
@Mojo(name = "set-version")
public class SetApiVersionMojo extends AbstractMojo {

    @Parameter(property = "file.path", defaultValue = "api.yaml")
    private String apiFilePath;

    @Parameter(defaultValue = "${project}")
    MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        var reader = new ApiVersionReader(apiFilePath);
        String version;
        try {
            version = reader.readVersion();
        } catch (IOException e) {
            throw new MojoExecutionException("Error reading API file", e);
        }

        MavenModuleReader moduleReader = new MavenModuleReader(project);
        List<String> modules;
        try {
            modules = moduleReader.readModules();
        } catch (IOException e) {
            throw new MojoExecutionException("Error reading pom.xml", e);
        }

        MavenProjectWriter writer;
        if (modules.isEmpty()) {
            // Only write the version in the root/local pom.xml
            try {
                writer = new MavenProjectWriter(project.getFile());
                writer.writeVersion(version);
            } catch (IOException e) {
                throw new MojoExecutionException("Error writing version to pom.xml", e);
            }
        } else {
            // Only write the version in every child pom
            for (String module : modules) {
                var moduleProject = new MavenProject();
                moduleProject.setFile(project.getFile().toPath().getParent().resolve(module).resolve("pom.xml").toFile());
                writer = new MavenProjectWriter(moduleProject.getFile());
                try {
                    writer.writeVersion(version);
                } catch (IOException e) {
                    throw new MojoExecutionException("Error writing version to pom.xml", e);
                }
            }
        }
    }

    public void setApiFilePath(String apiFilePath) {
        this.apiFilePath = apiFilePath;
    }

    public void setProject(MavenProject project) {
        this.project = project;
    }
}
