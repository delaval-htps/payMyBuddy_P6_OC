package com.paymybuddy.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.paymybuddy.security.oauth2.components.CustomOAuth2SuccessHandler;
import com.paymybuddy.security.oauth2.services.CustomOAuth2UserService;
import com.paymybuddy.security.services.CustomUserDetailsService;


@WebMvcTest(controllers = AuthController.class)
@TestMethodOrder(OrderAnnotation.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private CustomOAuth2UserService customOAuth2UserService;

    @MockBean
    private CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

    @Test
    @Order(1)
    @WithAnonymousUser
    void testShowLoginPage() throws Exception {

        MvcResult result = mockMvc.perform(get("/loginPage")).andExpect(status().isOk()).andDo(print()).andReturn();

        assertThat(result.getModelAndView().getViewName()).isEqualTo("login");
    }

    @Test
    @Order(2)
    @WithMockUser
    void testShowLoginPage_whenAuthenticationOk() throws Exception {

        MvcResult result = mockMvc.perform(get("/loginPage")).andExpect(status().isFound()).andExpect(redirectedUrl("/home")).andDo(print()).andReturn();

        assertThat(result.getModelAndView().getViewName()).isEqualTo("redirect:/home");
    }
}
