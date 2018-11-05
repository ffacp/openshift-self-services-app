package org.samir.openshift.selfservices.rest;

import java.util.List;

import org.samir.openshift.selfservices.dto.ClusterResourceQuotaDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import io.fabric8.openshift.api.model.ClusterResourceQuota;
import io.fabric8.openshift.api.model.ClusterResourceQuotaList;

@Component
public class ClusterQuotaClient {

	// @Autowired
	// RestTemplate restTemplate;

	@Value("${ocp.master.url}")
	private String masterURL;

	@Value("${ocp.system.oauth.token}")
	private String oauthToken;

	private String getUri() {
		return masterURL + "/oapi/v1/clusterresourcequotas";
	}

	public ClusterResourceQuota getOne(String clusterQuotaName) {
		
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + oauthToken);

		HttpEntity<?> entity = new HttpEntity<>(headers);

		ResponseEntity<ClusterResourceQuota> response = restTemplate.exchange(getUri() + "/" + clusterQuotaName,
				HttpMethod.GET, entity, ClusterResourceQuota.class);

		return response.getBody();
		
	}
	
	public ClusterResourceQuotaList getAll() {
		
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + oauthToken);

		HttpEntity<?> entity = new HttpEntity<>(headers);

		ResponseEntity<ClusterResourceQuotaList> response = restTemplate.exchange(getUri(),
				HttpMethod.GET, entity, ClusterResourceQuotaList.class);

		return response.getBody();
		
	}
	
	public ClusterResourceQuota create(ClusterResourceQuota quota) {
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + oauthToken);

		HttpEntity<ClusterResourceQuota> entity = new HttpEntity<>(quota, headers);

		ResponseEntity<ClusterResourceQuota> response = restTemplate.exchange(getUri(),
				HttpMethod.POST, entity, ClusterResourceQuota.class);

		return response.getBody();
	}

}
