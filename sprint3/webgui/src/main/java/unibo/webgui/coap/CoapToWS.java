package unibo.webgui.coap;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import unibo.basicomm23.utils.CommUtils;
import unibo.webgui.utils.HoldResponseParser;
import unibo.webgui.ws.WSHandler;
import javax.annotation.PostConstruct;

@Component
public class CoapToWS {

    private static final String COAP_ENDPOINT = "coap://127.0.0.1:8000/ctx_cargoservice/hold";

    private CoapClient client;
    private CoapObserveRelation observeRelation;

    @Autowired
    private WSHandler wsHandler;

    @PostConstruct
    public void init() {
        client = new CoapClient(COAP_ENDPOINT);
        observeRelation = client.observe(new CoapHandler() {
            @Override
            public void onLoad(CoapResponse response) {
                String content = response.getResponseText();
                CommUtils.outblue("CoAP payload: " + content);

                try {
                    JSONObject payload = HoldResponseParser.parseHoldState(content);
                    if (payload != null) {
                        wsHandler.sendToAll(payload.toString());
                    } else {
                    	CommUtils.outred("Evento CoAP non valido: " + content);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {
                System.err.println("Errore nell'osservazione CoAP.");
            }
        });
        System.out.println("Iniziata osservazione CoAP su: " + COAP_ENDPOINT);
    }
}

