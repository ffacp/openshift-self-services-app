package org.samir.openshift.selfservices.svc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.bind.ValidationException;

import org.samir.openshift.selfservices.rest.ClusterQuotaClient;
import org.samir.openshift.selfservices.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

import io.fabric8.openshift.api.model.ClusterResourceQuota;
import io.fabric8.openshift.api.model.ClusterResourceQuotaStatusNamespace;

@Service
public class ClusterQuotasService {
	
	@Autowired
	private ClusterQuotaClient client;
	
	public ClusterResourceQuota getClusterResourceQuota(String clusterQuotaName) {
		try {
			return client.getOne(clusterQuotaName);
		} catch (HttpClientErrorException e) {
			if(e.getMessage().contains("404")) {
				return null;
			} else {
				throw e;
			}
		}
	}
	
	public List<ClusterResourceQuota> getClusterResourceQuotas() {
		return filterResults(client.getAll().getItems());
	}
	
	private List<ClusterResourceQuota> filterResults(List<ClusterResourceQuota> quotas) {
		String username = (String) WebUtils.getSessionAttribute("username");
		Set<SimpleGrantedAuthority> authorities = (Set<SimpleGrantedAuthority>) WebUtils.getSessionAttribute("authorities");
		
		List<ClusterResourceQuota> myQuotas = new ArrayList<>();
		
		if (!authorities.contains(new SimpleGrantedAuthority("cluster-admin"))) {
			for (ClusterResourceQuota quota : quotas) {
				if(quota.getMetadata().getAnnotations() != null
						&& username.equalsIgnoreCase(quota.getMetadata().getAnnotations().get("openshift.io/quota-owner"))) {
					myQuotas.add(quota);
				}
			}
			return myQuotas;
		} else {
			return quotas;
		}
	}
	
	public ClusterResourceQuota createQuota(ClusterResourceQuota quota) {
		return client.create(quota);
	}
	
	public ClusterResourceQuota updateQuota(ClusterResourceQuota quota) {
		return client.update(quota);
	}
	
	public void deleteQuota(String name) throws ValidationException {
		ClusterResourceQuota quota = client.getOne(name);
		List<ClusterResourceQuotaStatusNamespace> namespacesList = quota.getStatus().getNamespaces();
		if(namespacesList != null && namespacesList.size() > 0) {
			List<String> namespaces = new ArrayList<>();
			for (ClusterResourceQuotaStatusNamespace namespace : namespacesList) {
				namespaces.add(namespace.getNamespace());
			}
			throw new ValidationException("Quota " + name + " is used by these projects: " 
											+ StringUtils.collectionToDelimitedString(namespaces, ", ")
											+ "<br>Please delete the projects firstly.");
		} else {
			client.delete(quota);
		}
	}
}
