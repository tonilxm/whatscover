package com.whatscover.domain.enumeration;

public enum ProductPremiumRateEntityStatus {

	UPDATE("update"), NEW("new"), DELETE("delete"), DEFAULT("default");

	private final String key;

	private ProductPremiumRateEntityStatus(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
}
