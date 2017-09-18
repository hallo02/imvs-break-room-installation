package io.flic.fliclib.javaclient;


import java.util.List;

import io.flic.fliclib.javaclient.enums.ClickType;

public class Command {
	private List<String> button;
	private ClickType firstChoice;
	private ClickType secondChoice;
	private int order;
	private Runnable action;
	private String classname;
	
	public Command(List<String> button,ClickType firstChoice,ClickType secondChoice, int order, Runnable action, String classname){
		this.button = button;
		this.firstChoice = firstChoice;
		this.secondChoice = secondChoice;
		this.order = order;
		this.action = action;
		this.classname = classname;
	}
	
	public ClickType getSecondChoice(){
		return this.secondChoice;
	}
	
	public ClickType getfirstChoice(){
		return this.firstChoice;
	}
	
	public int getOrder(){
		return this.order;
	}
	
	public List<String>	 getButtons(){
		return this.button;
	}
	
	public Runnable getAction(){
		return this.action;
	}
	
	public String getClassName(){
		return this.classname;
	}
	
	
	
	@Override
	public String toString(){
		String s = String.format("Command\nButtons:%s, clickType:%s",button.size(),firstChoice.toString());
		return s;
	}

}
