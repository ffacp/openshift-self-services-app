package org.samir.openshift.selfservices.utils;

import java.util.ArrayList;
import java.util.List;

import org.samir.openshift.selfservices.dto.ProjectDto;
import org.samir.openshift.selfservices.dto.RoleBindingDto;
import org.springframework.stereotype.Component;

@Component
public class RoleBindingsUtils {

	public static List<RoleBindingDto> generateRoleBindings(ProjectDto project){
		List<RoleBindingDto> roleBindings = new ArrayList<>();
		roleBindings.add(new RoleBindingDto("system:deployers", project.getName(), "system:deployer", "ServiceAccount", "deployer"));
		roleBindings.add(new RoleBindingDto("system:image-builders", project.getName(), "system:image-builder", "ServiceAccount", "builder"));
		
		roleBindings.add(new RoleBindingDto("system:image-pullers", project.getName(), "system:image-puller", "Group", "system:serviceaccounts:" + project.getName()));
		
		roleBindings.add(new RoleBindingDto("admin", project.getName(), "admin", "User", project.getRequester()));
		roleBindings.add(new RoleBindingDto("owner", project.getName(), "admin", "User", project.getQuotaOwner()));
	
		return roleBindings;
	}
}
