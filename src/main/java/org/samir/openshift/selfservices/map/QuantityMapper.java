package org.samir.openshift.selfservices.map;

import org.samir.openshift.selfservices.dto.QuantityDto;
import org.springframework.stereotype.Component;

import io.fabric8.kubernetes.api.model.Quantity;

@Component
public class QuantityMapper {

	public QuantityDto map(Quantity source) {
		if (source == null) {
			return null;
		} else {
			int amount = Integer.parseInt(source.getAmount().replaceAll("[^\\d]", ""));
			String unit = source.getAmount().replaceAll("[\\d]", "");
			QuantityDto destination = new QuantityDto(amount, unit);
			
			return destination;
		}
	}

	public Quantity map(QuantityDto source) {
		if (source == null) {
			return null;
		} else {
			String value = String.valueOf(source.getAmount()) + source.getUnit();
			Quantity destination = new Quantity(value);			
			return destination;
		}
	}
}
