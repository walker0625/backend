package kr.lifesemantics.canofymd.modulecore.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import kr.lifesemantics.canofymd.modulecore.exception.BusinessException;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Rotation;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtil {

    public static void saveOriginal(MultipartFile image, String originalImagePath) {
        try {
            if (FileUtil.makePath(new File(originalImagePath))) {
                File target = new File(originalImagePath);
                image.transferTo(target);
            }
        } catch (Exception e) {
            e.printStackTrace();
            FileUtil.deleteFile(originalImagePath);
            throw new BusinessException(ResponseStatus.FAILED_DIAGNOSE_IMAGE_SAVE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static void saveThumbnail(MultipartFile image, String thumbnailImagePath) {

        File saveImage = new File(thumbnailImagePath);
        BufferedImage buffer_original_image;

        try {
            if(FileUtil.makePath(new File(thumbnailImagePath))) {

                int thumbnail_width = 200;
                int thumbnail_height = 200;

                buffer_original_image = ImageIO.read(image.getInputStream());

                double imgWidth = buffer_original_image.getWidth();
                double imgHeight = buffer_original_image.getHeight();
                thumbnail_width = (int)(imgWidth * 0.5);
                thumbnail_height = (int)(imgHeight * 0.5);

                int imgType = (buffer_original_image.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
                BufferedImage buffer_thumbnail_image = new BufferedImage(thumbnail_width, thumbnail_height, imgType);

                Graphics2D graphic = buffer_thumbnail_image.createGraphics();
                graphic.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                graphic.drawImage(buffer_original_image, 0, 0, thumbnail_width, thumbnail_height, null);

                Rotation rotation = getRotation(getOrientation(image));

                if(rotation != null) {
                    BufferedImage rotatedImage = Scalr.rotate(buffer_thumbnail_image, rotation, null);
                    ImageIO.write(rotatedImage, "png", saveImage);
                } else {
                    ImageIO.write(buffer_thumbnail_image, "png", saveImage);
                }

                graphic.dispose();
            } else {
                log.error("makePath fail :: {}", thumbnailImagePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
            FileUtil.deleteFile(saveImage.getPath());
            throw new BusinessException(ResponseStatus.FAILED_DIAGNOSE_THUMBNAIL_IMAGE_SAVE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static int getOrientation(MultipartFile multipartFile) throws Exception {
        int orientation = 1;
        Metadata imageMeta = ImageMetadataReader.readMetadata(multipartFile.getInputStream());
        Directory directory = imageMeta.getFirstDirectoryOfType(ExifIFD0Directory.class);
        if(directory != null) {
            try {
                orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
            } catch (MetadataException e) {
                orientation = 0;
            }
        }
        return orientation;
    }

    private static Rotation getRotation(int orientation) {
        switch (orientation) {
            case 3:
                return Rotation.CW_180;
            case 6:
                return Rotation.CW_90;
            case 8:
                return Rotation.CW_270;
            default:
                return null;
        }
    }

	public static boolean makePath(File f) throws IOException {
		if(f.exists()) {
			return true;
		} else {
			return f.mkdirs();
		}
	}

    public static void deleteFile(String path) {
        File deleteFile = new File(path);
        deleteFile.delete();
    }

}
