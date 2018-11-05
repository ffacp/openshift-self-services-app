package io.fabric8.openshift.api.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.fabric8.kubernetes.api.model.Quantity;
import lombok.Data;

@Data
public class ClusterResourceQuotaStatusTotal extends BaseObject {

	@JsonIgnore
	private static final long serialVersionUID = 1L;
	
	private Map<String, Quantity> hard;
    private Map<String, Quantity> used;
}
