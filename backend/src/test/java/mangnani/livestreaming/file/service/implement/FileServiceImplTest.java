package mangnani.livestreaming.file.service.implement;


import mangnani.livestreaming.file.dto.response.FileResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {

	@InjectMocks
	private FileServiceImpl fileService;

	@Mock
	private MultipartFile multipartFile;

	private final String BOARD_IMAGE_PATH = "/test/boardImages/";
	private final String BOARD_IMAGE_URL = "/test/boardImages/";
	private final String PROFILE_PATH = "/test/boardImages/";
	private final String PROFILE_URL = "/test/boardImages/";
	private final String THUMBNAIL_PATH = "/test/boardImages/";
	private final String THUMBNAIL_URL = "/test/boardImages/";



	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@DisplayName("게시글 이미지 업로드 - 성공")
	@Test
	void uploadBoardImageTest() {
		MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());

		ResponseEntity<FileResponse> response = fileService.uploadBoardImage(file);

	}

}