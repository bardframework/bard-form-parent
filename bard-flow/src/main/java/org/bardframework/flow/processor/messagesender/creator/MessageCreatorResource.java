package org.bardframework.flow.processor.messagesender.creator;

import org.apache.commons.io.IOUtils;
import org.bardframework.commons.utils.StringTemplateUtils;
import org.bardframework.commons.web.utils.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MessageCreatorResource implements MessageCreator {

    protected final String resourcePathTemplate;
    protected String localeArgKey = "locale";

    public MessageCreatorResource(String resourcePathTemplate) {
        this.resourcePathTemplate = resourcePathTemplate;
    }

    @Override
    public String create(Map<String, String> args, Locale locale) throws IOException {
        Map<String, String> newArgs = new HashMap<>(args);
        newArgs.put(localeArgKey, locale.getLanguage());
        String resourcePath = StringTemplateUtils.fillTemplate(resourcePathTemplate, newArgs);
        try (InputStream inputStream = ResourceUtils.getResource(resourcePath).getInputStream()) {
            String contentTemplate = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            return StringTemplateUtils.fillTemplate(contentTemplate, newArgs);
        }
    }

    public String getResourcePathTemplate() {
        return resourcePathTemplate;
    }
}
