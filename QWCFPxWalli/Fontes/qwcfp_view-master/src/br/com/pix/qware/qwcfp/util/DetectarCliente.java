package br.com.pix.qware.qwcfp.util;
import java.util.regex.Matcher;  
import java.util.regex.Pattern;  
import javax.faces.context.FacesContext;  
  
public class DetectarCliente {  
      
    private final static String MOBILE_DEVICE_DETECTION_PARAM = "primefaces.mobile.DEVICE_DETECTION";  
      
    public static boolean ClienteMovel(String userAgent) {  
        Pattern pattern = Pattern.compile("(iPhone|iPad|iPod|Android|BlackBerry|Opera Mobi|Opera Mini|IEMobile)");  
        Matcher matcher = pattern.matcher(userAgent);  
          
        return matcher.find();  
    }  
      
}  