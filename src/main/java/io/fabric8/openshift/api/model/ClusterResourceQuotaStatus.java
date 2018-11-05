package io.fabric8.openshift.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class ClusterResourceQuotaStatus extends BaseObject {

	@JsonIgnore
	private static final long serialVersionUID = 1L;
	
	private ClusterResourceQuotaStatusTotal total;
    private List<ClusterResourceQuotaStatusNamespace> namespaces;
}
