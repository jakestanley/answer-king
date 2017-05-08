package answer.king.service;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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

import answer.king.NotFoundException;
import answer.king.repo.ItemRepository;
import answer.king.repo.OrderRepository;
import answer.king.repo.ReceiptRepository;
import answer.king.service.ReceiptServiceTest.WebConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes=WebConfig.class)
public class ReceiptServiceTest {

	@Configuration
    @EnableWebMvc
    static class WebConfig extends WebMvcConfigurerAdapter {

        @Bean
        public ReceiptRepository receiptRepository() {
            return Mockito.mock(ReceiptRepository.class);
        }

        @Bean
        public OrderRepository orderRepository() {
            return Mockito.mock(OrderRepository.class);
        }

        @Bean
        public ItemRepository itemRepository() {
            return Mockito.mock(ItemRepository.class);
        }

        @Bean
        public ReceiptService receiptService() {
            return new ReceiptService();
        }

    }
	
    @Rule
    public final ExpectedException expectation = ExpectedException.none();
	
	@Autowired
	private ReceiptRepository receiptRepository;
	
	@Autowired
	private ReceiptService receiptService;
	
	@Test
	public void testGet() throws NotFoundException {
		
		long existingReceiptId 	= 300001L;
		long missingReceiptId 	= 300666L;
		
		// mock does or does not exist checks
		Mockito.when(receiptRepository.exists(existingReceiptId))
			.thenReturn(true);
		
		Mockito.when(receiptRepository.exists(missingReceiptId))
			.thenReturn(false);
		
		// mock returning a valid 
		Mockito.when(receiptRepository.findOne(existingReceiptId))
			.thenReturn(any());
			
		// test
		try {
			receiptService.get(existingReceiptId);
		} catch(NotFoundException e) {
			fail("Called ReceiptService::get with an existing ID, but a "
					+ "NotFoundException was thrown");
		}
		
		expectation.expect(NotFoundException.class);
		receiptService.get(missingReceiptId);
	}

}
