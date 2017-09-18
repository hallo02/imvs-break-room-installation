package ch.fhnw.imvs.automation.java.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Scenarios {
	
	private final List<Scenario> scenarios = new CopyOnWriteArrayList<>();

	public List<Scenario> getScenarios() {
		return scenarios;
	}
	
	public void addScenario(String name, long id, int hue, int sat, int bri, int blindes, boolean on, String sonos, int volume,
			String music, int playlist, int radio){
		scenarios.add(new Scenario(name, id, hue, sat, bri, blindes, on, sonos, volume, music, playlist, radio));
	}
	
	public void deleteScenario(long idI){
		for(Scenario scenario : scenarios){
			if(idI == scenario.getId()){
				scenarios.remove(scenario);
			}
		}
	}
	
	public void updateScenario(long idI, String name, long id, int hue, int sat, int bri, int blindes, boolean on, String sonos, int volume,
			String music, int playlist, int radio){
		for(Scenario scenario : scenarios){
			if(idI == scenario.getId()){
				scenarios.set(scenarios.indexOf(scenario), new Scenario(name, id, hue, sat, bri, blindes, on, sonos, volume, music, playlist, radio));
			}
		}
	}
	
	public int getIndexOfScenario(long idI){
		int i = 0;
		for(Scenario scenario : scenarios){
			if(idI == scenario.getId()){
				i = (scenarios.indexOf(scenario));
			}
		}
		return i;
	}
}
