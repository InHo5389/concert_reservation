package concert.api.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import concert.application.WaitingTokenFacade;
import concert.domain.token.TokenStatus;
import concert.domain.token.dto.WaitingTokenIssueTokenDto;
import concert.domain.token.jwt.WaitingTokenValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(TokenController.class)
class TokenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WaitingTokenFacade waitingTokenFacade;

    @MockBean
    private WaitingTokenValidator waitingTokenValidator;

    @Test
    @DisplayName("토큰을 발급한다.")
    void issueToken() throws Exception {
        //given
        Long userId = 1L;

        WaitingTokenIssueTokenDto mockDto = new WaitingTokenIssueTokenDto(1L, userId, TokenStatus.ACTIVE,
                LocalDateTime.now(), LocalDateTime.now().plusHours(1), "dummyJwtToken");
        given(waitingTokenFacade.issueToken(userId)).willReturn(mockDto);
        //when
        //then
        mockMvc.perform(post("/concerts/tokens/{userId}", userId))
                .andDo(print())
                .andExpect(status().isOk());
    }

}