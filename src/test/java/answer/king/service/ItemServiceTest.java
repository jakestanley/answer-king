package answer.king.service;

import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import answer.king.ItemTest;
import answer.king.ItemValidationException;
import answer.king.service.ItemServiceTest.WebConfig;
import answer.king.model.Item;
import answer.king.repo.ItemRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes=WebConfig.class)
public class ItemServiceTest {

    @Configuration
    @EnableWebMvc
    static class WebConfig extends WebMvcConfigurerAdapter {

        @Bean
        public ItemRepository itemRepository() {
            return Mockito.mock(ItemRepository.class);
        }
        
        @Bean
        public ItemService itemService() {
        	return new ItemService();
        }
    }
    
	@Rule
	public final ExpectedException expectation = ExpectedException.none();
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private ItemService itemService;
	
	@Test
	public void testSave() throws ItemValidationException {
		
		// save a good record
		Long id = 1000003L;
		Item goodItem = ItemTest.createGoodItem(null);
		Item goodItemSaved = ItemTest.createGoodItem(id);
		
		Mockito.when(itemRepository.save(goodItem)).thenReturn(goodItemSaved);
		
		itemService.save(goodItem);
	}
	
	@Test
	public void testCreateEmptyName() throws ItemValidationException {

		expectation.expect(ItemValidationException.class);
		expectation.expectMessage("Item name cannot be empty");
		
		Item badItem = new Item();
		badItem.setId(null);
		badItem.setName("");
		badItem.setPrice(BigDecimal.valueOf(2.29));
		
		itemService.save(badItem);
	}
	
	@Test
	public void testCreateNullName() throws ItemValidationException {
		
		Item badItem = new Item();
		badItem.setId(null);
		badItem.setName(null);
		badItem.setPrice(BigDecimal.valueOf(2.29));
		
		expectation.expect(ItemValidationException.class);
		expectation.expectMessage("Item name cannot be empty");
		
		itemService.save(badItem);
	}
	
	@Test
	public void testCreateNegativePrice() throws ItemValidationException {
		
		Item badItem = new Item();
		badItem.setId(null);
		badItem.setName("Chicken Wrap");
		badItem.setPrice(BigDecimal.valueOf(-3.49));
		
		expectation.expect(ItemValidationException.class);
		expectation.expectMessage("Price cannot be negative");
		
		itemService.save(badItem);
	}
	
	@Test
	public void testCreateNullPrice() throws ItemValidationException {
		
		Item badItem = new Item();
		badItem.setId(null);
		badItem.setName("Chickpea Curry");
		badItem.setPrice(null);
		
		expectation.expect(ItemValidationException.class);
		expectation.expectMessage("Price cannot be empty");
		
		itemService.save(badItem);
	}

}
