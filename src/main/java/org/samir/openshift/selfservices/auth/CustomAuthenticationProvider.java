package org.samir.openshift.selfservices.auth;

import java.util.ArrayList;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.openshift.api.model.Project;
import io.fabric8.openshift.api.model.ProjectList;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String username = authentication.getName();
		String password = authentication.getCredentials().toString();
 
		/*IClient client = new ClientBuilder("https://192.168.99.101:8443")
				.withUserName(username)
				.withPassword(password)
				.build();*/
		Config config = new ConfigBuilder()
				.withMasterUrl("https://192.168.99.101:8443")
				.withUsername(username)
				.withPassword(password)
				.withNoProxy("192.168.99.101")
				.build();
		
		OpenShiftClient osClient = new DefaultOpenShiftClient(config);

		ProjectList myNs = osClient.projects().list();
		
		for (Project ns : myNs.getItems()) {
			System.out.println(ns.getMetadata().getName());
		}
		
		osClient.close();
		return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<GrantedAuthority>());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}