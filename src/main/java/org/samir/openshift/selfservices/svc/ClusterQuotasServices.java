package org.samir.openshift.selfservices.svc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.samir.openshift.selfservices.dto.ClusterResourceQuotaDto;
import org.samir.openshift.selfservices.rest.ClusterQuotaClient;
import org.samir.openshift.selfservices.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.openshift.api.model.ClusterResourceQuota;
import io.fabric8.openshift.api.model.ClusterResourceQuotaSelector;
import io.fabric8.openshift.api.model.ClusterResourceQuotaSpec;
import io.fabric8.openshift.api.model.HardResourceQuota;

@Service
public class ClusterQuotasServices {
	
	@Autowired
	private ClusterQuotaClient client;
	
	private SimpleDateFormat format;
	
	public ClusterQuotasServices() {
		format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
	}
	
	public ClusterResourceQuotaDto getClusterResourceQuota(String clusterQuotaName) {
		try {
			return convert(client.getOne(clusterQuotaName));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<ClusterResourceQuotaDto> getClusterResourceQuotas() {
		try {
			List<ClusterResourceQuotaDto> quotas = convert(client.getAll().getItems());
			
			return filterResults(quotas);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private List<ClusterResourceQuotaDto> filterResults(List<ClusterResourceQuotaDto> quotas) {
		String username = (String) WebUtils.getSessionAttribute("username");
		Set<SimpleGrantedAuthority> authorities = (Set<SimpleGrantedAuthority>) WebUtils.getSessionAttribute("authorities");
		
		List<ClusterResourceQuotaDto> myQuotas = new ArrayList<>();
		
		if (!authorities.contains(new SimpleGrantedAuthority("cluster-admin"))) {
			for (ClusterResourceQuotaDto quota : quotas) {
				if (quota.getOwner().equals(username)) {
					myQuotas.add(quota);
				}
			}
			return myQuotas;
		} else {
			return quotas;
		}
	}
	
	public ClusterResourceQuotaDto createQuota(ClusterResourceQuotaDto quota) {
		//TODO
		
		ObjectMeta metadata = new ObjectMetaBuilder().withName(quota.getName())
		.addToAnnotations("openshift.io/quota-id", quota.getName())
		.addToAnnotations("openshift.io/quota-owner", quota.getOwner())
		.addToAnnotations("openshift.io/quota-owner-email", quota.getOwnerEmail())
		.addToLabels("quota-id", quota.getName())
		.addToLabels("quota-owner", quota.getOwner())
		.build();
		
		Map<String, Quantity> hard = new HashMap<>();
		hard.put("requests.cpu", new Quantity(quota.getCpuRequest()));
		hard.put("requests.memory", new Quantity(quota.getMemoryRequest()));
		hard.put("limits.cpu", new Quantity(quota.getCpuLimit()));
		hard.put("limits.memory", new Quantity(quota.getMemoryLimit()));
		hard.put("gluster-dyn.storageclass.storage.k8s.io/requests.storage", new Quantity(quota.getGlusterStorage()));
		hard.put("blockstorage-class.storageclass.storage.k8s.io/requests.storage", new Quantity(quota.getBlockStorage()));
		
		HardResourceQuota hardQuota = new HardResourceQuota();
		hardQuota.setHard(hard);
		hardQuota.setScopes(null);
		
		Map<String, String> annotations = new HashMap<>();
		annotations.put("openshift.io/quota-id", quota.getName());
		
		ClusterResourceQuotaSelector selector = new ClusterResourceQuotaSelector();
		selector.setAnnotations(annotations);
		selector.setLabels(null);
		
		ClusterResourceQuotaSpec spec = new ClusterResourceQuotaSpec();
		spec.setQuota(hardQuota);
		spec.setSelector(selector);
		
		ClusterResourceQuota ocQuota = new ClusterResourceQuota();
		ocQuota.setMetadata(metadata);
		ocQuota.setSpec(spec);
		
		ClusterResourceQuota created = client.create(ocQuota);
		return convert(created);
	}
	
	private ClusterResourceQuotaDto convert(ClusterResourceQuota ocQuota) {
		if (ocQuota == null) {
			return null;
		} else {
			ClusterResourceQuotaDto quota = new ClusterResourceQuotaDto();
			quota.setName(ocQuota.getMetadata().getName());
			quota.setOwner(ocQuota.getMetadata().getAnnotations().get("openshift.io/quota-owner"));
			quota.setOwnerEmail(ocQuota.getMetadata().getAnnotations().get("openshift.io/quota-owner-email"));
			/*quota.setStatus(ocQuota.getStatus().getPhase());
			quota.setUrl(openShiftUtils.getMasterURL() + "/console/quota/" + project.getName());
			*/
			try {
				quota.setCreatedOn(format.parse(ocQuota.getMetadata().getCreationTimestamp()));
				String expireOnDate = ocQuota.getMetadata().getAnnotations().get("openshift.io/expiry-date");
				if(!StringUtils.isEmpty(expireOnDate)) {
					quota.setExpireOn(format.parse(expireOnDate));
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			quota.setCpuRequest(ocQuota.getSpec().getQuota().getHard().get("requests.cpu").getAmount());
			quota.setCpuLimit(ocQuota.getSpec().getQuota().getHard().get("limits.cpu").getAmount());
			quota.setMemoryRequest(ocQuota.getSpec().getQuota().getHard().get("requests.memory").getAmount());
			quota.setMemoryLimit(ocQuota.getSpec().getQuota().getHard().get("limits.memory").getAmount());
			quota.setGlusterStorage(ocQuota.getSpec().getQuota().getHard().get("gluster-dyn.storageclass.storage.k8s.io/requests.storage").getAmount());
			quota.setBlockStorage(ocQuota.getSpec().getQuota().getHard().get("blockstorage-class.storageclass.storage.k8s.io/requests.storage").getAmount());
			
			return quota;
		}
	}
	
	private List<ClusterResourceQuotaDto> convert(List<ClusterResourceQuota> ocQuotas) {
		List<ClusterResourceQuotaDto> projects = new ArrayList<>();
		if(ocQuotas != null && ocQuotas.size() > 0) {
			for(ClusterResourceQuota ocQuota : ocQuotas) {
				projects.add(convert(ocQuota));
			}
		}
		return projects;
	}
}
