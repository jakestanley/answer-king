package answer.king.controller;

import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public abstract class ControllerTest {

	protected MockMvc mockMvc;
	protected MediaType expectedMediaType;
	protected ResultMatcher expectedMediaTypeMatcher;
	
	@Autowired
	protected WebApplicationContext webAppContext;
	
	@Before
	public void setup() {
		
		MockitoAnnotations.initMocks(this);
		
		this.mockMvc = 
				MockMvcBuilders.webAppContextSetup(this.webAppContext).build();

		this.expectedMediaType = 
				MediaType.parseMediaType("application/json;charset=UTF-8");
		
		this.expectedMediaTypeMatcher = 
				MockMvcResultMatchers.content().contentType(expectedMediaType);
	}
	
}
