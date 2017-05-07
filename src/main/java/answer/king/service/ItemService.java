package answer.king.service;

import java.math.BigDecimal;
import java.util.List;

import org.h2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import answer.king.ItemValidationException;
import answer.king.NotFoundException;
import answer.king.model.Item;
import answer.king.repo.ItemRepository;
import lombok.NonNull;

@Service
@Transactional
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;

	public List<Item> getAll() {
		return itemRepository.findAll();
	}

	public Item save(Item item) throws ItemValidationException {
		
		validateItem(item);
		
		return itemRepository.save(item);
	}

	public Item update(Item item) throws NotFoundException, ItemValidationException {
		
		validateItem(item);
		
		Long id = item.getId();
		
		if(!itemRepository.exists(id)) {
			throw new NotFoundException();
		}
		
		return itemRepository.save(item);
	}
	
	private static void validateItem(Item item) throws ItemValidationException {

		// check name
		if(StringUtils.isNullOrEmpty(item.getName())) {
			throw new ItemValidationException("Item name cannot be empty");
		}

		// check price 
		BigDecimal price = item.getPrice();

		if(price == null) {
			throw new ItemValidationException("Price cannot be empty");
		}

		if(price.signum() == -1) {
			throw new ItemValidationException("Price cannot be negative");
		}
	}
}
