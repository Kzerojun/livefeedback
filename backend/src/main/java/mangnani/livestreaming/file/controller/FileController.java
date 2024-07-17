package mangnani.livestreaming.file.controller;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mangnani.livestreaming.file.dto.response.FileResponse;
import mangnani.livestreaming.file.exception.InvalidFileTypeException;
import mangnani.livestreaming.file.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

	private final FileService fileService;
	private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList("image/jpeg", "image/png");

	@PostMapping("/upload/boardImage")
	private ResponseEntity<FileResponse> uploadBoardImages(@RequestParam("file") MultipartFile file) {
		if (!isValidFileType(file)) {
			throw new InvalidFileTypeException();
		}

		return fileService.uploadBoardImage(file);
	}

	@PostMapping("/upload/profileImage")
	private ResponseEntity<FileResponse> uploadProfileImage(@RequestParam("file") MultipartFile file) {
		if (!isValidFileType(file)) {
			throw new InvalidFileTypeException();
		}
		return fileService.uploadProfileImage(file);
	}

	@GetMapping(value = "/thumbnail/{fileName}", produces = {
			MediaType.IMAGE_JPEG_VALUE,
			MediaType.IMAGE_PNG_VALUE})
	public Resource getThumbnail(@PathVariable("fileName") String fileName) {
		return fileService.getThumbnail(fileName);
	}

	@GetMapping(value = "/board/{fileName}", produces = {
			MediaType.IMAGE_JPEG_VALUE,
			MediaType.IMAGE_PNG_VALUE})
	public Resource getBoardImages(@PathVariable("fileName") String fileName) {
		return fileService.getBoardImages(fileName);
	}


	private boolean isValidFileType(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			return false;
		}
		String contentType = file.getContentType();
		return contentType != null && ALLOWED_CONTENT_TYPES.contains(contentType);
	}
}
