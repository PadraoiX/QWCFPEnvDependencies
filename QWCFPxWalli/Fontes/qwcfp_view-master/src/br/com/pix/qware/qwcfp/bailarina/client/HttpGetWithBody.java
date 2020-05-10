package br.com.pix.qware.qwcfp.bailarina.client;

import java.net.URI;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

public class HttpGetWithBody extends HttpEntityEnclosingRequestBase {

    

	public HttpGetWithBody(String uri) {
		super();
		setURI(URI.create(uri));
	}



	@Override
    public String getMethod() {
        return "GET";
    }
}
