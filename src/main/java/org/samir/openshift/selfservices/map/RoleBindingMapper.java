package org.samir.openshift.selfservices.map;

import java.util.ArrayList;
import java.util.List;

import org.samir.openshift.selfservices.dto.RoleBindingDto;
import org.springframework.stereotype.Component;

import io.fabric8.kubernetes.api.model.ObjectReferenceBuilder;
import io.fabric8.openshift.api.model.RoleBinding;
import io.fabric8.openshift.api.model.RoleBindingBuilder;

@Component
public class RoleBindingMapper {

	public RoleBindingDto map(RoleBinding source) {
		RoleBindingDto destination = new RoleBindingDto();
		
		destination.setName(source.getMetadata().getName());
		destination.setNamespace(source.getMetadata().getNamespace());
		destination.setRole(source.getRoleRef().getName());
		destination.setSubjectKind(source.getSubjects().get(0).getKind());
		destination.setSubject(source.getSubjects().get(0).getName());
		
		return destination;
	}

	public RoleBinding map(RoleBindingDto source) {
		return new RoleBindingBuilder()
		        .withNewMetadata().withName(source.getName()).withNamespace(source.getNamespace()).endMetadata()
		        .withRoleRef(new ObjectReferenceBuilder()
	                .withKind("Role")
	                .withName(source.getRole())
	                .build()
		        )
		        .addNewSubject().withKind(source.getSubjectKind()).withNamespace(source.getNamespace()).withName(source.getSubject()).endSubject()
	        .build();
	}
	
	public List<RoleBindingDto> map(List<RoleBinding> sourceList) {
		List<RoleBindingDto> destinationList = new ArrayList<>();
		
		if (sourceList != null && sourceList.size() > 0) {
			for (RoleBinding source : sourceList) {
				destinationList.add(map(source));
			}	
		}
		return destinationList;
	}
}
