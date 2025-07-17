package test.java;

import org.junit.Test;
import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.BeforeClass;


import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.ConnectionFactory;

public class HoldTest {
    private static Interaction conn;

    @BeforeClass
    public static void setup() {
        conn = ConnectionFactory.createClientSupport23(ProtocolType.tcp, "localhost", "8000");
    }

    // Scenario Test 1: Test prenotazione riuscita 
    
    @Test
    public void testReserveSlotSuccess() throws Exception {
        
        String requestStr = CommUtils.buildRequest("tester",
                "reserve_slot", "reserve_slot(100)", 
                "hold").toString();
        
        System.out.println("Richiesta Test 1: " + requestStr);
        
        String response = conn.request(requestStr);
        
        System.out.println("Risposta Test 1: " + response);
        
        assertTrue("TEST 1: prenotazione riuscita", 
                 response.contains("reserve_slot_success"));
    }

    
    
    //Scenario Test 2: Test intervento di carico rifiutato per superamento di MaxLoad
    
    @Test
    public void testReserveSlotFailExceedsMaxLoad() throws Exception {
        
        String requestStr = CommUtils.buildRequest("tester",
                "reserve_slot", "reserve_slot(600)", 
                "hold").toString();
        
        System.out.println("Richiesta Test 2: " + requestStr);
        
        String response = conn.request(requestStr);
        
        System.out.println("Risposta Test 2: " + response);
        
        assertTrue("TEST 2: Exceeds MaxLoad", 
                 response.contains("reserve_slot_fail") && 
                 response.contains("'Exceeds MaxLoad'"));
    }

    

    // Scenario Test 4: Richiesta dello stato corrente del deposito
    @Test
    public void testGetHoldState() throws Exception {
        String requestStr = CommUtils.buildRequest("tester",
                "get_hold_state", "get_hold_state(X)", 
                "hold").toString();
        
        System.out.println("Richiesta Test 4: " + requestStr);
        
        String response = conn.request(requestStr);
        
        System.out.println("Risposta Test 4: " + response);
        
        assertTrue("TEST 4: stato hold restituito correttamente", 
                 response.contains("hold_state") &&  
                 response.contains("currentLoad") && 
                 response.contains("slots"));
    }

    // Scenario Test 5: Verifica aggiornamento dello stato dopo una prenotazione
    @Test
    public void testStateUpdateAfterReservation() throws Exception {
       
    	
        String stateRequest = CommUtils.buildRequest("tester",
                "get_hold_state", "get_hold_state(X)", 
                "hold").toString();
        
        String initialState = conn.request(stateRequest);
        System.out.println("Test 5 --> Stato iniziale: " + initialState);
        
        int initialLoad = extractCurrentLoad(initialState);  //Carico attuale della nave
        
        int reservationWeight = 150;  	//Peso del container nuovo
        
        String reserveRequest = CommUtils.buildRequest("tester",
                "reserve_slot", "reserve_slot("+reservationWeight+")", 
                "hold").toString();
        
        String reserveResponse = conn.request(reserveRequest);
        System.out.println("Test 5 --> Risposta prenotazione: " + reserveResponse);
        
        String updatedState = conn.request(stateRequest);
        System.out.println("Test 5 --> Stato aggiornato: " + updatedState);
        
        int updatedLoad = extractCurrentLoad(updatedState);		//Peso dopo intervento di carico
        
        assertFalse("TEST 5: stato dovrebbe essere diverso dopo prenotazione", 
                initialState.equals(updatedState));
        assertEquals("TEST 5: currentLoad dovrebbe essere aumentato di " + reservationWeight, 
                 initialLoad + reservationWeight, updatedLoad);
    }
    
  //Scenario Test 3: Prenotazione fallita data da nessuno slot libero
    @Test
    public void testReserveSlotFailNoAvailableSlots() throws Exception {
       
        String state = conn.request(CommUtils.buildRequest("tester",
                "get_hold_state", "get_hold_state(X)", 
                "hold").toString());
        
        JSONObject slots = extractSlots(state);
        for (String slot : slots.keySet()) {
            if (slots.getString(slot).equals("free")) {
            	 
            	String requestStr = CommUtils.buildRequest("tester",
                         "reserve_slot", "reserve_slot(50)", 
                         "hold").toString();
                 
                 System.out.println("Richiesta Test 3: " + requestStr);
                 
                 String response = conn.request(requestStr);
                 
                 System.out.println("Risposta Test 3: " + response);
            }
        }  // Occupa gli slot liberi ai fini del test
        
        String response = conn.request(CommUtils.buildRequest("tester",
                "reserve_slot", "reserve_slot(50)", 
                "hold").toString());
        
        assertTrue("TEST 3: prenotazione dovrebbe fallire", 
                 response.contains("reserve_slot_fail"));
        assertTrue("TEST 3: motivo dovrebbe essere 'All slots are occupied'", 
                 response.contains("'All slots are occupied'"));
    }
    
    
 // Estrae il carico attuale della nave dalla risposta JSON
    private int extractCurrentLoad(String jsonResponse) {
        try {
           
            String jsonPart = jsonResponse.substring(
                    jsonResponse.indexOf("{"),
                    jsonResponse.lastIndexOf("}") + 1);
            
            JSONObject json = new JSONObject(jsonPart);
            return json.getInt("currentLoad");
        } catch (Exception e) {
            fail("Errore nell'estrazione del currentLoad: " + e.getMessage());
            return -1;
        }
    }
    
 // Estrae la lista degli slot della nave dalla risposta JSON
    
    private JSONObject extractSlots(String jsonResponse) throws Exception {
        String jsonPart = jsonResponse.substring(
            jsonResponse.indexOf("{"),
            jsonResponse.lastIndexOf("}") + 1);
        JSONObject json = new JSONObject(jsonPart);
        return json.getJSONObject("slots");
    }
}