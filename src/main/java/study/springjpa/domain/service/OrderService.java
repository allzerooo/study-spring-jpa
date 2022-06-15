package study.springjpa.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.springjpa.domain.Delivery;
import study.springjpa.domain.Member;
import study.springjpa.domain.Order;
import study.springjpa.domain.OrderItem;
import study.springjpa.domain.item.Item;
import study.springjpa.repository.ItemRepository;
import study.springjpa.repository.MemberRepository;
import study.springjpa.repository.OrderRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

	private final OrderRepository orderRepository;
	private final MemberRepository memberRepository;
	private final ItemRepository itemRepository;

	/**
	 * 주문
	 */
	@Transactional
	public Long order(Long memberId, Long itemId, int count) {

		// 엔티티 조회
		final Member member = memberRepository.findOne(memberId);
		final Item item = itemRepository.findOne(itemId);

		// 배송정보 생성
		Delivery delivery = new Delivery();
		delivery.setAddress(member.getAddress());

		// 주문상품 생성
		final OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

		// 주문 생성
		final Order order = Order.createOrder(member, delivery, orderItem);

		// 주문 저장
		orderRepository.save(order);  // OrderItem, Delivery에 Cascade.ALL 설정이 되어 있기 떄문에 Order를 persist 할 때 두 엔티티도 persist 된다

		return order.getId();
	}

	/**
	 * 주문 취소
	 */
	@Transactional
	public void cancelOrder(Long orderId) {
		// 주문 엔티티 조회
		Order order = orderRepository.findOne(orderId);
		// 주문 취소
		order.cancel();
	}
}
