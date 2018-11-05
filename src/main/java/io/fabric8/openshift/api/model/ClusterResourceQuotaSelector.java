package io.fabric8.openshift.api.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.fabric8.kubernetes.api.model.LabelSelector;
import lombok.Data;

@Data
public class ClusterResourceQuotaSelector extends BaseObject {

	@JsonIgnore
	private static final long serialVersionUID = 1L;
	
	private Map<String, String> annotations;
	private LabelSelector labels;
}
