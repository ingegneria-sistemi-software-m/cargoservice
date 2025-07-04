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
// responsabilità e userstory di _cargoservice_
// - la userstory consiste nello spiegara la lista di interazione con gli altri componenti che bisogna effettuare



Come detto nello sprint0, l’attore _cargoservice_ è il componente principale del sistema. Il suo compito è quello di fare da **orchestratore**; in altre parole, deve coordinare le operazioni degli altri componenti del sistema col fine di eseguire le operazioni specificate dai requisiti nel giusto ordine.

La tipica sequenza di attività del _cargoservice_ è la seguente:
1. _cargoservice_ **riceve una richiesta di carico** da parte di un cliente
    - la richiesta di carico contiene il PID del prodotto da caricare

2. dopo aver ricevuto la richiesta di carico, _cargoservice_ fa una richiesta a _productservice_ per **recuperare il peso del prodotto da caricare** associato al PID ricevuto dal cliente.

3. _cargoservice_ riceva la risposta alla sua query da _productservice_. Quest'ultima può contenere:
    - un errore in caso il PID inviato dal cliente non corrisponda a nessun prodotto registrato nel DB. **In questo caso _cargoservice_ può già rispondere al cliente con un opportuno messaggio di errore**. 
    - il peso del prodotto 

4. dopo aver recuperato il peso del prodotto da caricare, _cargoservice_  può passare a verificare se lo **stato del _deposito_ permette di soddisfare la richiesta**. Si è definito nello sprint 0 che il mantenimento dello stato del _deposito_ è responsabilità del componente _hold_; di conseguenza, _cargoservice_ invierà a quest'ultimo un messaggio contenente il peso del prodotto da caricare. Si possono verificare tre casi:
    - richiesta non soddisfacibile in quanto si eccederebbe il peso _MaxLoad_ del deposito. _Hold_ risponde con un opportuno messaggio di errore.
    - richiesta non soddisfacibile in quanto manca uno _slot_ libero in cui posizionare il _container_. _Hold_ risponde con un opportuno messaggio di errore.
    - richiesta soddisfacibile. _Hold_ risponde con un messaggio contenente il nome dello slot prenotato dalla richiesta corrente
        - **PUNTO APERTO: come fa cargoservice a sapere la posizione della laydown-position dato solo il nome dello slot???**
        - prevediamo un file di configurazione che mappa nomi e posizioni che cargoservice legge durante l'inizializzazione?

5. in base alle risposte di _hold_, _cargoservice_ decide se accettare o rifiutare la richiesta
    - se la richiesta viene rifiutata, si ritorna al punto 1
    - se la richiesta viene accettata, _cargoservice_ posiziona il _cargorobot_ alla _pickup-position_ davanti alla _IO-port_
        - NOTA: dal momento dell'accettazione fino alla fine della gestione della richiesta, altre richieste di carico che arrivano nel frattempo vengono **accodate**. 
        - **PUNTO APERTO: come prima, come fa cargoservice a sapere la posizione della pickup-position???**

6. con il _cargorobot_ posizionato davanti all'IO-port, _cargoservice_ aspetta che il _sonar_ notifichi l'evento di arrivo del _container_. Se questo evento è gia avvenuto _cargoservice_ non ha motivo di aspettare
    - abbiamo dello stato che viene aggiornato dall'evento

7. una volta ricevuto l'evento di arrivo e posizionato il _cargorobot_, _cargoservice_ può ordinare al _cargorobot_ di: 
- recuperare il container
- trasportare il container allo _slot_ prenotato (posizionando il _cargorobot_ nella corretta _laydown-position_)
- depositare il container nello _slot_ prenotato

8. terminato l'intervento di carico _cargoservice_ ordina al _cargorobot_ di ritornare nella _home_
    - **PUNTO APERTO: il commmittente ci ha detto che possiamo fare come ci pare per quanto riguarda il momento in cui _cargoservice_ può tornare a servire le richieste... la nostra scelta però deve essere opportunamente motivata. Cosa scegliamo???**

9. una volta tornato nella home cargoservice potrà servire altre richieste


**OPPURE** potrei rendere il _cargorobot_ un po' più intelligente

- punti da 1 a 4 uguali a prima
5. se la richiesta viene accettata, _cargoservice_ può semplicemente richiedere a _cargorobot_ di gestire il container, delegando a lui tutta la logica di attesa, trasporto e deposito con una operazione del tipo **_handle_container(slot)_**
6. quando _cargorobot_ avrà terminato la gestione del container risponderà a cargoservice rendendosi disponibile per la prossima richiesta


considerazioni:
- se uso un cargorobot scemo
    - quest'ultimo non deve conoscere le coordinate delle posizioni in cui deve andare in quanto gli vengono fornite tramite i messaggi di posizionamento espliciti
- se uso un cargorobot intelligente
    - il cargorobot diventa un ulteriore componente che deve leggere un file di config per caricare le posizioni in cui deve andare
    - l'abstraction gap tra i requisiti e cio che mi permette di fare il basic robot
    - rispetto meglio il principio di singola responsabilità
        - cargoservice si occupa solo di fare da orchestrare di vari componenti
        - cargorobot muove il roboto 









La sequenza di attività suggerisce dei nuovi messaggi
- hold oltre al nome dello slot è più comodo che ritorni anche la coordinata della relativa laydown-position  

// cargoservice fa molta roba, questo permette di evitare dipendenze tra gli altri componenti rendendoli quindi meno intelligenti ma più generali e riutilizzabili 




La sequenza di attività suggerisce gli stati dell'attore QAK con cui modellare cargoservice


// codice qak di cargoservice

















### Analisi cargorobot
// responsabilità e userstory di cargorobot

Durante l'analisi dei requisiti si è detto che il _cargorobot_ è il componente responsabile del comando del DDR. Il _cargorobot_ si interfaccia con il _basicrobot_ (fornito dal committente) per muovere il DDR. Estendendo le funzionalità del _basicrobot_ **colma l'abstraction gap** tra quest'ultimo e i requisiti.

L'analisi dei requisiti e l'analisi di _cargoservice_ hanno già delineato in parte la sequenza di attività del _cargorobot_:
1. _cargorobot_ riceve da _cargoservice_ una richiesta di gestione di un container
2. _cargorobot_ si dirige verso la pickup-position e aspetta che arrivi il container
    - **PUNTO APERTO: chi è che ascolta il sonar e aggiorna lo stato di cargorobot per confermare la presenza del container???** 
3. _cargorobot_ :
    - recupera il container
    - trasporta il container allo _slot_ prenotato (si posizione nella corretta _laydown-position_)
    - deposita il container nello _slot_ prenotato
4. terminato l'intervento di carico, _cargorobot_ può ritornare alla _home_ rispondendo a _cargoservice_ del successo del suo intervento di carico
    - **NB**: da questo momento in poi _cargoservice_ torna ad essere recettivo a richieste di carico da parte dei clienti 
    - **NB2**: da questo momenti in poi _cargorobot_ torna ad essere recettivo a richieste di carico da parte di _cargoservice_
    - **NB3**: per adesso non c'è nessun motivo di fallimento per il cargorobot (timeout??) e quindi la risposta serve solo a sincronizzare _cargoservice_ e _cargorobot_
    
**NB**: in un qualsiasi momento l'attività del cargorobot può essere interrotta. **PUNTO APERTO: come fa cargorobot a interrompersi e riprendere???**


**(sostituire i punti aperti con domande e risposte) PUNTO APERTO: Come fa _cargorobot_ a conoscere le posizioni notevoli in cui deve andare dato il nome di uno slot?** 




// codice qak di cargorobot















## Definizione dei messaggi



## Nuova architettura
Alla fine dello SPRINT, l’ARCHITETTURA INIZIALE DI RIFERIMENTO avrà subito una evoluzione che produce una nuova nuova ARCHITETTURA DI RIFERIMENTO, che sarà la base di partenza per lo sprint successivo.


## Piano di test






## Progettazione
Fase di progetto e realizzazione, che termina con il **deployment** del prodotto, unitamente a istruzioni (ad uso del committente) per la costruzione/esecuione del prdotto setsso.


parla di un file di config

parla di come ci si deve adattare all'interfaccia di productservice

parla di eventuali classi di supporto

ci sta anche lasciare inalterata della roba



## (Opzionale) Osservabilità (Logging con prolog?)


// Deployment



## Sintesi finale
Ogni SPRINT dovrebbe terminare con una pagina di sintesi che riporta l’architettura finale corrente del sistema (con i link al modello e ai Test). Questa pagina sarà l’inizio del documento relativo allo SPRINT successivo.