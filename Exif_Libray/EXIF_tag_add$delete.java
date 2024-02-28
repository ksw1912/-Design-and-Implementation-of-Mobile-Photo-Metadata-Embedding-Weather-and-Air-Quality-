package Exif_Libray;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;

public class EXIF_tag_add$delete {
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
                exifDirectory.removeField(ExifTagConstants.EXIF_TAG_USER_COMMENT);
                exifDirectory.add(ExifTagConstants.EXIF_TAG_USER_COMMENT, newDateTime);

                try (FileOutputStream fos = new FileOutputStream(newFile)) {
                    new ExifRewriter().updateExifMetadataLossless(jpegImageFile, fos, outputSet);
                }
                // 원본 파일을 임시 파일로 대체
                Files.move(newFile.toPath(), jpegImageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                
            }
        }
    }
}
