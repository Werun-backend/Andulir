package org.andulir.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration bean for XML file-related settings.
 */
@Component
@ConfigurationProperties(prefix = "andulir.file")
@Data
@NoArgsConstructor
public class XmlFileProperty {
    /**
     * Automatic configuration for the XML generation location.
     * root refers to the project's root directory.
     * work refers to the working directory of the Java application.
     */
    private String pathSetting;
    /**
     * Manual configuration for the XML generation location.
     */
    private String path;
    /**
     * XML file name.
     */
    private String filename;
}
