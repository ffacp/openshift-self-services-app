package org.samir.openshift.selfservices.dto;

import java.util.Date;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
public class Project {

	@NotBlank
	@Length(min = 2, message = "Project name must be at least 2 characters long")
	private String name;
	private String displayName;
	private String description;
	private String requester;
	private Date createdOn;

	@NotBlank
	@Pattern(regexp = "O-(AD-DEV|WY-UAT|DO-UAT|WY-PROD|DO-PROD)-.[0-9]{1,5}", message = "Invalid Quota ID Format")
	private String quotaId;

	@NotBlank
	private String quotaOwner;
	
	private String status;
	private String url;
}
