# Sprint 2


## Punto di partenza
Nello [sprint 1](https://github.com/ingegneria-sistemi-software-m/cargoservice/blob/master/sprint1) si sono implementati i componenti che definiscono il corebuisness del sistema: [_cargoservice_](https://github.com/ingegneria-sistemi-software-m/cargoservice/blob/master/sprint1/README.md#analisi-del-problema--cargoservice) e [_cargorobot_](https://github.com/ingegneria-sistemi-software-m/cargoservice/blob/master/sprint1/README.md#analisi-del-problema--cargorobot). 

Nel far questo si sono anche definite le interfaccie per i componenti _hold_ e _sonar_ da svilupparsi in questo sprint.

L'architettura del sistema risultante dallo sprint 1 è la seguente.

<img src="../sprint1/arch1.png"/>

<div class="page-break"></div>


## Obiettivi
L'obiettivo dello sprint 2 sarà affrontare il sottoinsieme dei requisiti relativi ai componenti _sonar_ e _hold_, effettuando l'analisi del problema e la progettazione. Particolare importanza verrà data alle **interazioni** che questi componenti dovranno avere con il resto del sistema.


<!-- togli la roba tra parentesi  -->

I [requisiti](https://github.com/ingegneria-sistemi-software-m/cargoservice/tree/master/requirements) affrontati nello sprint 2 saranno i seguenti:
- implementare un sistema in grado di rilevare la presenza/assenza di un _container_ presso l'_IO-port_
    - ( 2. is able to detect (by means of the sonar sensor) the presence of the product container at the ioport )
- implementare un sistema in grado di rilevare e gestire malfunzionamenti del sonar
    - ( 5. interrupts any activity and turns on a led if the sonar sensor measures a distance D > DFREE for at least 3 secs (perhaps a sonar failure). The service continues its activities as soon as the sonar measures a distance D <= DFREE. ) 
- implementare un sistema in grado di tenere traccia dello stato del deposito. Questo include:
    - lo stato libero/occupato di ogni _slot_
    - il peso totale dei container caricati all'interno del deposito
    - (The request is rejected when:
            - the product-weight is evaluated too high, since the ship can carry a maximum load of MaxLoad>0  kg.
            - the hold is already full, i.e. the 4 slots are alrready occupied.)
- implementare un sistema in grado di condividere con la _web-gui_ (o a qualunque altro componente interessato) lo stato del deposito
    - (4. is able to show the current state of the hold, by mesans of a dynamically updated web-gui.)




## Analisi del problema | Sonar
L'attore _sonar_ è responsabile di effettuare **periodicamente** delle misurazioni di distanze allo scopo di rilevare la presenza dei container che arrivano all'_IO-port_.  

Il tipico ciclo di attività di _sonar_ è il seguente:
1. _sonar_ effettua una misurazione _m_ comandando il sensore fisico.

2. _sonar_ controlla in quale intervallo ricade _m_, le possibilità sono tre:
    - _0 < m < DFREE/2_
    - _DFREE/2 <= m <= DFREE_
    - _m > DFREE_

3. _sonar_ considera l'intervallo a cui appartiene _m_, e le misurazioni precedenti effettuate **negli ultimi 3 secondi**, per decidere cosa fare. Le possibilità sono quattro:
    - se le misurazioni effettuate sono state consistentemente _> 0_ e _< DFREE/2_, **significa che è presente un _container_** presso l'IO-port e _sonar_ notifica questo evento al resto del sistema.
    - se le misurazioni effettuate sono state consistentemente _>= DFREE/2_ e  _<= DFREE_, **significa che NON è presente un _container_** presso l'_IO-port_ e _sonar_ notifica questo evento al resto del sistema.  
    - se le misurazioni effettuate sono state consistentemente _> DFREE_, **significa che il sonar fisico è guasto** e _sonar_ (il componente software) **emette l'evento 'interrompi_tutto'** introdotto nello sprint 1 per interrompere le attività del resto del sistema.
    - se le misurazioni effettuate NON sono state consistenti, **non si può dedurre nulla**. _sonar_ non fa nulla.
    
4.  solo nel caso in cui la misurazione corrente _m_ abbia portato al passo precedente alla rilevazione di un guasto nel sonar fisico, **_sonar_ cambia di stato** e attende la prima misurazione _m' < DFREE_ prima di tornare ad uno stato di corretto funzionamento. All'arrivo della misurazione _m'_, _sonar_ fa ripartire il resto del sistema **emettendo l'evento 'riprendi_tutto'** introdotto nello sprint 1.



### Considerazioni
Il ciclo di attività dell'attore _sonar_ è divisibile in due fasi:
- fase di recupero della misurazione
- fase di processamento della misurazione

Risulta quindi possibile separare _sonar_ in **due attori distinti**, uno per fase. Questo porta ad avere come vantaggio il poter **produrre misurazioni fittizzie** sostituendo l'attore che recupera le misurazioni dal sonar fisico con un attore mock, oppure con una test unit, rendendo facilmente testabile la logica di processamento. 



### Problematiche
L'analisi fatta fino ad ora fa sorgere le seguenti domande.

#### Come fa _sonar_ a comandare il sonar fisico per ottenere le misurazioni?
Il caro committente ha fornito uno script python che fa proprio questo. Più nel dettaglio, lo script fornito comanda i **pin GPIO** di un **Raspberry PI** a cui il sonar fisico è collegato, ottenendo **una misurazione al secondo**.

```python
# File: sonar.py
import RPi.GPIO as GPIO
import time
import sys

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
TRIG = 17
ECHO = 27

GPIO.setup(TRIG, GPIO.OUT)
GPIO.setup(ECHO, GPIO.IN)
GPIO.output(TRIG, False)   # TRIG parte LOW

print ('Waiting a few seconds for the sensor to settle')
time.sleep(2)

while True:
    GPIO.output(TRIG, True)    #invia impulso TRIG
    time.sleep(0.00001)
    GPIO.output(TRIG, False)

    pulse_start = time.time()
    #attendi che ECHO parta e memorizza tempo
    while GPIO.input(ECHO)==0:
        pulse_start = time.time()
    # register the last timestamp at which the receiver detects the signal.
    while GPIO.input(ECHO)==1:
        pulse_end = time.time()
    pulse_duration = pulse_end - pulse_start

    # velocità del suono ~= 340m/s 
    # distanza = v*t 
    # il tempo ottenuto misura un roundtrip -> distanza = v*t/2
    distance = pulse_duration * 17165
    distance = round(distance, 1)
    print ('Distance:', distance,'cm')
    sys.stdout.flush()
    time.sleep(1)
```


#### Come fa _sonar_ a capire se le misurazioni effettuate negli ultimi 3 secondi sono state consistenti?
È evidente che _sonar_ dovrà mantenere delle informazioni nel suo stato riguardanti le misurazioni precedenti. Più nel dettaglio, _sonar_ avrà bisogno di:
- una variabile che conta il numero di misurazioni consistenti effettuate.
- una variabile che indica in quale intervallo è ricaduta la misurazione precedente per capire quale intervallo considerare nel decidere se la misurazione corrente è consistente o meno.

Siccome le misurazioni vengono effettuate una volta al secondo, se il contatore raggiunge il valore 3 questo significa che le misurazioni sono state consistenti per almeno tre secondi e può quindi venire emesso l'evento corrispondente all'intervallo in cui le misurazioni sono ricadute.

Se una misurazione non è consistente, o se le misurazioni sono state consistenti per 3 secondi, il contatore viene resettato.


### Messaggi
_sonar_ emette tutti gli eventi definiti durante l'analisi di _cargorobot_ fatta nello sprint 1

```
Event container_arrived : container_arrived(X) 
Event container_absent  : container_absent(X)  
Event interrompi_tutto  : interrompi_tutto(X)  
Event riprendi_tutto    : riprendi_tutto(X)   
```

Oltre a questi, siccome si è deciso di separare _sonar_ in due attori distinti, si introduce un evento corrispondente ad una misurazione del sonar fisico. 

```
Event measurement 		: measurement(CM)
```


### Modello Sonar
L'analisi fatta fino ha portato al seguente modello.

```Java
QActor sonardevice context ctx_iodevices {
	[# 
		lateinit var reader : java.io.BufferedReader
	    lateinit var p : Process	
	    var Distance = 0
	#]	
	
	State s0 initial{
		println("$name | start") 
	 	[#
			p       = Runtime.getRuntime().exec("python sonar.py")
			reader  = java.io.BufferedReader( 
                        java.io.InputStreamReader(p.getInputStream())
                      )	
		#]		
	}
	Goto readSonarData
	
	State readSonarData{
		[# 
			var data = reader.readLine()
			
			if( data != null ){
				try{ 
					val vd = data.toFloat()
					val v  = vd.toInt()
					
					// filter the data maybe?
					if(v <= 100)
						Distance = v				
					else 
						Distance = 0
				}catch(e: Exception){
					CommUtils.outred("$name readSonarDataERROR: $e")
				}
			}
			
		#]	
		
		if [# Distance > 0 #] { 
		    println("$name | misurato $data cm") color yellow
			emitlocalstream measurement : measurement($Distance)			 
		}
	}
	Goto readSonarData
}




QActor measure_processor context ctx_iodevices {
	import "main.java.IntervalliMisurazioni"

	[# 
		val DFREE = 30 
        // uso degli enumerativi
		var CurrentIntervallo = IntervalliMisurazioni.PRIMA_MISURAZIONE
		var LastIntervallo = IntervalliMisurazioni.PRIMA_MISURAZIONE
		// conta quanti misurazioni di fila sono cadute nello stesso intervallo
		var CounterIntervallo = 1
		// flag che mi dice se sono in uno stato di malfunzionamento
		var Guasto = false
	#]	
	
	State s0 initial{
		println("$name | start") 
	 	subscribeTo sonardevice for measurement
	}
	Goto listen_for_measurement

	
	State listen_for_measurement {
		//aspetto
	}
	Transition t0
		whenEvent measurement -> process_measurement
		
		
	State process_measurement {
		onMsg(measurement : measurement(X)) {
			[# 
				val M = payloadArg(0).toInt()	
				CounterIntervallo++
			#]
			
			if [#  M < DFREE/2 #] { 
//				println("$name | container presente") color blue // DEBUG
				[# 
                    CurrentIntervallo =
                        IntervalliMisurazioni.CONTAINER_PRESENTE
                #]

				if [# Guasto #] {
					println("$name | sonar ripristinato") color green
					[# Guasto = false #]
					emit riprendi_tutto : riprendi_tutto(si)
				}
			}
			
			if [# M >= DFREE/2 && M <= DFREE #] { 
//				println("$name | container assente") color blue // DEBUG
				[# 
                    CurrentIntervallo =
                        IntervalliMisurazioni.CONTAINER_ASSENTE
                #]

				if [# Guasto #] {
					println("$name | sonar ripristinato") color green
					[# Guasto = false #]
					emit riprendi_tutto : riprendi_tutto(si)
				}
			}
			
			if [# M > DFREE #] { 
//				println("$name | guasto!!!") color blue // DEBUG
				[# CurrentIntervallo = IntervalliMisurazioni.GUASTO #]
			} 
			
			[#
				if(CurrentIntervallo==LastIntervallo &&
                   LastIntervallo!=IntervalliMisurazioni.PRIMA_MISURAZIONE)
                {
					// switch di Kotlin
					when(CurrentIntervallo) {
					    IntervalliMisurazioni.CONTAINER_PRESENTE -> {
					        if(CounterIntervallo == 3) {
					        	CommUtils.outmagenta("Container presente
                                                      consistentemente")
        	#]
								emit container_arrived :
                                     container_arrived(si)
			[#
					    		CounterIntervallo = 0
					    	}
					    }
					    IntervalliMisurazioni.CONTAINER_ASSENTE -> {
					    	if(CounterIntervallo == 3) {
					        	CommUtils.outmagenta("Container assente
                                                      consistentemente")
        	#]
								emit container_absent : 
                                     container_absent(si)
			[#
					    		CounterIntervallo = 0
					    	}
					    }
					    IntervalliMisurazioni.GUASTO -> {
					    	if(CounterIntervallo == 3) {
								CommUtils.outred("Guasto consistente")	
								Guasto = true
			#]
								emit interrompi_tutto : 
                                     interrompi_tutto(si)
			[#
								CounterIntervallo = 0			    		
					    	}
					    }
					    else -> {
					    	// ci vuole se no kotlin si lamenta in quanto
					    	// i casi sopra non sono esausitivi
					    }
					}
				} 
				else {
					// 1 in quanto questa è la prima misurazione 
                    // appartenente al suo intervallo
					CounterIntervallo = 1
				}
				
				LastIntervallo = CurrentIntervallo
			#]
		}
	}
	Goto listen_for_measurement
}
```



## Piano di test

### Sonar

#### Scenario 1: container presente per 3 secondi

#### Scenario 2: container presente per 3 secondi e poi assente per 3 secondi

#### Scenario 3: rilevazione guasto e ripristino

Successivamente, si è testato il sonar anche utilizzando i seguenti attori mock.

#### sonar_simul
```Java
QActor sonar_simul  context ctx_iodevices{
	State s0 initial{
	}
	Goto work

	State work{
		delay 1000 // attendo l'avvio di sonar_listener
		
		// misurazioni non consistenti
		emitlocalstream measurement      : measurement(30)
	    delay 1000
		emitlocalstream measurement      : measurement(15)
	    delay 1000
		emitlocalstream measurement      : measurement(10)
	    delay 1000
		emitlocalstream measurement      : measurement(0)
	    delay 1000
		emitlocalstream measurement      : measurement(40)
		
		// assente per 4 secondi
		emitlocalstream measurement      : measurement(20)
	    delay 1000
	    emitlocalstream measurement      : measurement(20)
	    delay 1000
	    emitlocalstream measurement      : measurement(20)
	    delay 1000
	    emitlocalstream measurement      : measurement(20)
	    delay 1000
	    
		// presente per 3 secondi
		emitlocalstream measurement      : measurement(10)
	    delay 1000
	    emitlocalstream measurement      : measurement(10)
	    delay 1000
	    emitlocalstream measurement      : measurement(10)
	    delay 1000
	    
	    // di nuovo presente per 3 secondi
	    emitlocalstream measurement      : measurement(10)
	    delay 1000
	    emitlocalstream measurement      : measurement(10)
	    delay 1000
	    emitlocalstream measurement      : measurement(10)
	    delay 1000
	    
	    // guasto per 5 secondi
	    emitlocalstream measurement      : measurement(31)
	    delay 1000
	    emitlocalstream measurement      : measurement(31)
	    delay 1000
	    emitlocalstream measurement      : measurement(31)
	    delay 1000
	    emitlocalstream measurement      : measurement(31)
	    delay 1000
	    emitlocalstream measurement      : measurement(31)
	    delay 1000
	    
	    // di nuovo presente per 2 secondi, assente per 3
	    emitlocalstream measurement      : measurement(10)
	    delay 1000
	    emitlocalstream measurement      : measurement(10)
	    delay 1000
	    emitlocalstream measurement      : measurement(20)
	    delay 1000
	    emitlocalstream measurement      : measurement(20)
	    delay 1000
	    emitlocalstream measurement      : measurement(20)
	    delay 1000
	}
}
```

#### sonar_listener
```Java
QActor sonar_listener context ctx_iodevices {
	State s0 initial{
		println("$name | start") 
	}
	Goto work
	
	
	State work {
		println("$name | working") color blue
		delay 1000
	}
	Transition t0
		whenEvent container_arrived -> container_arrived
		whenEvent container_absent -> container_absent
		whenEvent interrompi_tutto -> bloccato
		
		
	State container_arrived {
		println("$name | container_arrived") color green
		delay 1000
	} 
	Goto work
	
	
	State container_absent {
		println("$name | container_absent") color red
		delay 1000
	} 
	Goto work
	
		
	State bloccato {
		println("$name | bloccato!") color red
	}
	Transition t0
		whenEvent riprendi_tutto -> ripristinato
		
		
	State ripristinato {
		println("$name | ripristinato!") color green
	}
	Goto work
}
```


### Hold



## Progettazione


### sonar
non ha bisogno di progettazione