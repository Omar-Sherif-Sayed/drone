package com.drone.service;

import com.drone.error.ApplicationFailed;
import com.drone.util.ImageUtil;
import io.vavr.control.Either;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    @Value("${server.port}")
    private String serverPort;

    @Value("${server.servlet.context-path}")
    private String serverContextPath;

    public String uploadAndGetUrl(MultipartFile image, String fileExtension, Long itemId) {
        var filename = itemId + "." + fileExtension;
        ImageUtil.upload(filename, image);
        return "http://localhost:" + serverPort + serverContextPath + "/" + ImageUtil.UPLOAD_FOLDER + "/" + filename;
    }

    public Either<ApplicationFailed, String> validateImageAndGetExtension(MultipartFile image) {

        if (image.getSize() > 2000000)
            return Either.left(new ApplicationFailed.NotSupported("image size"));

        var fileExtension = ImageUtil.fileExtension(image);
        if (fileExtension.isLeft())
            return Either.left(fileExtension.getLeft());
        else
            return Either.right(fileExtension.get());
    }

}
