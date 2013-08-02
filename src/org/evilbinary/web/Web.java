/*
 * Create By EvilBinary 小E
 * 2011-10-10
 * rootntsd@gmail.com
 */
package org.evilbinary.web;

import java.awt.Image;
import java.io.*;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.xml.ws.http.HTTPException;

public class Web {
	private URL url;
	private HttpURLConnection urlConnection;
	private StringBuffer stringBuffer;
	private byte[] buffer;
	private String strUrl;
	private String parameters;
	private String postData;

	private List<String> cookies;
	private String referer;
	private String contentType;
	private String aceeptCharset;
	private String requestEncoding;
	private String accept;
	private String acceptEncoding;
	private String acceptLanguage;
	private String userAgent;
	private String host;

	private static int connectTimeOut = 10500;
	private static int readTimeOut = 11150000;
	private static Random random = new Random();

	public Web() {
		stringBuffer = new StringBuffer();
		userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.202 Safari/535.1";
		accept = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
		acceptEncoding = "gzip,deflate,sdch";
		acceptLanguage = "utf-8,gbk;q=0.7,*;q=0.3";
		host = "";
		contentType = "text/html";
	}

	public Web(String urls) {
		stringBuffer = new StringBuffer();
		strUrl = urls;
		parameters = "";
		cookies = new ArrayList<String>();
		contentType = "text/html";// "application/x-www-form-urlencoded"
		requestEncoding = "GBK";
		aceeptCharset = "utf-8";

		userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.202 Safari/535.1";
		accept = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
		acceptEncoding = "gzip,deflate,sdch";
		acceptLanguage = "utf-8;q=0.7,*;q=0.3";
		host = "";
	}

	public String boundary() {
		return "----WebKitFormBoundary" + randomString() + randomString() + randomString();
	}

	public String multipartFormateData(Map<String, String> postData) {
		StringBuffer buf = new StringBuffer();
		try {
			for (Iterator<?> iter = postData.entrySet().iterator(); iter.hasNext();) {
				Entry<?, ?> element = (Entry<?, ?>) iter.next();
				buf.append("------WebKitFormBoundary" + randomString() + randomString() + randomString());
				buf.append("\r\n");
				buf.append("Content-Disposition: form-data; name=\"" + element.getKey().toString() + "\"\r\n\r\n");

				buf.append(URLEncoder.encode(element.getValue().toString(), requestEncoding));

				buf.append("\r\n");
			}
			return buf.toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public void initUrl(String stringUrl) {
		try {
			url = new URL(stringUrl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("url: " + queryUrl + " paras: " + parameters);
	}

	private void open() {
		try {

			initUrl(strUrl + parameters);
			urlConnection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void openPost() {
		try {

			initUrl(strUrl);
			urlConnection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setParameters(String params) {
		parameters = "?" + params;
	}

	public void setParameters(String[][] params) {
		StringBuffer buf = new StringBuffer("?");
		for (int i = 0; i < params.length; i++) {
			buf.append(params[i][0] + "=" + params[i][1] + "&");
		}
		if (buf.length() > 0) {
			buf = buf.deleteCharAt(buf.length() - 1);
		}
		parameters = buf.toString();
		// System.out.println(this.strUrl+parameters);
	}

	public void setPostParamters(String[][] params) {
		// StringBuffer buf = new StringBuffer("?");
		// for (int i = 0; i < params.length; i++) {
		// buf.append(params[i][0] + "=" + params[i][1] + "&");
		// }
		// if (buf.length() > 0) {
		// buf = buf.deleteCharAt(buf.length() - 1);
		// }
		// this.postData = buf.toString();
	}

	public void setParameters(Map<?, ?> params) {
		StringBuffer buf = new StringBuffer("?");
		try {
			for (Iterator<?> iter = params.entrySet().iterator(); iter.hasNext();) {
				Entry<?, ?> element = (Entry<?, ?>) iter.next();
				buf.append(element.getKey().toString());
				buf.append("=");

				buf.append(URLEncoder.encode(element.getValue().toString(), requestEncoding));
				buf.append("&");
			}
			if (buf.length() > 0) {
				buf = buf.deleteCharAt(buf.length() - 1);
			}
			parameters += buf;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setPostData(String postData) {
		this.postData = postData;
	}

	public void setPostData(Map<?, ?> postData) {
		StringBuffer buf = new StringBuffer("&");
		try {
			for (Iterator<?> iter = postData.entrySet().iterator(); iter.hasNext();) {
				Entry<?, ?> element = (Entry<?, ?>) iter.next();
				buf.append(element.getKey().toString());
				buf.append("=");

				buf.append(URLEncoder.encode(element.getValue().toString(), requestEncoding));
				buf.append("&");
			}
			if (buf.length() > 0) {
				buf = buf.deleteCharAt(buf.length() - 1);
			}
			this.postData += buf;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getImage() {
		try {
			open();
			urlConnection.setRequestMethod("GET");
			urlConnection.setConnectTimeout(Web.connectTimeOut);
			urlConnection.setReadTimeout(Web.readTimeOut);
			urlConnection.setRequestProperty("Content-type", this.contentType);
			urlConnection.setRequestProperty("Connection", "Keep-Alive");
			urlConnection.setRequestProperty("Accept;", "*/*");
			urlConnection.setRequestProperty("Accept-Charset", this.aceeptCharset);
			InputStream is = urlConnection.getInputStream();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int len = 0;
			while ((len = is.read(buf)) != -1) {
				os.write(buf, 0, len);
			}
			buffer = os.toByteArray();
			os.close();
			cookies = urlConnection.getHeaderFields().get("Set-Cookie");

			int code = urlConnection.getResponseCode();
//			System.out.println("code:" + code);
//			System.out.println(urlConnection.getResponseMessage());
			switch (code) {
			case HttpURLConnection.HTTP_NOT_IMPLEMENTED:

				break;
			case HttpURLConnection.HTTP_ACCEPTED:

				break;
			}
			is.close();

		} catch (HTTPException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
	}
	public void getImage(List<String> lcookies) {
		try {
			open();
			String c = "";
			for (String s : lcookies) {
				c += s;
			}
			urlConnection.setRequestMethod("GET");
			urlConnection.setConnectTimeout(Web.connectTimeOut);
			urlConnection.setReadTimeout(Web.readTimeOut);
			urlConnection.setRequestProperty("Content-type", this.contentType);
			urlConnection.setRequestProperty("Connection", "Keep-Alive");
			urlConnection.setRequestProperty("Accept;", "*/*");
			urlConnection.setRequestProperty("Accept-Charset", this.aceeptCharset);
			urlConnection.setRequestProperty("Cookie", c);
			InputStream is = urlConnection.getInputStream();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int len = 0;
			while ((len = is.read(buf)) != -1) {
				os.write(buf, 0, len);
			}
			buffer = os.toByteArray();
			os.close();
			cookies = urlConnection.getHeaderFields().get("Set-Cookie");

			int code = urlConnection.getResponseCode();
//			System.out.println("code:" + code);
//			System.out.println(urlConnection.getResponseMessage());
			switch (code) {
			case HttpURLConnection.HTTP_NOT_IMPLEMENTED:

				break;
			case HttpURLConnection.HTTP_ACCEPTED:

				break;
			}
			is.close();

		} catch (HTTPException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
	}
	
	public void getWeb() {
		try {
			open();

			urlConnection.setRequestMethod("GET");
			urlConnection.setConnectTimeout(Web.connectTimeOut);
			urlConnection.setReadTimeout(Web.readTimeOut);
			urlConnection.setRequestProperty("Content-type", this.contentType);
//			urlConnection.setRequestProperty("Connection", "Keep-Alive");
//			urlConnection.setRequestProperty("Accept;", this.accept);
//			urlConnection.setRequestProperty("Accept-Charset", this.aceeptCharset);
//			urlConnection.setRequestProperty("Accept-Encoding", this.acceptEncoding);
//			urlConnection.setRequestProperty("Accept-Language", this.acceptLanguage);
			urlConnection.setRequestProperty("Keep-Alive", "3000");
			if (referer != null && !referer.equals(""))
				urlConnection.setRequestProperty("Referer", referer);

			urlConnection.setDoOutput(true);
			urlConnection.connect();
			// System.out.println(cookies);
			// System.out.println("Length:" + urlConnection.getContentLength());
			InputStream is = urlConnection.getInputStream();
			String str = null;
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			while ((str = in.readLine()) != null) {
				// print(lineStr);
				stringBuffer.append(str + "\n");
			}
			// System.out.println( stringBuffer);

			cookies = urlConnection.getHeaderFields().get("Set-Cookie");

			int code = urlConnection.getResponseCode();
			// System.out.println("code:" + code);
			// System.out.println(urlConnection.getResponseMessage());
			switch (code) {
			case HttpURLConnection.HTTP_NOT_IMPLEMENTED:

				break;
			case HttpURLConnection.HTTP_ACCEPTED:

				break;
			}
			in.close();
			is.close();

		} catch (HTTPException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
	}

	public void getWeb(List<String> lcookies) {
		try {
			open();
			String c = "";
			for (String s : lcookies) {
				c += s;
			}
			urlConnection.setRequestMethod("GET");
			urlConnection.setConnectTimeout(Web.connectTimeOut);
			urlConnection.setReadTimeout(Web.readTimeOut);
			urlConnection.setRequestProperty("Content-type", this.contentType);
//			urlConnection.setRequestProperty("Connection", "Keep-Alive");
//			urlConnection.setRequestProperty("Accept;", this.accept);
//			urlConnection.setRequestProperty("Accept-Charset", this.aceeptCharset);
//			urlConnection.setRequestProperty("Accept-Encoding", this.acceptEncoding);
//			urlConnection.setRequestProperty("Accept-Language", this.acceptLanguage);
//			urlConnection.setRequestProperty("Keep-Alive", "3000");
			if (referer != null && !referer.equals(""))
				urlConnection.setRequestProperty("Referer", referer);

			urlConnection.setRequestProperty("Cookie", c);
			urlConnection.setDoOutput(true);
			urlConnection.connect();

			// System.out.println(cookies);
			// System.out.println("Length:" + urlConnection.getContentLength());
			InputStream is = urlConnection.getInputStream();
			String str = null;
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			while ((str = in.readLine()) != null) {
				// print(lineStr);
				stringBuffer.append(str + "\n");
			}
			// System.out.println( stringBuffer);
			cookies=urlConnection.getHeaderFields().get("Set-Cookie");
//			cookies.add(urlConnection.getHeaderFields().get("Set-Cookie").toString());
//			 System.out.println(cookies.get(0));

			int code = urlConnection.getResponseCode();
			// System.out.println("code:" + code);
//			System.out.println(urlConnection.getResponseMessage());
			switch (code) {
			case HttpURLConnection.HTTP_NOT_IMPLEMENTED:

				break;
			case HttpURLConnection.HTTP_ACCEPTED:

				break;
			}
			in.close();
			is.close();

		} catch (HTTPException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println(e.getMessage());
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
	}

	public List<String> getCookies() {

		return cookies;
	}

	public void printWeb() {
		try {
			System.out.println(new String(new String(stringBuffer).getBytes(), "gbk"));

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void printWeb(String contentEncode) {
		try {
			System.out.println(new String(new String(stringBuffer).getBytes(), contentEncode));

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void saveImage(String fileName) {
		File file = new File(fileName);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			fos.write(buffer);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public byte[] getBuffer() {
		return buffer;
	}
	public void clearImageBuffer(){
		this.buffer=null;
	}

 

	public void saveWeb(String fileName) {
		File file = new File(fileName);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			String s = new String(stringBuffer);
			fos.write(s.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void saveWeb(String fileName, String charset) {
		File file = new File(fileName);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			String s = new String(stringBuffer);
			s = new String(s.getBytes(), charset);
			fos.write(s.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void test() {
		String homeUrlStr = "http://hi.baidu.com/evilbinary/home";
		URL homeUrl = null;
		HttpURLConnection homeConn = null;
		try {
			homeUrl = new URL(homeUrlStr);
			homeConn = (HttpURLConnection) homeUrl.openConnection();
			homeConn.setRequestMethod("GET");
			homeConn.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(homeConn.getInputStream()));
			String lineStr = null;
			while ((lineStr = in.readLine()) != null) {
				// print(lineStr);
				System.out.println(lineStr);
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void httpsWeb(String method) {
		BufferedInputStream httpsBufIns = null;
		BufferedOutputStream fileBufOus = null;
		BufferedOutputStream hurlBufOus = null;
		try {
			SSLContext sslContext = SSLContext.getInstance("SSL"); // SSL TLS
			sslContext.init(null, new TrustManager[] { new MyTrustManager() }, new java.security.SecureRandom());
			URL console = new URL(this.strUrl + parameters);
			if (sslContext != null) {
				HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
			}
			// HttpsURLConnection.setDefaultHostnameVerifier(new
			// MyHostnameVerifier());
			HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
			conn.setSSLSocketFactory(sslContext.getSocketFactory());
			conn.setHostnameVerifier(new MyHostnameVerifier());
			// conn.setRequestProperty("User-Agent",
			// "Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)");
			conn.setRequestProperty("Content-Type", this.contentType);
			// conn.setRequestProperty("Content-type", "text/html");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Accept;", "*/*");
			conn.setRequestProperty("Accept-Charset", this.aceeptCharset);
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.10)"
					+ " Gecko/2009042316 Firefox/3.0.10");
			conn.setRequestProperty("Keep-Alive", "3000");

			conn.setFollowRedirects(true);
			conn.setInstanceFollowRedirects(true);
			conn.setAllowUserInteraction(false);
			conn.setUseCaches(false);
			conn.setDoInput(true);
			if (this.postData != null && !"".equals(this.postData) && "POST".equals(method)) {
				conn.setDoOutput(true);
				OutputStream os = conn.getOutputStream();
				os.write(this.postData.getBytes());
				os.flush();
				os.close();
			} else {
				conn.setRequestMethod(method);
			}
			conn.connect();
			System.out.println("Method:" + conn.getRequestMethod());
			// String cookie = conn.getHeaderField("Set-Cookie");
			for (String s : urlConnection.getHeaderFields().get("Set-Cookie")) {
				cookies.add(s);
			}

			// System.out.println("cookies:" + cookies);
			System.out.println("code:" + conn.getResponseCode());
			String str = null;
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((str = in.readLine()) != null) {
				stringBuffer.append(str + "\n");
			}

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void postWeb() {
		try {

			openPost();
			urlConnection.setRequestMethod("POST");
			urlConnection.setConnectTimeout(Web.connectTimeOut);
			urlConnection.setReadTimeout(Web.readTimeOut);
			if (referer != null && !referer.equals(""))
				urlConnection.setRequestProperty("Referer", referer);
			if (!"".equals(this.contentType))
				urlConnection.setRequestProperty("Content-type", this.contentType);

			urlConnection.setRequestProperty("Content-type", this.contentType);
			urlConnection.setRequestProperty("Connection", "Keep-Alive");
			urlConnection.setRequestProperty("Accept;", this.accept);
			urlConnection.setRequestProperty("Accept-Charset", this.aceeptCharset);
			urlConnection.setRequestProperty("Accept-Encoding", this.acceptEncoding);
			urlConnection.setRequestProperty("Accept-Language", this.acceptLanguage);
			urlConnection.setRequestProperty("User-Agent", userAgent);
			urlConnection.setRequestProperty("Keep-Alive", "3000");
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			OutputStream os = urlConnection.getOutputStream();
			os.write(postData.getBytes());
			os.close();
			urlConnection.connect();
			cookies = urlConnection.getHeaderFields().get("Set-Cookie");
			// System.out.println(cookies);
//			System.out.println("Length:" + urlConnection.getContentLength());

			urlConnection.connect();

			InputStream is = urlConnection.getInputStream();

			String str = null;
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			while ((str = in.readLine()) != null) {
				// print(lineStr);
				stringBuffer.append(str + "\n");
			}
			// System.out.println( stringBuffer);

			int code = urlConnection.getResponseCode();
			// System.out.println("code:" + code);
			// System.out.println(urlConnection.getResponseMessage());
			switch (code) {
			case HttpURLConnection.HTTP_NOT_IMPLEMENTED:

				break;
			case HttpURLConnection.HTTP_ACCEPTED:

				break;
			}
			in.close();
			is.close();

		} catch (HTTPException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
	}

	public void clearParamters() {
		this.parameters = "";
	}

	public void postWeb(List<String> lcookies) {
		try {
			String c = "";
			for (String s : lcookies) {
				c += s;
			}
			openPost();
			urlConnection.setRequestMethod("POST");
			urlConnection.setConnectTimeout(Web.connectTimeOut);
			urlConnection.setReadTimeout(Web.readTimeOut);
			if (referer != null && !referer.equals(""))
				urlConnection.setRequestProperty("Referer", referer);
			if (!"".equals(this.contentType))
				urlConnection.setRequestProperty("Content-type", this.contentType);
//
//			urlConnection.setRequestProperty("Connection", "Keep-Alive");
//			urlConnection.setRequestProperty("Accept;", this.accept);
//			urlConnection.setRequestProperty("Accept-Charset", this.aceeptCharset);
//			urlConnection.setRequestProperty("Accept-Encoding", this.acceptEncoding);
//			urlConnection.setRequestProperty("Accept-Language", this.acceptLanguage);
//			urlConnection.setRequestProperty("User-Agent", userAgent);
//			urlConnection.setRequestProperty("Keep-Alive", "3000");
			urlConnection.setRequestProperty("Cookie", c);
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);

			urlConnection.connect();
			OutputStream os = urlConnection.getOutputStream();
			// PrintWriter out=new PrintWriter(urlConnection.getOutputStream());
			// out.print(postData);
			// out.close();
			os.write(postData.getBytes());
			os.flush();
			os.close();
			// System.out.println(postData);
//			System.out.println(urlConnection.getResponseCode() + " " + urlConnection.getResponseMessage());
			InputStream is = urlConnection.getInputStream();
			if (is == null) {
				System.out.println("inputstream is null");
			}
			String str = null;
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			while ((str = in.readLine()) != null) {
				// print(lineStr);
				stringBuffer.append(str + "\n");
			}
			// System.out.println( stringBuffer);
			cookies = urlConnection.getHeaderFields().get("Set-Cookie");
			int code = urlConnection.getResponseCode();

			switch (code) {
			case HttpURLConnection.HTTP_NOT_IMPLEMENTED:

				break;
			case HttpURLConnection.HTTP_ACCEPTED:

				break;
			}
			in.close();
			is.close();

		} catch (HTTPException e) {
			System.out.println(e.getMessage());
			// e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			// e.printStackTrace();
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
	}

	public void printCookies() {
		if (cookies == null) {
			System.out.println("cookies is null.");
			return;
		}
		for (int i = 0; i < cookies.size(); i++) {
			System.out.println(cookies.get(i));
		}
	}

	public String getAceeptCharset() {
		return aceeptCharset;
	}

	public void setAceeptCharset(String aceeptCharset) {
		this.aceeptCharset = aceeptCharset;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public void clearBuffer() {
		stringBuffer.delete(0, stringBuffer.length());
		// stringBuffer=new StringBuffer();
	}

	private static String randomString() {
		return Long.toString(random.nextLong(), 36);
	}

	public String getWebContent() {
		return stringBuffer.toString();
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getStrUrl() {
		return strUrl;
	}

	public void setStrUrl(String strUrl) {
		this.strUrl = strUrl;
	}

	public void printOpenGetUrl() {
		System.out.println(strUrl + parameters);
	}

	public void printOpenPostUrl() {
		System.out.println(strUrl);
	}
}
