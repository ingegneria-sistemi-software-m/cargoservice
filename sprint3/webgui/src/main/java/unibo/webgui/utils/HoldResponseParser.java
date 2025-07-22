package unibo.webgui.utils;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class HoldResponseParser {
	

    // Parsa una stringa JSON completa e restituisce un oggetto JSON 
	// nel formato leggibile dalla webgui.
    public static JSONObject parseHoldState(String message) {
		// TODO: togli questo quando sei sicuro. 
		// I messaggi dovrebbero arrivare già sotto forma di json stringified
		// alla peggio le risposte a get_hold_state arrivano racchiuse tra apici      
//        // update
//        if (message.startsWith("hold_update(") && message.endsWith(")")) {
//            String jsonString = message.substring(
//                "hold_update(".length(), 
//                message.length() - 1 // Rimuovi il wrapper "hold_update(" e la parentesi finale ")"
//            );
//
//            JSONObject original = new JSONObject(jsonString);
//            int currentLoad = original.getInt("currentLoad");
//            JSONObject slotsObject = original.getJSONObject("slots");
//
//            List<String> slotStatusList = new ArrayList<>();
//            for (int i = 1; i <= 4; i++) {
//                String slotKey = "slot" + i;
//                String status = slotsObject.getString(slotKey);
//                slotStatusList.add(status.equals("occupied") ? "pieno" : "libero");
//            }
//
//            payload.put("shipLoad", currentLoad);
//            payload.put("slots", slotStatusList);
//        } 
//        // richiesta
//        else if (message.startsWith("'hold_state(") && message.endsWith(")'")) {
//        	String jsonString = message.substring(
//                "'hold_state(".length(), 
//                message.length() - 2 // Rimuovi il wrapper "'hold_update(" e il suffisso ")'"
//            );
//
//            JSONObject original = new JSONObject(jsonString);
//            int currentLoad = original.getInt("currentLoad");
//            JSONObject slotsObject = original.getJSONObject("slots");
//
//            List<String> slotStatusList = new ArrayList<>();
//            for (int i = 1; i <= 4; i++) {
//                String slotKey = "slot" + i;
//                String status = slotsObject.getString(slotKey);
//                slotStatusList.add(status.equals("occupied") ? "pieno" : "libero");
//            }
//
//            payload.put("shipLoad", currentLoad);
//            payload.put("slots", slotStatusList);
//          
//        }
//        else {
//            System.err.println("Formato non valido -> msg received: " + message);
//        }
    	
    	
        String jsonString = null;
		JSONObject payload = new JSONObject();
		
        // tolgo eventuali apici
		if (message.startsWith("'") && message.endsWith("'")) {
			jsonString = message.substring(1, message.length()-1);
		} 
		else if(message.startsWith("{")) {
			jsonString = message;
		}
		else {
			// TODO: messaggio spurio di coap, c'è da capire come gestirlo
		}
		
		// costruisco un oggetto con slot e peso
		System.out.println(jsonString);
		JSONObject original = new JSONObject(jsonString);
		int currentLoad = original.getInt("currentLoad");
		JSONObject slotsObject = original.getJSONObject("slots");
	
		List<String> slotStatusList = new ArrayList<>();
		for (int i = 1; i <= 4; i++) {
			String slotKey = "slot" + i;
			String status = slotsObject.getString(slotKey);
			slotStatusList.add(status.equals("occupied") ? "pieno" : "libero");
		}
		
		payload.put("shipLoad", currentLoad);
		payload.put("slots", slotStatusList);
            
		return payload;
    }
}