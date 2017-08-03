package com.whatscover.web.rest.errors;

import java.io.Serializable;

/**
 * Created by toni on 31/07/2017.
 */
public class BusinessErrorVM implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String message;
    private final String description;

    public BusinessErrorVM(String message) {
        this(message, null);
    }

    public BusinessErrorVM(String message, String description) {
        this.message = message;
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
