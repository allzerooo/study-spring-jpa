package study.springjpa.repository;

import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.springjpa.domain.Order;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

	private final EntityManager em;

	public void save(Order order) {
		em.persist(order);
	}

	public Order findOne(Long id) {
		return em.find(Order.class, id);
	}

	public List<Order> findAllWithMemberDelivery() {
		return em.createQuery(
				"select o from Order o" +
						" join fetch o.member m" +
						" join fetch o.delivery d", Order.class)
				.getResultList();
	}

}
