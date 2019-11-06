package signal9.springboot.restdoc;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestDocApplication.class)
//@ActiveProfiles("test")
public class RestDocTest {
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private RestDocumentationResultHandler document;

    @Before
    public void setup() throws Exception {
        this.objectMapper = new ObjectMapper();

        this.document = document(
                "{method-name}",
                preprocessRequest(prettyPrint(),
                        modifyUris().host("signal9.co.kr").scheme("https").removePort())
                , preprocessResponse(prettyPrint())
        );

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                //.apply(springSecurity())
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(document)
                .build();
    }

    @Test
    public void list() throws Exception {
        log.debug("## call test : list");
        this.mockMvc.perform(RestDocumentationRequestBuilders
                        .get("/list/{pageNo}", 1)
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                //.header("Authorization", accessToken)
                //.cookie(new javax.servlet.http.Cookie("SESSIONID", cookieValue))
                )
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("pageNo").description("조회 페이지 번호.")
                        ),
                        responseFields(
                                fieldWithPath("status").description("결과 상태."),
                                fieldWithPath("posts").description("글 목록."),
                                fieldWithPath("posts.*").description("글 순번."),
                                fieldWithPath("posts.*.subject").description("글 제목."),
                                fieldWithPath("posts.*.content").description("글 내용.")
                        ))
                );
    }


    @Test
    public void add() throws Exception {
        log.debug("## call add : list");

        String contentBody = "{\"subject\":\"add test\", \"content\":\"bla~ bla~\"}";
        this.mockMvc.perform(RestDocumentationRequestBuilders
                        .post("/add")
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(contentBody)
                )
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestFields(
                                fieldWithPath("subject").description("글 제목."),
                                fieldWithPath("content").description("글 본문.")
                        ),
                        responseFields(
                                fieldWithPath("status").description("결과 상태.")
                        ))
                );
    }
}
