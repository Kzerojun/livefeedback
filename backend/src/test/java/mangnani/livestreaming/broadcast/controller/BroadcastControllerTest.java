package mangnani.livestreaming.broadcast.controller;

import static mangnani.livestreaming.utils.ApiDocumentUtils.getDocumentRequest;
import static mangnani.livestreaming.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;
import java.util.List;

import mangnani.livestreaming.broadcast.constant.BroadcastAttribute;
import mangnani.livestreaming.broadcast.constant.BroadcastCategory;
import mangnani.livestreaming.auth.jwt.filter.JwtAuthenticationFilter;
import mangnani.livestreaming.auth.jwt.provider.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import mangnani.livestreaming.broadcast.dto.request.StartBroadcastRequest;
import mangnani.livestreaming.broadcast.dto.response.GetBroadcastInfoResponse;
import mangnani.livestreaming.broadcast.dto.response.GetBroadcastListResponse;
import mangnani.livestreaming.broadcast.dto.response.StartBroadcastResponse;
import mangnani.livestreaming.broadcast.service.BroadcastService;
import mangnani.livestreaming.broadcast.dto.object.BroadcastInfo;
import mangnani.livestreaming.broadcast.exception.NoExistedBroadcast;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseMessage;

@WebMvcTest(BroadcastController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class BroadcastControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BroadcastService broadcastService;

	@MockBean
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@MockBean
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private ObjectMapper objectMapper;

	@RegisterExtension
	final RestDocumentationExtension restDocumentation = new RestDocumentationExtension(
			"build/generated-snippets");

	@BeforeEach
	void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(documentationConfiguration(restDocumentation))
				.build();
	}

	@Nested
	@DisplayName("방송 시작 테스트")
	class StartBroadcastTests {

		private final String BROADCAST_START_URL = "/broadcast/start";
		@Test
		@DisplayName("방송 시작 - 성공")
		void startBroadcastSuccess() throws Exception {
			StartBroadcastRequest request = new StartBroadcastRequest();
			request.setTitle("테스트 방송");
			request.setStreamerId("testStreamer");
			request.setStreamerNickname("테스트 스트리머");
			request.setStreamKey("testStreamKey");
			request.setCategory(BroadcastCategory.CPP);
			request.setAttribute(BroadcastAttribute.FAN);

			StartBroadcastResponse response = StartBroadcastResponse.success(1L);
			when(broadcastService.startBroadcast(any(StartBroadcastRequest.class)))
					.thenReturn(ResponseEntity.ok(response));

			mockMvc.perform(post(BROADCAST_START_URL)
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(request)))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
					.andExpect(jsonPath("$.message").value(ResponseMessage.SUCCESS))
					.andExpect(jsonPath("$.broadcastId").value(1))
					.andDo(document("broadcast/start/success",
							requestFields(
									fieldWithPath("title").description("방송 제목"),
									fieldWithPath("streamerId").description("스트리머 ID"),
									fieldWithPath("streamerNickname").description("스트리머 닉네임"),
									fieldWithPath("streamKey").description("고유 스트림 키"),
									fieldWithPath("category").description("방송 카테고리"),
									fieldWithPath("attribute").description("방송 속성")
							),
							responseFields(
									fieldWithPath("code").description("응답 코드"),
									fieldWithPath("message").description("응답 메시지"),
									fieldWithPath("broadcastId").description("생성된 방송 ID")
							)));
		}

		@Test
		@DisplayName("방송 시작 - 실패 (잘못된 요청)")
		void startBroadcastFailure() throws Exception {
			StartBroadcastRequest request = new StartBroadcastRequest();
			// 필수 필드를 비워두어 잘못된 요청을 시뮬레이션

			mockMvc.perform(post(BROADCAST_START_URL)
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(request)))
					.andExpect(status().isBadRequest())
					.andDo(document("broadcast/start/fail/inValidRequest",
							requestFields(
									fieldWithPath("title").description("방송 제목").optional(),
									fieldWithPath("streamerId").description("스트리머 ID").optional(),
									fieldWithPath("streamerNickname").description("스트리머 닉네임").optional(),
									fieldWithPath("streamKey").description("고유 스트림 키").optional(),
									fieldWithPath("category").description("방송 카테고리").optional(),
									fieldWithPath("attribute").description("방송 속성").optional()

							),
							responseFields(
									fieldWithPath("code").description("응답 코드"),
									fieldWithPath("message").description("응답 메시지")
							)));
		}
	}

	@Nested
	@DisplayName("방송 목록 조회 테스트")
	class GetBroadcastsTests {

		private final String BROADCAST_CATEGORY_URL = "/broadcast/category";
		@Test
		@DisplayName("카테고리별 방송 목록 조회 - 성공")
		void getBroadcastsByCategorySuccess() throws Exception {
			List<BroadcastInfo> broadcastInfos = Arrays.asList(
					new BroadcastInfo("테스트방송", "test12", null, 1L, "localhost:8080/file/thumbnail1"),
					new BroadcastInfo("테스트방송2", "test123", null, 2L, "localhost:8080/file/thumbnail2")
			);
			GetBroadcastListResponse response = GetBroadcastListResponse.success(broadcastInfos);

			when(broadcastService.getBroadcastsByCategory(any(String.class)))
					.thenReturn(ResponseEntity.ok(response));

			mockMvc.perform(get(BROADCAST_CATEGORY_URL)
							.param("category", "JAVA"))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
					.andExpect(jsonPath("$.message").value(ResponseMessage.SUCCESS))
					.andExpect(jsonPath("$.broadcastInfos").isArray())
					.andExpect(jsonPath("$.broadcastInfos[0].broadcastId").value(1))
					.andDo(document("broadcast/category/success",
							getDocumentRequest(),
							getDocumentResponse(),
							queryParameters(
									parameterWithName("category").description("필터링할 카테고리")
							),
							responseFields(
									fieldWithPath("code").description("응답 코드"),
									fieldWithPath("message").description("응답 메시지"),
									fieldWithPath("broadcastInfos").description("방송 정보 목록"),
									fieldWithPath("broadcastInfos[].broadcastId").description("방송 ID"),
									fieldWithPath("broadcastInfos[].title").description("방송 제목"),
									fieldWithPath("broadcastInfos[].streamerId").description("스트리머 ID"),
									fieldWithPath("broadcastInfos[].profileImage").description("스트리머 프로필 이미지"),
									fieldWithPath("broadcastInfos[].thumbnail").description("방송 썸네일")
							)));
		}


	}

	@Nested
	@DisplayName("방송 정보 조회 테스트")
	class GetBroadcastInfoTests {

		private final String BROADCAST_INFO_URL = "/broadcast/info";
		@Test
		@DisplayName("방송 정보 조회 - 성공")
		void getBroadcastInfoSuccess() throws Exception {
			GetBroadcastInfoResponse response = GetBroadcastInfoResponse.success("테스트스트리머", "테스트 방송", "testStreamKey");

			when(broadcastService.getBroadcastInfo(any(Long.class)))
					.thenReturn(ResponseEntity.ok(response));

			mockMvc.perform(get(BROADCAST_INFO_URL)
							.param("broadcastId", "1"))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
					.andExpect(jsonPath("$.message").value(ResponseMessage.SUCCESS))
					.andExpect(jsonPath("$.streamerNickname").value("테스트스트리머"))
					.andExpect(jsonPath("$.title").value("테스트 방송"))
					.andExpect(jsonPath("$.streamKey").value("testStreamKey"))
					.andDo(document("broadcast/info/success",
							getDocumentRequest(),
							getDocumentResponse(),
							queryParameters(
									parameterWithName("broadcastId").description("방송 ID")
							),
							responseFields(
									fieldWithPath("code").description("응답 코드"),
									fieldWithPath("message").description("응답 메시지"),
									fieldWithPath("streamerNickname").description("스트리머 닉네임"),
									fieldWithPath("title").description("방송 제목"),
									fieldWithPath("streamKey").description("방송 스트림 키")
							)));
		}

		@Test
		@DisplayName("방송 정보 조회 - 실패 (존재하지 않는 방송)")
		void getBroadcastInfoFailure() throws Exception {
			when(broadcastService.getBroadcastInfo(any(Long.class)))
					.thenThrow(new NoExistedBroadcast());

			mockMvc.perform(get(BROADCAST_INFO_URL)
							.param("broadcastId", "999"))
					.andExpect(status().isNotFound())
					.andExpect(jsonPath("$.code").value(ResponseCode.NO_EXISTED_BROADCAST))
					.andExpect(jsonPath("$.message").value(ResponseMessage.NO_EXISTED_BROADCAST))
					.andDo(document("broadcast/info/fail/noExistedBroadcast",
							queryParameters(
									parameterWithName("broadcastId").description("방송 ID")
							),
							responseFields(
									fieldWithPath("code").description("응답 코드"),
									fieldWithPath("message").description("응답 메시지")
							)));
		}
	}
}