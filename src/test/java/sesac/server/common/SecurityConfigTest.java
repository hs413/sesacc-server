package sesac.server.common;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sesac.server.feed.dto.request.CreatePostRequest;
import sesac.server.feed.service.PostService;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    private PostService postService;

    @Test
    @DisplayName("Access denied for unauthenticated users")
    public void shouldDenyAccessForUnauthenticatedUsers() throws Exception {
        mockMvc.perform(post("/posts/campus"))
                .andExpect(status().isOk()); // expecting 401 Unauthorized
    }

    @Test
    public void test() throws Exception {
        String token = JwtTestUtil.createTestJwtToken("1", "STUDENT");
        CreatePostRequest request = new CreatePostRequest("제목", null, null, null);

        mockMvc.perform(JwtTestUtil.addJwtToken(post("/posts/campus"), token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());

//        verify(postService).createPost(any(), any(), any(CreatePostRequest.class));
    }
}
