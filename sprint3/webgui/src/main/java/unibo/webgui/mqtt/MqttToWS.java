//package unibo.webgui.mqtt;
//
//import org.eclipse.paho.client.mqttv3.*;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import unibo.webgui.ws.WSHandler;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.annotation.PostConstruct;
//
//@Component
//public class MqttToWS implements MqttCallback {
//
//    private static final String BROKER_URL = "tcp://192.168.1.53:1883";
//    private static final String CLIENT_ID = "WebGuiClient";
//    private static final String TOPIC = "hold/updates";
//
//    private MqttClient client;
//
//    @Autowired
//    private WSHandler wsHandler;
//
//    @PostConstruct
//    public void init() {
//        try {
//            client = new MqttClient(BROKER_URL, CLIENT_ID);
//            MqttConnectOptions options = new MqttConnectOptions();
//            options.setCleanSession(true);
//
//            client.setCallback(this);
//            client.connect(options);
//
//            client.subscribe(TOPIC);
//            System.out.println("Sottoscritto al topic: " + TOPIC);
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void connectionLost(Throwable cause) {
//        System.out.println("Connessione MQTT persa: " + cause.getMessage());
//    }
//
//    @Override
//    public void messageArrived(String topic, MqttMessage message) {
//        try {
//            String rawMsg = new String(message.getPayload());
//            System.out.println("RAW MQTT payload: " + rawMsg);
//
//            // Controlla che il messaggio
//            if (rawMsg.startsWith("hold_update(") && rawMsg.endsWith(")")) {
//                
//                String jsonString = rawMsg.substring(
//                    "hold_update(".length(), 
//                    rawMsg.length() - 1 // Rimuovi il wrapper "hold_update(" e la parentesi finale ")"
//                );
//
//                System.out.println("Extracted JSON: " + jsonString);
//
//                JSONObject original = new JSONObject(jsonString);
//                int currentLoad = original.getInt("currentLoad");
//                JSONObject slotsObject = original.getJSONObject("slots");
//
//                List<String> slotStatusList = new ArrayList<>();
//                for (int i = 1; i <= 4; i++) {
//                    String slotKey = "slot" + i;
//                    String status = slotsObject.getString(slotKey);
//                    slotStatusList.add(status.equals("occupied") ? "pieno" : "libero");
//                }
//
//                JSONObject payload = new JSONObject();
//                payload.put("shipLoad", currentLoad);
//                payload.put("slots", slotStatusList);
//
//                wsHandler.sendToAll(payload.toString());
//            } else {
//                System.err.println("Formato del messaggio MQTT non valido.");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    
//    @Override
//    public void deliveryComplete(IMqttDeliveryToken token) {
//    }
//}
