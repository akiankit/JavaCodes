package com.bluestone.app.payment.gateways.amex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLSocketFactory;

import com.sun.net.ssl.SSLContext;
import com.sun.net.ssl.X509TrustManager;
                

/**
 * Helper class to perform 2 party Virtual Payment Client transactions.
 * The helper functions contained in this class are able to generate the post data
 * to be sent to the Payment Server for processing, as well as determining whether 
 * data has been returned in the response to the transaction request.
 */
@Deprecated
public class VPCPaymentConnection {
    
    /** Creates a new instance of VPCPaymentConnection */
    public VPCPaymentConnection() {
    }
    
    /**
     * Declare a static X509 trust manager.
     */
    public static X509TrustManager s_x509TrustManager = null;
    /**
     * Declare a static ssl Socket Factory.
     */
    public static SSLSocketFactory s_sslSocketFactory = null;
    
    static {
            s_x509TrustManager = new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[] {}; } 
            public boolean isClientTrusted(X509Certificate[] chain) { return true; } 
            public boolean isServerTrusted(X509Certificate[] chain) { return true; } 
        };

        java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        try {
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[] { s_x509TrustManager }, null);
            s_sslSocketFactory = context.getSocketFactory();
        } catch (Exception e) {            
            throw new RuntimeException(e.getMessage());
        }
    }
    
    /**
     * Helper function to generate and send the POST data for a 2 party VPC txn.
     * @param vpc_Host is the host where the 2 party transaction request is being sent to.
     * @param data is the data being sent in the POST request.
     * @return the response data for the 2 party transaction request.
     * @throws java.io.IOException when unable to perform the POST request for the 2 party transaction.
     */
	public static String doPost(String vpc_Host, String data) throws Exception {

		InputStream is = null;
		OutputStream os = null;
		int vpc_Port = 443;
		String fileName = "";
		boolean useSSL = false;
		String body = "";
		Socket s = null;

		try {
			// determine if SSL encryption is being used
			if (vpc_Host.substring(0, 8).equalsIgnoreCase("HTTPS://")) {
				useSSL = true;
				// remove 'HTTPS://' from host URL
				vpc_Host = vpc_Host.substring(8);
				// get the filename from the last section of vpc_URL
				fileName = vpc_Host.substring(vpc_Host.lastIndexOf("/"));
				// get the IP address of the VPC machine
				vpc_Host = vpc_Host.substring(0, vpc_Host.lastIndexOf("/"));
			}
			// use next block of code if using SSL encryption
			if (useSSL) {
				s = s_sslSocketFactory.createSocket(vpc_Host, vpc_Port);
				os = s.getOutputStream();
				is = s.getInputStream();
				// use next block of code if NOT using SSL encryption
			} else {				
				s = new Socket(vpc_Host, vpc_Port);
				os = s.getOutputStream();
				is = s.getInputStream();
			}
			String req = "POST " + fileName + " HTTP/1.0\r\n" + "User-Agent: HTTP Client\r\n" + "Content-Type: application/x-www-form-urlencoded\r\n" + "Content-Length: " + data.length() + "\r\n\r\n" + data;

			os.write(req.getBytes());
			String res = new String(readAll(is));

			// check if a successful connection
			if (res.indexOf("200") < 0) {
				throw new IOException("Connection Refused - " + res);
			}

			if (res.indexOf("404 Not Found") > 0) {
				throw new IOException("File Not Found Error - " + res);
			}

			int resIndex = res.indexOf("\r\n\r\n");
			body = res.substring(resIndex + 4, res.length());
		} catch (Exception e) {
			throw e;
		} finally {
			if(is!=null){
				is.close();
			}
			if(os!=null){
				os.close();
			}
			if(s!=null){
				s.close();
			}
		}
		return body;
	}
    
    /**
     * Helper function to read the response data.
     * @param is the response data returned.
     * @return a byte array of the input data.
     * @throws java.io.IOException when it is unable to read the response data.
     */
    public static byte[] readAll(InputStream is) throws IOException {
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try{        	
            byte[] buf = new byte[1024];
            
            while (true) {
                int len = is.read(buf);
                if (len < 0) {
                    break;
                }
                baos.write(buf, 0, len);
            }
            byte[] byteArray = baos.toByteArray();
            return byteArray;
        }finally{
        	baos.close();
        }
        
    }        
        
}
