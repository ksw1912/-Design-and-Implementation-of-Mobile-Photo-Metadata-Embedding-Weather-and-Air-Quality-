package KakaoApi_RestAPI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;



public class CoordinateConvert {

	private static final String TM_URL = "https://dapi.kakao.com/v2/local/geo/transcoord.json";
    private static final String KakaoAuthentication = "KakaoAK " + "309e6277a09d31f2a88498fcfe4449a6";

    // 좌표로 변환하기 위한 메소드 (해당앱에서는 TM을사용해야함)
    public static HashMap<String, String> CoordinateTransformations(double x, double y, String input_coord, String output_coord) {
        HashMap<String, String> coordinates = new HashMap<>();
        
        try {
            String query = String.format("?x=%f&y=%f&input_coord=%s&output_coord=%s", x, y, input_coord, output_coord);
			URL url = new URL(TM_URL + query);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", KakaoAuthentication);
            con.setRequestProperty("content-type", "application/json");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), Charset.forName("UTF-8")));

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println("카카오api:"+con.getResponseCode());
            System.out.println(response.toString());
            JSONObject jsonResponse = (JSONObject) JSONValue.parse(response.toString());
            JSONObject meta = (JSONObject) jsonResponse.get("meta");
            JSONArray documents = (JSONArray) jsonResponse.get("documents");
            long size = (long) meta.get("total_count");
            
            if (size>0) {
                JSONObject document = (JSONObject) documents.get(0);
                String convertedX = document.get("x").toString();
                String convertedY = document.get("y").toString();
                coordinates.put("x", convertedX);
                coordinates.put("y", convertedY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coordinates;
    }

}