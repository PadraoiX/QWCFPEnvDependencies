package br.com.pix.qware.qwcfp.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class NormalizeFileName 
{
	private String acentuados   = "áéíóúâêîôûãẽĩõũàèìòùçüäëïöü";
	private String normalizados = "aeiouaeiouaeiouaeioucuaeiou";
	private MessageDigest md = null; 
	
	public String get( String fileName )
	{
		int i, l, p;
		
		StringBuffer sb = new StringBuffer();
		fileName = fileName.toLowerCase().replaceAll("\\s+", " ");
		
		for( i = 0, l = fileName.length(); i < l; i++ )
		{
			char c = fileName.charAt(i);
			p = acentuados.indexOf(c);
			if( p >= 0 )
				sb.append( normalizados.charAt(p) );
			else
				sb.append(c);
		}
		
		try 
		{
			if( md == null )
				md = MessageDigest.getInstance("MD5");
			
			md.reset();
			
	        byte[] array = md.digest(sb.toString().getBytes());

	        StringBuffer md5 = new StringBuffer();
	        
	        for ( i = 0; i < array.length; ++i) {
	          md5.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
	        }
	        
	        return md5.toString();
		} 

		catch (NoSuchAlgorithmException e) 
		{
			return sb.toString();
		}
	
	}
	
}
