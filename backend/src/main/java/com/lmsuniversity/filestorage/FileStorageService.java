package com.lmsuniversity.filestorage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FileStorageService {

	private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
			"image/jpeg", "image/png", "image/webp", "image/gif");

	@Value("${app.upload.dir}")
	private String uploadDir;

	@Value("${app.upload.max-image-size-bytes}")
	private long maxImageSizeBytes;

	public String storeImage(MultipartFile file, String subfolder) {
		if (file == null || file.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No file was provided.");
		}
		if (!ALLOWED_IMAGE_TYPES.contains(file.getContentType())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Unsupported file type. Allowed types: JPEG, PNG, WEBP, GIF.");
		}
		if (file.getSize() > maxImageSizeBytes) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"File is too large. Maximum allowed size is " + (maxImageSizeBytes / (1024 * 1024)) + "MB.");
		}

		String filename = UUID.randomUUID() + getExtension(file.getOriginalFilename());

		try {
			Path targetDir = Paths.get(uploadDir, subfolder).toAbsolutePath().normalize();
			Files.createDirectories(targetDir);
			file.transferTo(targetDir.resolve(filename));
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store the uploaded file.", e);
		}

		return "/" + uploadDir + "/" + subfolder + "/" + filename;
	}

	public void deleteIfExists(String webPath) {
		if (webPath == null || webPath.isBlank()) {
			return;
		}
		try {
			String relative = webPath.startsWith("/") ? webPath.substring(1) : webPath;
			Files.deleteIfExists(Paths.get(relative).toAbsolutePath().normalize());
		} catch (IOException e) {
			// best-effort cleanup of the previous file; failing to delete it should not block the request
		}
	}

	private String getExtension(String originalFilename) {
		if (originalFilename == null) {
			return "";
		}
		int dotIndex = originalFilename.lastIndexOf('.');
		return dotIndex >= 0 ? originalFilename.substring(dotIndex) : "";
	}
}
