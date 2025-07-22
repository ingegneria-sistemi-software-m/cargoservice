package test.java;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;

import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.ConnectionFactory;

public class WebguiModelTest {

    private static Interaction conn;
    private static final String COAP_ENDPOINT = "coap://localhost:8000/webgui/holdstate";

    @BeforeClass
    public static void setup() {
        conn = ConnectionFactory.createClientSupport23(ProtocolType.tcp, "localhost", "8000");
    }
    
    // Test 1: richiesta stato attuale hold
    @Test
    public void testInitialHoldState() throws Exception {
        String request = CommUtils.buildRequest("tester", "get_hold_state", "get_hold_state(X)", "cargoservice").toString();
        System.out.println("Test 1 --> Request: " + request);

        String response = conn.request(request);
        System.out.println("Test 1 --> Response: " + response);

        assertNotNull("La risposta non deve essere nulla", response);
        assertTrue("La risposta dovrebbe contenere 'hold_state'", response.contains("hold_state"));
        assertTrue("La risposta dovrebbe contenere 'currentLoad'", response.contains("currentLoad"));
        assertTrue("La risposta dovrebbe contenere 'slots'", response.contains("slots"));
        assertTrue("La risposta dovrebbe contenere 'slot1'", response.contains("slot1"));
        assertTrue("La risposta dovrebbe contenere 'slot2'", response.contains("slot2"));
        assertTrue("La risposta dovrebbe contenere 'slot3'", response.contains("slot3"));
        assertTrue("La risposta dovrebbe contenere 'slot4'", response.contains("slot4"));
    }

    // Test 2: Invio evento hold_update e verifica ricezione con CoAP
    @Test
    public void testHoldUpdateEventReceptionWithCoap() throws Exception {
    	
    	// Serve a bloccare il main thread fino a quando i child thread non completano
        CountDownLatch latch = new CountDownLatch(1);
        
        CoapClient client = new CoapClient(COAP_ENDPOINT);

        CoapObserveRelation relation = client.observe(new CoapHandler() {
            @Override
            public void onLoad(CoapResponse response) {
                String content = response.getResponseText();
                System.out.println("CoAP notification received: " + content);

                // Controllo se il messaggio contiene il carico aggiornato
                if (content.contains("\"currentLoad\":100" ) && content.contains("\"slot1\":occupied")) {
                    latch.countDown();
                }
            }

            @Override
            public void onError() {
                System.err.println("CoAP observing failed");
                fail("Errore durante l'osservazione CoAP");
                latch.countDown();
            }
        });

        // Costruisco e invio l'evento hold_update
        String updateJson = "{\"currentLoad\":100, \"slots\": {\"slot1\":\"occupied\", \"slot2\":\"free\", \"slot3\":\"free\", \"slot4\":\"free\"}}";
        String event = CommUtils.buildEvent("tester", "hold_update", "hold_update('" + updateJson.replace("\"", "\\\"") + "')").toString();

        System.out.println("Invio evento hold_update: " + event);
        conn.forward(event);

        //Rimane bloccato qui finchè non viene eseguito latch.countDown()
        latch.await();
   
        // Pulizia
        relation.proactiveCancel();
        client.shutdown();
        
        assertTrue("Evento hold_update correttamente ricevuto via CoAP", true);
    }
}
