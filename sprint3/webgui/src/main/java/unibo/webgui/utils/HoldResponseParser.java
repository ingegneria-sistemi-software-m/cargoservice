package unibo.webgui.utils;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class HoldResponseParser {
	

    // Parsa una stringa JSON completa e restituisce un oggetto JSON 
	// nel formato leggibile dalla webgui.
    public static JSONObject parseHoldState(String message) {
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