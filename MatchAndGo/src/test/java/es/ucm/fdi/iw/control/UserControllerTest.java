package es.ucm.fdi.iw.control;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import cucumber.api.java.Before;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,
        classes = UserController.class)
public class UserControllerTest {
    @Autowired private WebApplicationContext wac;
    private MockMvc mockMVC;
    @Before
    public void setUp(){
        this.mockMVC =  MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

@Test
public void loginTest(){

}

}