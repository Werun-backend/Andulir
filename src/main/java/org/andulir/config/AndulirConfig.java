package org.andulir.config;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class AndulirConfig {
    @Bean
    public PodamFactory podamFactory() {
        return new PodamFactoryImpl();
    }

    @Bean
    public Document document() {
        return DocumentHelper.createDocument();
    }

    /**
     * Naming and path settings for XML files
     * Create a folder in the root directory
     * Default naming is changed to timestamp-based
     * @param property XML file property settings
     * @return xmlFile
     */
    @Bean
    public File file(XmlFileProperty property) throws IOException {
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();
        // Create a date-time formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        // Format the current date and time as a string
        String formattedDateTime = now.format(formatter);

        String path = "", fileSeparator = System.getProperty("file.separator");
        File dir;
        if(property.getPathSetting().equals("root") || (property.getPath() == null)) {
            path = path.concat(new File("").getCanonicalPath());
            path = path + fileSeparator + "andulirTest" + fileSeparator;
        } else if (property.getPathSetting().equals("work")) {
            path = path.concat(System.getProperty("user.dir"));
            path = path + fileSeparator + "andulirTest" + fileSeparator;
        } else {
            path = property.getPath();
        }

        // Create the directory
        dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs(); // Create the directory, including any necessary but nonexistent parent directories
        }

        path = path + formattedDateTime
                + (property.getFilename() != null ? property.getFilename() : "_atest.xml");
        return new File(path);
    }

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolProperty property) {
        return new ThreadPoolExecutor(
                property.getCoreSize(),
                property.getMaxSize(),
                property.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }
}
