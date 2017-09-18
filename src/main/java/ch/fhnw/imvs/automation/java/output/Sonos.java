package ch.fhnw.imvs.automation.java.output;

import org.apache.commons.lang3.StringEscapeUtils;
import org.tensin.sonos.gen.Queue;

public class Sonos {

	private static org.tensin.sonos.commander.Sonos sonos;
	
	private static Sonos instance = null;
	public static Sonos getInstance(){
		if (instance==null){
				instance = new ch.fhnw.imvs.automation.java.output.Sonos();
		}
		return instance;
	}
	private Sonos() {
		try{
			sonos = new org.tensin.sonos.commander.Sonos();
		}
		catch(Exception e){}
	}
	
	public org.tensin.sonos.commander.Sonos getSonosInstance(){
		return this.sonos;
	}

	public void pause(String zonePlayer) {
		sonos.pause(sonos.getPlayer(zonePlayer));
	}
	
	public void play(String zonePlayer){
		sonos.play(sonos.getPlayer(zonePlayer));
	}
	
	public void next(String zonePlayer){
		sonos.next(sonos.getPlayer(zonePlayer));
	}
	
	public void clearQueue(String zonePlayer){
		sonos.clearQueue(sonos.getPlayer(zonePlayer));
	}
	
	public void previous(String zonePlayer){
		sonos.previous(sonos.getPlayer(zonePlayer));
	}
		
	public void setVolume(String zonePlayer, String volume){
		sonos.setVolume(sonos.getPlayer(zonePlayer), Integer.parseInt(volume));
	}
	
	public void setAvTransportURI(String zonePlayer, String instanceId, String url, String _metadata){
		String metadata = (_metadata.toLowerCase().contains("null")) ? null : StringEscapeUtils.unescapeXml(_metadata);
		sonos.setAVTransportURI(sonos.getPlayer(zonePlayer), Integer.parseInt(instanceId), url, metadata);
	}
	
	public void mute(String zonePlayer, boolean mute){
		sonos.mute(sonos.getPlayer(zonePlayer), mute);
	}
	
	public int getVolume(String zonePlayer){
		return sonos.volume(sonos.getPlayer(zonePlayer));
	}
	
	public String info(String zonePlayer){
		return sonos.getInfo(sonos.getPlayer(zonePlayer));
	}
	
	public void playlist(String zonePlayer, String instanceId, String first){
		String metadata = "";
		sonos.setAVTransportURI(sonos.getPlayer(zonePlayer), Integer.parseInt(instanceId), first, metadata);
	}
	
	public void playlistQ(String zonePlayer, String instanceId, String url, String _metadata){
		sonos.enqueue2(sonos.getPlayer(zonePlayer), url, _metadata);
	}
	
	public Queue getQueue(String zonePlayer){
		return sonos.getQueue(sonos.getPlayer(zonePlayer));
	}
	
}
