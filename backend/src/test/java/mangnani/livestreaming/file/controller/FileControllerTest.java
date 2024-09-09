package mangnani.livestreaming.file.controller;

import static mangnani.livestreaming.utils.ApiDocumentUtils.getDocumentRequest;
import static mangnani.livestreaming.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import mangnani.livestreaming.auth.jwt.filter.JwtAuthenticationFilter;
import mangnani.livestreaming.auth.jwt.provider.JwtTokenProvider;
import mangnani.livestreaming.broadcast.controller.BroadcastController;
import mangnani.livestreaming.file.dto.response.FileResponse;
import mangnani.livestreaming.file.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(FileController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class FileControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FileService fileService;

	@MockBean
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@MockBean
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private ObjectMapper objectMapper;

	private MockMultipartFile mockFile;

	@RegisterExtension
	final RestDocumentationExtension restDocumentation = new RestDocumentationExtension(
			"build/generated-snippets");


	@BeforeEach
	void setUp(WebApplicationContext webApplicationContext,
			RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(documentationConfiguration(restDocumentation))
				.build();

		mockFile = new MockMultipartFile(
				"file",
				"test.jpg",
				MediaType.IMAGE_JPEG_VALUE,
				"test image content".getBytes()
		);
	}

	@Nested
	@DisplayName("게시글 이미지 업로드 테스트")
	class UploadBoardImage {

		MockMultipartFile validJpegFile;
		MockMultipartFile validPngFile;
		MockMultipartFile invalidFile;

		String BOARD_IMAGES_URL = "http://localhost:8080/file/boardImages/";
		String BOARD_IMAGES_PATH = "/Users/gim-yeongjun/Desktop/2024-feedback/livefeedback/boardImages/";

		@BeforeEach
		void setUp() {
			validJpegFile = new MockMultipartFile(
					"file",
					"test.jpg",
					MediaType.IMAGE_JPEG_VALUE,
					"test image content".getBytes()
			);

			validPngFile = new MockMultipartFile(
					"file",
					"test.png",
					MediaType.IMAGE_PNG_VALUE,
					"test image content".getBytes()
			);

			invalidFile = new MockMultipartFile(
					"file",
					"test.txt",
					MediaType.TEXT_PLAIN_VALUE,
					"test content".getBytes()
			);
		}

		@Test
		@DisplayName("게시글 이미지 파일 업로드 - 성공 JPG")
		void uploadBoardImageJpegTest() throws Exception {
			String fileName = "uploaded-board-image.jpg";
			String expectedUrl = BOARD_IMAGES_URL + fileName;
			FileResponse fileResponse = FileResponse.success(expectedUrl);
			ResponseEntity<FileResponse> responseEntity = ResponseEntity.ok(fileResponse);

			when(fileService.uploadBoardImage(any())).thenReturn(responseEntity);

			mockMvc.perform(RestDocumentationRequestBuilders.multipart("/file/upload/boardImage")
							.file(validJpegFile))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andDo(document("file/uploadBoardImages/success/jpeg",
							getDocumentRequest(),
							getDocumentResponse(),
							requestParts(
									partWithName("file").description("업로드할 게시글 이미지 파일")
							),
							responseFields(
									fieldWithPath("code").description("코드"),
									fieldWithPath("message").description("메시지"),
									fieldWithPath("url").description("업로드된 게시글 이미지의 URL")
							)));
		}


		@Test
		@DisplayName("게시글 이미지 파일 업로드 - 성공 PNG")
		void uploadBoardImagePngTest() throws Exception {
			String fileName = "uploaded-board-image.png";
			String expectedUrl = BOARD_IMAGES_URL + fileName;
			FileResponse fileResponse = FileResponse.success(expectedUrl);
			ResponseEntity<FileResponse> responseEntity = ResponseEntity.ok(fileResponse);

			when(fileService.uploadBoardImage(any())).thenReturn(responseEntity);

			mockMvc.perform(RestDocumentationRequestBuilders.multipart("/file/upload/boardImage")
							.file(validJpegFile))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andDo(document("file/uploadBoardImages/success/png",
							getDocumentRequest(),
							getDocumentResponse(),
							requestParts(
									partWithName("file").description("업로드할 게시글 이미지 파일")
							),
							responseFields(
									fieldWithPath("code").description("코드"),
									fieldWithPath("message").description("메시지"),
									fieldWithPath("url").description("업로드된 게시글 이미지의 URL")
							)));
		}

		@Test
		@DisplayName("프로필 파일 업로드 - 실패 파일타입 일치X")
		void uploadProfileImageInvalidFileTypeTest() throws Exception {
			mockMvc.perform(RestDocumentationRequestBuilders.multipart("/file/upload/profileImage")
							.file(invalidFile))
					.andExpect(status().isBadRequest())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andDo(print())
					.andDo(document("file/uploadBoardImages/fail/invalidFileType",
							getDocumentRequest(),
							getDocumentResponse(),
							requestParts(
									partWithName("file").description("업로드할 잘못된 형식의 파일")
							),
							responseFields(
									fieldWithPath("code").description("에러 메시지"),
									fieldWithPath("message").description("에러 메시지")
							)));
		}

	}


	@Nested
	@DisplayName("프로필 이미지 업로드 테스트")
	class UploadProfileImage {

		MockMultipartFile validJpegFile;
		MockMultipartFile validPngFile;
		MockMultipartFile invalidFile;

		String PROFILE_URL = "http://localhost:8080/file/profile";
		String PROFILE_PATH = "/Users/gim-yeongjun/Desktop/2024-feedback/livefeedback/profileImages/";

		@BeforeEach
		void setUp() {
			validJpegFile = new MockMultipartFile(
					"file",
					"test.jpg",
					MediaType.IMAGE_JPEG_VALUE,
					"test image content".getBytes()
			);

			validPngFile = new MockMultipartFile(
					"file",
					"test.png",
					MediaType.IMAGE_PNG_VALUE,
					"test image content".getBytes()
			);

			invalidFile = new MockMultipartFile(
					"file",
					"test.txt",
					MediaType.TEXT_PLAIN_VALUE,
					"test content".getBytes()
			);
		}

		@Test
		@DisplayName("프로필 파일 업로드 - 성공 JPG")
		void uploadProfileImageValidJpegTest() throws Exception {
			String fileName = "uploaded-profile-image.jpg";
			String expectedUrl = PROFILE_URL + fileName;
			FileResponse fileResponse = FileResponse.success(expectedUrl);
			ResponseEntity<FileResponse> responseEntity = ResponseEntity.ok(fileResponse);

			when(fileService.uploadProfileImage(any())).thenReturn(responseEntity);

			mockMvc.perform(RestDocumentationRequestBuilders.multipart("/file/upload/profileImage")
							.file(validJpegFile))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andDo(document("file/uploadProfileImage/success/jpeg",
							getDocumentRequest(),
							getDocumentResponse(),
							requestParts(
									partWithName("file").description("업로드할 JPEG 프로필 이미지 파일")
							),
							responseFields(
									fieldWithPath("code").description("코드"),
									fieldWithPath("message").description("메시지"),
									fieldWithPath("url").description("업로드된 프로필 이미지의 URL")
							)));
		}

		@Test
		@DisplayName("프로필 파일 업로드 - 성공 PNG")
		void uploadProfileImageValidPngTest() throws Exception {
			String fileName = "uploaded-profile-image.png";
			String expectedUrl = PROFILE_URL + fileName;
			FileResponse fileResponse = FileResponse.success(expectedUrl);
			ResponseEntity<FileResponse> responseEntity = ResponseEntity.ok(fileResponse);

			when(fileService.uploadProfileImage(any())).thenReturn(responseEntity);

			mockMvc.perform(RestDocumentationRequestBuilders.multipart("/file/upload/profileImage")
							.file(validPngFile))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andDo(document("file/uploadProfileImage/success/png",
							getDocumentRequest(),
							getDocumentResponse(),
							requestParts(
									partWithName("file").description("업로드할 PNG 프로필 이미지 파일")
							),
							responseFields(
									fieldWithPath("code").description("코드"),
									fieldWithPath("message").description("메시지"),
									fieldWithPath("url").description("업로드된 프로필 이미지의 URL")
							)));
		}

		@Test
		@DisplayName("프로필 파일 업로드 - 실패 파일타입 일치X")
		void uploadProfileImageInvalidFileTypeTest() throws Exception {
			mockMvc.perform(RestDocumentationRequestBuilders.multipart("/file/upload/profileImage")
							.file(invalidFile))
					.andExpect(status().isBadRequest())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andDo(print())
					.andDo(document("file/uploadProfileImage/fail/invalidFileType",
							getDocumentRequest(),
							getDocumentResponse(),
							requestParts(
									partWithName("file").description("업로드할 잘못된 형식의 파일")
							),
							responseFields(
									fieldWithPath("code").description("에러 메시지"),
									fieldWithPath("message").description("에러 메시지")
							)));
		}

	}


}

