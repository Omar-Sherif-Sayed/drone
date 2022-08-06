package com.drone.util;

import com.drone.error.ApplicationFailed;
import io.vavr.control.Either;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class ImageUtil {

    private ImageUtil() {
    }

    private static final Logger logger = LogManager.getLogger(ImageUtil.class);

    public static final String UPLOAD_DIR = "src/main/resources/static/images";
    public static final String UPLOAD_FOLDER = "images";

    public static void upload(String fileName, MultipartFile multipartFile) {

        var uploadPath = Paths.get(UPLOAD_DIR);

        try {

            if (!Files.exists(uploadPath))
                Files.createDirectories(uploadPath);

            var inputStream = multipartFile.getInputStream();
            var filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error("Error in ImageUtil in upload()", e);
        }
    }

    public static Either<ApplicationFailed, String> fileExtension(MultipartFile image) {
        var originalFilename = image.getOriginalFilename();
        if (originalFilename == null)
            return Either.left(new ApplicationFailed.Required("image name"));

        var originalFilenameSplitLength = originalFilename.split("\\.").length;
        if (originalFilenameSplitLength < 2)
            return Either.left(new ApplicationFailed.Required("image extension"));

        var fileExtension = originalFilename.split("\\.")[originalFilenameSplitLength - 1];
        List<String> validExtensions = List.of("PNG", "JPG", "GIF");

        if (!validExtensions.contains(fileExtension.toUpperCase()))
            return Either.left(new ApplicationFailed.NotSupported("image extension"));

        return Either.right(fileExtension);
    }

}
