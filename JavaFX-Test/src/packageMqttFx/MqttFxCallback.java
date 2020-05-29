package packageMqttFx;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttFxCallback implements MqttCallback {

	@Override
	public void connectionLost(Throwable cause) {
		System.out.println("[MqttFxCallback.connectionLost] Connection lost");
		
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.out.println("[MqttFxCallback.messageArrived] Message arrived");
		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		System.out.println("[MqttFxCallback.deliveryComplete] Delivery complete");
		
	}

}
