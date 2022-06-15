package study.springjpa.domain.service;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.springjpa.domain.Address;
import study.springjpa.domain.Member;
import study.springjpa.domain.Order;
import study.springjpa.domain.OrderStatus;
import study.springjpa.domain.exception.NotEnoughStockException;
import study.springjpa.domain.item.Book;
import study.springjpa.domain.item.Item;
import study.springjpa.repository.OrderRepository;

@SpringBootTest
@Transactional
class OrderServiceTest {

	@Autowired EntityManager em;
	@Autowired OrderService orderService;
	@Autowired OrderRepository orderRepository;

	@Test
	void 상품주문() {
	  // given
		Member member = createMember();

		Book book = createBook("JPA", 10000, 10);

		int orderCount = 2;

	  // when
		final Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

		// then
		final Order getOrder = orderRepository.findOne(orderId);

		assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER");
		assertEquals(1, getOrder.getOrderItems().size(), "주문할 상품 종류 수가 정확해야 한다");
		assertEquals(10000 * orderCount, getOrder.getTotalPrice(), "주문 가격은 가격 * 수량이다");
		assertEquals(8, book.getStockQuantity(), "주문 수량만큼 재고가 줄어야 한다");
	}

	@Test
	void 상품주문_재고수량초과() {
	  // given
	  Member member = createMember();
		Item item = createBook("JPA", 10000, 10);

		int orderCount = 11;

	  // when & then
		assertThrows(NotEnoughStockException.class, () -> orderService.order(member.getId(), item.getId(), orderCount));

	}

	@Test
	void 주문최소() {
	  // given
		Member member = createMember();
		Book item = createBook("JPA", 10000, 10);

		int orderCount = 2;

		Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

	  // when
		orderService.cancelOrder(orderId);

	  // then
		Order getOrder = orderRepository.findOne(orderId);

		assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "주문 취소시 상태는 CANCEL 이다");
		assertEquals(10, item.getStockQuantity(), "주문이 취소된 상품은 그만큼 재고가 증가해야 한다");

	}

	private Book createBook(final String name, final int price, final int stockQuantity) {
		Book book = new Book();
		book.setName(name);
		book.setPrice(price);
		book.setStockQuantity(stockQuantity);
		em.persist(book);
		return book;
	}

	private Member createMember() {
		Member member = new Member();
		member.setName("회원1");
		member.setAddress(new Address("서울", "강가", "123-123"));
		em.persist(member);
		return member;
	}
}