package Cntrollers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import DTO.DTO_Response;
import DTO.DTO_User;
import Service.Service_User;
import ServiceImplementation.ServiceImplementation_User;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.User;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {Controller_User.class})
@ExtendWith(SpringExtension.class)
class Controller_UserTest {
    @Autowired
    private Controller_User controller_User;

    @MockBean
    private Service_User service_User;

    @Test
    void testAccountDeletionReverse() {
        Controller_User controller_User = new Controller_User(new ServiceImplementation_User());
        ResponseEntity<?> actualAccountDeletionReverseResult = controller_User.accountDeletionReverse(123L,
                new MockHttpSession());
        assertEquals("<401 UNAUTHORIZED Unauthorized,DTO_Response(statusCode=401, message=user not logged in),[]>",
                actualAccountDeletionReverseResult.toString());
        assertTrue(actualAccountDeletionReverseResult.hasBody());
        assertTrue(actualAccountDeletionReverseResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.UNAUTHORIZED, actualAccountDeletionReverseResult.getStatusCode());
        assertEquals("user not logged in", ((DTO_Response) actualAccountDeletionReverseResult.getBody()).getMessage());
        assertEquals(401, ((DTO_Response) actualAccountDeletionReverseResult.getBody()).getStatusCode().intValue());
    }

    @Test
    void testDeleteUser() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/blog/user/delete/{Id}", 123L);
        MockMvcBuilders.standaloneSetup(this.controller_User)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"statusCode\":401,\"message\":\"user not logged in\"}"));
    }

    @Test
    void testDeleteUser2() throws Exception {
        MockHttpServletRequestBuilder deleteResult = MockMvcRequestBuilders.delete("/api/blog/user/delete/{Id}", 123L);
        deleteResult.secure(true);
        MockMvcBuilders.standaloneSetup(this.controller_User)
                .build()
                .perform(deleteResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"statusCode\":401,\"message\":\"user not logged in\"}"));
    }

    @Test
    void testGetAllUsers() throws Exception {
        when(this.service_User.allUsers()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/blog/user");
        MockMvcBuilders.standaloneSetup(this.controller_User)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testGetAllUsers2() throws Exception {
        when(this.service_User.allUsers()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/blog/user");
        getResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(this.controller_User)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testGetUser() throws Exception {
        User user = new User();
        user.setIsDelete(1);
        user.setPersonDeactivated(1);
        user.setRemoveDate("2020-03-01");
        user.setUserEmail("jane.doe@example.org");
        user.setUserId(123L);
        user.setUserPassword("iloveyou");
        Optional<User> ofResult = Optional.of(user);
        when(this.service_User.getUserById((Long) any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/blog/user/{userId}", 123L);
        MockMvcBuilders.standaloneSetup(this.controller_User)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"userId\":123,\"userEmail\":\"jane.doe@example.org\",\"userPassword\":\"iloveyou\"}"));
    }

    @Test
    void testGetUser2() throws Exception {
        when(this.service_User.getUserById((Long) any())).thenReturn(Optional.empty());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/blog/user/{userId}", 123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.controller_User)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testLoginUser() throws Exception {
        User user = new User();
        user.setIsDelete(1);
        user.setPersonDeactivated(1);
        user.setRemoveDate("2020-03-01");
        user.setUserEmail("jane.doe@example.org");
        user.setUserId(123L);
        user.setUserPassword("iloveyou");

        DTO_User dto_User = new DTO_User();
        dto_User.setData(user);
        dto_User.setMessage("Not all who wander are lost");
        dto_User.setStatus(true);
        when(this.service_User.logInUser((User) any())).thenReturn(dto_User);

        User user1 = new User();
        user1.setIsDelete(1);
        user1.setPersonDeactivated(1);
        user1.setRemoveDate("2020-03-01");
        user1.setUserEmail("jane.doe@example.org");
        user1.setUserId(123L);
        user1.setUserPassword("iloveyou");
        String content = (new ObjectMapper()).writeValueAsString(user1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/blog/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.controller_User)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"message\":\"Not all who wander are lost\",\"data\":{\"userId\":123,\"userEmail\":\"jane.doe@example.org\","
                                        + "\"userPassword\":\"iloveyou\"},\"status\":true}"));
    }

    @Test
    void testLoginUser2() throws Exception {
        User user = new User();
        user.setIsDelete(1);
        user.setPersonDeactivated(1);
        user.setRemoveDate("2020-03-01");
        user.setUserEmail("jane.doe@example.org");
        user.setUserId(123L);
        user.setUserPassword("iloveyou");

        DTO_User dto_User = new DTO_User();
        dto_User.setData(user);
        dto_User.setMessage("Not all who wander are lost");
        dto_User.setStatus(false);
        when(this.service_User.logInUser((User) any())).thenReturn(dto_User);

        User user1 = new User();
        user1.setIsDelete(1);
        user1.setPersonDeactivated(1);
        user1.setRemoveDate("2020-03-01");
        user1.setUserEmail("jane.doe@example.org");
        user1.setUserId(123L);
        user1.setUserPassword("iloveyou");
        String content = (new ObjectMapper()).writeValueAsString(user1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/blog/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.controller_User)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(406))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"message\":\"Not all who wander are lost\",\"data\":{\"userId\":123,\"userEmail\":\"jane.doe@example.org\","
                                        + "\"userPassword\":\"iloveyou\"},\"status\":false}"));
    }

    @Test
    void testRegisterUser() throws Exception {
        User user = new User();
        user.setIsDelete(1);
        user.setPersonDeactivated(1);
        user.setRemoveDate("2020-03-01");
        user.setUserEmail("jane.doe@example.org");
        user.setUserId(123L);
        user.setUserPassword("iloveyou");

        DTO_User dto_User = new DTO_User();
        dto_User.setData(user);
        dto_User.setMessage("Not all who wander are lost");
        dto_User.setStatus(true);
        when(this.service_User.addUser((User) any())).thenReturn(dto_User);

        User user1 = new User();
        user1.setIsDelete(1);
        user1.setPersonDeactivated(1);
        user1.setRemoveDate("2020-03-01");
        user1.setUserEmail("jane.doe@example.org");
        user1.setUserId(123L);
        user1.setUserPassword("iloveyou");
        String content = (new ObjectMapper()).writeValueAsString(user1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/blog/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.controller_User)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"userId\":123,\"userEmail\":\"jane.doe@example.org\",\"userPassword\":\"iloveyou\"}"));
    }

    @Test
    void testRegisterUser2() throws Exception {
        User user = new User();
        user.setIsDelete(1);
        user.setPersonDeactivated(1);
        user.setRemoveDate("2020-03-01");
        user.setUserEmail("jane.doe@example.org");
        user.setUserId(123L);
        user.setUserPassword("iloveyou");

        DTO_User dto_User = new DTO_User();
        dto_User.setData(user);
        dto_User.setMessage("Not all who wander are lost");
        dto_User.setStatus(false);
        when(this.service_User.addUser((User) any())).thenReturn(dto_User);

        User user1 = new User();
        user1.setIsDelete(1);
        user1.setPersonDeactivated(1);
        user1.setRemoveDate("2020-03-01");
        user1.setUserEmail("jane.doe@example.org");
        user1.setUserId(123L);
        user1.setUserPassword("iloveyou");
        String content = (new ObjectMapper()).writeValueAsString(user1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/blog/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.controller_User)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(406));
    }

    @Test
    void testRegisterUser3() throws Exception {
        User user = new User();
        user.setIsDelete(1);
        user.setPersonDeactivated(1);
        user.setRemoveDate("2020-03-01");
        user.setUserEmail("jane.doe@example.org");
        user.setUserId(123L);
        user.setUserPassword("iloveyou");

        DTO_User dto_User = new DTO_User();
        dto_User.setData(user);
        dto_User.setMessage("This email has been registered by another user");
        dto_User.setStatus(false);
        when(this.service_User.addUser((User) any())).thenReturn(dto_User);

        User user1 = new User();
        user1.setIsDelete(1);
        user1.setPersonDeactivated(1);
        user1.setRemoveDate("2020-03-01");
        user1.setUserEmail("jane.doe@example.org");
        user1.setUserId(123L);
        user1.setUserPassword("iloveyou");
        String content = (new ObjectMapper()).writeValueAsString(user1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/blog/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.controller_User)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(409));
    }
}

