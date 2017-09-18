package ch.fhnw.imvs.automation.java.api.persistance;

import java.io.IOException;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ch.fhnw.imvs.automation.java.AutomationConfiguration;
import ch.fhnw.imvs.automation.java.api.mqtt.MQTTOutput;
import ch.fhnw.imvs.automation.java.output.HttpRequester;


@RestController
public class KnxController {
	private String up;
	private String down;
	private String stop;
	private String token;
	private String stepdown;
	private String stepup; 
	private String url;

	
	public KnxController(){
		up = AutomationConfiguration.getConfig("knx.url.up");
		down = AutomationConfiguration.getConfig("knx.url.down");
		stop = AutomationConfiguration.getConfig("knx.url.stop");
		token = AutomationConfiguration.getConfig("knx.token");
		stepdown = AutomationConfiguration.getConfig("knx.url.stepdown");
		stepup = AutomationConfiguration.getConfig("knx.url.stepup");
		
	}

	
	//id1 = down, id2 = stop, id3 = up, id4 = stepdown, id5 = stepup
	@CrossOrigin
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = {"/knx/{id}"}, method = RequestMethod.POST)
	public ResponseEntity<String> seKnx(@PathVariable(value = "id") int id) throws JSONException, IOException{
		switch(id){
		case 1: 
			url = down;
			HttpRequester.getInstance().postRequest(url, token);
			System.out.println(token);
			return new ResponseEntity<String>("{\"knx\":\"down\"}", HttpStatus.OK); 
		case 2: 
			url = stop;
			HttpRequester.getInstance().postRequest(url, token);
			return new ResponseEntity<String>("{\"knx\":\"stop\"}", HttpStatus.OK);
        case 3: 
        	url = up;
        	HttpRequester.getInstance().postRequest(url, token);
			return new ResponseEntity<String>("{\"knx\":\"up\"}", HttpStatus.OK);
        case 4:
        	url = stepdown;
        	HttpRequester.getInstance().postRequest(url, token);
			return new ResponseEntity<String>("{\"knx\":\"stepdown\"}", HttpStatus.OK);
        case 5:
        	url = stepup;
        	HttpRequester.getInstance().postRequest(url, token);
			return new ResponseEntity<String>("{\"knx\":\"stepup\"}", HttpStatus.OK);
        default: 
        	return new ResponseEntity<String>("{\"knx\":\"shit\"}", HttpStatus.OK); 
		
		}
		
		
	}
}
