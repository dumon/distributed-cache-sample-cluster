package com.dumon.cache.storage.helper;

import static com.dumon.cache.storage.helper.Constants.JVM.JVM_PARAM_PREFIX;

import com.dumon.cache.storage.entity.Blog;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class LoadHelper {

    private static final Logger LOG = LoggerFactory.getLogger(LoadHelper.class);

    /**
     * Open the *.json, unmarshal every entry into a Object.
     *
     * @return a List of User objects.
     * @throws IOException if ObjectMapper unable to open file.
     */
    public static List<Blog> importDefaultBlogs() throws IOException {
        InputStream resource = Blog.class.getResourceAsStream("/blogs.json");
        return new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY).
                readValue(resource,new TypeReference<List<Blog>>(){});
    }

    public static List<Blog> importBlogsFromFile(final String filePath) throws IOException {
        try {
            File file = Paths.get(filePath).toFile();
            if (!file.exists()) {
                throw new IOException("Invalid file path!");
            }
            return new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY).
                    readValue(file, new TypeReference<List<Blog>>() {});
        } catch (final IOException exc) {
            LOG.error("Cannot read blogs from file {}, default will be loaded", filePath, exc);
            return importDefaultBlogs();
        }
    }

    public static Optional<String> getJvmArg(final String argName) {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        List<String> listOfArguments = runtimeMXBean.getInputArguments();

        return listOfArguments.stream()
                .filter(arg -> arg.contains(argName))
                .map(arg -> arg.substring(JVM_PARAM_PREFIX.length() + argName.length() + 1))
                .findFirst();
    }
}
