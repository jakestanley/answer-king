package answer.king.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import answer.king.ItemTest;
import answer.king.controller.OrderControllerTest.WebConfig;
import answer.king.model.Item;
import answer.king.model.Order;
import answer.king.model.Receipt;
import answer.king.repo.ItemRepository;
import answer.king.repo.OrderRepository;
import answer.king.service.OrderService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes=WebConfig.class)
public class OrderControllerTest extends ControllerTest {
	
	@Configuration
	@EnableWebMvc
	static class WebConfig extends WebMvcConfigurerAdapter {

		@Bean
		public ItemRepository itemRepository() {
			return Mockito.mock(ItemRepository.class);
		}
		
		@Bean
		public OrderRepository orderRepository() {
			return Mockito.mock(OrderRepository.class);
		}

		@Bean
		public OrderService orderService() {
			return Mockito.mock(OrderService.class);
		}

		@Bean
		public OrderController controller() {
			return new OrderController();
		}
	}

	@Autowired
	private OrderService orderService;

	@Test
	public void testCreateOrder() throws Exception {

		long orderId = 1000001L;
		Order order = new Order();
		order.setId(orderId);

		// set up service mocking
		Mockito.when(orderService.save(any()))
			.thenReturn(order);

		MockHttpServletRequestBuilder postAction;
		postAction = post("/order/").accept(expectedMediaType);

		MvcResult result = 
			this.mockMvc.perform(postAction)
				.andExpect(status().isOk())
				.andExpect(this.expectedMediaTypeMatcher)
				.andReturn();

		// validate the response
		MockHttpServletResponse response = result.getResponse();
		String content = response.getContentAsString(); 
		JSONObject jsonResponse = new JSONObject(content);

		try {
			assertEquals(jsonResponse.getLong("id"), orderId);
			assertFalse(jsonResponse.getBoolean("paid"));

		} catch (JSONException e) {
			fail("Exception thrown when getting a value from the "
					+ "response" + e);
		}
		
		// check items is empty or null for this just initialised order
		try {
			JSONArray items = jsonResponse.getJSONArray("items");
			assert(items.length() < 1);
		} catch(JSONException e) {
			// items is null. this is expected
		}
	}
	
	@Test
	public void testAddItem() throws Exception {

		long orderId 	= 1000002L;
		long itemId 	= 1000004L;

		// mock an existing item
		Item item = ItemTest.createGoodItem(itemId);

		// mock an existing order
		Order order = new Order();
		order.setId(orderId);
		item.setOrder(order);
		order.setItems(Arrays.asList(item));

		// ok test prep over, time to run the test
		MockHttpServletRequestBuilder putRequest;
		putRequest = put("/order/" + orderId + "/addItem/" + itemId);

		ResultActions actions = this.mockMvc.perform(putRequest);

		// check http response
		actions.andExpect(status().isOk()).andReturn();
	}
	
	@Test
	public void testPay() throws Exception {

		long orderId 	= 1000003L;
		long itemId 	= 1000005L;
		BigDecimal payment 	= BigDecimal.valueOf(5.00);
		BigDecimal price 	= BigDecimal.valueOf(2.99);
		BigDecimal change 	= BigDecimal.valueOf(2.01);

		// mock item
		Item item = ItemTest.createGoodItem(itemId);
		item.setPrice(price);

		// mock an existing order with items
		Order order = new Order();
		order.setId(orderId);
		item.setOrder(order);
		order.setItems(Arrays.asList(item));

		// mock up a valid receipt
		Receipt receipt = new Receipt();
		receipt.setPayment(payment);
		receipt.setOrder(order);

		// mock service behaviour
		Mockito.when(orderService.pay(any(), any()))
			.thenReturn(receipt);

		// do request
		MockHttpServletRequestBuilder putRequest;
		putRequest = put("/order/" + orderId + "/pay")
				.accept(expectedMediaType)
				.contentType(expectedMediaType)
				.content(payment.toString());

		ResultActions actions = this.mockMvc.perform(putRequest);

		MvcResult result = actions
				.andExpect(status().isOk())
				.andExpect(this.expectedMediaTypeMatcher)
				.andReturn();
		
		// validate the response
		String content = result.getResponse().getContentAsString();
		JSONObject jsonResponse = new JSONObject(content);
		
		try {
			assertEquals(jsonResponse.getBigDecimal("payment"), payment);
			assertEquals(jsonResponse.getBigDecimal("change"), change);
			
			JSONObject rspOrder = jsonResponse.getJSONObject("order");
			assertEquals(rspOrder.getLong("id"), orderId);
			// TODO validate more order variables
		} catch (JSONException e) {
			fail("Exception thrown when getting a value from the "
					+ "response" + e);
		}
		
		return;
	}

}
