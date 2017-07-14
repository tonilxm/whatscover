package com.whatscover.service.dto;

import java.time.Instant;

/**
 * A DTO for AbstractAuditingEntity.
 */
public class AbstractAuditingDTO {

	protected String createdBy;

	protected Instant createdDate;

	protected String lastModifiedBy;

	protected Instant lastModifiedDate;

	public String getCreatedBy() {
		return createdBy;
	}

	public Instant getCreatedDate() {
		return createdDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public Instant getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Instant lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
}
