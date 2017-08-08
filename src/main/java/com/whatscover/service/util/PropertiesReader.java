package com.whatscover.service.util;

import org.springframework.context.i18n.LocaleContextHolder;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Simple class for reading Properties File.
 *
 * @author minh
 */
public final class PropertiesReader {
	public static final String DEFAULT_PROPERTIES = "config" + File.separator + "ApplicationResources";
	// public static final String MESSAGES_PROPERTIES = "MessagesRes.ources";
	public static Locale locale = LocaleContextHolder.getLocale();

	public static String getPropertiesValue(String propertiesKey) {
		return ResourceBundle.getBundle(DEFAULT_PROPERTIES, locale).getString(propertiesKey);
	}

	public static String getPropertiesValue(String propertiesKey, Object[] params) {
		String message = ResourceBundle.getBundle(DEFAULT_PROPERTIES, locale).getString(propertiesKey);
		for (int i = 0; i < params.length; i++) {
			message = message.replace("{" + i + "}", String.valueOf(params[i]));
		}

		return message;
	}

	public static String getPropertiesValue(String propertiesKey, String fileName) {
		return ResourceBundle.getBundle(fileName, locale).getString(propertiesKey);
	}

	public static String getPropertiesValue(String propertiesKey, Object[] params, String fileName) {
		String message = ResourceBundle.getBundle(fileName, locale).getString(propertiesKey);

		if (message != null && !message.trim().isEmpty()) {
			for (int i = 0; i < params.length; i++) {
				message = message.replace("{" + i + "}", String.valueOf(params[i]));
			}
		}

		return message;
	}
}
