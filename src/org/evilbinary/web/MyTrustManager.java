package org.evilbinary.web;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public  class MyTrustManager implements X509TrustManager {
    
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    	
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    	 System.out.println("cert: " + chain[0].toString() + ", 认证方式: " + authType);
    }

    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[]{};
    }
}

