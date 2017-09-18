package ch.fhnw.imvs.automation.java;

import io.flic.fliclib.javaclient.*;
import io.flic.fliclib.javaclient.enums.*;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class Processor {


	private Set<Command> commands = new HashSet<Command>();
	private Map<String, Executor> executors = new ConcurrentHashMap<String, Executor>();

	// 2-dimensional state machine
	private volatile ClickType lastClickType;
	private volatile long lastClickHappening = 0;

	private Callbacks buttonCallbacks = new Callbacks() {
		
		// Just stdout
		@Override
		public void onCreateConnectionChannelResponse(ButtonConnectionChannel channel,
				CreateConnectionChannelError createConnectionChannelError, ConnectionStatus connectionStatus) {
			System.out.println("Create response " + channel.getBdaddr() + ": " + createConnectionChannelError + ", "
					+ connectionStatus);
		}
		
		// Just stdout
		@Override
		public void onRemoved(ButtonConnectionChannel channel, RemovedReason removedReason) {
			System.out.println("Channel removed for " + channel.getBdaddr() + ": " + removedReason);
		}
		
		// Just stdout
		@Override
		public void onConnectionStatusChanged(ButtonConnectionChannel channel, ConnectionStatus connectionStatus,
				DisconnectReason disconnectReason) {
			System.out.println("New status for " + channel.getBdaddr() + ": " + connectionStatus
					+ (connectionStatus == ConnectionStatus.Disconnected ? ", " + disconnectReason : ""));
		}
		
		// Receives Single, Double and Hold Events
		// With a 2-dimensional-statemachine and 3 possible triggers there are 9 events possible
		@Override
		public void onButtonSingleOrDoubleClickOrHold(ButtonConnectionChannel channel, ClickType clickType,
				boolean wasQueued, int timeDiff) throws IOException {
			long now = System.currentTimeMillis();
			// Check if second click follows the first click in a given time range
			if (( now - lastClickHappening) > 400
					&& (now - lastClickHappening) < 2600) {
				lastClickHappening = 0;
				// Fire and trigger
				// Pass channel, secondChoice, firstChoice
				fire(channel, clickType, lastClickType);
				System.out.println("Fired: 1."+lastClickType+" - 2."+clickType);
			} 
			// Otherwise reset the fist click
			else {
				System.out.println("Set 1. Click");
				lastClickHappening = System.currentTimeMillis();
				lastClickType = clickType;
			}

		}

	};

	private void loadCommands() throws Exception {
		// Load actions section from the config file
		JSONArray actions = (JSONArray) AutomationConfiguration.config.get("actions");
		
		// Create one consumer method that to be added each action
		Consumer<JSONObject> consumer = action -> {
			try {
				// Get Buttons
				JSONArray b = (JSONArray) action.get("buttons");

				List<String> buttons = (List<String>) b.stream().collect(Collectors.toList());

				// Get firstChoice
				ClickType firstChoice;
				switch (((String) action.get("firstChoice")).toLowerCase()) {
				case "single":
					firstChoice = ClickType.ButtonSingleClick;
					break;
				case "double":
					firstChoice = ClickType.ButtonDoubleClick;
					break;
				case "hold":
					firstChoice = ClickType.ButtonHold;
					break;
				default:
					firstChoice = null;
					throw new Exception(((String) action.get("event")).toLowerCase() + "is no valid ClickType");
				}

				// Get secondChoice
				ClickType secondChoice;
				switch (((String) action.get("secondChoice")).toLowerCase()) {
				case "single":
					secondChoice = ClickType.ButtonSingleClick;
					break;
				case "double":
					secondChoice = ClickType.ButtonDoubleClick;
					break;
				case "hold":
					secondChoice = ClickType.ButtonHold;
					break;
				default:
					secondChoice = null;
					throw new Exception(((String) action.get("event")).toLowerCase() + "is no valid ClickType");
				}
				
				// Get order
				int order = Integer.parseInt((String) action.get("order"));

				// Get class
				Class c = Class.forName("ch.fhnw.imvs.automation.java.output." + (String) action.get("class"));

				// Get instance
				Method instance = Arrays.asList(c.getMethods()).stream()
						.filter(meth -> meth.getName().toLowerCase().equals("getinstance")).findFirst().get();
				// Get method to invoke
				Method m = Arrays.asList(c.getMethods()).stream().filter(
						meth -> meth.getName().toLowerCase().equals(((String) action.get("method")).toLowerCase()))
						.findFirst().get();
				// Get parametes for invoking method
				JSONArray p = (JSONArray) action.get("parameters");
				List<String> parameters = (List<String>) p.stream().collect(Collectors.toList());

				// Runnable
				Runnable runnable = () -> {
					try {
						m.invoke(instance.invoke(null, null), parameters.toArray());
						// Sleep until several Tasks per Executor.
						Thread.sleep(500);
					} catch (Exception e) {
						System.out.println(e);
						System.err.println(e.getCause());
					}
				};

				// Add command
				commands.add(new Command(buttons, firstChoice, secondChoice, order, runnable, c.getName()));
			} catch (Exception e) {
				System.out.println(e);
			}
		};
		actions.forEach(consumer);
	}

	private void fire(ButtonConnectionChannel channel, ClickType secondChoice, ClickType firstChoice) {
		commands.stream().filter(c -> c.getSecondChoice() == secondChoice && c.getfirstChoice() == firstChoice
				&& (c.getButtons().contains( channel.getBdaddr().toString()) || c.getButtons().contains("ANY") )) 
				.sorted(new Comparator<Command>(){
					@Override
					public int compare(Command c1, Command c2) {
						if(c1.getOrder()<c2.getOrder())
							return -1;
						else if(c1.getOrder() == c2.getOrder())
							return 0;
						else
							return 1;
					}
					
				})
				.forEach(c -> {
					if (!executors.containsKey(c.getClassName())) {
						Executor e = Executors.newSingleThreadExecutor();
						executors.put(c.getClassName(), e);
						e.execute(c.getAction());
						System.out.println(c.getfirstChoice()+", "+c.getSecondChoice()+" from "+channel.getBdaddr().toString()+" Command goes to\nNew executor: "+c.getClassName());
					} else {
						executors.get(c.getClassName()).execute(c.getAction());
						System.out.println(c.getfirstChoice()+", "+c.getSecondChoice()+" from "+channel.getBdaddr().toString()+" Command goes to\nExisting executor: "+c.getClassName());
					}
				});
	}

	public static void main(String[] args) throws Exception {
		
		final FlicClient client = new FlicClient("localhost");
		Processor p = new Processor();
		
		p.loadCommands();

		System.out.println("--------------------");
		System.out.println("Loaded commands:");
		p.commands.forEach(c -> System.out.println(c));
		System.out.println("--------------------");

		client.getInfo(new GetInfoResponseCallback() {
			@Override
			public void onGetInfoResponse(BluetoothControllerState bluetoothControllerState, Bdaddr myBdAddr,
					BdAddrType myBdAddrType, int maxPendingConnections, int maxConcurrentlyConnectedButtons,
					int currentPendingConnections, boolean currentlyNoSpaceForNewConnection, Bdaddr[] verifiedButtons)
					throws IOException {

				for (final Bdaddr bdaddr : verifiedButtons) {
					client.addConnectionChannel(new ButtonConnectionChannel(bdaddr, p.buttonCallbacks));
				}
			}
		});
		client.setGeneralCallbacks(new GeneralCallbacks() {
			@Override
			public void onNewVerifiedButton(Bdaddr bdaddr) throws IOException {
				System.out.println("Another client added a new button: " + bdaddr + ". Now connecting to it...");
				client.addConnectionChannel(new ButtonConnectionChannel(bdaddr, p.buttonCallbacks));
			}
		});
		client.handleEvents();	
	}
}
