package study.springjpa.domain.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.springjpa.domain.item.Item;
import study.springjpa.repository.ItemRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

	private final ItemRepository itemRepository;

	@Transactional
	public void saveItem(Item item) {
		itemRepository.save(item);
	}

	public Item findOne(Long itemId) {
		return itemRepository.findOne(itemId);
	}

	public List<Item> findItems() {
		return itemRepository.findAll();
	}
}
