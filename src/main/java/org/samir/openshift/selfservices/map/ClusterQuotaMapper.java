package org.samir.openshift.selfservices.map;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.samir.openshift.selfservices.dto.ClusterResourceQuotaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.openshift.api.model.ClusterResourceQuota;
import io.fabric8.openshift.api.model.ClusterResourceQuotaSelector;
import io.fabric8.openshift.api.model.ClusterResourceQuotaSpec;
import io.fabric8.openshift.api.model.HardResourceQuota;

@Component
public class ClusterQuotaMapper {
	
	private QuantityMapper quantityMapper;

	private SimpleDateFormat format;

	@Autowired
	public ClusterQuotaMapper(QuantityMapper quantityMapper) {
		this.quantityMapper = quantityMapper;

		format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	public ClusterResourceQuotaDto map(ClusterResourceQuota source) {
		if (source == null) {
			return null;
		} else {
			ClusterResourceQuotaDto destination = new ClusterResourceQuotaDto();
			String expireOnDate = null;
			
			destination.setName(source.getMetadata().getName());
			destination.setUid(source.getMetadata().getUid());
			if(source.getMetadata().getAnnotations() != null) {
				destination.setOwner(source.getMetadata().getAnnotations().get("openshift.io/quota-owner"));
				destination.setOwnerEmail(source.getMetadata().getAnnotations().get("openshift.io/quota-owner-email"));
				destination.setCreatedBy(source.getMetadata().getAnnotations().get("openshift.io/created-by"));
				expireOnDate = source.getMetadata().getAnnotations().get("openshift.io/expiry-date");
			}			
			
			try {
				destination.setCreatedOn(format.parse(source.getMetadata().getCreationTimestamp()));				
				if(!StringUtils.isEmpty(expireOnDate)) {
					destination.setExpireOn(format.parse(expireOnDate));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			destination.setCpuRequest(quantityMapper.map(getQuotaAmount(source, "requests.cpu")));
			destination.setCpuLimit(quantityMapper.map(getQuotaAmount(source, "limits.cpu")));
			destination.setMemoryRequest(quantityMapper.map(getQuotaAmount(source, "requests.memory")));
			destination.setMemoryLimit(quantityMapper.map(getQuotaAmount(source, "limits.memory")));
			destination.setGlusterStorage(quantityMapper.map(getQuotaAmount(source, "gluster-dyn.storageclass.storage.k8s.io/requests.storage")));
			destination.setBlockStorage(quantityMapper.map(getQuotaAmount(source, "blockstorage-class.storageclass.storage.k8s.io/requests.storage")));
		
			return destination;
		}
	}

	public ClusterResourceQuota map(ClusterResourceQuotaDto source) {
		if (source == null) {
			return null;
		} else {
			ClusterResourceQuota destination = new ClusterResourceQuota();		
			
			ObjectMeta metadata = new ObjectMetaBuilder().withName(source.getName())
					.addToAnnotations("openshift.io/quota-id", source.getName())
					.addToAnnotations("openshift.io/quota-owner", source.getOwner())
					.addToAnnotations("openshift.io/quota-owner-email", source.getOwnerEmail())
					.addToAnnotations("openshift.io/created-by", source.getCreatedBy())
					.addToLabels("quota-id", source.getName())
					.addToLabels("quota-owner", source.getOwner())
					.build();
					
			Map<String, Quantity> hard = new HashMap<>();
			hard.put("requests.cpu", quantityMapper.map(source.getCpuRequest()));
			hard.put("requests.memory", quantityMapper.map(source.getMemoryRequest()));
			hard.put("limits.cpu", quantityMapper.map(source.getCpuLimit()));
			hard.put("limits.memory", quantityMapper.map(source.getMemoryLimit()));
			hard.put("gluster-dyn.storageclass.storage.k8s.io/requests.storage", quantityMapper.map(source.getGlusterStorage()));
			hard.put("blockstorage-class.storageclass.storage.k8s.io/requests.storage", quantityMapper.map(source.getBlockStorage()));
			
			HardResourceQuota hardQuota = new HardResourceQuota();
			hardQuota.setHard(hard);
			hardQuota.setScopes(null);
			
			Map<String, String> annotations = new HashMap<>();
			annotations.put("openshift.io/quota-id", source.getName());
			
			ClusterResourceQuotaSelector selector = new ClusterResourceQuotaSelector();
			selector.setAnnotations(annotations);
			selector.setLabels(null);
			
			ClusterResourceQuotaSpec spec = new ClusterResourceQuotaSpec();
			spec.setQuota(hardQuota);
			spec.setSelector(selector);
			
			
			destination.setMetadata(metadata);
			destination.setSpec(spec);
			
			return destination;
		}
	}
	
	public List<ClusterResourceQuotaDto> map(List<ClusterResourceQuota> sourceList) {
		List<ClusterResourceQuotaDto> destinationList = new ArrayList<>();
		
		if (sourceList != null && sourceList.size() > 0) {
			for (ClusterResourceQuota source : sourceList) {
				destinationList.add(map(source));
			}	
		}
		return destinationList;
	}
	
	private Quantity getQuotaAmount(ClusterResourceQuota ocQuota, String name) {
		return ocQuota.getSpec().getQuota().getHard().get(name);
	}
	
	public void map(ClusterResourceQuotaDto source, ClusterResourceQuota destination) {
		if (source != null && destination != null) {
			if(destination.getMetadata().getAnnotations() != null) {
				destination.getMetadata().getAnnotations().put("openshift.io/quota-owner", source.getOwner());
				destination.getMetadata().getAnnotations().put("openshift.io/quota-owner-email", source.getOwnerEmail());
			}
			
			if(destination.getMetadata().getLabels() != null) {
				destination.getMetadata().getLabels().put("quota-owner", source.getOwner());
			}
			
			destination.getSpec().getQuota().getHard().put("requests.cpu", quantityMapper.map(source.getCpuRequest()));
			destination.getSpec().getQuota().getHard().put("requests.memory", quantityMapper.map(source.getMemoryRequest()));
			destination.getSpec().getQuota().getHard().put("limits.cpu", quantityMapper.map(source.getCpuLimit()));
			destination.getSpec().getQuota().getHard().put("limits.memory", quantityMapper.map(source.getMemoryLimit()));
			destination.getSpec().getQuota().getHard().put("gluster-dyn.storageclass.storage.k8s.io/requests.storage", quantityMapper.map(source.getGlusterStorage()));
			destination.getSpec().getQuota().getHard().put("blockstorage-class.storageclass.storage.k8s.io/requests.storage", quantityMapper.map(source.getBlockStorage()));
		}
	}
}
