package io.fabric8.openshift.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import lombok.Data;

@Data
public class ClusterResourceQuotaList extends BaseObject {

	@JsonIgnore
	private static final long serialVersionUID = 1L;
	
	private String apiVersion = "v1";
	private String kind = "ClusterResourceQuotaList";
	private ObjectMeta metadata;

	private List<ClusterResourceQuota> items;
}
