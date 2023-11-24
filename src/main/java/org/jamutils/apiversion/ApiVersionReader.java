package org.jamutils.apiversion;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Used to read the version from an OpenAPI spec.
 */
public class ApiVersionReader {

    private String filePath;

    public ApiVersionReader(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Reads the version from the API file.
     * @return The version of the OpenAPI spec
     * @throws IOException If there is an error reading the API file
     */
    public String readVersion() throws IOException {
        ObjectMapper mapper;

        // Determine the format by file extension
        if (filePath.endsWith(".json")) {
            mapper = new ObjectMapper(new JsonFactory());
        } else if (filePath.endsWith(".yaml") || filePath.endsWith(".yml")) {
            mapper = new ObjectMapper(new YAMLFactory());
        } else {
            throw new IOException("Unsupported file format");
        }
        // Load the file into a Map
        Map<String, Object> data = mapper.readValue(new File(filePath), new TypeReference<Map<String, Object>>() {
        });

        // Get version from the Map
        Map<String, Object> info = (Map<String, Object>) data.get("info");
        String version = (String) info.get("version");

        if (version == null) {
            throw new IOException("Version not found in API file");
        }

        return version;

    }

    /**
     * Sets the path to the API file.
     * Mainly used for testing.
     * @param filePath The path to the API file
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Gets the path to the API file.
     * Mainly used for testing.
     * @return The path to the API file
     */
    public String getFilePath() {
        return filePath;
    }
}
