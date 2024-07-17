package mangnani.livestreaming.file.service.implement;

import java.io.File;
import java.util.UUID;
import mangnani.livestreaming.file.dto.response.FileResponse;
import mangnani.livestreaming.file.service.FileService;
import org.hibernate.validator.cfg.defs.UUIDDef;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {

	@Value("${spring.file.boardImagesPath}")
	private String BOARD_IMAGE_PATH;

	@Value("${spring.file.boardImagesUrl}")
	private String BOARD_IMAGE_URL;

	@Value("${spring.file.profilePath}")
	private String PROFILE_PATH;

	@Value("${spring.file.profileUrl}")
	private String PROFILE_URL;

	@Value("${spring.file.thumbnailUrl}")
	private String THUMBNAIL_URL;

	@Value("${spring.file.thumbnailPath}")
	private String thumbnailPath;

	@Override
	public ResponseEntity<FileResponse> uploadBoardImage(MultipartFile file) {
		FileResponse fileResponse = uploadFile(file, BOARD_IMAGE_PATH, BOARD_IMAGE_URL);
		return ResponseEntity.ok().body(fileResponse);
	}

	@Override
	public ResponseEntity<FileResponse> uploadProfileImage(MultipartFile file) {
		FileResponse fileResponse = uploadFile(file, PROFILE_PATH, PROFILE_URL);
		return ResponseEntity.ok().body(fileResponse);
	}

	@Override
	public String getThumbnailUrl(String fileName) {
		return getUrl(THUMBNAIL_URL, thumbnailPath);
	}


	@Override
	public Resource getBoardImages(String fileName) {
		return getImage(BOARD_IMAGE_PATH, fileName);
	}

	@Override
	public Resource getThumbnail(String fileName) {
		return getImage(thumbnailPath, THUMBNAIL_URL);
	}

	private FileResponse uploadFile(MultipartFile file, String filePath, String fileUrl) {
		if (file.isEmpty()) {
			return null;
		}

		String originalFileName = file.getOriginalFilename();
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		String uuid = UUID.randomUUID().toString();
		String saveFileName = uuid + extension;
		String savePath = filePath + saveFileName;

		try {
			file.transferTo(new File(savePath));
		} catch (Exception exception) {
			return null;
		}

		return FileResponse.success(fileUrl + saveFileName);
	}


	private Resource getImage(String filePath,String fileName) {
		try {
			return new UrlResource("file:" + filePath + fileName);
		} catch (Exception e) {
			return null;
		}
	}

	private String getUrl(String fileUrl, String fileName) {
		return fileUrl + fileName;
	}

}




