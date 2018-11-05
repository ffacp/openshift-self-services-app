package org.samir.openshift.selfservices.map;

import java.util.ArrayList;
import java.util.List;

import org.samir.openshift.selfservices.dto.LimitRangeDto;
import org.springframework.stereotype.Component;

import io.fabric8.kubernetes.api.model.LimitRange;
import io.fabric8.kubernetes.api.model.LimitRangeBuilder;
import io.fabric8.kubernetes.api.model.LimitRangeItemBuilder;
import io.fabric8.kubernetes.api.model.Quantity;

@Component
public class LimitRangeMapper {

	public LimitRangeDto map(LimitRange source) {
		LimitRangeDto destination = new LimitRangeDto();
		
		destination.setName(source.getMetadata().getName());
		destination.setNamespace(source.getMetadata().getNamespace());
		destination.setType(source.getSpec().getLimits().get(0).getType());

		destination.setDefaultCpuRequest(source.getSpec().getLimits().get(0).getDefaultRequest().get("cpu").getAmount());
		destination.setDefaultMemoryRequest(source.getSpec().getLimits().get(0).getDefaultRequest().get("memory").getAmount());
		destination.setDefaultCpu(source.getSpec().getLimits().get(0).getDefault().get("cpu").getAmount());
		destination.setDefaultMemory(source.getSpec().getLimits().get(0).getDefault().get("memory").getAmount());
		
		return destination;
	}

	public LimitRange map(LimitRangeDto source) {
		return new LimitRangeBuilder()
			.withNewMetadata().withName(source.getName()).withNamespace(source.getNamespace()).endMetadata()
			.withNewSpec().addToLimits(
				new LimitRangeItemBuilder()
		  			.withType(source.getType())
		  			.addToDefaultRequest("cpu", new Quantity(source.getDefaultCpuRequest()))
		  			.addToDefaultRequest("memory", new Quantity(source.getDefaultMemoryRequest()))
		  			.addToDefault("cpu", new Quantity(source.getDefaultCpu()))
		  			.addToDefault("memory", new Quantity(source.getDefaultMemory()))
	  			.build()
	    	)
	      .endSpec()
	      .build();
	}
	
	public List<LimitRangeDto> map(List<LimitRange> sourceList) {
		List<LimitRangeDto> destinationList = new ArrayList<>();
		
		if (sourceList != null && sourceList.size() > 0) {
			for (LimitRange source : sourceList) {
				destinationList.add(map(source));
			}	
		}
		return destinationList;
	}
}
