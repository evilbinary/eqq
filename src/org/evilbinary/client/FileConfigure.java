package org.evilbinary.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.text.html.HTMLDocument.Iterator;

public class FileConfigure {
	private Map<String, String> map;
	private String file;

	public FileConfigure() {
		file = "protocl.ini";
		map = new HashMap<String, String>();
	}

	public FileConfigure(String file) {
		this.file = file;
		map = new HashMap<String, String>();
	}

	public Map<String, String> getMap() {
		return this.map;
	}

	public String get(String key) {
		return map.get(key);
	}

	public void put(String key, String value) {
		map.put(key, value);
	}

	public void saveConfigure() {
		try {
			FileWriter fw = new FileWriter(file);
			BufferedWriter br = new BufferedWriter(fw);
			String line = "";
			Set<Entry<String, String>> set = map.entrySet();
			java.util.Iterator<Entry<String, String>> ite = set.iterator();
			while (ite.hasNext()) {
				Entry<String, String> en = ite.next();
				line = en.getKey() + "=" + en.getValue();
				br.write(line);
				br.newLine();
			}
			br.close();
			fw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void print() {
		Set<Entry<String, String>> set = map.entrySet();
		java.util.Iterator<Entry<String, String>> ite = set.iterator();
		String line = "";
		while (ite.hasNext()) {
			Entry<String, String> en = ite.next();
			line = en.getKey() + "=" + en.getValue();
			System.out.println(line);
		}
	}

	public void saveConfigure(String fileName) {
		try {
			BufferedWriter br = new BufferedWriter(new FileWriter(fileName));
			String line = "";
			Set<Entry<String, String>> set = map.entrySet();
			java.util.Iterator<Entry<String, String>> ite = set.iterator();
			while (ite.hasNext()) {
				Entry<String, String> en = ite.next();
				line = en.getKey() + "=" + en.getValue();
			}
			br.write(line);
			br.newLine();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void parseFile() {
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				parseLine(line);
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void parseLine(String line) {
		String key, value;
		line.trim();
		line = URLDecoder.decode(line);
		if (line == null || line.isEmpty() || line.length() <= 2) {
			return;
		} else if (line.charAt(0) == '#') {
			return;
		} else {
			int pos = line.indexOf('=');
			key = line.substring(0, pos);
			value = line.substring(pos + 1);
			map.put(key, value);
			// System.out.println(key);
			// System.out.println(value);
		}
		// System.out.println();
	}
}
