package packageMqttFx;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttFx {

	private String topic;          
	private String broker;     
	private String clientId;   
	private int qos  = 2;    
	private static MemoryPersistence persistence;
	private static MqttClient mqttClient;
	private static MqttConnectOptions connOpts;
	//private MqttCallback mqttCallback;
	
	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
	public MqttFx(String broker, MqttCallback mqttCallback) throws MqttSecurityException, MqttException 
	{	
		if(mqttClient == null)
		{
			this.broker = broker;
			this.clientId = randomAlphaNumeric(8);
			
			persistence = new MemoryPersistence();  
			
			mqttClient = new MqttClient(broker, clientId, persistence);
			mqttClient.setCallback(mqttCallback);
					
			connOpts = new MqttConnectOptions();
	        connOpts.setCleanSession(true);
	        System.out.println("[MqttFx] Connecting to broker: "+broker);
	        
			mqttClient.connect(connOpts);
			
	        System.out.println("[MqttFx] Client connected");
		}
		else if(!mqttClient.isConnected())
		{
			mqttClient.reconnect();
			System.out.println("[MqttFx] Client reconnected");
		}
	}

	public boolean isConnected() {
		if(mqttClient != null)
			return mqttClient.isConnected();
		else
			return false;
	}
	
	public static String randomAlphaNumeric(int count) {
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
	}
	
	public boolean subscribe(String topic) throws MqttException
	{
		if(mqttClient != null && mqttClient.isConnected())
		{
			System.out.println("[MqttFx.subscribe] Subscribing to topic: " + topic);
			mqttClient.subscribe(topic);
			return true;
		}
		else
		{
			System.out.println("[MqttFx.subscribe] Not subscribed to topic, mqtt client either null or not connected");	
			return false;
		}
		
	}
	
	public boolean unsubscribe(String topic) throws MqttException
	{
		if(mqttClient != null && mqttClient.isConnected())
		{
			System.out.println("[MqttFx.unsubscribe] Unubscribing topic: " + topic);
			mqttClient.unsubscribe(topic);
			return true;
		}
		else
		{
			System.out.println("[MqttFx.subscribe] Not subscribed to topic, mqtt client either null or not connected");	
			return false;
		}
		
	}
	
	public void publish(String message) throws MqttPersistenceException, MqttException
	{
		if(mqttClient != null && mqttClient.isConnected())
		{

			System.out.println("[MqttFx.publish] Publishing message: "+message);
	        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
	        mqttMessage.setQos(qos);
	        mqttClient.publish(topic, mqttMessage);
	        System.out.println("[MqttFx.publish] Message published");
						
		} 
		else
		{
			System.out.println("[MqttFx.publish] Message not published, mqtt client either null or not connected");	
		}
	}
	
	public void publish(String topic, byte[] payload) throws MqttPersistenceException, MqttException
	{
		if(mqttClient != null && mqttClient.isConnected())
		{			
					mqttClient.publish(topic,payload,2,false);
			        System.out.println("[MqttFx.publish] Message published");		
		}  
		else
		{
			 System.out.println("[MqttFx.publish] Message not published, mqtt client either null or not connected");	
		}
	}
	
	public void disconnect() throws MqttException
	{
		if(mqttClient != null && mqttClient.isConnected())
		{	
			mqttClient.disconnect();				
			System.out.println("[MqttFx.close] Client disconnected");
		}
	}
	
	public void close() throws MqttException
	{
		disconnect();
		if(mqttClient != null)
		{
			mqttClient.close();			
			mqttClient = null;
			System.out.println("[MqttFx.close] Client closed");
		}
		
	}
}
