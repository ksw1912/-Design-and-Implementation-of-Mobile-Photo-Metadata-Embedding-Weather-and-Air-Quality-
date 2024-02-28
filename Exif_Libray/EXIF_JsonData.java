package Exif_Libray;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.ImageMetadata.ImageMetadataItem;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class EXIF_JsonData {
    
    // 메타데이터를 json 형식으로 변경 key: value
    @SuppressWarnings("unchecked")
    public static JSONObject MetadataJson(String imageFilePath) throws ImageReadException, IOException {
        File file = new File(imageFilePath);   
        // 이미지 메타데이터 읽기
        ImageMetadata metadata = Imaging.getMetadata(file);  
        JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;

        HashMap<String, String> hashmap = new HashMap<>(); 
        List<ImageMetadataItem> ImageMetadata = jpegMetadata.getItems();
        if (ImageMetadata != null) {
            for (int i = 0; i < ImageMetadata.size(); i++) {
                String[] split = String.valueOf(ImageMetadata.get(i)).split(":", 2);
                if (split.length == 2) {
                    hashmap.put(split[0], split[1]);
                }
            }
            JSONObject data = new JSONObject();
            for (String key : hashmap.keySet()) {
                String value = hashmap.get(key);
                data.put(key, value);
            }   
            JSONArray meta = new JSONArray();
            meta.add(data);

            JSONObject jsonmake = new JSONObject();
            jsonmake.put("Metadata", meta);
            return jsonmake;
        }
        // 데이터가 없을 경우
        return new JSONObject();
    }
}
