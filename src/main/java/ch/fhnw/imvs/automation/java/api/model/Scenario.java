package ch.fhnw.imvs.automation.java.api.model;

public class Scenario {
	
	private String name;
	private long id;
	private int hue;
	private int sat;
	private int bri;
	private int blindes;
	private boolean on;
	private String sonos;
	private int volume;
	private String music;
	private int playlist;
	private int radio;
	

	public Scenario(String name, long id, int hue, int sat, int bri, int blindes, boolean on, String sonos, int volume,
			String music, int playlist, int radio) {
		this.name = name;
		this.id = id;
		this.hue = hue;
		this.sat = sat;
		this.bri = bri;
		this.blindes = blindes;
		this.on = on;
		this.sonos = sonos;
		this.volume = volume;
		this.music = music;
		this.playlist = playlist;
		this.radio = radio;
	}
	
	public int getRadio(){
		return radio;
	}
	
	public void setRadio(int radio){
		this.radio = radio;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getHue() {
		return hue;
	}
	public void setHue(int hue) {
		this.hue = hue;
	}
	public int getSat() {
		return sat;
	}
	public void setSat(int sat) {
		this.sat = sat;
	}
	public int getBri() {
		return bri;
	}
	public void setBri(int bri) {
		this.bri = bri;
	}
	public int getBlindes() {
		return blindes;
	}
	public void setBlindes(int blindes) {
		this.blindes = blindes;
	}
	public boolean isOn() {
		return on;
	}
	public void setOn(boolean on) {
		this.on = on;
	}
	public String getSonos() {
		return sonos;
	}
	public void setSonos(String sonos) {
		this.sonos = sonos;
	}
	public int getVolume() {
		return volume;
	}
	public void setVolume(int volume) {
		this.volume = volume;
	}
	public String getMusic() {
		return music;
	}
	public void setMusic(String music) {
		this.music = music;
	}
	public int getPlaylist() {
		return playlist;
	}
	public void setPlaylist(int playlist) {
		this.playlist = playlist;
	}
	
	


}
