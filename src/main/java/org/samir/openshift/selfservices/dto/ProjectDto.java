package org.samir.openshift.selfservices.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.samir.openshift.selfservices.utils.WebUtils;

import lombok.Data;

@Data
public class ProjectDto extends BaseDTO {

	private static final long serialVersionUID = 1L;
	
	@NotBlank
	@Length(min = 2, message = "Project name must be at least 2 characters long")
	private String name;
	private String displayName;
	private String description;
	private String requester;
	private Date createdOn;

	@NotBlank
	@Pattern(regexp = "o-(ad-dev|wy-uat|do-uat|wy-prod|do-prod)-.[0-9]{1,5}", message = "Invalid Quota ID Format")
	private String quotaId;

	@NotBlank
	private String quotaOwner;
	
	private String status;
	private String url;
	
	private LimitRangeDto limits = new LimitRangeDto();
	
	private List<RoleBindingDto> roleBindings = new ArrayList<>();
	
	public ProjectDto() {
		super();
		this.requester = (String) WebUtils.getSessionAttribute("username");
	}
}
