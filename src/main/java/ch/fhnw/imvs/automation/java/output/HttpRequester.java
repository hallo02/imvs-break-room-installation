package ch.fhnw.imvs.automation.java.output;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class HttpRequester {

	private static HttpClient httpClient = HttpClientBuilder.create().build();
	private static HttpRequester instance = null;

	public static HttpRequester getInstance() {
		if (instance == null)
			instance = new HttpRequester();
		return instance;
	}

	private HttpRequester() {
	}

	public void getRequest(String url) {
		try {
			HttpGet request = new HttpGet(url);
			HttpResponse response = httpClient.execute(request);
			EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (Exception e) {
		}
	}

	public void putRequest(String url, String payload, String... header) {
		try {
			HttpPut request = new HttpPut(url);
			request.addHeader("Content-Type:", "application/json");
			StringEntity entity = new StringEntity(payload);
			request.setEntity(entity);
			HttpResponse response = httpClient.execute(request);
			EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (Exception e) {
		}
	}

	public void postRequest(String url, String payload) {
		try {
			HttpPost request = new HttpPost(url);
			StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);
			request.setEntity(entity);
//			request.addHeader("Content-Type:", "application/json"); throws error 500
			HttpResponse response = httpClient.execute(request);
			EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
