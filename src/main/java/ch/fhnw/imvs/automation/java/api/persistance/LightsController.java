package ch.fhnw.imvs.automation.java.api.persistance;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.fhnw.imvs.automation.java.AutomationConfiguration;
import ch.fhnw.imvs.automation.java.api.mqtt.MQTTOutput;
import ch.fhnw.imvs.automation.java.output.PhilipsHue;


@RestController
public class LightsController {
	//private String user = "philipshue.username";
	//private String bridgeip = "philipshue.ip";
	//private String url = "http://" + bridgeip + "/api/" + user + "/";
	private String user = AutomationConfiguration.getConfig("philipshue.username");
	private String bridgeip = AutomationConfiguration.getConfig("philipshue.ip");
	private String url = "http://" + bridgeip + "/api/" + user + "/";
	MQTTOutput mqtt = new MQTTOutput();
	private String message = "imvsautomation/light";

	//Request to PhillipsHue GET State, http://<bridge ip address>/api/<user>
	@CrossOrigin
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = {"/hue/state"}, method = RequestMethod.GET)
	public ResponseEntity<String> getState() throws JSONException, IOException{
		JSONObject json = PhilipsHue.readJsonFromUrl(url);
		return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
	}
	
	//Request to PhillipsHue GET Lights, http://<bridge ip address>/api/<user>/lights
	@CrossOrigin
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = {"/hue/state/lights"}, method = RequestMethod.GET)
	public ResponseEntity<String> getLights() throws JSONException, IOException{
		String url2 = url + "lights";
		JSONObject json =  PhilipsHue.readJsonFromUrl(url2);
		return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
	}
	
	//Request to PhillipsHue Get Light, http://<bridge ip address>/api/<user>/lights/<light id>
	@CrossOrigin
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = {"/hue/state/light/id={id}"}, method = RequestMethod.GET)
	public ResponseEntity<String> getLight(@PathVariable(value = "id") String id) throws JSONException, IOException{
		String url2 = url + "lights/" + id ;
		JSONObject json = PhilipsHue.readJsonFromUrl(url2);

		return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
	}
	
	//Request to change brightness, colour, saturation or state of specific light
	@CrossOrigin
	@RequestMapping(value = {"hue/light/id={id}"}, method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> commadLight(@PathVariable(value = "id") String id, 
			@RequestParam(value = "on", defaultValue="null") String on, 
			@RequestParam(value = "bri", defaultValue= "255") int bri,
			@RequestParam(value = "sat", defaultValue="255") int sat, 
			@RequestParam(value = "hue", defaultValue="65536") int hue){
		Map<String, Object> resultObject = new HashMap<String, Object>();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		String ObjectUrl = url + "lights/" + id + "/state";
		SimpleDateFormat format1 = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy");
		boolean stateOfRequest = PhilipsHue.getInstance().putRequestHue(ObjectUrl, on, bri, sat, hue);
		resultObject.put("Timestamp: ", format1.format(cal.getTime()));
		resultObject.put("State: ", stateOfRequest);
		mqtt.output(message, "lights changed");
		return new ResponseEntity<Map<String, Object>>(resultObject, HttpStatus.OK);	
	}
	
	//Request to change brightness, colour, saturation or state of multiple lights
	@CrossOrigin
	@RequestMapping(value = {"hue/group/id={id}"}, method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> commadLights(@PathVariable(value = "id") long id,
			@RequestParam(value = "on", defaultValue="null") String on, 
			@RequestParam(value = "bri", defaultValue="255") int bri,
			@RequestParam(value = "sat", defaultValue="255") int sat, 
			@RequestParam(value = "hue", defaultValue="65536") int hue){
		Map<String, Object> resultObject = new HashMap<String, Object>();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		String ObjectUrl =  url+ "groups/" + id + "/action";
		SimpleDateFormat format1 = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy");
		boolean stateOfRequest = PhilipsHue.getInstance().putRequestHue(ObjectUrl, on, bri, sat, hue);
		resultObject.put("Timestamp: ", format1.format(cal.getTime()));
		resultObject.put("State: ", stateOfRequest);
		mqtt.output(message, "lights changed");
		return new ResponseEntity<Map<String, Object>>(resultObject, HttpStatus.OK);		
	}
	
	
	// Not supported by philips hue --> may be supported in the future
	@CrossOrigin
	@RequestMapping(value = {"hue/alert/"}, method = RequestMethod.POST)
	public void alert(){
		long id = 0;
		String typ = "alert";
		String ObjectUrl =  url+ "groups/" + id + "/action";
		boolean state = PhilipsHue.getInstance().special(ObjectUrl, typ);
	}
	
	
	//Not supported by philips hue --> may be supported in the future 
 	@CrossOrigin
	@RequestMapping(value = {"hue/party={on}"}, method = RequestMethod.POST)
	public void partyMode(@PathVariable(value = "on") boolean on){
		long id = 0;
		if(on == true){
			String typ = "partyOn";
			String ObjectUrl =  url+ "groups/" + id + "/action";
			boolean state = PhilipsHue.getInstance().special(ObjectUrl, typ);
		}else{
			String typ = "partyOff";
			String ObjectUrl =  url+ "groups/" + id + "/action";
			boolean state = PhilipsHue.getInstance().special(ObjectUrl, typ);
		}

		
	}

}
