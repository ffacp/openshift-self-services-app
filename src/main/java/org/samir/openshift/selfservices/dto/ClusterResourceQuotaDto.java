package org.samir.openshift.selfservices.dto;

import java.util.Date;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
public class ClusterResourceQuotaDto extends BaseDTO {

	private static final long serialVersionUID = 1L;

	@NotBlank
	@Pattern(regexp = "o-(ad-dev|wy-uat|do-uat|wy-prod|do-prod)-.[0-9]{1,5}", message = "Invalid Quota ID Format")
	private String name;
	
	@NotBlank
	private String owner;
	
	@NotBlank
	private String ownerEmail;
	
	private Date createdOn;
	private Date expireOn;
	
	private String cpuRequest;
	private String cpuLimit;
	
	private String memoryRequest;
	private String memoryLimit;
	
	private String glusterStorage;
	private String blockStorage;
	
}
