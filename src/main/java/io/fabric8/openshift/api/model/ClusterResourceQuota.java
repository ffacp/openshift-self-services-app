package io.fabric8.openshift.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import lombok.Data;

@Data
public class ClusterResourceQuota extends BaseObject {

	@JsonIgnore
	private static final long serialVersionUID = 1L;
	
	private String apiVersion = "v1";
	private String kind = "ClusterResourceQuota";
	private ObjectMeta metadata;
	
	private ClusterResourceQuotaSpec spec;
	private ClusterResourceQuotaStatus status;
}
