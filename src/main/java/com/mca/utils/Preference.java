package com.mca.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Preference {

    /**
     * Utility method to fetch property value on below precedence
     * 1. System property(ie value passed from command line)
     * 2. from default.properties
     * 3. null if it's neighter 1 nor 2
     *
     * @param property
     * @return
     */
    public static String getPreference(final String property) {

        if (System.getProperties().containsKey(property)) {
            return System.getProperty(property);
        }

        String configValue;
        if ((configValue = readConfig(property)) != null) {
            return configValue;
        }

        return null;
    }

    private static String readConfig(final String property) {

        final InputStream input = ClassLoader.getSystemClassLoader().getResourceAsStream("config/defaults.properties");
        if (input != null) {
            try {
                final Properties properties = new Properties();
                properties.load(input);
                return properties.getProperty(property);
            } catch (final Exception e) {
            }
        }
        return null;
    }
}
