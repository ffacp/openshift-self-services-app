package io.fabric8.openshift.api.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class BaseObject implements Serializable {

	@JsonIgnore
	private static final long serialVersionUID = 1L;

}
