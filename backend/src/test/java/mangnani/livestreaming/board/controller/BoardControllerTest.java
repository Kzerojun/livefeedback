package mangnani.livestreaming.board.controller;


import static mangnani.livestreaming.utils.ApiDocumentUtils.getDocumentRequest;
import static mangnani.livestreaming.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import mangnani.livestreaming.board.dto.response.GetBoardDetailResponse;
import mangnani.livestreaming.board.exception.NoExistedBoard;
import mangnani.livestreaming.boardcategory.exception.NoExistedBoardCategory;
import mangnani.livestreaming.member.exception.NoExistedMember;
import mangnani.livestreaming.station.exception.NoExistedStation;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import mangnani.livestreaming.board.dto.object.BoardItem;
import mangnani.livestreaming.board.dto.request.PostBoardRequest;
import mangnani.livestreaming.board.dto.response.GetBoardListResponse;
import mangnani.livestreaming.board.dto.response.PostBoardResponse;
import mangnani.livestreaming.board.service.BoardService;
import mangnani.livestreaming.global.config.SpringSecurityConfig;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseMessage;
import mangnani.livestreaming.auth.jwt.filter.JwtAuthenticationFilter;
import mangnani.livestreaming.auth.jwt.provider.JwtTokenProvider;
import mangnani.livestreaming.utils.MockMvcHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = BoardController.class, excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SpringSecurityConfig.class)})
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class BoardControllerTest {

	@Autowired
	private MockMvc mockMvc;

	private MockMvcHelper mockMvcHelper;

	@MockBean
	private BoardService boardService;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	JwtTokenProvider jwtTokenProvider;

	@MockBean
	JwtAuthenticationFilter jwtAuthenticationFilter;

	@RegisterExtension
	final RestDocumentationExtension restDocumentation = new RestDocumentationExtension(
			"build/generated-snippets");

	@BeforeEach
	void setUp(WebApplicationContext webApplicationContext,
			RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(documentationConfiguration(restDocumentation))
				.build();
		this.mockMvcHelper = new MockMvcHelper(mockMvc, objectMapper);
	}

	@Nested
	class PostBoard {

		private final String POST_BOARD_URL = "/board/post";
		private final String ACCESS_TOKEN = "accessToken";

		private final String AUTHORIZATION = "Authorization";
		private final String BEARER = "Bearer";
		private static final String INVALID_TOKEN = "invalid_token";

		@Test
		@DisplayName("게시글 작성 성공")
		@WithMockUser(username = "jake", roles = "USER")
		void postBoard() throws Exception {
			//given
			PostBoardResponse response = PostBoardResponse.success();
			when(boardService.postBoard(any(PostBoardRequest.class), any(String.class))).thenReturn(
					ResponseEntity.ok().body(response));

			//when
			PostBoardRequest request = new PostBoardRequest();
			request.setTitle("테스트제목");
			request.setContent("테스트본문");
			request.setCategory("자유게시판");

			ResultActions result = mockMvc.perform(post(POST_BOARD_URL)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request))
					.header(AUTHORIZATION,
							BEARER + ACCESS_TOKEN) // Authorization 헤더에 accessToken 추가
					.accept(MediaType.APPLICATION_JSON));

			//then
			result.andExpect(status().isOk())
					.andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
					.andExpect(jsonPath("$.message").value(ResponseMessage.SUCCESS))
					.andDo(document("board/post/success",
							getDocumentRequest(),
							getDocumentResponse(),
							requestFields(
									fieldWithPath("title").type(JsonFieldType.STRING)
											.description("제목"),
									fieldWithPath("content").type(JsonFieldType.STRING)
											.description("본문"),
									fieldWithPath("category").type(JsonFieldType.STRING)
											.description("카테고리"),
									fieldWithPath("boardImageList").type(JsonFieldType.ARRAY)
											.description("이미지 리스트").optional()
							),
							responseFields(
									fieldWithPath("code").type(JsonFieldType.STRING)
											.description("결과 코드"),
									fieldWithPath("message").type(JsonFieldType.STRING)
											.description("결과 메시지")
							)
					));
		}

		@Test
		@DisplayName("게시글 작성 실패 - 존재하지 않는 회원")
		@WithMockUser(username = "nonexistentuser", roles = "USER")
		void postBoardFailNoExistingMember() throws Exception {
			// given
			PostBoardRequest request = new PostBoardRequest();
			request.setTitle("Test Title");
			request.setContent("Test Content");
			request.setCategory("자유게시판");

			when(boardService.postBoard(any(PostBoardRequest.class), eq("nonexistentuser")))
					.thenThrow(new NoExistedMember());

			// when
			ResultActions result = mockMvc.perform(post(POST_BOARD_URL)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request))
					.header(AUTHORIZATION, BEARER + ACCESS_TOKEN)
					.accept(MediaType.APPLICATION_JSON));

			// then
			result.andExpect(status().isNotFound())  // This expectation will cause the test to fail
					.andExpect(jsonPath("$.code").value(ResponseCode.NO_EXISTED_MEMBER))
					.andExpect(jsonPath("$.message").value(ResponseMessage.NO_EXISTED_MEMBER))
					.andDo(document("board/post/fail/no-existed-member",
							getDocumentRequest(),
							getDocumentResponse(),
							requestFields(
									fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
									fieldWithPath("content").type(JsonFieldType.STRING).description("본문"),
									fieldWithPath("category").type(JsonFieldType.STRING).description("카테고리"),
									fieldWithPath("boardImageList").type(JsonFieldType.ARRAY).description("이미지 리스트").optional()
							),
							responseFields(
									fieldWithPath("code").type(JsonFieldType.STRING).description("결과 코드"),
									fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지")
							)
					));
		}

		//TODO
		@Test
		@DisplayName("게시글 작성 실패 - 인증 실패")
		void postBoardFailAuthenticationFailure() throws Exception {
			// given
			PostBoardRequest request = new PostBoardRequest();
			request.setTitle("Test Title");
			request.setContent("Test Content");
			request.setCategory("Test Category");
			when(jwtTokenProvider.validate(any(String.class))).thenReturn(false);

			// when
			ResultActions result = mockMvc.perform(post(POST_BOARD_URL)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request))
					.header(AUTHORIZATION, BEARER + INVALID_TOKEN)
					.accept(MediaType.APPLICATION_JSON));

			// then
			result.andExpect(status().isUnauthorized())
					.andDo(print())
					.andExpect(jsonPath("$.code").value("UNAUTHORIZED"))
					.andExpect(jsonPath("$.message").value("Authentication failed"))
					.andDo(document("board/post/fail/unauthenticated",
							getDocumentRequest(),
							getDocumentResponse(),
							requestFields(
									fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
									fieldWithPath("content").type(JsonFieldType.STRING).description("본문"),
									fieldWithPath("category").type(JsonFieldType.STRING).description("카테고리"),
									fieldWithPath("boardImageList").type(JsonFieldType.ARRAY).description("이미지 리스트").optional()
							),
							responseFields(
									fieldWithPath("code").type(JsonFieldType.STRING).description("결과 코드"),
									fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지")
							)
					));
		}
	}
	@Nested
	class GetBoardList{
		@Test
		@DisplayName("게시글 조회 성공")
		void testGetBoardSuccess() throws Exception {
			String userLoginId = "testUser";
			String category = "testCategory";
			Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"));

			List<BoardItem> boardItems = Arrays.asList(
					BoardItem.builder()
							.boardId(1L)
							.title("Title 1")
							.content("Content 1")
							.viewCount(10)
							.boardTitleImage("localhost:8080/file/encodedFileName")
							.createdAt(LocalDateTime.now())
							.build(),
					BoardItem.builder()
							.boardId(2L)
							.title("Title 2")
							.content("Content 2")
							.viewCount(20)
							.boardTitleImage("localhost:8080/file/encodedFileName2")
							.createdAt(LocalDateTime.now())
							.build()
			);
			int totalBoardsSize = 2;

			GetBoardListResponse response = GetBoardListResponse.success(boardItems, totalBoardsSize);

			when(boardService.getBoardList(eq(userLoginId), eq(category), any(Pageable.class)))
					.thenReturn(ResponseEntity.ok(response));

			ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/board/{userLoginId}", userLoginId)
					.param("category", category)
					.contentType(MediaType.APPLICATION_JSON));

			result.andExpect(status().isOk())
					.andExpect(jsonPath("$.code").value("SU"))
					.andExpect(jsonPath("$.boardItems").isArray())
					.andExpect(jsonPath("$.boardItems.length()").value(2))
					.andExpect(jsonPath("$.totalBoardsSize").value(totalBoardsSize))
					.andDo(document("board/get-board-list/success",
							getDocumentRequest(),
							getDocumentResponse(),
							pathParameters(
									parameterWithName("userLoginId").description("User's login ID")
							),
							responseFields(
									fieldWithPath("code").description("Whether the request was successful"),
									fieldWithPath("message").description("message"),
									fieldWithPath("boardItems").description("List of board items"),
									fieldWithPath("boardItems[].boardId").description("Board ID"),
									fieldWithPath("boardItems[].title").description("Board title"),
									fieldWithPath("boardItems[].content").description("Board content"),
									fieldWithPath("boardItems[].viewCount").description("Number of views"),
									fieldWithPath("boardItems[].boardTitleImage").description("URL of the board's title image"),
									fieldWithPath("boardItems[].createdAt").description("Creation date and time"),
									fieldWithPath("totalBoardsSize").description("Total number of boards")
							)
					));
		}

		@Test
		@DisplayName("게시글 조회 실패 - 방송국 존재 X")
		void testGetBoardNoExistedStation() throws Exception {
			String userLoginId = "nonExistentUser";
			String category = "testCategory";

			when(boardService.getBoardList(eq(userLoginId), eq(category), any(Pageable.class)))
					.thenThrow(new NoExistedStation());

			mockMvc.perform(RestDocumentationRequestBuilders.get("/board/{userLoginId}", userLoginId)
							.param("category", category)
							.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest())
					.andExpect(jsonPath("$.code").value("NES"))
					.andExpect(jsonPath("$.message").value("방송국이 존재하지 않습니다."))
					.andDo(document("board/get-board-list/fail/no-existed-station",
							getDocumentRequest(),
							getDocumentResponse(),
							pathParameters(
									parameterWithName("userLoginId").description("User's login ID")
							),
							queryParameters(
									parameterWithName("category").description("category")
							),
							responseFields(
									fieldWithPath("code").description(
											"Whether the request was fail"),
									fieldWithPath("message").description("Error message")
							)
					));
		}

		@Test
		@DisplayName("게시글 조회 실패 - 카테고리 존재 X")
		void testGetBoardNoExistedBoardCategory() throws Exception {
			String userLoginId = "testUser";
			String category = "nonExistentCategory";

			when(boardService.getBoardList(eq(userLoginId), eq(category), any(Pageable.class)))
					.thenThrow(new NoExistedBoardCategory());

			mockMvc.perform(RestDocumentationRequestBuilders.get("/board/{userLoginId}", userLoginId)
							.param("category", category)
							.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest())
					.andExpect(jsonPath("$.code").value("NEC"))
					.andExpect(jsonPath("$.message").value("게시판 카테고리가 존재하지 않습니다."))
					.andDo(document("board/get-board-list/fail/no-category",
							getDocumentRequest(),
							getDocumentResponse(),
							pathParameters(
									parameterWithName("userLoginId").description("User's login ID")
							),
							queryParameters(
									parameterWithName("category").description("category")
							),
							responseFields(
									fieldWithPath("code").description("Whether the request was fail"),
									fieldWithPath("message").description("Error message")
							)
					));
		}
	}

	@Nested
	class GetBoardDetail{
		@Test
		@DisplayName("게시글 세부사항 조회 -성공")
		void testGetBoardDetailSuccess() throws Exception {
			Long boardId = 1L;
			String title = "Test Board";
			String content = "This is a test board content.";
			List<String> images = List.of("localhost:8080/file/encoded.image");

			GetBoardDetailResponse response = GetBoardDetailResponse.success(title, content, images);

			when(boardService.getBoardDetail(anyLong())).thenReturn(ResponseEntity.ok(response));

			mockMvc.perform(RestDocumentationRequestBuilders.get("/board/detail")
							.param("boardId", boardId.toString())
							.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.code").value("SU"))
					.andExpect(jsonPath("$.message").value("성공"))
					.andExpect(jsonPath("$.title").value(title))
					.andExpect(jsonPath("$.content").value(content))
					.andExpect(jsonPath("$.images").isArray())
					.andExpect(jsonPath("$.images[0]").value("localhost:8080/file/encoded.image"))
					.andDo(document("board/get-board-detail/success",
							getDocumentRequest(),
							getDocumentResponse(),
							queryParameters(
									parameterWithName("boardId").description("ID of the board to retrieve")
							),
							responseFields(
									fieldWithPath("code").description("Response code"),
									fieldWithPath("message").description("Response message"),
									fieldWithPath("title").description("Title of the board"),
									fieldWithPath("content").description("Content of the board"),
									fieldWithPath("images").description("List of image URLs associated with the board")
							)
					));
		}

		@Test
		@DisplayName("게시글 조회 실패 - 게시글 존재 X")
		void testGetBoardDetailNotFound() throws Exception {
			Long boardId = 999L;

			when(boardService.getBoardDetail(anyLong())).thenThrow(new NoExistedBoard());

			mockMvc.perform(RestDocumentationRequestBuilders.get("/board/detail")
							.param("boardId", boardId.toString())
							.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isNotFound())
					.andDo(document("board/get-board-detail/fail/no-existed-board",
							getDocumentRequest(),
							queryParameters(
									parameterWithName("boardId").description("ID of the board to retrieve")
							),
							responseFields(
									fieldWithPath("code").description("Response code"),
									fieldWithPath("message").description("Response message")
							)
					));
		}
	}
}