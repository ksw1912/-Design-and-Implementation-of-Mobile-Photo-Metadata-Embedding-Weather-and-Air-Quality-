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

public class Dust_Measuring_station_activity_measurement_information {
	
	public static HashMap<String, String> Dust_Information(String station_name) {
        HashMap<String, String> getMeasurementStationinfo = new HashMap<>();
		try {
			 StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty");
			 	urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "="+"Cb9svkpTc7OgRdH2ER7NQw9BhksARZnQFt7K2z3kzqQX7nq8rQq%2B0hljyCbuxX2sR0c156pZSZRf%2FC5y5svhUQ%3D%3D"); /*Service Key*/; 
			 	urlBuilder.append("&" + URLEncoder.encode("returnType","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*xml 또는 json*/
			 	urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*한 페이지 결과 수*/
			 	urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
			    urlBuilder.append("&" + URLEncoder.encode("stationName","UTF-8") + "=" + URLEncoder.encode(station_name, "UTF-8")); /*측정소 이름*/
			    urlBuilder.append("&" + URLEncoder.encode("dataTerm","UTF-8") + "=" + URLEncoder.encode("DAILY", "UTF-8")); /*요청 데이터기간(1일: DAILY, 1개월: MONTH, 3개월:*/
			    urlBuilder.append("&" + URLEncoder.encode("ver","UTF-8") + "=" + URLEncoder.encode("1.5", "UTF-8")); /*버전 1.5*/
				URL url = new URL(urlBuilder.toString());
		        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		        conn.setRequestMethod("GET");
		        conn.setRequestProperty("Content-type", "application/json");
		        
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
		        System.out.println("공공데이터 포털 특정 측정소: "+ conn.getResponseCode());
		        rd.close();
		        conn.disconnect();
		        System.out.println(sb.toString());
		        
		        //json 데이터 불러오기
		        JSONObject jsonResponse = (JSONObject) JSONValue.parse(sb.toString());
		    	if (jsonResponse == null) {
		            System.out.println("공공데이터 포털 데이터 문제로 json을 추출 불가");
		        }
		    	JSONObject rp = (JSONObject) jsonResponse.get("response");
		    	JSONObject body = (JSONObject) rp.get("body");
		    	JSONArray items = (JSONArray) body.get("items");
		    	JSONObject item = (JSONObject) items.get(0);
		    	
		    	String PM10Value = item.get("pm10Value").toString();
		      	String PM25Value = item.get("pm25Value").toString();
		    	
		    	if(PM25Value.equals("-")) {
		    		PM25Value = "0";
		        }
		        if(PM10Value.equals("-")) {
		        	PM10Value = "0";
		        }
		        getMeasurementStationinfo.put("PM10Value", PM10Value);		    	
		    	getMeasurementStationinfo.put("PM25Value", PM25Value);
		    	getMeasurementStationinfo.get("PM10Value");
		    	getMeasurementStationinfo.get("PM25Value");
		    	System.out.println("PM10: "+PM10Value);
		    	System.out.println("PM2.5: "+PM25Value);
		    	
		    }catch (IOException e) {
		       e.getMessage();
		    }
		return getMeasurementStationinfo;
		    	
	 }		   
}
