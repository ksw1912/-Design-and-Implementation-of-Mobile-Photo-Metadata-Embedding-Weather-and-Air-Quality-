package Servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import KakaoApi_RestAPI.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.json.simple.JSONObject;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import Exif_Libray.EXIF_GPS;
import Exif_Libray.EXIF_JsonData;
import Exif_Libray.EXIF_Tag_Add;
import Information.Dust_Measurement_Station;
import Information.Dust_Measuring_station_activity_measurement_information;



@WebServlet("/upload")
public class MainControl extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private String imageFilePath; // 전역 변수로 선언
	private String fileName;	
	protected void doPost(HttpServletRequest request,HttpServletResponse response) 
			throws ServletException, IOException {
//		response.setHeader("Access-Control-Allow-Origin", "*"); // 모든 도메인에서의 요청을 허용
//		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS"); // 허용할 HTTP 메소드 지정
//		response.setHeader("Access-Control-Allow-Headers", "Content-Type"); // 클라이언트가 서버로 전송할 수 있는 헤더 지정
		request.setCharacterEncoding("UTF-8");
//	
		//서버 경로
		String path=getServletContext().getRealPath("/images");
        File imagesDir = new File(path);

		//년//월//일//이미지.jpg경로형식 폴더 생성
		String total_path = CreateFileRoot.createFilePath(path);
		
		System.out.println("서버파일 경로확인:"+total_path);
	
		int size = 10*1024*1024; 
		
		//파일 업로드
//		@SuppressWarnings("unused")
//		MultipartRequest multi = new MultipartRequest(request,total_path,size,"UTF-8",new DefaultFileRenamePolicy());
//		String filename = multi.getFilesystemName("uploadfile");
//		System.out.println("파일명:"+filename);
//		//특정 파일 이미지 경로   
//        String imageFilePath = total_path +"/"+filename;
		
		  DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
	        fileItemFactory.setRepository(imagesDir);
	        ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);
	        
	        List<FileItem> items = fileUpload.parseRequest(new ServletRequestContext(request));
            for (FileItem item : items) {
                if (item.isFormField()) {
                    System.out.printf("파라미터 명 : %s, 파라미터 값 :  %s \n", item.getFieldName(), item.getString("UTF-8"));
                } else {
                    System.out.printf("파라미터 명 : %s, 파일 명 : %s,  파일 크기 : %s bytes \n", item.getFieldName(),
                            item.getName(), item.getSize());
                    if (item.getSize() > 0) {
                        String separator = File.separator;
                        int index = item.getName().lastIndexOf(separator);
                        String fileName = item.getName().substring(index + 1);
                        File uploadFile = new File(path + separator + fileName);
                        
                     // 이미 같은 이름의 파일이 존재하는지 확인
                        int fileCount = 0;
                        String baseFileName = fileName;
                        while (uploadFile.exists()) {
                            // 파일명에 숫자를 추가하여 중복 처리
                            fileCount++;
                            int dotIndex = fileName.lastIndexOf(".");
                            String nameWithoutExtension = fileName.substring(0, dotIndex);
                            String fileExtension = fileName.substring(dotIndex);
                            fileName = nameWithoutExtension + "_" + fileCount + fileExtension;
                            uploadFile = new File(path + separator + fileName);
                        }
                        
                        try {
							item.write(uploadFile);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        imageFilePath = uploadFile.getPath();
                        fileName = uploadFile.getName();
                        System.out.println(imageFilePath);
                        
                 
                    }
                }
            }
        //exif gps 추출
        List<Double> gpsCoordinate = EXIF_GPS.conVertGps(imageFilePath);

        double x = gpsCoordinate.get(0);
        double y = gpsCoordinate.get(1);	

        //좌표 바꾸기 위한 좌표명 
        final String input_coord ="WGS84"; 
        final String output_coord ="TM"; 
        
		//TM 좌표계 변환(x,y,현재좌표계,바꾸기위한좌표계)
        HashMap<String,String> TMGetCoordinate = CoordinateConvert.CoordinateTransformations(x,y, input_coord, output_coord);
        String tmX = TMGetCoordinate.get("x");
        String tmY = TMGetCoordinate.get("y");
         
        //측정소 먼지 정보 불러오기
 		HashMap<String,String> Dust_Value = Dust_Measurement_Station.getNearbyMeasurementStation(tmX,tmY);
        String nearest_measuring_station = Dust_Value.get("stationName");
        @SuppressWarnings("unused")
		String nearest_TM = Dust_Value.get("tm");
       
        HashMap<String, String> info = Dust_Measuring_station_activity_measurement_information.Dust_Information(nearest_measuring_station);
        	
        String pm10 = info.get("PM10Value");
        String pm25 = info.get("PM25Value");             
        
        System.out.println(pm10);
        System.out.println(pm25);

        
        //exif 태그에넣기 
	    try {
	    	EXIF_Tag_Add.modifyExif(imageFilePath,pm10,pm25);
		} catch (ImageReadException | ImageWriteException | IOException e) {
			e.printStackTrace();
		}
	   
	 // 클라이언트에게 이미지의 경로를 전달
	    response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(imageFilePath);
	}	
}