package org.evilbinary.client;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	private String fileName;
	private String buffer;
	private int logNumber;
	private static int MAXBUFSIZE = 3;
	private String newline;
	private boolean isEcho;

	
	public boolean isEcho() {
		return isEcho;
	}

	public void setEcho(boolean isEcho) {
		this.isEcho = isEcho;
	}

	public Logger(String fileName) {
		this.fileName = fileName;
		buffer = new String();
		logNumber = 0;
		isEcho=false;
		newline = System.getProperty("line.separator");
	}
 
	public void debug(String info) {
		Date date = new Date();
		SimpleDateFormat fmt = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		write("DEBUG "+fmt.format(date) + " " + info);
		if(isEcho)
		System.out.println("DEBUG "+fmt.format(date) + " " + info);
	}

	public void warn(String info) {
		Date date = new Date();
		SimpleDateFormat fmt = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		write("WARN "+fmt.format(date) + " " + info);
		if(isEcho)
		System.out.println("WARN "+fmt.format(date) + " " + info);
	}

	public void fatal(String info) {
		Date date = new Date();
		SimpleDateFormat fmt = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		write("FATAL "+fmt.format(date) + " " + info);
		if(isEcho)
		System.out.println("FATAL "+fmt.format(date) + " " + info);
	}

	public void log(String log) {
		Date date = new Date();
		SimpleDateFormat fmt = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		write("LOG " + fmt.format(date) + " " + log);
		if(isEcho)
		System.out.println("LOG "+fmt.format(date)+" "+ log);
	}

	private synchronized void write(String info) {
		try {
			logNumber++;
			buffer += info;
			buffer += newline;
			if (logNumber >= MAXBUFSIZE) {
				FileOutputStream fos = new FileOutputStream(fileName, true);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				PrintStream out = new PrintStream(bos);
				out.print(buffer);
				out.close();
				bos.close();
				fos.close();
//				System.out.println("INFO save log "+buffer);
				logNumber = 0;
				buffer = "";
			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	public void test() {
		for (int i = 0; i < 10; i++) {
			log(i + "test log.");
		}
	}

	private synchronized void read() {

	}
}
