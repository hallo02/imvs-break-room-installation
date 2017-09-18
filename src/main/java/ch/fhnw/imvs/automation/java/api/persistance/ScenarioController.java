package ch.fhnw.imvs.automation.java.api.persistance;

import java.io.IOException;
import org.json.simple.parser.*;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ch.fhnw.imvs.automation.java.AutomationConfiguration;
import ch.fhnw.imvs.automation.java.output.HttpRequester;
import ch.fhnw.imvs.automation.java.output.PhilipsHue;
import ch.fhnw.imvs.automation.java.output.Sonos;
import ch.fhnw.imvs.automation.java.api.model.Scenario;
import ch.fhnw.imvs.automation.java.api.model.Scenarios;
import ch.fhnw.imvs.automation.java.api.mqtt.MQTTOutput;

@RestController
public class ScenarioController {
	


	JSONArray playlistAll = AutomationConfiguration.getPlaylist();
	JSONArray radioAll = AutomationConfiguration.getRadio();
	Scenarios scenarioModel = AutomationConfiguration.getScenario();
	private String up = AutomationConfiguration.getConfig("knx.url.up");
	private String down = AutomationConfiguration.getConfig("knx.url.down");
	private String stop = AutomationConfiguration.getConfig("knx.url.stop");
	private String token = AutomationConfiguration.getConfig("knx.token");
	private String stepdown = AutomationConfiguration.getConfig("knx.url.stepdown");
	private String stepup = AutomationConfiguration.getConfig("knx.url.stepup"); 
	private String user = AutomationConfiguration.getConfig("philipshue.username");
	private String bridgeip = AutomationConfiguration.getConfig("philipshue.ip");
	private String url = "http://" + bridgeip + "/api/" + user + "/";
	private String group = AutomationConfiguration.getConfig("sonos.group");
	private String instanceId = AutomationConfiguration.getConfig("sonos.instanceid");
	private String urlKnx;
	MQTTOutput mqtt = new MQTTOutput();
	private String message = "imvsautomation/scenario";
	//private Scenarios scenarioModel;
	
	//Request to get list of scenario/WEPAPI/src/main/java/model
	@SuppressWarnings("unchecked")
	@CrossOrigin
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = {"scenario/list"}, method = RequestMethod.GET)
	public ResponseEntity<String> getScenarioList() throws JSONException, IOException{
		JSONArray response = new JSONArray();
		//System.out.println(scenarioModel.);
		java.util.List<Scenario> scenarios = scenarioModel.getScenarios();
		for(Scenario scenario : scenarios){
			JSONObject objScenario = new JSONObject();
			objScenario.put("name", scenario.getName());
			objScenario.put("id", scenario.getId());
			objScenario.put("hue", scenario.getHue());
			objScenario.put("sat", scenario.getSat());
			objScenario.put("bri", scenario.getBri());
			objScenario.put("blindes", scenario.getBlindes());
			objScenario.put("on", scenario.isOn());
			objScenario.put("sonos", scenario.getSonos());
			objScenario.put("volume", scenario.getVolume());
			objScenario.put("music", scenario.getMusic());
			objScenario.put("radio", scenario.getRadio());
			objScenario.put("playlist", scenario.getPlaylist());
			response.add(objScenario);
		}
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);	
	}
	
	//Request to add scenario
	@SuppressWarnings("unchecked")
	@CrossOrigin
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = {"scenario/add"}, method = RequestMethod.POST)
	public ResponseEntity<String> addScenario(HttpEntity<byte[]> requestEntity) throws JSONException, IOException{
		byte[] requestBody = requestEntity.getBody();
		String value = new String(requestBody, "UTF-8");
		JSONObject body = new JSONObject();
		JSONParser parser = new JSONParser();
		try {
			body = (JSONObject) parser.parse(value);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(body.size() == 12){
			if(body.keySet().toString().equals("[volume, music, playlist, sat, name, bri, hue, blindes, id, sonos, on, radio]")){
				String name = body.get("name").toString();
				long id = System.currentTimeMillis();
				int hue = Integer.valueOf(body.get("hue").toString());
				int sat = Integer.valueOf(body.get("sat").toString());
				int bri = Integer.valueOf(body.get("bri").toString());
				int blindes = Integer.valueOf(body.get("blindes").toString());
				boolean on = Boolean.valueOf(body.get("on").toString());
				String sonos = body.get("sonos").toString();
				int volume = Integer.valueOf(body.get("volume").toString());
				String music = body.get("music").toString();	
				int playlist = Integer.valueOf(body.get("playlist").toString());
				int radio = Integer.valueOf(body.get("radio").toString());
				scenarioModel.addScenario(name, id, hue, sat, bri, blindes, on, sonos, volume, music, playlist, radio);
				mqtt.output(message, "Added");
				return new ResponseEntity<String>("{\"response\":\"scenario added\"}", HttpStatus.OK);
			}else{
				return new ResponseEntity<String>("{\"response\":\"wrong data\"}", HttpStatus.OK);
			}	
		}else{
			return new ResponseEntity<String>("{\"response\":\"wrong data\"}", HttpStatus.OK);
		}
	}
	
	//Request to delete scenario
		@SuppressWarnings("unchecked")
		@CrossOrigin
		@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = {"scenario/delete/{id}"}, method = RequestMethod.POST)
		public ResponseEntity<String> deleteScenario(@PathVariable(value = "id")long id){
			scenarioModel.deleteScenario(id);
			mqtt.output(message, "Deleted");
			return new ResponseEntity<String>("{\"response\":\"scenario deleted\"}", HttpStatus.OK);

	}
		
	//Request to edit scenario
	@SuppressWarnings("unchecked")
	@CrossOrigin
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = {"scenario/update"}, method = RequestMethod.POST)
	public ResponseEntity<String> editScenario(HttpEntity<byte[]> requestEntity) throws JSONException, IOException{
		byte[] requestBody = requestEntity.getBody();
		String value = new String(requestBody, "UTF-8");
		JSONObject body = new JSONObject();
		JSONParser parser = new JSONParser();
		try {
			body = (JSONObject) parser.parse(value);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(body.size() == 12){
			if(body.keySet().toString().equals("[volume, music, playlist, sat, name, bri, hue, blindes, id, sonos, on, radio]")){
				long i = Long.valueOf(body.get("id").toString());
				String name = body.get("name").toString();
				long id = Long.valueOf(body.get("id").toString());
				int hue = Integer.valueOf(body.get("hue").toString());
				int sat = Integer.valueOf(body.get("sat").toString());
				int bri = Integer.valueOf(body.get("bri").toString());
				int blindes = Integer.valueOf(body.get("blindes").toString());
				boolean on = Boolean.valueOf(body.get("on").toString());
				String sonos = body.get("sonos").toString();
				int volume = Integer.valueOf(body.get("volume").toString());
				String music = body.get("music").toString();	
				int playlist = Integer.valueOf(body.get("playlist").toString());
				int radio = Integer.valueOf(body.get("radio").toString());
				mqtt.output(message, "Updated");
				scenarioModel.updateScenario(i, name, id, hue, sat, bri, blindes, on, sonos, volume, music, playlist, radio);
				return new ResponseEntity<String>("{\"response\":\"scenario updated\"}", HttpStatus.OK);
			}else{
				return new ResponseEntity<String>("{\"response\":\"scenario not updated\"}", HttpStatus.OK);
			}
		}else{
			return new ResponseEntity<String>("{\"response\":\"scenario not updated\"}", HttpStatus.OK);
		}
	}
	
	//Request to set scenario
	@CrossOrigin
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = {"/scenario/exe/{id}"}, method = RequestMethod.POST)
	public ResponseEntity<String> setScenario(@PathVariable(value = "id") long id){
		int scene = scenarioModel.getIndexOfScenario(id);
			int hue = scenarioModel.getScenarios().get(scene).getHue();
			int sat = scenarioModel.getScenarios().get(scene).getSat();
			int bri = scenarioModel.getScenarios().get(scene).getBri();
			String on = String.valueOf(scenarioModel.getScenarios().get(scene).isOn());
			int blindes = scenarioModel.getScenarios().get(scene).getBlindes();
			String volume = String.valueOf(scenarioModel.getScenarios().get(scene).getVolume());
			String sonos = scenarioModel.getScenarios().get(scene).getSonos();
			String music = scenarioModel.getScenarios().get(scene).getMusic();
			int radio = scenarioModel.getScenarios().get(scene).getRadio();
			int playlist = scenarioModel.getScenarios().get(scene).getPlaylist();
			//call all devices
			callPhilipsHue(on, bri, sat, hue);
			callKnx(blindes);
			callSonos(volume, sonos, music, radio, playlist);
			mqtt.output(message, "scenario set");
			return new ResponseEntity<String>("hallo", HttpStatus.OK);
	}
	
	//Handels philips Hue
	private void callPhilipsHue(String on, int bri, int sat, int hue){
		long id = 0;
		String ObjectUrl =  url+ "groups/" + id + "/action";
		boolean stateOfRequest = PhilipsHue.getInstance().putRequestHue(ObjectUrl, on, bri, sat, hue);
		System.out.println(stateOfRequest);
	}

	//Handels knx
	private void callKnx(int blindes){
		if(1 == blindes){
			urlKnx = down;
			HttpRequester.getInstance().postRequest(urlKnx, token);
		}
		if(2 == blindes){
			urlKnx = stop;
			HttpRequester.getInstance().postRequest(urlKnx, token);
		}
		if(3 == blindes){
			urlKnx = up;
			HttpRequester.getInstance().postRequest(urlKnx, token);
		}
		if(4 == blindes){
			urlKnx = stepdown;
			HttpRequester.getInstance().postRequest(urlKnx, token);
		}
		if(5 == blindes){
			urlKnx = stepup;
			HttpRequester.getInstance().postRequest(urlKnx, token);
		}
	}
	
	//Handels sonos
	private void callSonos(String volume, String sonos, String music, int radio, int playlist){
		Sonos.getInstance().setVolume(group, volume);
		if(sonos.equals("pause")){
			Sonos.getInstance().pause(group);
		}else{
			if(music.equals("radio")){
				JSONObject b = (JSONObject) radioAll.get(radio);
				String uriRadio = (String) b.get("uri");
				String metaRadio = (String) b.get("meta");
				Sonos.getInstance().setAvTransportURI(group, instanceId, uriRadio, metaRadio);
				Sonos.getInstance().play(group);
			}else{
				Sonos.getInstance().clearQueue(group);
				JSONObject a = (JSONObject) playlistAll.get(playlist);
				String uri = (String) a.get("uri");
				String meta = (String) a.get("meta");
				String first = (String) a.get("first");
				Sonos.getInstance().playlist(group, instanceId, first);
				Sonos.getInstance().playlistQ(group, instanceId, uri, meta);
				Sonos.getInstance().play(group);
			}
		}	
	}
}
