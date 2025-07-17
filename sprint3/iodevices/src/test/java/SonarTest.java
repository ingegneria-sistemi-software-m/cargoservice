package test.java;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.junit.Before;
import org.junit.BeforeClass;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.ConnectionFactory;

public class SonarTest {
	private static final String CoapEndopoint = "coap://localhost:8001/ctx_iodevices/measure_processor";
	private static Interaction conn;
	
	@Before
	public void setup() {
	    conn = ConnectionFactory.createClientSupport23(ProtocolType.tcp, "localhost", "8001");
	}
	
	
	// Scenario 1: container presente per 3 secondi
    @Test
    public void testContainerArrived() throws Exception {
    	// versione Java dei waitgroup di Go.
    	// Serve a bloccare il main thread fino a quando 
    	// i child thread non completano
        CountDownLatch latch = new CountDownLatch(1);
        
    	// osservo il coap endpoint per ricevere gli eventi di reazione 
    	// agli eventi che genero nel test
    	CoapClient client = new CoapClient(SonarTest.CoapEndopoint);  
  	    CoapObserveRelation relation = client.observe(
			new CoapHandler() {
				@Override
				public void onLoad(CoapResponse response) {
					String content = response.getResponseText();
					CommUtils.outgreen("ActorObserver | value=" + content );
					
					assertTrue("TEST: container_arrived non ricevuto", content.contains("container_arrived"));
					
					latch.countDown();
				}					
				@Override
				public void onError() {
					CommUtils.outred("OBSERVING FAILED");
					
					fail("errore nella osservazione del sonar");
					
					latch.countDown();
				}
			}
		);	
  	    
  	    // container presente per tre misurazioni
    	IApplMessage measurement = CommUtils.buildEvent("tester",
    										"measurement", "measurement(10)");
		conn.forward(measurement);
		conn.forward(measurement);
		conn.forward(measurement);
  	    
		// Aspetto la risposta del coap endpoint.
		// latch.await() restituisce false se scade il timeout
		boolean arrived = latch.await(5, TimeUnit.SECONDS);
	    relation.proactiveCancel();
	    client.shutdown();
	    // verifico anche che il timeout non sia scaduto
	    assertTrue("onLoad non è stato invocato entro il timeout", arrived);
    }
    
    
    // Scenario 2: container assente per 3 secondi e poi presente per 3 secondi
    @Test
    public void testContainerArrivedThenAbsent() throws Exception {
    	// versione Java dei waitgroup di Go.
    	// Serve a bloccare il main thread fino a quando 
    	// i child thread non completano
        CountDownLatch latch = new CountDownLatch(2);
        
    	// osservo il coap endpoint per ricevere gli eventi di reazione 
    	// agli eventi che genero nel test
    	CoapClient client = new CoapClient(SonarTest.CoapEndopoint);  
  	    CoapObserveRelation relation = client.observe(
			new CoapHandler() {
				int counter = 0;
				@Override
				public void onLoad(CoapResponse response) {
					String content = response.getResponseText();
					CommUtils.outgreen("ActorObserver | value=" + content );
					
					if(counter==1) {
						System.out.println("desiderato: presente; ricevuto: "+content);
						assertTrue("TEST: container_arrived non ricevuto", content.contains("container_arrived"));
					}
					else if(counter==2) {
						System.out.println("desiderato: assente; ricevuto: "+content);
						assertTrue("TEST: container_absent non ricevuto dopo container_arrived", content.contains("container_absent"));
					}
					latch.countDown();
					
					counter++;
				}					
				@Override
				public void onError() {
					CommUtils.outred("OBSERVING FAILED");
					
					fail();
					
					latch.countDown();
					counter++;
				}
			}
		);	
  	    
  	    // container presente per tre misurazioni
    	IApplMessage present_measurement = CommUtils.buildEvent("tester",
    										"measurement", "measurement(10)");
    	IApplMessage absent_measurement = CommUtils.buildEvent("tester",
											"measurement", "measurement(20)");
		
		conn.forward(absent_measurement);
		conn.forward(absent_measurement);
		conn.forward(absent_measurement);
		conn.forward(present_measurement);
		conn.forward(present_measurement);
		conn.forward(present_measurement);
		
		// Aspetto la risposta del coap endpoint.
		// latch.await() restituisce false se scade il timeout
		boolean arrived = latch.await(5, TimeUnit.SECONDS);
	    relation.proactiveCancel();
	    client.shutdown();
	    // verifico anche che il timeout non sia scaduto
	    assertTrue("onLoad non è stato invocato entro il timeout", arrived);
    }
    
    
    // Scenario 3: rilevazione guasto e ripristino
    @Test
    public void testFaultySonarAndRecovery() throws Exception {
    	// versione Java dei waitgroup di Go.
    	// Serve a bloccare il main thread fino a quando 
    	// i child thread non completano
        CountDownLatch latch = new CountDownLatch(2);
        
    	// osservo il coap endpoint per ricevere gli eventi di reazione 
    	// agli eventi che genero nel test
    	CoapClient client = new CoapClient(SonarTest.CoapEndopoint);  
  	    CoapObserveRelation relation = client.observe(
			new CoapHandler() {
				int counter = 0;
				@Override
				public void onLoad(CoapResponse response) {
					String content = response.getResponseText();
					CommUtils.outgreen("ActorObserver | value=" + content );
					
					if(counter==0) {
						System.out.println("desiderato: guasto; ricevuto: "+content);
						assertTrue("TEST: guasto non ricevuto", content.contains("guasto"));
					}
					else if(counter==1) {
						System.out.println("desiderato: ripristinato; ricevuto: "+content);
						assertTrue("TEST: ripristino non ricevuto", content.contains("ripristinato"));
					}
					
					latch.countDown();
					
					counter++;
				}					
				@Override
				public void onError() {
					CommUtils.outred("OBSERVING FAILED");
					
					fail();
					
					latch.countDown();
					counter++;
				}
			}
		);	
  	    
  	    // container presente per tre misurazioni
    	IApplMessage guasto_measurement = CommUtils.buildEvent("tester",
    										"measurement", "measurement(31)");
    	IApplMessage recovery_measurement = CommUtils.buildEvent("tester",
											"measurement", "measurement(20)");
		
		conn.forward(guasto_measurement);
		conn.forward(guasto_measurement);
		conn.forward(guasto_measurement);
		conn.forward(recovery_measurement);

		
		// Aspetto la risposta del coap endpoint.
		// latch.await() restituisce false se scade il timeout
		boolean arrived = latch.await(5, TimeUnit.SECONDS);
	    relation.proactiveCancel();
	    client.shutdown();
	    // verifico anche che il timeout non sia scaduto
	    assertTrue("onLoad non è stato invocato entro il timeout", arrived);
    }
}