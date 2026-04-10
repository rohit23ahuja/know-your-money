package com.kym.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class PropertyUtils {
    @Autowired
    private Environment environment;

    public Map<String, String> getPropertiesMap() {
        Map<String, String> result = new LinkedHashMap<>();

        ((AbstractEnvironment) environment).getPropertySources()
                .forEach(propertySource -> {
                    if (propertySource instanceof EnumerablePropertySource<?>) {
                        for (String key : ((EnumerablePropertySource<?>) propertySource).getPropertyNames()) {
                                result.put(key, environment.getProperty(key));
                        }
                    }
                });

        return result;
    }
}
