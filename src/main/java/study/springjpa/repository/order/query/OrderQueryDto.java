package study.springjpa.repository.order.query;

import lombok.Data;
import study.springjpa.domain.Address;

@Data
public class OrderQueryDto {

	private Long orderId;
	private String name;
	private Address address;

	public OrderQueryDto(final Long orderId, final String name, final Address address) {
		this.orderId = orderId;
		this.name = name;
		this.address = address;
	}
}
