package ch.fhnw.imvs.automation.java.api.persistance;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
import ch.fhnw.imvs.automation.java.output.Sonos;

@RestController
public class SonosController {
	JSONArray radioAll = AutomationConfiguration.getRadio();
	JSONArray playlist = AutomationConfiguration.getPlaylist();
	private String group = AutomationConfiguration.getConfig("sonos.group");
	private String instanceId = AutomationConfiguration.getConfig("sonos.instanceid");
	MQTTOutput mqtt = new MQTTOutput();
	private String message = "imvsautomation/music";

	//Request to set sonos to play
		@CrossOrigin
		@RequestMapping(value = {"/sonos/play"}, method = RequestMethod.POST)
		public ResponseEntity<String> setPlay(){
			Sonos.getInstance().play(group);
			return new ResponseEntity<String>("{\"response\":\"done\"}", HttpStatus.OK);
		}
		
		//Request to set sonos to pause
		@CrossOrigin
		@RequestMapping(value = {"/sonos/pause"}, method = RequestMethod.POST)
		public ResponseEntity<String> setPause(){
			Sonos.getInstance().pause(group);
			return new ResponseEntity<String>("{\"response\":\"done\"}", HttpStatus.OK);
		}
		
		//Request to play next song
		@CrossOrigin
		@RequestMapping(value = {"/sonos/next"}, method = RequestMethod.POST)
		public ResponseEntity<String> setNext(){
			Sonos.getInstance().next(group);
			return new ResponseEntity<String>("{\"response\":\"done\"}", HttpStatus.OK);
		}
		
		//Request to clear playlist
		@CrossOrigin
		@RequestMapping(value = {"/sonos/clear"}, method = RequestMethod.POST)
		public ResponseEntity<String> setClear(){
			Sonos.getInstance().clearQueue(group);;
			return new ResponseEntity<String>("{\"response\":\"done\"}", HttpStatus.OK);
		}
		
		//Request to play previous song
		@CrossOrigin
		@RequestMapping(value = {"/sonos/previous"}, method = RequestMethod.POST)
		public ResponseEntity<String> setPrevious(){
			Sonos.getInstance().previous(group);
			return new ResponseEntity<String>("{\"response\":\"done\"}", HttpStatus.OK);
		}
		
		//Request to set Volume --> {volume} equals number between 0-100
		@CrossOrigin
		@RequestMapping(value = {"/sonos/volume={volume}"}, method = RequestMethod.POST)
		public ResponseEntity<String> setVolume(@PathVariable(value = "volume") String volume){
			if(Integer.parseInt(volume) < 100 && Integer.parseInt(volume) >= 0){
				Sonos.getInstance().setVolume(group, volume);
				mqtt.output(message, "Volume changed");
				return new ResponseEntity<String>("{\"response\":\"done\"}", HttpStatus.OK);
			}else{
				return new ResponseEntity<String>("{\"response\":\"does not exist\"}", HttpStatus.OK);
			}
		}
		
		//Request to set radiostation and set sonos in play mode
		@CrossOrigin
		@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = {"/sonos/radio={id}"}, method = RequestMethod.POST)
		public ResponseEntity<String> setRadio(@PathVariable(value = "id") int id)throws JSONException, IOException{
			if(id < (radioAll.size())){
				JSONObject b = (JSONObject) radioAll.get(id);
				String uriRadio = (String) b.get("uri");
				String metaRadio = (String) b.get("meta");
				Sonos.getInstance().setAvTransportURI(group, instanceId, uriRadio, metaRadio);
				Sonos.getInstance().play(group);
				return new ResponseEntity<String>("{\"response\":\"done\"}", HttpStatus.OK);
			}else{
				return new ResponseEntity<String>("{\"response\":\"no entry vor this radiostation\"}", HttpStatus.OK);
			}
		}
		
		//Request to mute sonos
		@CrossOrigin
		@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = {"/sonos/mute={mute}"}, method = RequestMethod.POST)
		public ResponseEntity<String> mute(@PathVariable(value = "mute") boolean mute)throws JSONException, IOException{
			Sonos.getInstance().mute(group, mute);
					return null;
		}
		
		//Request to get Volume
		@CrossOrigin
		@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = {"/sonos/volume"}, method = RequestMethod.GET)
		public ResponseEntity<Map<String, Object>> getVolume(){
			Map<String, Object> resultObject = new HashMap<String, Object>();
			int volume = Sonos.getInstance().getVolume(group);
			resultObject.put("volume", volume);
			return new ResponseEntity<Map<String, Object>>(resultObject, HttpStatus.OK);
		}
		
		//Request to get info XML (no useful information)
		@CrossOrigin
		@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = {"/sonos/info"}, method = RequestMethod.GET)
		public ResponseEntity<String> mute()throws JSONException, IOException{
			String sb = Sonos.getInstance().info(group);
			return new ResponseEntity<String>(sb, HttpStatus.OK);
		}
		
		//Not in use
		@CrossOrigin
		@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = {"/sonos/song"}, method = RequestMethod.POST)
		public ResponseEntity<String> playlist()throws JSONException, IOException{
			Sonos.getInstance().playlist(group, "IMVS", "");
			return null;
		}
		
		//Request to add playlist to queue and play
		@CrossOrigin
		@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = {"/sonos/playlist/{id}"}, method = RequestMethod.POST)
		public ResponseEntity<String> setPlaylist(@PathVariable(value = "id") int id)throws JSONException, IOException{
			if(id <= playlist.size()){
				JSONObject a = (JSONObject) playlist.get(id);
				String first = (String) a.get("first");
				String uri = (String) a.get("uri");
				String meta = (String) a.get("meta");
				Sonos.getInstance().playlist(group, instanceId, first);
				Sonos.getInstance().playlistQ(group, instanceId, uri, meta);
				Sonos.getInstance().play(group);
				return new ResponseEntity<String>("halo", HttpStatus.OK);
			}else{
				return new ResponseEntity<String>("{\"response\":\"Playlist does not exist\"}", HttpStatus.OK);
			}
		}
		
		//Request to get list of radio
		@CrossOrigin
		@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = {"sonos/radiolist"}, method = RequestMethod.GET)
		public ResponseEntity<String> getRadioList() throws JSONException, IOException{
			JSONArray b = AutomationConfiguration.radio;
			return new ResponseEntity<String>(b.toString(), HttpStatus.OK);	
		}
		
		//Request to get list of playlist
		@CrossOrigin
		@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = {"sonos/playlistlist"}, method = RequestMethod.GET)
		public ResponseEntity<String> getPlaylistList() throws JSONException, IOException{
			JSONArray b = AutomationConfiguration.playlist;
			return new ResponseEntity<String>(b.toString(), HttpStatus.OK);	
		}
		/*Not working
		//Request to get Queue
		@CrossOrigin
		@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = {"sonos/queue"}, method = RequestMethod.GET)
		public ResponseEntity<String> getQueue(){
			JSONObject queue = Sonos.getInstance().getQueue(group).;
			queue.toString();
			Gson gson = new Gson();
			String jsonInString = gson.toJson(queue);
			String jsonInString = mapper.writeValueAsString(queue);
			String test = queue.toString();
			return new ResponseEntity<String>("asdf", HttpStatus.OK);
		}*/
}
