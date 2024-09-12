package sesac.server.common;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import sesac.server.account.controller.AccountController;
import sesac.server.account.service.AccountService;
import sesac.server.auth.filter.AccessTokenFilter;
import sesac.server.auth.filter.RefreshTokenFilter;
import sesac.server.auth.resolver.AuthPrincipalArgumentResolver;
import sesac.server.common.config.SecurityConfig;
import sesac.server.common.config.WebMvcConfig;
import sesac.server.feed.controller.PostController;
import sesac.server.feed.dto.request.CreatePostRequest;
import sesac.server.feed.service.LikesService;
import sesac.server.feed.service.PostService;
import sesac.server.feed.service.ReplyService;

@WebMvcTest(controllers = PostController.class)
@Import({TestSecurityConfig.class})
@AutoConfigureMockMvc
public class SecurityConfigTest2 {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthPrincipalArgumentResolver authPrincipalArgumentResolver;

    @MockBean
    private PostService postService;

    @MockBean
    private LikesService likesService;

    @MockBean
    private ReplyService replyService;

    @MockBean
    private AccessTokenFilter accessTokenFilter;


    @MockBean
    RefreshTokenFilter refreshTokenFilter;
    @Autowired
    protected ObjectMapper objectMapper;

    @Test
    @DisplayName("Access denied for unauthenticated users")
    public void shouldDenyAccessForUnauthenticatedUsers() throws Exception {
        Mockito.doNothing().when(accessTokenFilter)
                .doFilter(Mockito.any(), Mockito.any(), Mockito.any());

        mockMvc.perform(post("/posts/campus"))
                .andExpect(status().isUnauthorized()); // expecting 401 Unauthorized
    }

    @Test
    @DisplayName("Access denied for unauthenticated users")
    public void Test() throws Exception {
        Mockito.doNothing().when(accessTokenFilter)
                .doFilter(Mockito.any(), Mockito.any(), Mockito.any());
        String token = JwtTestUtil.createTestJwtToken("1", "STUDENT");
        CreatePostRequest request = new CreatePostRequest("제목", null, null, null);

        mockMvc.perform(JwtTestUtil.addJwtToken(post("/posts/campus"), token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());
    }
}
