package Servlet;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CreateFileRoot {
	/*/upload 하위 폴더 경로 생성 */
	public static String createFilePath(String path) {
		LocalDateTime date = LocalDateTime.now();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		String[] paths = formatter.format(date).split("/");
		
		String result =path+ File.separator+ paths[0] + File.separator + paths[1] + File.separator + paths[2];
		
        File uploadDir = new File(result);
        
        if(uploadDir.exists()== false) {
         uploadDir.mkdirs();
      }
		return result; 
	}
}
