package answer.king.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import answer.king.ItemTest;
import answer.king.OrderTest;
import answer.king.model.Item;
import answer.king.model.Order;
import answer.king.model.Receipt;
import answer.king.repo.ItemRepository;
import answer.king.repo.OrderRepository;
import answer.king.service.OrderServiceTest.WebConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes=WebConfig.class)
public class OrderServiceTest {

    @Configuration
    @EnableWebMvc
    static class WebConfig extends WebMvcConfigurerAdapter {

        @Bean
        public OrderRepository orderRepository() {
            return Mockito.mock(OrderRepository.class);
        }

        @Bean
        public ItemRepository itemRepository() {
            return Mockito.mock(ItemRepository.class);
        }

        @Bean
        public OrderService service() {
            return new OrderService();
        }

    }

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderService service;

	@Test
	public void testSave() {
		
		// basic for now
		Order order = OrderTest.createGoodOrder(null);
		Order saved = OrderTest.createGoodOrder(200003L);
		
		Mockito.when(orderRepository.save(order)).thenReturn(saved);
		assertNotNull(service.save(order));
	}

    @Test
    public void testAddItem() {

        long orderId = 1000006L;
        long itemId = 2000006L;

        // create mock entities
        Order order = OrderTest.createGoodOrder(orderId);
        order.setItems(new ArrayList<>());

        Item item = ItemTest.createGoodItem(itemId);

        // mock repository lookups
        Mockito.when(orderRepository.findOne(orderId))
            .thenReturn(order);

        Mockito.when(itemRepository.findOne(itemId))
            .thenReturn(item);

        // run test
        service.addItem(orderId, itemId);
        
        // the item should have been added
        assertNotNull(order.getItems());
        assertEquals(order.getItems().get(0), item);
    }

    @Test
    public void testPay() {

        long orderId    = 1000007L;
        long itemId     = 1000007L;
        BigDecimal payment  = BigDecimal.valueOf(5.00);
        BigDecimal price    = BigDecimal.valueOf(2.99);
        BigDecimal change   = BigDecimal.valueOf(2.01);

        // mock item and order
        Item item = ItemTest.createGoodItem(itemId);
        item.setPrice(price);

        Order order = new Order();
        order.setId(orderId);
        item.setOrder(order);
        order.setItems(Arrays.asList(item));

        // mock service/repository behaviours
        Mockito.when(orderRepository.findOne(orderId)).thenReturn(order);
        
        // do test
        Receipt receipt; 
        receipt = service.pay(orderId, payment);

        // validate the receipt
        assertEquals((long) receipt.getOrder().getId(), orderId);
        assertEquals(receipt.getPayment(), payment);
        assertEquals(receipt.getChange(), change);
    }

}
