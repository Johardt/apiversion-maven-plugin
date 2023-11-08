package org.jamutils.apiversion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class MavenProjectWriter {

    private File pomFile;

    public MavenProjectWriter(File pomFile) {
        this.pomFile = pomFile;
    }

    public void writeVersion(String version) throws IOException {
        MavenXpp3Reader mavenReader = new MavenXpp3Reader();
        MavenXpp3Writer mavenWriter = new MavenXpp3Writer();
        Model model;

        // Read the existing pom.xml into the model object.
        try (InputStream in = Files.newInputStream(pomFile.toPath())) {
            model = mavenReader.read(in, false);
        } catch (FileNotFoundException | XmlPullParserException e) {
            throw new IOException(e);
        }

        // Update the version in the model object.
        model.setVersion(version);

        // Write the model object back to pom.xml.
        try (OutputStream out = new FileOutputStream(pomFile)) {
            mavenWriter.write(out, model);
        }
    }

}
