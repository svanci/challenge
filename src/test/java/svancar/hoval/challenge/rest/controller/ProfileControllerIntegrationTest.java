package svancar.hoval.challenge.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;
import svancar.hoval.challenge.ChallengeApplication;
import svancar.hoval.challenge.exception.exceptions.NotFoundException;
import svancar.hoval.challenge.model.Profile;
import svancar.hoval.challenge.service.impl.ProfileServiceImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.MOCK, classes={ ChallengeApplication.class })
public class ProfileControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private ProfileServiceImpl profileService;

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void should_GetProfile_When_ValidGetRequest() throws Exception {
        Profile profile = new Profile("Martin", "Svancar", "desc", "Java Developer", "", null);
        when(profileService.getProfile(1L)).thenReturn(profile);


        mockMvc.perform(get("/api/profiles/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Martin"))
                .andExpect(jsonPath("$.lastName").value("Svancar"))
                .andExpect(jsonPath("$.description").value("desc"))
                .andExpect(jsonPath("$.occupation").value("Java Developer"))
                .andExpect(jsonPath("$.linkedIn").value(""))
                .andExpect(jsonPath("$.profileImagePath").value(IsNull.nullValue()));
    }

    @Test
    public void should_Return404_When_ProfileNotFound() throws Exception {
        when(profileService.getProfile(2L)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/api/profiles/{id}", 2)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void should_updateProfile_When_ValidPutRequest() throws Exception {
        Profile profile = new Profile("Peter", "New", "new desc", "Java Developer", "", null);

        when(profileService.save(any(Profile.class))).thenReturn(profile);

        mockMvc.perform(put("/api/profiles/{id}", 1)
                .content("{\"firstName\": \"a\",\"lastName\": \"df\",\"occupation\": \"Java Developer\",\"linkedIn\": \"www.linkedin.com/in/msvancar\", \"description\": \"\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Peter"))
                .andExpect(jsonPath("$.lastName").value("New"))
                .andExpect(jsonPath("$.description").value("new desc"));

    }

    @Test
    public void should_return400_When_ValidationError() throws Exception {
        Profile profile = new Profile("", "New", "new desc", "Java Developer", "", null);

        when(profileService.save(any(Profile.class))).thenReturn(profile);

        mockMvc.perform(put("/api/profiles/{id}", 1)
                .content("{\"firstName\": \"\",\"lastName\": \"df\",\"occupation\": \"Java Developer\",\"linkedIn\": \"www.linkedin.com/in/msvancar\", \"description\": \"\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }



}
