package org.samir.openshift.selfservices.dto;

import lombok.Data;

@Data
public class LimitRangeDto extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private String name = "limits";
	private String namespace;
	
	private String type = "Container";
	
	private String defaultCpuRequest = "100m";
	private String defaultCpu = "1";
	
	private String defaultMemoryRequest = "256Mi";
	private String defaultMemory = "1Gi";
}
