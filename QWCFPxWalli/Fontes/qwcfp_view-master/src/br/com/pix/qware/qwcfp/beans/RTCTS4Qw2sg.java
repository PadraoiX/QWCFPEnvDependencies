package br.com.pix.qware.qwcfp.beans;

import java.io.File;

public interface RTCTS4Qw2sg {
	public static final String CATALOG_FILE_NAME = "QW2SG_SERVCES_CATALOG";
	public static final String DIR4XML_FILE_NAME = ".";
	public static final String EXT_CAT_FILE_NAME = ".xml";
	public static final String CATFULL_FILE_NAME = DIR4XML_FILE_NAME 
												 + File.separator 
												 + CATALOG_FILE_NAME 
												 + EXT_CAT_FILE_NAME;
	
	public static final String QW2SG_RESOURCE_PATH = "/" 
													 + CATALOG_FILE_NAME 
													 + EXT_CAT_FILE_NAME; 
}
