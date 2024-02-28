package Servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.imaging.ImageReadException;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import Exif_Libray.EXIF_JsonData;

@WebServlet("/Exif")
public class ExifOffer extends HttpServlet{
 
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8");
    	
    	StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        
        String jsonData = stringBuilder.toString();
        JSONObject jsonResponse = (JSONObject) JSONValue.parse(jsonData);
        String ImageFilePath = (String) jsonResponse.get("path");
       
   
    	JSONObject metadatas = null;
		try {
			metadatas = EXIF_JsonData.MetadataJson(ImageFilePath);
			
		} catch (ImageReadException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    	PrintWriter out = response.getWriter();
    	response.setContentType("application/json");
    	response.setCharacterEncoding("UTF-8");
    	System.out.println(metadatas);
    	out.print(metadatas);
    	out.flush();
        
    }    
    
}