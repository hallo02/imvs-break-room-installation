package ch.fhnw.imvs.automation.java.api.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTOutput {
	
    private String broker = "tcp://[IP]";
    private String clientId = "[clientid]";
    private String userName = "[user]";
    private String password = "[pwd]";
    private int qos = 0;
    MemoryPersistence persistence = new MemoryPersistence();



	public void output(String topic, String message){
	    try{
	        MqttClient mqttClient = new MqttClient(broker, clientId, persistence);
	        MqttConnectOptions conn0pts = new MqttConnectOptions();
	        conn0pts.setCleanSession(true);
	        conn0pts.setUserName(userName);
	        conn0pts.setPassword(password.toCharArray());
	        mqttClient.connect(conn0pts);
	        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
	        mqttMessage.setQos(qos);
	        mqttClient.publish(topic, mqttMessage);
	        mqttClient.disconnect();
	    } catch (MqttException e) {
	        System.out.println("reason " + e.getReasonCode());
	        System.out.println("msg " + e.getMessage());
	        System.out.println("loc " + e.getLocalizedMessage());
	        System.out.println("cause " + e.getCause());
	        System.out.println("excep " + e);
	        e.printStackTrace();
	    }
	}
}