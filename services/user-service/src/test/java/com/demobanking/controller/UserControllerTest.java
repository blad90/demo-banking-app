package com.demobanking.controller;

import com.demobanking.dto.UserDTO;
import com.demobanking.service.IUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IUserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test Case: Verifying on controller whether an user can be registered")
    public void testUserControllerCreateUser() throws Exception {
        UserDTO userDTO = new UserDTO("Jeff","Matos","JM","234-9586-90","TEST ADDRESS","TEST ###", "jmm@email.com");

        given(userService.createUser(ArgumentMatchers.any()))
                .willAnswer(InvocationOnMock::getArguments);

        ResultActions response = mockMvc.perform(post("/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)));

        response.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("Test Case: Verifying on controller whether all registered users can be retrieved")
    public void testUserControllerCRetrieveAllUsers() throws Exception {
        UserDTO userDTO1 = new UserDTO("Jeff","Matos","JM","234-9586-90","TEST ADDRESS","TEST ###", "jmm@email.com");
        UserDTO userDTO2 = new UserDTO("Jeff","Matos","JM","234-9586-90","TEST ADDRESS","TEST ###", "jmm@email.com");
        List<UserDTO> testUsers = List.of(userDTO1, userDTO2);
        given(userService.retrieveAllUsers())
                .willReturn(testUsers);

        ResultActions response = mockMvc.perform(get("/users/all")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
