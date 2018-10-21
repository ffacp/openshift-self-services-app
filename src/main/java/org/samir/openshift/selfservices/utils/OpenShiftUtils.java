package org.samir.openshift.selfservices.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

@Component
public class OpenShiftUtils {

	@Value("${ocp.master.url}")
	private String masterURL;

	@Value("${ocp.certs.trust}")
	private Boolean trustCerts;
	
	@Value("${ocp.system.oauth.token}")
	private String oauthToken;

	private OpenShiftClient systemClient;
	
	public OpenShiftClient getSystemClient() {
		if(systemClient == null) {
			Config config = new ConfigBuilder()
					.withMasterUrl(masterURL)
					.withOauthToken(oauthToken)
					.withTrustCerts(trustCerts).build();

			systemClient = new DefaultOpenShiftClient(config);

			systemClient.projects().list();
		}
		
		return systemClient;
	}
	
	public void createUserClient(String username, String password) {
		Config config = new ConfigBuilder()
				.withMasterUrl(masterURL)
				.withUsername(username)
				.withPassword(password)
				.withTrustCerts(trustCerts).build();

		OpenShiftClient osClient = new DefaultOpenShiftClient(config);

		osClient.projects().list();

		WebUtils.setSessionAttribute("osClient", osClient);
	}

	public OpenShiftClient getUserClient() {
		return (OpenShiftClient) WebUtils.getSessionAttribute("osClient");
	}
}
