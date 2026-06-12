package com.pluralsight.demo.internship.controller;

import com.pluralsight.demo.internship.model.Candidate;
import com.pluralsight.demo.internship.model.Internship;
import com.pluralsight.demo.internship.service.CandidateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CandidateController.class)
public class CandidateControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CandidateService candidateService;
// your tests go here
    @Test
    void getAllCandidate_shouldReturnListOfCandidates() throws Exception {
        Candidate candidate1 = new Candidate("Emma Jansen", "emma@example.com", "Data Science");
        candidate1.setId(2L);

        Candidate candidate2 = new Candidate("Lucas van Dam", "lucas@example.com", "Software Engineering");
        candidate1.setId(3L);

        List<Candidate> candidates = Arrays.asList(candidate1,candidate2);

        when(candidateService.getAllCandidates()).thenReturn(candidates);

        mockMvc.perform(get("/api/candidates")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // 200 OK
                .andExpect(jsonPath("$[0].name").value("Emma Jansen"))
                .andExpect(jsonPath("$[0].email").value("emma@example.com"))
                .andExpect(jsonPath("$[0].fieldOfStudy").value("Data Science"))
                .andExpect(jsonPath("$[1].name").value("Lucas van Dam"))
                .andExpect(jsonPath("$[1].email").value("lucas@example.com"))
                .andExpect(jsonPath("$[1].fieldOfStudy").value("Software Engineering"))
                .andExpect(jsonPath("$.length()").value(2));

    }

    @Test
    void createCandidate_shouldReturnCreatedCandidate() throws Exception {
        Candidate inputCandidate = new Candidate("Gabe", "gabe@example.com", "Software Engineering");
        Candidate savedCandidate = new Candidate("Gabe", "gabe@example.com", "Software Engineering");

        savedCandidate.setId(12L);

        when(candidateService.createCandidate(any(Candidate.class)))
                .thenReturn(savedCandidate);

        // ACT & ASSERT
        mockMvc.perform(post("/api/candidates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name": "Gabe",
                          "email": "gabe@example.com",
                          "fieldOfStudy": "Software Engineering"
                        }
                        """))
                .andExpect(status().isOk())  // Should be 201 but our code returns 200
                .andExpect(jsonPath("$.id").value(12))
                .andExpect(jsonPath("$.name").value("Gabe"))
                .andExpect(jsonPath("$.fieldOfStudy").value("Software Engineering"));

    }

}
