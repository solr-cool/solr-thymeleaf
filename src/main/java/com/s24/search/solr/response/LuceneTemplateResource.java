package com.s24.search.solr.response;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.apache.lucene.analysis.util.ResourceLoader;
import org.thymeleaf.templateresource.ITemplateResource;
import org.thymeleaf.util.StringUtils;
import org.thymeleaf.util.Validate;

public class LuceneTemplateResource
    implements ITemplateResource {

    private final ResourceLoader resourceLoader;

    private final String path;

    private final String characterEncoding;

    public LuceneTemplateResource(ResourceLoader resourceLoader, String path, String characterEncoding) {
        this.resourceLoader = resourceLoader;
        this.path = path;
        this.characterEncoding = characterEncoding;
    }

    @Override
    public String getDescription() {
        return path;
    }

    @Override
    public String getBaseName() {
        if (StringUtils.isEmptyOrWhitespace(path)) {
            return null;
        }
        String basePath = (path.charAt(path.length() - 1) == '/' ? path.substring(0, path.length() - 1) : path);
        int slashPos = basePath.lastIndexOf('/');
        int dotPos = basePath.lastIndexOf('.');
        if (slashPos == -1) {
            if (dotPos != -1) {
                return basePath.substring(0, dotPos);
            }
        } else {
            if (dotPos != -1 && dotPos > slashPos + 1) {
                return basePath.substring(slashPos + 1, dotPos);
            }
            return basePath.substring(slashPos + 1);
        }
        return basePath.isEmpty() ? null : basePath;
    }

    @Override
    public boolean exists() {
        try {
            resourceLoader.openResource(path);
            return true;
        } catch (IOException exception) {
            return false;
        }
    }

    @Override
    public Reader reader() throws IOException {
        InputStream inputStream = resourceLoader.openResource(path);
        if (!StringUtils.isEmptyOrWhitespace(characterEncoding)) {
            return new BufferedReader(new InputStreamReader(new BufferedInputStream(inputStream), characterEncoding));
        }
        return new BufferedReader(new InputStreamReader(new BufferedInputStream(inputStream), StandardCharsets.UTF_8));
    }

    @Override
    public ITemplateResource relative(String relativeLocation) {
        Validate.notEmpty(relativeLocation, "Relative Path cannot be null or empty");
        String fullRelativeLocation;
        int separatorPos = path.lastIndexOf('/');
        if (separatorPos == -1) {
            fullRelativeLocation = relativeLocation;
        } else {
            StringBuilder relativeBuilder = new StringBuilder(path.length() + relativeLocation.length());
            relativeBuilder.append(path, 0, separatorPos);
            if (relativeLocation.charAt(0) != '/') {
                relativeBuilder.append('/');
            }
            relativeBuilder.append(relativeLocation);
            fullRelativeLocation = relativeBuilder.toString();
        }
        return new LuceneTemplateResource(resourceLoader, fullRelativeLocation, characterEncoding);
    }
}
