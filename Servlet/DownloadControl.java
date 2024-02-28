package Servlet;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@WebServlet("/download")
public class DownloadControl extends HttpServlet{
	private static final long serialVersionUID = 1L;


	 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 response.setHeader("Access-Control-Allow-Origin", "*");
		    response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		    response.setHeader("Access-Control-Allow-Headers", "Content-Type");	
		 request.setCharacterEncoding("UTF-8");

		        StringBuilder stringBuilder = new StringBuilder();
		        try (BufferedReader reader = request.getReader()) {
		            String line;
		            while ((line = reader.readLine()) != null) {
		                stringBuilder.append(line);
		            }
		        }

		        // 읽어온 JSON 데이터 출력
		        String jsonData = stringBuilder.toString();
		        System.out.println("Received JSON data: " + jsonData);
		        JSONObject jsonResponse = (JSONObject) JSONValue.parse(jsonData);
		         String path= (String) jsonResponse.get("imagePath");
		       
	        String fileName = "downloaded_image.jpg";
	        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

	        // 이미지 파일을 읽어 클라이언트에게 전송
	        try (InputStream in = new BufferedInputStream(new FileInputStream(path));
	             ServletOutputStream out = response.getOutputStream()) {

	            byte[] buffer = new byte[4096];
	            int bytesRead;
	            while ((bytesRead = in.read(buffer)) != -1) {
	                out.write(buffer, 0, bytesRead);
	            }
	        }catch(Exception e) {
	        	e.printStackTrace();	        
	        	}
	    }
}
