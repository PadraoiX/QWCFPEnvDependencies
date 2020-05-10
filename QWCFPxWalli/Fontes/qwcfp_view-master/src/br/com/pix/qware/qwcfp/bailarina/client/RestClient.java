package br.com.pix.qware.qwcfp.bailarina.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import br.com.pix.qware.qwcfp.util.QwcfpInitials;

public class RestClient {

	private static final String prefix = "http://";
	private static final String wsContext = "/bailarina/v1.0/";

	public static final String URI_SUFFIX_ENDPOINTS = "endpoints";
	public static final String URI_SUFFIX_ENDPOINT = "endpoint/%s";
	public static final String URI_SUFFIX_ENDPOINT_OP_FORMAT = "endpoint/%s/operations";
	public static final String URI_SUFFIX_ENDPOINT_OP1_FORMAT = "endpoint/%s/operation/%s";
	public static final String URI_SUFFIX_ENDPOINT_PAR_FORMAT = "endpoint/%s/operation/%s/parameters";
	public static final String URI_SUFFIX_ENDPOINT_PAR1_FORMAT = "endpoint/%s/operation/%s/parameter/%s";
	public static final String URI_SUFFIX_TEST_OPERATION = "service/%s/%s";
	public static final String URI_SUFFIX_STATISTICS = "statistics/%s";

	public static final int REQUEST_GET = 0;
	public static final int REQUEST_POST = 1;
	public static final int REQUEST_PUT = 2;
	public static final int REQUEST_DELETE = 3;

	private String host;
	private String port;
	private String server;
	private HttpClient httpClient;
	private HttpUriRequest request;
	private HttpResponse response;
	private StringEntity httpBody;
	private String responseString;
	private StatusLine statusLine;
	private String content;

	public RestClient() {
		super();

		host = QwcfpInitials.getInstance().getProperties().getProperty("wsBailarinaHost", "172.16.253.17").trim();
		port = QwcfpInitials.getInstance().getProperties().getProperty("wsBailarinaPort", "5000").trim();
		server = prefix + host + ":" + port;
		httpClient = HttpClientBuilder.create().build();
	}

	public RestClient body(String body) throws UnsupportedEncodingException {
		httpBody = new StringEntity(body, ContentType.APPLICATION_JSON);
		return this;
	}

	public RestClient body(String body, String contentType) throws UnsupportedEncodingException {
		httpBody = new StringEntity(body, contentType);
		return this;
	}

	public RestClient request(String uriSuffix, int requestType) {

		switch (requestType) {
		case REQUEST_GET:

			HttpGetWithBody get = new HttpGetWithBody(server + wsContext + uriSuffix);
			get.setEntity(httpBody);
			request = get;
			request.addHeader("User-Agent", "QWCFP Web View");
			request.addHeader("accept", "application/json");
			request.setHeader("Content-type", "application/json");
			break;

		case REQUEST_POST:

			HttpPost post = new HttpPost(server + wsContext + uriSuffix);
			post.setEntity(httpBody);
			request = post;
			request.addHeader("User-Agent", "QWCFP Web View");
			request.addHeader("accept", "application/json");
			request.setHeader("Content-type", "application/json");
			break;

		case REQUEST_PUT:

			HttpPut put = new HttpPut(server + wsContext + uriSuffix);
			put.setEntity(httpBody);
			request = put;
			request.addHeader("User-Agent", "QWCFP Web View");
			request.addHeader("accept", "application/json");
			request.setHeader("Content-type", "application/json");
			break;

		case REQUEST_DELETE:

			request = new HttpDelete(server + wsContext + uriSuffix);
			request.addHeader("User-Agent", "QWCFP Web View");
			request.addHeader("accept", "application/json");
			break;

		default:
			throw new RuntimeException("Não foi possivel gerar requisição rest para o servidor.");
		}

		return this;
	}

	public RestClient execute() throws ClientProtocolException, IOException {
		response = httpClient.execute(request);

		statusLine = response.getStatusLine();

		if (statusLine.getStatusCode() == 200) {
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			responseString = result.toString();
		} else {
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			content =  result.toString();
			
		}
		return this;
	}

	public ResponseString getResponseString() {
		return new ResponseString(responseString, statusLine.getReasonPhrase(), statusLine.getStatusCode(), content);
	}

	public String getserverUri() {
		return server + wsContext;
	}
}
