# Sprint 1

## Obiettivi
affrontare il sottoinsieme dei requisiti relativo al cargoservice 

Enunciazione esplicita dei requisiti considerati nello SPRINT

## requisiti

## Architettura di partenza
// link allo sprint precedente


## Analisi del problema
Fase di analisi del problema, che termina con la definizione di
- una architettura logica del sistema
- di modelli eseguibili 
- e alcuni significativi piani di testing.

**NB**:  E’ raccomandato che i risultati di questa fase vengano presentati al committente (con opportuno appuntamento) prima della consegna finale del prodotto.






### Analisi cargoservice
```
- Deve essere raggiungibile dalle due GUI per gestirne le richieste e tenere nota di eventuali richieste sospese in attesa di risposta (Queued).
- Deve gestire la logica dei ticket, inglobando al suo interno una base di conoscenza persistente, che corrisponde ai ticket emessi in precedenza. In questa sede, i ticket vengono semplicisticamente descritti da numeri interi.
- Deve tenere nota dello stato della service area, inteso come posizione corrente del trolley e stato del trolley (Busy / Idle).

Il principio di singola responsabilità non risulta dunque in questo stadio rispettato. Si decide di posticipare alla fase di progettazione l'eventuale risoluzione del problema. 

Il comportamento della ColdStorage è schematicamente riassunto dal seguente diagramma degli stati, dettagliato in sede di analisi ed espresso dal codice sprint1Robot/src/analisi.qak:

```
// responsabilità e userstory di _cargoservice_
// - la userstory consiste nello spiegara la lista di interazione con gli altri componenti che bisogna effettuare



Come detto nello sprint0, l’attore _cargoservice_ è il componente principale del sistema. Il suo compito è quello di fare da **orchestratore**; in altre parole, deve coordinare le operazioni degli altri componenti del sistema col fine di eseguire le operazioni richieste dai requisiti nel giusto ordine.

La tipica sequenza di attività del _cargoservice_ è la seguente:
1. _cargoservice_ riceve una richiesta di carico da parte di un cliente
    - la richiesta di carico contiene il PID del prodotto da caricare

2. dopo aver ricevuto la richiesta di carico, _cargoservice_ fa una richiesta a _productservice_ per **recuperare il peso del prodotto da caricare** associato al PID ricevuto dal cliente.

3. _cargoservice_ riceva la risposta alla sua query da _productservice_. Quest'ultima può contenere:
    - un errore in caso il PID inviato dal cliente non corrisponda a nessun prodotto registrato nel DB. **In questo caso _cargoservice_ può già rispondere al cliente con un opportuno messaggio di errore**. 
    - il peso del prodotto 

4. dopo aver recuperato il peso del prodotto da caricare, _cargoservice_  può passare a verificare se lo stato del _deposito_ permette di soddisfare la richiesta. Si è definito nello sprint 0 che il mantenimento dello stato del _deposito_ è responsabilità del componente _hold_; di conseguenza, _cargoservice_ invierà a quest'ultimo un messaggio contenente il peso del prodotto da caricare. Si possono verificare tre casi:
    - richiesta non soddisfacibile in quanto si eccederebbe il peso _MaxLoad_ del deposito. _Hold_ risponde con un opportuno messaggio di errore.
    - richiesta non soddisfacibile in quanto manca uno _slot_ libero in cui posizionare il _container_. _Hold_ risponde con un opportuno messaggio di errore.
    - richiesta soddisfacibile. _Hold_ risponde con un messaggio contenente il nome dello slot prenotato dalla richiesta corrente
        - **PUNTO APERTO: come fa cargoservice a sapere la posizione della laydown-position dato solo il nome dello slot???**
        - prevediamo un file di configurazione che mappa nomi e posizioni che cargoservice legge durante l'inizializzazione?

5. in base alle risposte di _hold_, _cargoservice_ decide se accettare o rifiutare la richiesta
    - se la richiesta viene rifiutata, si ritorna al punto 1
    - se la richiesta viene accettata, _cargoservice_ posiziona il _cargorobot_ alla _pickup-position_ davanti alla _IO-port_
        - NOTA: dal momento dell'accettazione fino alla fine della gestione della richiesta, altre richieste di carico che arrivano nel frattempo vengono accodate. 
        - **PUNTO APERTO: come prima, come fa cargoservice a sapere la posizione della pickup-position???**

6. con il _cargorobot_ posizionato davanti all'IO-port, _cargoservice_ aspetta che il _sonar_ notifichi l'evento di arrivo del _container_. Se questo evento è gia avvenuto _cargoservice_ non ha motivo di aspettare
    - abbiamo dello stato che viene aggiornato dall'evento

7. una volta ricevuto l'evento di arrivo e posizionato il _cargorobot_, _cargoservice_ può ordinare al _cargorobot_ di: 
- recuperare il container
- trasportare il container allo _slot_ prenotato (posizionando il _cargorobot_ nella corretta _laydown-position_)
- depositare il container nello _slot_ prenotato

8. terminato l'intervento di carico _cargoservice_ ordina al _cargorobot_ di ritornare nella _home_
    - **NB**: il basicrobot non supporta spostamenti asincroni del robot; in altre parole, non posso interrompere il robot mentre sta tornando nella home per fargli servire altre richieste.
    - questo non è un problema in quanto il commmittente ci ha detto che possiamo fare come ci pare... però sono triste

9. una volta tornato nella home cargoservice potrà servire altre richieste


**OPPURE**
potrei rendere il cargorobot un po' più intelligente





La sequenza di attività suggerisce dei nuovi messaggi
- hold oltre al nome dello slot è più comodo che ritorni anche la coordinata della relativa laydown-position  

// cargoservice fa molta roba, questo permette di evitare dipendenze tra gli altri componenti rendendoli quindi meno intelligenti ma più generali e riutilizzabili 




La sequenza di attività suggerisce gli stati dell'attore QAK con cui modellare cargoservice


// codice qak di cargoservice

















### Analisi cargorobot
// responsabilità e userstory di cargorobot 

**PUNTO APERTO**: in un qualsiasi momento l'attività del cargorobot può essere interrotta. Questo è problematico in quanto il basicrobot come detto sopra supporta solo spostamenti sincroni. Mi tocca estendere modificare il basicrobot?


// codice qak di cargorobot














## Definizione dei messaggi



## Nuova architettura
Alla fine dello SPRINT, l’ARCHITETTURA INIZIALE DI RIFERIMENTO avrà subito una evoluzione che produce una nuova nuova ARCHITETTURA DI RIFERIMENTO, che sarà la base di partenza per lo sprint successivo.


## Piano di test






## Progettazione
Fase di progetto e realizzazione, che termina con il **deployment** del prodotto, unitamente a istruzioni (ad uso del committente) per la costruzione/esecuione del prdotto setsso.


parla di un file di config

parla di eventuali classi di supporto

ci sta anche lasciare inalterata della roba



## (Opzionale) Osservabilità (Logging con prolog?)


// Deployment



## Sintesi finale
Ogni SPRINT dovrebbe terminare con una pagina di sintesi che riporta l’architettura finale corrente del sistema (con i link al modello e ai Test). Questa pagina sarà l’inizio del documento relativo allo SPRINT successivo.