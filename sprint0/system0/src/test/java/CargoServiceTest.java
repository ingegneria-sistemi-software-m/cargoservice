package test.java;

import org.junit.Test;
import static org.junit.Assert.*;

import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.ConnectionFactory;

public class CargoServiceTest {
	private Interaction conn = ConnectionFactory.createClientSupport23(ProtocolType.tcp, "localhost", "8000");
	
    // Scenario di test 1: Richiesta di intervento di carico accettata da cargoservice
    @Test
    public void testLoadRequestAccepted() throws Exception {
        //Costruzione di richiesta con PID valido.

        String requestStr = CommUtils.buildRequest("tester",
                "load_product", "load_product(10)", 
                "cargoservice").toString();
        
        System.out.println("Richiesta: " + requestStr);
        
        //Risposta accettata perchè peso legato al PID inferiore di MaxLoad 
        String response = conn.request(requestStr);
        
        System.out.println("Risposta: " + response); // Risposta contenente lo slot libero dove posizionare il container
        
        //Verifica che sia stata accettata
        assertTrue("TEST: richiesta accettata", 
                 response.contains("load_accepted"));
    }

    //Scenario di test 2: Doppia richiesta di intervento di carico accettata

    @Test
    public void testDoubleLoadRequest() throws Exception {
	    // Costruzione della prima richiesta con PID valido.
	    String request1 = CommUtils.buildRequest("tester",
	            "load_product", "load_product(9)", 
	            "cargoservice").toString();
	
	    //Risposta accettata perchè il peso legato al PID è inferiore di MaxLoad 
	    String response1 = conn.request(request1);
	    System.out.println("Risposta: " + response1); // Risposta contenente lo slot libero dove posizionare il container
	    assertTrue("TEST: Prima richiesta accettata", 
	             response1.contains("load_accepted")); 
	    
	   // Costruzione della seconda richiesta con PID valido.
	    String request2 = CommUtils.buildRequest("tester",
	            "load_product", "load_product(10)", 
	            "cargoservice").toString();
	
	    //Risposta positiva perchè il peso legato al PID è inferiore di MaxLoad 
	    String response2 = conn.request(request2);
	    System.out.println("Risposta: " + response2); // Risposta contenente lo slot libero dove posizionare il container
	    assertTrue("TEST: Seconda richiesta accettata", 
	    		response2.contains("load_accepted"));
    }


    //Scenario di test 3: richiesta di intervento di carico rifiutata a causa della mancanza di slot libero
    @Test
    public void testLoadRequestDeniedNoAvailableSlots() throws Exception {
    //Costruisci la richiesta con un PID valido.
    String requestStr = CommUtils.buildRequest("tester",
            "load_product", "load_product(20)",
            "cargoservice").toString();
    
    System.out.println("Richiesta: " + requestStr);
    
    //Risposta negativa a causa della non presenza di slot liberi.
    String response = conn.request(requestStr);
    
    System.out.println("Risposta: " + response); // Risposta contenente la causa del rifiuto dell'intervento di carico
    
    //Verifica che sia rifiutata per mancanza di slot
    assertTrue("TEST: richiesta rifiutata per slot pieni",
            response.contains("load_refused") && 
            response.contains("no_slot_liberi"));
    }

    // Scenario di test 4: Richiesta di intervento di carico rifiutata a causa del peso eccessivo del container.
    @Test
    public void testLoadRequestDeniedByWeight() throws Exception {
        //Costruisci la richiesta con PID valido.
        String requestStr = CommUtils.buildRequest("tester",
                "load_product", "load_product(11)",
                "cargoservice").toString();
        
        System.out.println("Richiesta: " + requestStr);
        
        //Risposta negativa perchè il peso legato al PID è superiore a MaxLoad
        String response = conn.request(requestStr);
        
        System.out.println("Risposta: " + response); //Risposta contenente la causa del rifiuto dell'intervento di carico
        
        // 3. Verifica che sia stata rifiutata per il peso eccessivo
        assertTrue("TEST: richiesta rifiutata", 
                 response.contains("load_refused") && 
                 response.contains("overweight"));
    }
}