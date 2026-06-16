package com.janwypych.ForumApi.security.like;

import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.services.LikeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DeleteLikeSecurityTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LikeService likeService;

    public Authentication createAuthentication(String role) {
        Account account = TestDataUtil.createAccount();

        return new UsernamePasswordAuthenticationToken(
                account,
                null,
                List.of(new SimpleGrantedAuthority(role))
        );
    }

    private RequestPostProcessor authenticatedUser(String role) {
        return authentication(createAuthentication(role));
    }

    @Test
    public void testThatDeleteLikeReturnsHttp401WhenUserIsUnauthenticated() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/posts/1/likes")
        ).andExpect(
                status().isUnauthorized()
        );
    }

    @Test
    public void testThatDeleteLikeReturnsHttp401WhenTokenIsInvalid() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/posts/1/likes")
                        .header("Authorization", "Bearer invalidtoken")
        ).andExpect(
                status().isUnauthorized()
        );
    }

    @Test
    public void testThatDeleteLikeReturnsHttp204WhenUserIsAuthenticated() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/posts/1/likes")
                        .with(authenticatedUser("ROLE_USER"))
        ).andExpect(
                status().isNoContent()
        );
    }

    @Test
    public void testThatDeleteLikeReturnsHttp204WhenAdminIsAuthenticated() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/posts/1/likes")
                        .with(authenticatedUser("ROLE_ADMIN"))
        ).andExpect(
                status().isNoContent()
        );
    }
}
