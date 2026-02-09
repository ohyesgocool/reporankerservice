package org.git.reporankerservice.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0, stubs = "classpath:/wiremock/mappings")
class RepositoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGetRepositories() throws Exception {
        stubFor(WireMock.get(urlPathEqualTo("/search/repositories"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("github-response.json")));

        mockMvc.perform(get("/api/repositories/search")
                        .param("createdFrom", "2022-01-01")
                        .param("language", "java"))
                .andExpect(status().isOk());
    }
}
