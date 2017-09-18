package ch.fhnw.imvs.automation.java.output;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import ch.fhnw.imvs.automation.java.AutomationConfiguration;



public class PhilipsHue {
	private String user = AutomationConfiguration.getConfig("philipshue.username");
	private String bridgeip = AutomationConfiguration.getConfig("philipshue.ip");
	private String url = "http://" + bridgeip + "/api/" + user + "/";
	private HttpClient httpClient = HttpClientBuilder.create().build();
	private static PhilipsHue instance = null;

	public static PhilipsHue getInstance() {
		if (instance == null)
			instance = new PhilipsHue();
		return instance;

	}

	private PhilipsHue() {
	}

	public void putCommand(String objectAddress, String message) {
		try {
			String objectPath = (objectAddress.substring(0, 1).equals("/")) ? objectAddress.substring(1)
					: objectAddress;
			HttpPut request = new HttpPut(url + objectPath);
			StringEntity entity = new StringEntity(message);
			request.setEntity(entity);
			HttpResponse response = httpClient.execute(request);
			EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (Exception e) {
		}
	}

	public void random(String objectAddress) {
		try {
			String objectPath = (objectAddress.substring(0, 1).equals("/")) ? objectAddress.substring(1)
					: objectAddress;
			HttpPut request = new HttpPut(url + objectPath);
			String message = "{\"bri\":" + (int) (Math.random() * 254) + ",\"xy\":["+Math.random()+","+Math.random()+"]}";
			StringEntity entity = new StringEntity(message);
			request.setEntity(entity);
			HttpResponse response = httpClient.execute(request);
			EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (Exception e) {
		}
	}
	public void randomFullBrightness(String objectAddress) {
		try {
			String objectPath = (objectAddress.substring(0, 1).equals("/")) ? objectAddress.substring(1)
					: objectAddress;
			HttpPut request = new HttpPut(url + objectPath);
			String message = "{\"bri\":254,\"xy\":["+Math.random()+","+Math.random()+"]}";
			StringEntity entity = new StringEntity(message);
			request.setEntity(entity);
			HttpResponse response = httpClient.execute(request);
			EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (Exception e) {
		}
	}
	
	private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }

	//get JSON from URL
	  public static JSONObject readJsonFromUrl(String url) throws IOException {
	    InputStream is = new URL(url).openStream();
	    try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      String jsonText = readAll(rd);
	      JSONObject json = new JSONObject(jsonText);
	      return json;
	    } finally {
	      is.close();
	    }
	  }
	  
//		Put Request to Philips Hue, ObjectUrl --> URL of Hue, [on, bri, sat, hue] --> JSON payload
		public boolean putRequestHue(String ObjectUrl, String on, int bri, int sat, int hue){
			URL url;
			try {
				url = new URL(ObjectUrl);
			HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setRequestMethod("PUT");
			httpCon.setRequestProperty("Content-Type", "application/json");
			httpCon.setRequestProperty("Accept", "application/json");
			JSONObject hueP = new JSONObject();
			String effect = "none";
			hueP.put("effect", effect);
			if(on.equals("null") ){
				
			}else{
				hueP.put("on", Boolean.valueOf(on));
			}
			if(sat == 255){
				
			}else{
				hueP.put("sat",sat);
			}
			if(bri == 255){
				
			}else{
				hueP.put("bri", bri);
			}
			if(hue == 65536){
				
			}else{
				hueP.put("hue", hue);
			}
			if(hueP.length() == 0){
				
			}else{
				OutputStreamWriter out = new OutputStreamWriter(
						httpCon.getOutputStream());
				out.write(hueP.toString());
				out.flush();
				out.close();
			}
			httpCon.getInputStream();
			return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		public boolean special(String ObjectUrl, String typ){
			URL url;
			try {
				url = new URL(ObjectUrl);
			HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setRequestMethod("PUT");
			httpCon.setRequestProperty("Content-Type", "application/json");
			httpCon.setRequestProperty("Accept", "application/json");
			JSONObject hueP = new JSONObject();
			if(typ.equals("alert")){
				String alert = "select";
				hueP.put("alert", alert );
			}else{
				if(typ.equals("partyOn")){
					String alert = "colorloop";
					hueP.put("effect", alert );
				}else{
					String alert = "none";
					hueP.put("effect", alert );
				}
			}
			OutputStreamWriter out = new OutputStreamWriter(
					httpCon.getOutputStream());
			out.write(hueP.toString());
			out.flush();
			out.close();
		
		httpCon.getInputStream();
		return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
