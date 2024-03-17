package com.example.jsonview.controller;

import com.example.jsonview.model.*;
import com.example.jsonview.service.UserServiceImpl;
import com.example.jsonview.util.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserServiceImpl userService;

    private final User user = new User();
    private final UserOrder order = new UserOrder();
    private final Product product = new Product();

    @BeforeEach
    public void setUp() {
        user.setId(1L);
        user.setName("John");
        user.setSurname("Doe");
        user.setEmail("john@example.com");

        product.setId(1L);
        product.setName("Apple");
        product.setPrice(10L);

        order.setId(1L);
        order.setAmount(10L);
        order.setStatus(Status.CREATED);
        order.setProducts(new ArrayList<>(List.of(product)));
        user.setOrders(new ArrayList<>(List.of(order)));
    }

    @Test
    public void getUserByIdTest() throws Exception {

        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.orders[0]").exists());

        verify(userService, times(1)).getUserById(anyLong());
    }

    @Test
    public void getAllUsersTest() throws Exception {
        when(userService.getAllUsers()).thenReturn(new ArrayList<>(List.of(user)));

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void createUserTest() throws Exception {
        when(userService.createUser(user)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .content(getMapper().writeValueAsString(user))
                        .contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.orders[0]").exists());

        verify(userService, times(1)).createUser(user);
    }

    @Test
    public void updateUserTest() throws Exception {
        when(userService.updateUser(1L, user)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.put("/user/1")
                        .content(getMapper().writeValueAsString(user))
                        .contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.orders[0]").exists());

        verify(userService, times(1)).updateUser(1L, user);
    }

    @Test
    public void removeUserTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/1"))
                .andExpect(status().isOk());

        verify(userService, times(1)).removeUser(anyLong());
    }

    @Test
    public void whenUseDetailsView_thenAllSerialized() throws JsonProcessingException {
        String result = getMapper().writerWithView(Views.Details.class).writeValueAsString(user);

        assertThat(result, containsString("John"));
        assertThat(result, containsString("Doe"));
        assertThat(result, containsString("john@example.com"));
        assertThat(result, containsString("orders"));
    }

    @Test
    public void whenUseSummaryView_thenOnlySummarySerialized() throws JsonProcessingException {
        String result = getMapper().writerWithView(Views.Summary.class).writeValueAsString(user);

        assertThat(result, containsString("John"));
        assertThat(result, containsString("Doe"));
        assertThat(result, containsString("john@example.com"));
        assertThat(result, not(containsString("orders")));
    }

    private ObjectMapper getMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        return objectMapper;
    }

}
