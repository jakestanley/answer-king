package answer.king.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import answer.king.ItemValidationException;
import answer.king.NotFoundException;
import answer.king.model.Item;
import answer.king.service.ItemService;

@RestController
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private ItemService itemService;

	@RequestMapping(method = RequestMethod.GET)
	public List<Item> getAll() {
		return itemService.getAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Item> create(@RequestBody Item item) {
		
		try {
			Item saved = itemService.save(item);
			return new ResponseEntity<Item>(saved, HttpStatus.OK);
		} catch(Exception e) { // TODO change to type of exception
			return new ResponseEntity<Item>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/{id}/changePrice", method = RequestMethod.PUT)
	public ResponseEntity<Item> updatePrice(
			@PathVariable("id") Long id, @RequestBody BigDecimal price) {
		
		try {
			Item updated = itemService.updatePrice(id, price);
			return new ResponseEntity<Item>(updated, HttpStatus.OK);
		} catch(NotFoundException e) {
			return new ResponseEntity<Item>(HttpStatus.NOT_FOUND);
		} catch (ItemValidationException e) {
			e.printStackTrace(); // TODO send error to front end
			return new ResponseEntity<Item>(HttpStatus.BAD_REQUEST);			
		}
	}
}
