package sesac.server.feed.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;
import sesac.server.auth.filter.AccessTokenFilter;
import sesac.server.auth.filter.RefreshTokenFilter;
import sesac.server.common.JwtTestUtil;
import sesac.server.common.TestSecurityConfig;
import sesac.server.common.exception.GlobalExceptionHandler;
import sesac.server.common.util.EnumBindingInitializer;
import sesac.server.feed.dto.request.CreatePostRequest;
import sesac.server.feed.service.LikesService;
import sesac.server.feed.service.PostService;
import sesac.server.feed.service.ReplyService;


@Log4j2
@WebMvcTest(controllers = {PostController.class})
@Import({TestSecurityConfig.class, GlobalExceptionHandler.class, EnumBindingInitializer.class})
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;
    @MockBean
    private LikesService likesService;
    @MockBean
    private ReplyService replyService;

    @MockBean
    AccessTokenFilter accessTokenFilter;
    @MockBean
    RefreshTokenFilter refreshTokenFilter;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private Validator validator;


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                        new PostController(postService, likesService, replyService))
                .setControllerAdvice(new EnumBindingInitializer(), new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    public void test() throws Exception {
        String token = JwtTestUtil.createTestJwtToken("1", "STUDENT");
        CreatePostRequest request = new CreatePostRequest("제목", "내요요옹", null, null);
        mockMvc.perform(JwtTestUtil.addJwtToken(post("/posts/campus"), token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());

        verify(postService).createPost(any(), any(), any(CreatePostRequest.class));
    }

//    @Test
//    public void test2() throws Exception {
//        CreatePostRequest request = new CreatePostRequest("제목", null, null, null);
//        var a = mockMvc.perform(post("/posts/campus")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request))
//                )
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message")
//                        .value(PostErrorCode.REQUIRED_CONTENT.getMessage()));
//        log.info("aa");
//
//    }
}

//class JwtTestToken extends JwtUtil {
//
//    private static final String key = "testtesttesttesttesttesttesttesttesttesttesttesttesttestf";
//
//    public static String createToken(Map<String, Object> payloads) {
//        return createToken(payloads, 60);
//    }
//
//    public static String createToken(Map<String, Object> payloads, Integer expired) {
//        Map<String, Object> headers = new HashMap<>();
//        headers.put("typ", "JWT");
//        headers.put("alg", "HS256");
//
//        return Jwts.builder()
//                .header().add(headers).and()
//                .claims(payloads)
//                .issuedAt(Date.from(ZonedDateTime.now().toInstant()))
//                .expiration(Date.from(ZonedDateTime.now().plusSeconds(expired).toInstant()))
//                .signWith(SignatureAlgorithm.HS256, key.getBytes(StandardCharsets.UTF_8))
//                .compact();
//    }
//}