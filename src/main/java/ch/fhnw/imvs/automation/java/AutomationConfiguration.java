package ch.fhnw.imvs.automation.java;

import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ch.fhnw.imvs.automation.java.api.model.Scenarios;


public class AutomationConfiguration {
	public static volatile JSONObject config;
	public static volatile JSONArray scenario;
	public static volatile JSONArray playlist;
	public static volatile JSONArray radio;
	public static volatile Scenarios scenarioModel = new Scenarios();
	
	
	
	// parsing the config file 
	public static void init(String configFile){
		JSONParser parser = new JSONParser();
		//Scenarios scenarioModel = new Scenarios();
		Object obj;
		try {
			obj = parser.parse(new FileReader(configFile));
			config = (JSONObject) obj;
			scenario = (JSONArray) config.get("scenario");
			for(int i = 0; i < scenario.size(); i++){
				JSONObject addScenario = (JSONObject) scenario.get(i);
				String name = addScenario.get("name").toString();
				long id = Long.valueOf(addScenario.get("id").toString());
				int hue = Integer.valueOf(addScenario.get("hue").toString());
				int sat = Integer.valueOf(addScenario.get("sat").toString());
				int bri = Integer.valueOf(addScenario.get("bri").toString());
				int blindes = Integer.valueOf(addScenario.get("blindes").toString());
				boolean on = Boolean.valueOf(addScenario.get("on").toString());
				String sonos = addScenario.get("sonos").toString();
				int volume = Integer.valueOf(addScenario.get("volume").toString());
				String music = addScenario.get("music").toString();	
				int playlist = Integer.valueOf(addScenario.get("playlist").toString());
				int radio = Integer.valueOf(addScenario.get("radio").toString());
				scenarioModel.addScenario(name, id, hue, sat, bri, blindes, on, sonos, volume, music, playlist, radio);
			}
			radio = (JSONArray) config.get("radio");
			playlist = (JSONArray) config.get("playlist");
		} catch (IOException | ParseException e) {
			System.out.println("Error while parsing of config");
			e.printStackTrace();
		}
	}
	
	public static String getConfig(String key){
		return (String)((JSONObject)config.get("config")).get(key);
	}
	
	public static Scenarios getScenario(){
		return scenarioModel;
	}
	
	public static JSONArray getPlaylist(){
		return playlist;
	}
	
	public static JSONArray getRadio(){
		return radio;
	}
}
