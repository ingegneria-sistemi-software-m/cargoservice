package unibo.webgui.service;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.ConnectionFactory;
import unibo.webgui.utils.HoldResponseParser;
import unibo.webgui.ws.WSHandler;

@RestController
public class HoldStateService {

    @Autowired
    private WSHandler wsHandler;

    private Interaction conn;

    public HoldStateService() {
        try {
            conn = ConnectionFactory.createClientSupport23(ProtocolType.tcp, "127.0.0.1", "8000");
        } catch (Exception e) {
            System.err.println("Errore nella connessione TCP iniziale: " + e.getMessage());
        }
    }

    @GetMapping("/holdstate")
    public String getHoldState() {
        try {
            IApplMessage request = CommUtils.buildRequest("webgui", "get_hold_state", "get_hold_state(X)", "hold");
            IApplMessage response = conn.request(request);
            System.out.println(response.msgContent());
            
            String jsonString = response.msgContent().substring(
                    "'hold_state(".length(), 
                    response.msgContent().length() - 2 // Rimuovi il wrapper "'hold_update(" e il suffisso ")'"
                );
            
            System.out.println(jsonString);

            JSONObject payload = HoldResponseParser.parseHoldState(jsonString);
            if (payload != null) {
                wsHandler.sendToAll(payload.toString());
                return payload.toString();
            } else {
                return "{\"error\":\"payload nullo\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\":\"" + e.getMessage() + "\"}";
        }
    }
}
