package Information;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

public class Dust_Measurement_Station {

    // 미세먼지 측정소 정보를 조회하는 메소드
	 public static HashMap<String, String> getNearbyMeasurementStation(String tmX, String tmY) throws IOException {
		 HashMap<String, String> getMeasurementStation = new HashMap<>();
		
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552584/MsrstnInfoInqireSvc/getNearbyMsrstnList");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=Cb9svkpTc7OgRdH2ER7NQw9BhksARZnQFt7K2z3kzqQX7nq8rQq%2B0hljyCbuxX2sR0c156pZSZRf%2FC5y5svhUQ%3D%3D"); 
        urlBuilder.append("&" + URLEncoder.encode("returnType", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("tmX", "UTF-8") + "=" + URLEncoder.encode(tmX, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("tmY", "UTF-8") + "=" + URLEncoder.encode(tmY, "UTF-8"));
        //urlBuilder.append("&" + URLEncoder.encode("ver", "UTF-8") + "=" + URLEncoder.encode("1.2", "UTF-8"));
        
//        String url = "http://apis.data.go.kr/B552584/MsrstnInfoInqireSvc/getNearbyMsrstnList";
//        url += "?serviceKey =" + "cbXg8We05JmqLuOAT2GngTEHJUqcV9IbGEUhY9%2FCxwrlNQqq0ahYjbLEDw%2F958pnWevRvEWlsdgHBM6PdSFP5Q%3D%3D";
//        url += "&returnType=json"; // 리턴 타입
//		url += "&numOfRows=10"; // 결과 개수
		URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        
        System.out.println("공공데이터 포털 미세먼지 측정소:"+conn.getResponseCode());
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());
       
        //json 데이터 불러오기
        JSONObject jsonResponse = (JSONObject) JSONValue.parse(sb.toString());
        if (jsonResponse == null) {
            System.out.println("공공데이터 포털 데이터 문제로 json을 추출 불가");
        } else {
            JSONObject rp = (JSONObject) jsonResponse.get("response");
            JSONObject body = (JSONObject) rp.get("body");
            JSONArray items = (JSONArray) body.get("items");
            
          
                JSONObject item = (JSONObject) items.get(0);
                String stationName = item.get("stationName").toString();
                String tm = item.get("tm").toString();
                System.out.println(stationName);
                getMeasurementStation.put("stationName", stationName);
        }        
        return getMeasurementStation;
   
    }
}