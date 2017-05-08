package answer.king.controller;

import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
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
public class ItemControllerTest extends ControllerTest {
	
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
	
	@Rule
	public final ExpectedException expectation = ExpectedException.none();
	
	@Autowired
	private ItemService itemService;

	@Autowired
	private ItemController controller;
	
	@Test
	public void testCreateGoodItem() throws Exception {
		
		Item item = ItemTest.createGoodItem(null);

		// copy to a JSON object before adding the mock id
		JSONObject json = ItemTest.itemToJson(item);

		// set the mock ID that will be returned
		item.setId(1000001L);
		
		// mock a valid return value from the service
		Mockito.when(itemService.save(any()))
			.thenReturn(item);

		MvcResult result = 
				this.mockMvc.perform(
						post("/item/")
						.accept(expectedMediaType)
						.contentType(expectedMediaType)
						.content(json.toString()))
						.andExpect(status().isOk())
						.andExpect(this.expectedMediaTypeMatcher)
						.andReturn();
						
		MockHttpServletResponse response = result.getResponse(); // TODO assert JSON response is valid
		String content = response.getContentAsString();
		
		return;
	}

	@Test
	public void testUpdateGoodItem() throws Exception {

		final Long id = 1000002L;
		Item item = ItemTest.createGoodItem(id);
		
		JSONObject json = ItemTest.itemToJson(item);
		
		// mock the service update to return the above item
		Mockito.when(itemService.update(any()))
			.thenReturn(item);
		
		MockHttpServletRequestBuilder putRequest;
			putRequest = put("/item/" + id)
					.accept(expectedMediaType)
					.contentType(expectedMediaType)
					.content(json.toString());

		ResultActions actions = this.mockMvc.perform(putRequest);

		MvcResult result = actions
				.andExpect(status().isOk())
				.andExpect(this.expectedMediaTypeMatcher)
				.andReturn();

		MockHttpServletResponse response = result.getResponse();
		String content = response.getContentAsString();

		return;
	}
}
