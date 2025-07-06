# Sprint 1

## Obiettivi
L'analisi dei requisiti avvenuta nello [Sprint 0](https://github.com/ingegneria-sistemi-software-m/cargoservice/tree/master/sprint0/sprint0.pdf) ha portato a definire una **prima architettura generale del sistema**. 

![arch0](../sprint0/arch0.png)

L'obiettivo dello sprint 1 sarà affrontare il sottoinsieme dei requisiti relativi ai componenti _cargorservice_ e _cargorobot_, effettuandone l'analisi del problema e la progettazione. Particolare importanza verrà data alle **interazioni** che questi componenti dovranno avere con il resto del sistema.

I [requisiti](https://github.com/ingegneria-sistemi-software-m/cargoservice/tree/master/requirements) affrontati nello sprint 1 saranno i seguenti:
- implementare un sistema che è in grado di accettare/rifiutare le richieste di carico
- implementare un sistema in grado di effettuare un intervento di carico nella sua interezza. Questo significa in ordine:
    - andare all'_IO-port_
    - aspettare il _container_ se non è già presente
    - caricare il _container_ una volta arrivato
    - spostare il _cargorobot_ nella _laydown-position_ corretta
    - scaricare il _container_ nello slot
    - ritornare alla _home_
- implementare un sistema in grado di interrompere ogni attività in caso di sonar malfunzionante, e in grado di riprendere le attività interrotte una volta risolto il guasto

Va notato che i requisiti affronati in questo sprint presupporrebbere già l'implementazione di altri componenti del sistema come _hold_ e _sonar_. L'implementazione di questi componenti verrà però affrontata solamente negli sprint successivi. Per questo motivo nello sprint 1 verranno utilizzati dei **componenti mock** che simuleranno il comportamento dei componenti mancanti in **maniera, però, semplicistica**. Ad esempio, _hold_ non causerà mai il rifiuto di una richiesta di carico in quanto non terrà traccia di alcuno stato del deposito.





<!--
## Analisi del problema
Fase di analisi del problema, che termina con la definizione di
- una architettura logica del sistema
- di modelli eseguibili 
- e alcuni significativi piani di testing.

**NB**:  E’ raccomandato che i risultati di questa fase vengano presentati al committente (con opportuno appuntamento) prima della consegna finale del prodotto.
 -->





## Analisi del problema | cargoservice
Come detto nello sprint0, l’attore _cargoservice_ è il componente principale del sistema. Il suo compito è quello di fare da **orchestratore**; in altre parole, deve coordinare le operazioni degli altri componenti del sistema col fine di eseguire le operazioni specificate dai requisiti nel giusto ordine.

La tipica sequenza di attività di _cargoservice_ è la seguente:
1. _cargoservice_ **riceve una richiesta di carico** da parte di un cliente.
    - la richiesta di carico contiene il PID del prodotto da caricare

2. dopo aver ricevuto la richiesta di carico, _cargoservice_ fa una richiesta a _productservice_ per **recuperare il peso del prodotto da caricare** associato al PID ricevuto dal cliente.

3. _cargoservice_ riceva la risposta alla sua query da _productservice_. Quest'ultima può contenere:
    - un errore in caso il PID inviato dal cliente non corrisponda a nessun prodotto registrato nel DB. **In questo caso _cargoservice_ può già rispondere al cliente con un opportuno messaggio di errore**
    - il peso del prodotto 

4. dopo aver recuperato il peso del prodotto da caricare, _cargoservice_  può passare a verificare se lo **stato del _deposito_ permette di soddisfare la richiesta**. Si è definito nello sprint 0 che il mantenimento dello stato del _deposito_ è responsabilità del componente _hold_; di conseguenza, _cargoservice_ invierà a quest'ultimo un messaggio contenente il peso del prodotto da caricare. Si possono verificare tre casi:
    - richiesta non soddisfacibile in quanto si eccederebbe il peso _MaxLoad_ del deposito. _Hold_ risponde con un opportuno messaggio di errore
    - richiesta non soddisfacibile in quanto manca uno _slot_ libero in cui posizionare il _container_. _Hold_ risponde con un opportuno messaggio di errore
    - richiesta soddisfacibile. _Hold_ risponde con un messaggio contenente il nome dello slot prenotato dalla richiesta corrente

5. se la richiesta viene accettata, _cargoservice_ può semplicemente richiedere a _cargorobot_ di gestire il container, delegando a lui tutta la logica di attesa, trasporto e deposito del _container_ con una operazione del tipo **_handle\_load\_operation(slot)_**. 

6. _cargoservice_ attende il completamento dell'intervento di carico da parte di _cargorobot_. Nel frattempo, eventuali altre richieste di carico vengono accodate.

7. terminato l'intervento di carico, _cargorobot_ sblocca _cargoservice_ rispondendo alla sua precedente richiesta (evento di sincronizzazione). Da questo punto in poi _cargoservice_ torna a poter servire le richieste di carico.


### Considerazioni
Le attività che cargoservice deve effettuare non pongono particolari problemi da analizzare, si tratta solo di effettuare una serie di richieste. Tuttavia, è stata presa una decisione: quella di **rendere il _cargorobot_ "intelligente"**.

Si sarebbe potuto rendere il _cargorobot_ un mero esecutore di comandi, aggiungendo a _cargoservice_ la responsabilità di dettare la sua posizione e che cosa deve fare in ogni momento. Si è preferito, invece, rendere il _cargorobot_ più intelligente e indipendente per tre motivi principali:   
- l'analisi del dominio effettuata nello sprint 0 ha delineato il _cargorobot_ come un componente con delle mosse più sofisticate
- _cargoservice_ giova di un _cargorobot_ con delle mosse più sofisticate in quanto queste producono un abstraction gap minore 
- principio di singola responsabilità: _cargoservice_ si occupa di fare solo da orchestrare mentre _cargorobot_ si occupa di effettuare le azioni del _DDR_ descritto nei requisiti  


### Nuovi Messaggi
Nello [Sprint 0](https://github.com/ingegneria-sistemi-software-m/cargoservice/tree/master/sprint0/sprint0.pdf) si erano definiti i messaggi con cui interagire con: _cargoservice_, _productservice_ e _basicrobot_. 

L'analisi della sequenza di attività del _cargoservice_ suggerisce dei nuovi messaggi. In particolare, abbiamo:


**Messaggi per l'interazione con hold**
```
Request reserve_slot         : reserve_slot(WEIGHT)
Reply   reserve_slot_success : reserve_slot_success(SLOT) for reserve_slot
Reply   reserve_slot_fail    : reserve_slot_fail(CAUSA) for reserve_slot
```

**Messaggi per l'interazione con cargorobot**
```
Request handle_load_operation   : handle_load_operation(SLOT)
Reply   load_operation_complete : load_operation_complete(OK) for handle_load_operation
```



### Modello 
La sequenza di attività suggerisce gli stati dell'attore QAK con cui modellare cargoservice




























## Analisi cargorobot
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

**PUNTO APERTO: come fa _cargorobot_ a sapere se il container è già presente all'IO-port o no?**

- **PUNTO APERTO: il commmittente ci ha detto che possiamo fare come ci pare per quanto riguarda il momento in cui _cargoservice_ può tornare a servire le richieste... la nostra scelta però deve essere opportunamente motivata. Cosa scegliamo???**


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