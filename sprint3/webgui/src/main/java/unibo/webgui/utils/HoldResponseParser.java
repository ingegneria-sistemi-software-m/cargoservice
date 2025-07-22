package unibo.webgui.utils;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class HoldResponseParser {

    //Parsa una stringa JSON completa (senza wrapper come "hold_update(...)") 
	//e restituisce un oggetto JSON nel formato leggibile dalla webgui.
     
	
    public static JSONObject parseHoldState(String message) {
        	
            JSONObject payload = new JSONObject();

            if (message.startsWith("hold_update(") && message.endsWith(")")) {
                
                String jsonString = message.substring(
                    "hold_update(".length(), 
                    message.length() - 1 // Rimuovi il wrapper "hold_update(" e la parentesi finale ")"
                );

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
                
            } else {
                System.err.println("Formato non valido -> msg received: " + message);
            }
    		return payload;
           
        }
    }