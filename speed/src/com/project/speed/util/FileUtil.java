package com.project.speed.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtil {
	
	private static String getDir(String path){
		int index = path.lastIndexOf('/');
		if (index < 0){
			index = path.lastIndexOf('\\');
		}
		return path.substring(0, index);
	}
	
	public static boolean exists(String path){
		return new File(path).exists();
	}
	
	public static boolean copy(InputStream is, OutputStream os) throws IOException{
		if (is == null || os == null){
			return false;
		}
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = is.read(buffer)) != -1) {
			os.write(buffer, 0, len);
		}
		is.close();
		os.close();
		return true;
	}
	
	
	public static void cut(String from, String to) throws IOException{
		File outputFile = new File(getDir(to));
		if (!outputFile.exists()){
			outputFile.mkdirs(); 
		}
		copy(new FileInputStream(from), new FileOutputStream(to));
		deleteFile(from);
	}
	
	public static void copy(String from, String to) throws IOException {
		File outputFile = new File(getDir(to));
		if (!outputFile.exists()){
			outputFile.mkdirs(); 
		}
		copy(new FileInputStream(from), new FileOutputStream(to));
	}	
	
	public static void copyResFile(String from, String to) throws IOException {
		File outputFile = new File(getDir(to));
		if (!outputFile.exists()){
			outputFile.mkdirs(); 
		}
		InputStream is = FileUtil.class.getResourceAsStream(from);
		if (null == is){
			is = new FileInputStream(from.substring(1));
		}
		copy(is, new FileOutputStream(to));
	}	
	
	public static void deleteFile(String file) throws IOException {
		new File(file).delete();
	}
	
	public static void unZip(String zipFile ,String descDir){
		File pathFile = new File(descDir);  
        if(!pathFile.exists()){  
            pathFile.mkdirs();  
        }  

		try {
			ZipFile zip = new ZipFile(zipFile);
			Enumeration<? extends ZipEntry> it = zip.entries();
			while (it.hasMoreElements()){
				ZipEntry entry = (ZipEntry)it.nextElement();  
				String zipEntryName = entry.getName();
				String outPath = (descDir + "/" + zipEntryName).replaceAll("\\*", "/");
				System.out.println(outPath);
	            if(entry.isDirectory()){
	            	File folder = new File(outPath);
	            	if (!folder.exists()){
						folder.mkdirs();
					}
	            }else{
					File folder = new File(getDir(outPath));
					if (!folder.exists()){
						folder.mkdirs();
					}
					copy(zip.getInputStream(entry), new FileOutputStream(outPath));
	            }
				
			}
			zip.close();
		} catch (IOException e) {
			e.printStackTrace();
		}  
        System.out.println("******************解压完毕********************"); 
	}
	
	public static String readText(String file, String charSet) throws IOException{
		RandomAccessFile rf = new RandomAccessFile(file, "r");
		byte[] mem = new byte[(int) rf.length()];
		rf.read(mem);
		rf.close();
		return new String(mem, charSet);
	}
	
	public static void setText(String fileName, String text, String charSet) throws IOException{
		File outputFile = new File(fileName);
		FileOutputStream outputStream = new FileOutputStream(outputFile);
		outputStream.write(text.getBytes(charSet));
		outputStream.close();
	}
}