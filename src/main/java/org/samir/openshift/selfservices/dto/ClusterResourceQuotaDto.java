package org.samir.openshift.selfservices.dto;

import java.util.Date;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.samir.openshift.selfservices.utils.WebUtils;

import lombok.Data;

@Data
public class ClusterResourceQuotaDto extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private String uid;
	
	@NotBlank
	@Pattern(regexp = "o-(ad-dev|wy-uat|do-uat|wy-prod|do-prod)-[0-9]{1,5}", message = "Invalid Quota ID Format")
	private String name;
	
	@NotBlank
	private String owner;
	
	@NotBlank
	private String ownerEmail;
	
	private Date createdOn;
	private String createdBy;
	
	private Date expireOn;
	
	private QuantityDto cpuRequest = new QuantityDto(0, "");
	private QuantityDto cpuLimit = new QuantityDto(0, "");
	
	private QuantityDto memoryRequest = new QuantityDto(0, "Gi");
	private QuantityDto memoryLimit = new QuantityDto(0, "Gi");
	
	private QuantityDto glusterStorage = new QuantityDto(0, "Gi");
	private QuantityDto blockStorage = new QuantityDto(0, "Gi");
	
	public ClusterResourceQuotaDto() {
		super();
		this.createdBy = (String) WebUtils.getSessionAttribute("username");
	}
	
	public void setName(String name) {
		this.name = (name != null) ? name.toLowerCase() : null;
	}
	
	public void setOwner(String owner) {
		this.owner = (owner != null) ? owner.toLowerCase() : null;
	}
	
	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = (ownerEmail != null) ? ownerEmail.toLowerCase() : null;
	}
	
}
