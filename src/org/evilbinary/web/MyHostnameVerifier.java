package org.evilbinary.web;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class MyHostnameVerifier implements HostnameVerifier {
	 
	public boolean verify(String hostname, SSLSession session) {
		  System.out.println("hostname:" + hostname);
        return true;
    }

 
}