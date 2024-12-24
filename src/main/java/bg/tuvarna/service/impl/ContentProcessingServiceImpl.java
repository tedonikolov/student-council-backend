package bg.tuvarna.service.impl;

import bg.tuvarna.service.ContentProcessingService;
import bg.tuvarna.service.S3Service;
import com.luciad.imageio.webp.WebPWriteParam;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.tika.Tika;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@ApplicationScoped
public class ContentProcessingServiceImpl implements ContentProcessingService {
    @Inject
    S3Service s3Service;

    public String process(File file, String fileName) throws IOException {
        try {
            String mimeType = detectFileType(file.toPath());

            if (mimeType != null) {
                if (mimeType.startsWith("image")) {
                    if (!mimeType.contains("webp")) { //should check if there is a created thumbnail as well
                        File webPFile = convertToWebP(file);
                        fileName = fileName.replaceFirst("\\.[^.]+$", ".webp");
                        s3Service.deleteFile(fileName);
                        s3Service.uploadToS3(webPFile, fileName);
                        //TODO here add retry content processing  logic
                        try {
                            createAndUploadThumbnail(webPFile, fileName);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        fileName = "images/" + fileName;
                        webPFile.delete();
                    }
                } else if (mimeType.startsWith("video")) {
                    s3Service.deleteFile(fileName);
                    fileName = "videos/" + fileName;
                    processAndUploadVideo(file, fileName);
                } else {
                    System.out.println("Unknown file type: " + mimeType);
                }
            } else {
                System.out.println("Could not determine the file type.");
            }
            Files.deleteIfExists(file.toPath());
            return fileName;
        } catch (Exception e) {
            Files.deleteIfExists(file.toPath());
            e.printStackTrace();
            return null;
        }
    }

    private String detectFileType(Path filePath) {
        Tika tika = new Tika();
        try {
            return tika.detect(filePath.toFile());
        } catch (IOException e) {
            System.out.println("Tika failed to determine file format: " + e.getMessage());
            e.printStackTrace();
            return "unknown";
        }
    }

    public File convertToWebP(File inputFile) throws IOException {
        BufferedImage image = ImageIO.read(inputFile);

        File webPFile = new File("converted.webp");
        ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();

        try (ImageOutputStream ios = ImageIO.createImageOutputStream(webPFile)) {
            writer.setOutput(ios);
            WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
            writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            if (writeParam.getCompressionTypes() != null && writeParam.getCompressionTypes().length > 0) {
                writeParam.setCompressionType(writeParam.getCompressionTypes()[0]);
            }
            writeParam.setCompressionQuality(0.9f);

            writer.write(null, new javax.imageio.IIOImage(image, null, null), writeParam);
        }

        return webPFile;
    }

    public void createAndUploadThumbnail(File file, String key) throws IOException {
        final int THUMBNAIL_WIDTH = 120;
        final int THUMBNAIL_HEIGHT = 120;
        key = "thumbnails/" + key;

        BufferedImage originalImage = ImageIO.read(file);
        BufferedImage thumbnail = new BufferedImage(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = thumbnail.createGraphics();
        g.drawImage(originalImage.getScaledInstance(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, Image.SCALE_SMOOTH), 0, 0, null);
        g.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(thumbnail, "webp", baos);
        baos.flush();
        byte[] thumbnailBytes = baos.toByteArray();
        baos.close();
        s3Service.uploadThumbnail(thumbnailBytes, key);
    }

    public String processAndUploadVideo(File inputFile, String key) throws IOException {
        File outputFile = new File("output.mp4");
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputFile)) {
            grabber.start();

            int width = grabber.getImageWidth();
            int height = grabber.getImageHeight();
            int audioChannels = grabber.getAudioChannels();
            double frameRate = grabber.getFrameRate();
            int videoBitrate = grabber.getVideoBitrate();

            FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputFile, width, height, audioChannels);
            recorder.setFormat("mp4");
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264); //H264 format
            recorder.setFrameRate(frameRate);
            recorder.setVideoBitrate(videoBitrate);
            recorder.setAudioChannels(audioChannels);
            recorder.setSampleRate(grabber.getSampleRate());

            recorder.start();

            long maxDuration = 30 * 1_000_000;
            while (grabber.getTimestamp() < maxDuration) {
                Frame frame = grabber.grabFrame();
                if (frame == null) {
                    break;
                }
                recorder.record(frame);
            }
            recorder.stop();
            grabber.stop();
        } catch (Exception e) {
            e.printStackTrace();
            outputFile.delete();
            throw e;
        }
        s3Service.uploadToS3(outputFile, key);
        outputFile.delete();
        return key;
    }
}
