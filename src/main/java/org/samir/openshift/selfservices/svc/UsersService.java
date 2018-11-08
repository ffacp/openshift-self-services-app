package org.samir.openshift.selfservices.svc;

import org.samir.openshift.selfservices.utils.OpenShiftUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import io.fabric8.openshift.api.model.User;

@Service
public class UsersService {
	
	@Autowired
	private OpenShiftUtils openShiftUtils;
	
	public User getUser(String userName) {
		try {
			return openShiftUtils.getSystemClient().users().withName(userName).get();
		} catch (HttpClientErrorException e) {
			if(e.getMessage().contains("404")) {
				return null;
			} else {
				throw e;
			}
		}
	}
}
