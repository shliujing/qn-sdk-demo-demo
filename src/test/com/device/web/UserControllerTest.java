package com.device.web;

import com.device.MainApplication;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = MainApplication.class)
public class UserControllerTest {

//    @Autowired
//    private WebApplicationContext wac;
//
//    private MockMvc mockMvc;
//
//    @Before
//    public void setup() {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
//    }
//
//    @Test
//    public void testHome() throws Exception {
//        this.mockMvc.perform(get("/")).andExpect(status().isOk())
//                .andExpect(content().string(containsString("<title>Messages")));
//    }

}