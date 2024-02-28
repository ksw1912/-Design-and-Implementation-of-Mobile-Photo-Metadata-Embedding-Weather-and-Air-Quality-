package Exif_Libray;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.RationalNumber;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants;


public class EXIF_GPS {
	
	public static List<Double> conVertGps(String imageFilePath) {
		List<Double> arr = new ArrayList<>(); 

        File file = new File(imageFilePath);                    
        try {
            ImageMetadata metadata = Imaging.getMetadata(file);
            if (metadata instanceof JpegImageMetadata) {
                JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
                
                final TiffField gpsLatitudeRefField = jpegMetadata.findEXIFValueWithExactMatch(GpsTagConstants.GPS_TAG_GPS_LATITUDE_REF);
                final TiffField gpsLatitudeField = jpegMetadata.findEXIFValueWithExactMatch(GpsTagConstants.GPS_TAG_GPS_LATITUDE);
                final TiffField gpsLongitudeRefField = jpegMetadata.findEXIFValueWithExactMatch(GpsTagConstants.GPS_TAG_GPS_LONGITUDE_REF);
                final TiffField gpsLongitudeField = jpegMetadata.findEXIFValueWithExactMatch(GpsTagConstants.GPS_TAG_GPS_LONGITUDE);
                
                if (gpsLatitudeRefField != null && gpsLatitudeField != null && gpsLongitudeRefField != null && gpsLongitudeField != null) {
                    // 경도,위도 좌표 변환
                	double longitude = convert((RationalNumber[]) gpsLongitudeField.getValue());
                    double latitude = convert((RationalNumber[]) gpsLatitudeField.getValue());
                   
                    System.out.println("경도: " + longitude);
                    System.out.println("위도: " + latitude);
                    
                    arr.add(longitude);
                    arr.add(latitude);
                                       
                }
            }
        } catch (ImageReadException | IOException e) {
            e.printStackTrace();
        }
        return arr;
    }
    
    // 좌표로 변환 공식 메소드
    private static double convert(RationalNumber[] rationalNumbers) {
        double degrees = rationalNumbers[0].doubleValue();
        double minutes = rationalNumbers[1].doubleValue() / 60.0;
        double seconds = rationalNumbers[2].doubleValue() / 3600.0;
        return degrees + minutes + seconds;
    }
    
}