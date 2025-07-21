package unibo.webgui.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.ConnectionFactory;
import unibo.webgui.utils.HoldResponseParser;
import unibo.webgui.ws.WSHandler;
import javax.annotation.PostConstruct;

@Component
public class HoldStateInitializer {

    @Autowired
    private WSHandler wsHandler;

    private Interaction conn;

    @PostConstruct
    public void init() {
        try {
            conn = ConnectionFactory.createClientSupport23(ProtocolType.tcp, "127.0.0.1", "8000");

            String request = CommUtils.buildRequest("webgui", "get_hold_state", "get_hold_state(X)", "hold").toString();
            String response = conn.request(request);
//          System.out.println("HoldStateInitializer risposta ricevuta: " + response);

            JSONObject payload = HoldResponseParser.parseHoldState(response);
            if (payload != null) {
                wsHandler.sendToAll(payload.toString());
            }

        } catch (Exception e) {
            System.err.println("Errore HoldStateInitializer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
