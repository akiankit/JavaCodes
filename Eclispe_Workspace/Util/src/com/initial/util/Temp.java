package com.initial.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Temp {

	public static void main(String[] args) throws Exception {
		File wordfile = new File("temp.pdf");
		File pdfFile = new File("C:\\Users\\ankit.k2\\Downloads\\temp.txt");
		InputStream is = new FileInputStream(pdfFile);
		OutputStream os = new FileOutputStream(wordfile);
		BufferedInputStream bis = new BufferedInputStream(is);
		BufferedOutputStream bos = new BufferedOutputStream(os);
		byte[] data = new byte[(int) pdfFile.length()];
		bis.read(data);
		bos.write(data);
		bos.flush();
		bos.close();
		os.close();
		is.close();
		bis.close();
	}

}
