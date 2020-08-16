package svancar.hoval.challenge.utils;

import org.springframework.web.multipart.MultipartFile;
import svancar.hoval.challenge.exception.exceptions.FileValidationException;

import java.util.Arrays;

import static org.apache.http.entity.ContentType.IMAGE_JPEG;
import static org.apache.http.entity.ContentType.IMAGE_PNG;

public class FileUtils {

    /**
     * Validates file content type. Allowed types are currently: JPEG, PNG
     * @param file
     */
    public static void ensureFileTypeIsValid(MultipartFile file) {
        if(!Arrays.asList(IMAGE_JPEG.getMimeType(), IMAGE_PNG.getMimeType()).contains(file.getContentType())) {
            throw new FileValidationException("File is not an image. File type: " + file.getContentType());
        }
    }

    /**
     * Check if file is not empty and throws the exception if it is
     * @param file
     */
    public static void ensureFileNotEmpty(MultipartFile file) {
        if(file.isEmpty()) {
            throw new FileValidationException("Cannot upload an empty file [ " + file.getSize() + "]");
        }
    }
}
