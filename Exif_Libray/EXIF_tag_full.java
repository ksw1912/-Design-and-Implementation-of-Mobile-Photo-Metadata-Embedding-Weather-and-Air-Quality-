package Exif_Libray;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.ImageMetadata.ImageMetadataItem;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.iptc.IptcBlock;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class EXIF_tag_full {

	public static void ExifTagFullOffer(String imageFilePath) {
		HashMap<String, String> ExifTagInfo = new HashMap<>();
        File file = new File(imageFilePath);
        try {
        	//이미지 메타데이터 읽기
            ImageMetadata metadata = Imaging.getMetadata(file);
	
	            if (metadata instanceof JpegImageMetadata) {
	                JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
	              //전체 태그 출력(offset include)
//	                printAllExifMetadata(jpegMetadata);
//	                System.out.println();
	                // 전체  태그 출력
	                printAllExifMetadata_Simple(jpegMetadata);
	                System.out.println();

	              } 
        }catch (ImageReadException | IOException e) {
                e.printStackTrace();
            }
        
	}
	//메타데이터 전체 태그 출력 메소드(모든 단위 정보 포함)
		public static void printAllExifMetadata(JpegImageMetadata jpegMetadata) throws ImageReadException {
	        final TiffImageMetadata exifMetadata = jpegMetadata.getExif();
	        if (exifMetadata != null) {
	            for (final TiffField meta : exifMetadata.getAllFields()) {
				    System.out.println(meta);
				}
	        }  
		}
		  // 메타데이터 전체 태그 출력 메소드(키 :값)
	    public static void printAllExifMetadata_Simple(JpegImageMetadata jpegMetadata) {
	        List<ImageMetadataItem>  ImageMetadata = jpegMetadata.getItems();
	        if (ImageMetadata != null) {
	        	for(int i = 0; i<ImageMetadata.size(); i++) {	
	        		System.out.println(ImageMetadata.get(i));
	            }
	        }
	    }
}
