package study.springjpa.repository;

import lombok.Getter;
import lombok.Setter;
import study.springjpa.domain.OrderStatus;

@Getter @Setter
public class OrderSearch {

	private String userName;  // 회원 이름
	private OrderStatus orderStatus; // 주문 상태

}
