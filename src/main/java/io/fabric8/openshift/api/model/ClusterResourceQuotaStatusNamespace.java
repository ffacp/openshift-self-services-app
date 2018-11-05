package io.fabric8.openshift.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class ClusterResourceQuotaStatusNamespace extends BaseObject {

	@JsonIgnore
	private static final long serialVersionUID = 1L;
	
	private String namespace;
	private ClusterResourceQuotaStatusTotal status;
}
