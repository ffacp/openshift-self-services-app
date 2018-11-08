package org.samir.openshift.selfservices.dto;

import javax.validation.constraints.Digits;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
public class QuantityDto extends BaseDTO {

	private static final long serialVersionUID = 1L;

	@NotBlank
	@Digits(integer = 10, fraction = 0, message = "Must be number")
	private int amount;

	private String unit;

	public QuantityDto() {
		super();
	}

	public QuantityDto(int amount, String unit) {
		this();
		this.amount = amount;
		this.unit = unit;
	}

	@Override
	public String toString() {
		// return "QuantityDto [amount=" + amount + ", unit=" + unit + "]";

		return String.valueOf(amount) + unit;
	}

}
