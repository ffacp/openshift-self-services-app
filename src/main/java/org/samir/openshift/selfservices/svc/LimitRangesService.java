package org.samir.openshift.selfservices.svc;

import org.samir.openshift.selfservices.utils.OpenShiftUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.fabric8.kubernetes.api.model.LimitRange;

@Service
public class LimitRangesService {

	@Autowired
	private OpenShiftUtils openShiftUtils;
	
	public void saveLimitRange(LimitRange limitRange) {
		openShiftUtils.getSystemClient().limitRanges()
			.inNamespace(limitRange.getMetadata().getNamespace())
			.createOrReplace(limitRange);
	}
}
