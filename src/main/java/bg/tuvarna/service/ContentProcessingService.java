package bg.tuvarna.service;

import java.io.File;
import java.io.IOException;

public interface ContentProcessingService {
    String process(File file, String fileName) throws IOException;

    File convertToWebP(File file) throws IOException;

    void createAndUploadThumbnail(File file, String fileName) throws IOException;

    String processAndUploadVideo(File inputFile, String fileName) throws IOException;
}
