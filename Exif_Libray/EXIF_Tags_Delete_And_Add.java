package Exif_Libray;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class EXIF_Tags_Delete_And_Add {
    public static void modifyExifDateTimeDigitized(String imagePath, String newDateTime) throws IOException, ImageReadException, ImageWriteException {
        File jpegImageFile = new File(imagePath);
        // 임시 파일 생성
        File newFile = File.createTempFile("temp_", ".jpg");

        JpegImageMetadata jpegMetadata = (JpegImageMetadata) Imaging.getMetadata(jpegImageFile);
        if (jpegMetadata != null) {
            TiffImageMetadata exif = jpegMetadata.getExif();
            if (exif != null) {
                TiffOutputSet outputSet = exif.getOutputSet();
                if (outputSet == null) {
                    outputSet = new TiffOutputSet();
                }
                final TiffOutputDirectory exifDirectory = outputSet.getOrCreateExifDirectory();
                outputSet.getOrCreateExifDirectory().removeField(ExifTagConstants.EXIF_TAG_DATE_TIME_DIGITIZED);
                outputSet.getOrCreateExifDirectory().add(ExifTagConstants.EXIF_TAG_DATE_TIME_DIGITIZED, newDateTime);

                try (FileOutputStream fos = new FileOutputStream(newFile)) {
                    new ExifRewriter().updateExifMetadataLossless(jpegImageFile, fos, outputSet);
                }
                // 원본 파일을 임시 파일로 대체
                Files.move(newFile.toPath(), jpegImageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                
            }
        }
    }

    public static void main(String[] args) {
        try {
            String imagePath = "C:/Users/user/Desktop/exif7.jpg";
            String newDateTime = "1970:01:01 12:34:56";
            modifyExifDateTimeDigitized(imagePath, newDateTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
