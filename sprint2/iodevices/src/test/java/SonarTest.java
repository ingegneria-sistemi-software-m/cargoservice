package test.java;

import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.BeforeClass;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.ConnectionFactory;

public class SonarTest {
	private static Interaction conn;

	@BeforeClass
	public static void setup() {
	    conn = ConnectionFactory.createClientSupport23(ProtocolType.tcp, "localhost", "8001");
	}
	
	
    // Scenario 1: container presente per 3 secondi
    @Test
    public void testLoadRequestAccepted() throws Exception {
        //Costruzione di richiesta con PID valido.

        IApplMessage measurement = CommUtils.buildEvent("tester",
                "measurement", "measurement(10)");
        
        conn.forward(measurement);
        conn.forward(measurement);
        conn.forward(measurement);
        
        IApplMessage res = conn.receive(); // non riceve gli eventi... NON VOGLIO USARE COAP/MQTT
        System.out.println(res);
        
        
        //Verifica che sia stata accettata
        assertTrue("TEST: container presente", 
                 res.toString().contains("container_arrived"));
    }
}