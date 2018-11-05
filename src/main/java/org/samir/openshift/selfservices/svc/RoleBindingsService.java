package org.samir.openshift.selfservices.svc;

import org.samir.openshift.selfservices.utils.OpenShiftUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.fabric8.openshift.api.model.RoleBinding;

@Service
public class RoleBindingsService {

	@Autowired
	private OpenShiftUtils openShiftUtils;
	
	public void saveRoleBinding(RoleBinding roleBinding) {
		openShiftUtils.getSystemClient().roleBindings()
			.inNamespace(roleBinding.getMetadata().getNamespace())
			.createOrReplace(roleBinding);
	}
}
