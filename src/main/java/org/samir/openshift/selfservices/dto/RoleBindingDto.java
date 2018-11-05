package org.samir.openshift.selfservices.dto;

import lombok.Data;

@Data
public class RoleBindingDto extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private String name;
	private String namespace;
	private String role;
	private String subjectKind;
	private String subject;
	
	public RoleBindingDto() {
		
	}
			
	public RoleBindingDto(String name, String namespace, String role, String subjectKind, String subject) {
		super();
		this.name = name;
		this.namespace = namespace;
		this.role = role;
		this.subjectKind = subjectKind;
		this.subject = subject;
	}	
}
