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

import answer.king.InsufficientFundsException;
import answer.king.model.Order;
import answer.king.model.Receipt;
import answer.king.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@RequestMapping(method = RequestMethod.GET)
	public List<Order> getAll() {
		return orderService.getAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	public Order create() {
		return orderService.save(new Order());
	}

	@RequestMapping(value = "/{id}/addItem/{itemId}", method = RequestMethod.PUT)
	public void addItem(@PathVariable("id") Long id, @PathVariable("itemId") Long itemId) {
		orderService.addItem(id, itemId);
	}

	@RequestMapping(value = "/{id}/pay", method = RequestMethod.PUT)
	public ResponseEntity<Receipt> pay(	@PathVariable("id") Long id, 
										@RequestBody BigDecimal payment) {
		try {
			Receipt receipt = orderService.pay(id, payment);
			return new ResponseEntity<Receipt>(receipt, HttpStatus.OK);
		} catch(InsufficientFundsException e) {
			// TODO proper spring exception handling
			return new ResponseEntity<Receipt>(HttpStatus.NOT_ACCEPTABLE);
		}
		
	}
}
