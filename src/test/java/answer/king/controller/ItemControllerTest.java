package answer.king.controller;

import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import answer.king.ItemTest;
import answer.king.controller.ItemControllerTest.WebConfig;
import answer.king.model.Item;
import answer.king.repo.ItemRepository;
import answer.king.service.ItemService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes=WebConfig.class)
public class ItemControllerTest {
	
	@Configuration
	@EnableWebMvc
	static class WebConfig extends WebMvcConfigurerAdapter {
		
		@Bean
		public ItemRepository itemRepository() {
			return Mockito.mock(ItemRepository.class);
		}
		
		@Bean
		public ItemService itemService() {
			return Mockito.mock(ItemService.class);
		}
		
		@Bean
		public ItemController controller() {
			return new ItemController();
		}
	}
	
	private MockMvc mockMvc;
	private MediaType expectedMediaType;
	
	@Rule
	public final ExpectedException expectation = ExpectedException.none();
	
	@Autowired
	private WebApplicationContext webAppContext;

	@Autowired
	private ItemService itemService;

	@Autowired
	private ItemController controller;
	
	@Before
	public void setup() {
		
		MockitoAnnotations.initMocks(this);
		
		this.mockMvc = 
				MockMvcBuilders.webAppContextSetup(this.webAppContext).build();

		this.expectedMediaType = 
				MediaType.parseMediaType("application/json;charset=UTF-8");
	}
	
	@Test
	public void testCreateGoodItem() throws Exception {
		
		Item item = ItemTest.createGoodItem();
		
		// mock a valid return value from the service
		Mockito.when(itemService.save(any()))
			.thenReturn(item);
		
		JSONObject json = ItemTest.itemToJson(item);

		MvcResult result = 
				this.mockMvc.perform(
						post("/item/")
						.accept(expectedMediaType)
						.contentType(expectedMediaType)
						.content(json.toString()))
						.andExpect(status().isOk())
						.andExpect(MockMvcResultMatchers.content().contentType(expectedMediaType))
						.andReturn();
						
		String responseContent = result.getResponse().getContentAsString(); // TODO assert JSON response is valid
		return;

	}
}
