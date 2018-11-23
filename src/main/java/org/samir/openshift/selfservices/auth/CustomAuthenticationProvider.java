package org.samir.openshift.selfservices.auth;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.samir.openshift.selfservices.utils.OpenShiftUtils;
import org.samir.openshift.selfservices.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import io.fabric8.kubernetes.api.model.ObjectReference;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.openshift.api.model.ClusterRoleBinding;
import io.fabric8.openshift.api.model.Group;

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
			
			//Object roles = openShiftUtils.getSystemClient().groups().withName("").get().getUsers();
		} catch (KubernetesClientException e) {
			e.printStackTrace();
			throw new BadCredentialsException(e.getMessage(), e);
		}
		
		Set<GrantedAuthority> authorities = getUserPrivileges(username);
		WebUtils.setSessionAttribute("authorities", authorities);
		
		return new UsernamePasswordAuthenticationToken(username, password, authorities);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
	private List<String> getUserGroups(String username) {
		List<String> userGroups = new ArrayList<>();
		List<Group> groups = openShiftUtils.getSystemClient().groups().list().getItems();
		
		if(groups != null && groups.size() > 0) {
			for (Group group : groups) {
				if(group.getUsers().contains(username)) {
					userGroups.add(group.getMetadata().getName());
				}
			}
		}
			
		return userGroups;
	}
	
	private Set<GrantedAuthority> getUserPrivileges(String username) {
		Set<GrantedAuthority> authorities = new HashSet<>();
		
		List<String> userGroups = getUserGroups(username);
		WebUtils.setSessionAttribute("userGroups", userGroups);
		
		List<ClusterRoleBinding> roles = openShiftUtils.getSystemClient().clusterRoleBindings().inAnyNamespace().list().getItems();
		if(roles != null && roles.size() > 0) {
			for (ClusterRoleBinding role : roles) {
				for (ObjectReference subject : role.getSubjects()) {
					if (subject.getKind().equalsIgnoreCase("User") && subject.getName().equalsIgnoreCase(username)) {
						authorities.add(new SimpleGrantedAuthority(role.getRoleRef().getName()));
					} else if (subject.getKind().equalsIgnoreCase("Group") && userGroups != null) {
						for (String group : userGroups) {
							if(subject.getName().equalsIgnoreCase(group)) {
								authorities.add(new SimpleGrantedAuthority(role.getRoleRef().getName()));
							}
						}
					}
				}
			}
		}
		
		return authorities;
	}
}