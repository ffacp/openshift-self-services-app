package org.samir.openshift.selfservices.auth;

import java.util.ArrayList;

import org.samir.openshift.selfservices.utils.OpenShiftUtils;
import org.samir.openshift.selfservices.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.fabric8.kubernetes.client.KubernetesClientException;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private OpenShiftUtils openShiftUtils;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String username = authentication.getName();
		String password = authentication.getCredentials().toString();
		try {
			openShiftUtils.createUserClient(username, password);
			WebUtils.setSessionAttribute("username", username);
		} catch (KubernetesClientException e) {
			throw new BadCredentialsException(e.getMessage(), e);
		}

		return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<GrantedAuthority>());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}