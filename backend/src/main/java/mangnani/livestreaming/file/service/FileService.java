package mangnani.livestreaming.file.service;

import mangnani.livestreaming.file.dto.response.FileResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	ResponseEntity<FileResponse> uploadBoardImage(MultipartFile file);

	ResponseEntity<FileResponse> uploadProfileImage(MultipartFile file);

	Resource getBoardImages(String fileName);

	Resource getThumbnail(String fileName);

	String getThumbnailUrl(String fileName);
}
