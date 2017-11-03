package org.jaylee.project1117;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.client.methods.AsyncCharConsumer;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.protocol.HttpContext;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AsyncNaverShortenURL {

	private final String apiURL = "https://openapi.naver.com/v1/util/shorturl";
	private final String headerContentType = "application/x-www-form-urlencoded";
	private final String headerProperty1 = "X-Naver-Client-Id";
	private final String headerProperty2 = "X-Naver-Client-Secret";
	private String clientID;
	private String clientSecret;
	private ArrayList<String> inputURL;
	private HashMap<String,String> outputURL;
	private RequestConfig requestConfig;
	private CloseableHttpAsyncClient httpClient;
	private ArrayList<HttpPost> httpRequests;
	private HttpAsyncRequestProducer producer;
	private AsyncCharConsumer<HttpResponse> consumer;
	
	public AsyncNaverShortenURL(Settings setting) {
		// TODO Auto-generated constructor stub
		this.clientID = setting.ClientID;
		this.clientSecret = setting.ClientSecret;
		this.inputURL = new ArrayList<String>(setting.input);
		this.setRequestConfig();
		this.setHttpClient();
		this.setHttpRequest();
		this.setConsumer();
		this.outputURL = new HashMap<String,String>();
	}

	protected HashMap<String,String> getOutput() {
		return this.outputURL;
	}
	
	protected void setRequestConfig() {
		this.requestConfig = RequestConfig.custom()
				.setSocketTimeout(3000)
				.setConnectTimeout(3000)
				.build();
	}
	
	protected void setHttpClient() {
		this.httpClient = HttpAsyncClients.custom()
				.setDefaultRequestConfig(this.requestConfig)
				.build();
	}
	
	protected void setHttpRequest() {
		this.httpRequests = new ArrayList<HttpPost>();
		for(int i=0; i<this.inputURL.size(); i++) {
			HttpPost request = new HttpPost(this.apiURL);
			request.addHeader(HttpHeaders.CONTENT_TYPE, this.headerContentType);
			request.addHeader(this.headerProperty1, this.clientID);
			request.addHeader(this.headerProperty2, this.clientSecret);
			try {
				request.setEntity(new StringEntity("url="+this.inputURL.get(i)));
				this.httpRequests.add(request);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
		
	protected void setConsumer() {
		this.consumer =  new AsyncCharConsumer<HttpResponse>() {

            HttpResponse response;
            
            @Override
            protected void onResponseReceived(final HttpResponse response) {
                this.response = response;
            }

            @Override
            protected void onCharReceived(final CharBuffer buf, final IOControl ioctrl) throws IOException {
                try {
                	JSONParser jsonParser = new JSONParser();
                	JSONObject jsonObj = (JSONObject)jsonParser.parse(buf.toString());
                	JSONObject result = (JSONObject)jsonObj.get("result");
                	String shortenURL = result.get("url").toString();
                	String originURL = result.get("orgUrl").toString();
                	outputURL.put(originURL, shortenURL);
                } catch(ParseException e) {
                	e.printStackTrace();
                }
            }

            @Override
            protected void releaseResources() {
            }

            @Override
            protected HttpResponse buildResult(final HttpContext context) {
                return this.response;
            }

        };
	}
	

	
	protected void run() throws Exception {
		try {
			this.httpClient.start();
			final CountDownLatch latch = new CountDownLatch(this.httpRequests.size());
			for(int i=0; i<this.httpRequests.size(); i++) {
				this.producer = HttpAsyncMethods.create(this.httpRequests.get(i));
				this.httpClient.execute(this.producer, this.consumer, new FutureCallback<HttpResponse>() {
	                @Override
	                public void completed(final HttpResponse response) {
	                	latch.countDown();
	                    //System.out.println(response.getStatusLine());
	                }

	                @Override
	                public void failed(final Exception ex) {
	                	latch.countDown();
	                    System.out.println("Request failed : " + ex);
	                }

	                @Override
	                public void cancelled() {
	                	latch.countDown();
	                    System.out.println("Cancelled");
	                }
	            });
			}
			latch.await();
			System.out.println("Shtting down");
		} finally {
			System.out.println("Down");
		}
	}

}
