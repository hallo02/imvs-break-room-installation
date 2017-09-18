package ch.fhnw.imvs.automation.java.output;

import tuwien.auto.calimero.tools.ProcComm;

public class Knx{
	
	private static Knx instance = null;
	
	public static Knx getInstance(){
		if (instance == null)
			instance = new Knx();
		return instance;
	}
	
	public void command(String from, String destination, String rw, String type, String value, String groupAddress){
		ProcComm.main(new String[]{"-localhost",from,destination,rw,type,value,groupAddress});
	}
	
	private Knx(){}
}
