package study.springjpa.repository.order.query;

import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

	private final EntityManager em;

	public List<OrderQueryDto> findOrderDtos() {
		return em.createQuery(
				"select new study.springjpa.repository.order.query.OrderQueryDto(o.id, m.name, d.address)" +
						" from Order o" +
						" join o.member m" +
						" join o.delivery d", OrderQueryDto.class)
				.getResultList();
	}
}
