# Sprint 3

## Indice

- [Punto di Partenza](#punto-di-partenza)
- [Obiettivi](#obiettivi)
- [Led](#led)
- [WebGui](#webgui)
- [Deployment](#deployment)
- [Sintesi Finale e Nuova Architettura](#sintesi-finale-e-nuova-architettura)
- [Tempo Impiegato e Ripartizione del Lavoro](#tempo-impiegato-e-ripartizione-del-lavoro)

## Punto di Partenza

Nello sprint precedente si sono implementati i componenti: [sonar](https://github.com/ingegneria-sistemi-software-m/cargoservice/blob/master/sprint2/README.md#analisi-del-problema--sonar) e [hold](https://github.com/ingegneria-sistemi-software-m/cargoservice/blob/master/sprint2/README.md#analisi-del-problema--hold). Grazie al primo, è diventato possibile rilevare la presenza/assenza dei container, grazie al secondo è diventato possibile gestire lo stato del deposito completando in questa maniera la logica del sistema.

Durante l'analisi del componente Hold si sono anche definiti i messaggi che quest'ultimo dovrà scambiarsi con la web-gui, componente che si implementerà nello sprint 3.

L'architettura del sistema risultante da questo sprint è suddivisibile in due macrocontesti.

<img src="../sprint2/arch2.png"/>

<img src="../sprint2/iodevicesarch.png"/>

<div class="page-break"></div>

## Obiettivi

L'obiettivo dello sprint 3 sarà affrontare il sottoinsieme dei requisiti relativi ai componenti _webgui_ e _led_, effettuando l'analisi del problema e la progettazione. Particolare importanza verrà data alle **interazioni** che questi componenti dovranno avere con il resto del sistema.

I [requisiti](https://github.com/ingegneria-sistemi-software-m/cargoservice/tree/master/requirements) affrontati nello sprint 3 sono i seguenti:
- is able to show the current state of the hold, by means of a dynamically updated web-gui,
- interrupts any activity and turns on a led if the sonar sensor measures a distance D > DFREE for at least 3 secs (perhaps a sonar failure) and the service continues its activities as soon as the sonar measures a distance D <= DFREE.

## Led

### Analisi del Problema 

L'attore led è semplice: è responsabile di segnalare l'eventuale malfunzionamento del sonar.

#### Modello

## WebGui

### Analisi del Problema 

L'attore webgui è responsabile di presentare lo stato degli slot e il peso complessivo dei container nel deposito. 

Il tipico ciclo di attività di webgui è il seguente:
1. Inizialmente richiede lo stato iniziale del deposito.
1. Una volta ricevuto lo stato initerno diventa di perenne attesa dell'evento di modifica del deposito.
1. Ogni volta che si verifica una modifica al deposito la webgui viene modificata di conseguenza, di modo da mostrare i cambiamenti.

#### Modello

#### Piano di Test

### Progettazione

L'applicazione _webgui_ è un sistema sviluppato utilizzando SpringBoot un framework Java-based scelto per la sua semplicità di configurazione, l'integrazione nativa con pattern MVC e l'uso di annotazioni per la gestione semplificata dei Controller.

La _webgui_ è stata progettata col fine di monitorare lo stato della stiva (_hold_) e visualizzarne i cambiamenti in tempo reale attraverso un'interfaccia web. La _Webgui_ è un sistema passivo, capace di ricevere e inoltrare aggiornamenti all'interfaccia web.

Ogni componente della _webgui_ è responsabile di una specifica funzionalità, come voluto dal principio di singola responsabilità. Ritroviamo tre principali componenti quali:

- HoldStateService: Accesso TCP e interrogazione su richiesta dello stato.
- WSHandler: Gestione WebSocket.
- CoapToWS: Client CoAP e gestione degli aggiornamenti.

#### Componenti e responsabilità

WSHandler:

Il WSHandler è il componente di comunicazione WebSocket occupandosi della gestione delle connessioni con i client browser. Le sessioni attive sono mantenute in memoria, e ogni aggiornamento ricevuto dal sistema viene immediatamente inoltrato ai client tramite broadcast JSON. Le sue funzioni principali sono quelle di:
- Gestire tutte le sessioni client WebSocket connesse
- Fornire un metodo **sendToAll**, che trasmette il messaggio JSON ricevuto da **HoldStateService** o **CoapToWS** a tutti i client attivi.

WebSocketConfig:

Implementa WebSocketConfigurer per registrare l'handler WebSocket su un endpoint specifico (/status-updates).

CoapToWS:

Questa componente si comporta come un osservatore CoAP. Alla sua inizializzazione si sottoscrive come osservatore dello stato interno di _hold_ accessibile tramite un path specifico: 

```
coap://localhost:8000/ctx_cargoservice/hold
```
Ogni volta che lo stato di _hold_ si aggiorna, il CoAP client riceve l'aggiornamento corrispondente tramite un messaggio. Il messaggio viene elaborato estraendo le informazioni e costruendo un nuovo JSON che viene successivamente inviato via WebSocket ai client, tramite WSHandler.


HoldStateService:

Questa componente ha il compito di inviare una richiesta TCP all'attore esterno _hold_ per ottenere il suo stato. Il flusso è il seguente:
1. L'utente accede al sito web
2. L'interfaccia invia una richiesta HTTP GET all'endopoint **/holdstate** fornito dal backend SpringBoot.
3. Il controller delega a HoldStateService, che  aprendo una connessione TCP verso l'attore _hold, inoltra una richiesta **get_hold_state(X)**.
4. I dati ricevuti vengono convertiti in un oggetto JSON comprensibile dal browser.
5. Il JSON successivamente viene inviato via WebSocker al browser.


CallerService:

Questo componente rappresenta un altro punto d'ingresso per le richieste HTTP provenienti dal browser. Espone l'endpoint /callet?pid=XX, che invia al sistema principale una richiesta di tipo **load_product(pid)**

Interfaccia web:

La pagina principale di monitoraggio è una semplice interfaccia HTML+CSS+JS responsiva.Include:
- Visualizzazione del carico totale della nave
- Stato dei 4 slot del deposito
- Connessione automatica all' endpoint /status-updates 
- Riconessione automatica in caso di disconnessione
- Data e ora dell'ultimo aggiornamento ricevuto

Al caricamento della pagina viene eseguita anche una chiamata fetch a /holdstate per ottenere lo stato iniziale.

E' stata predisposta anche una seconda pagina HTML che consente all'utente di inviare un **Product ID** al sistema. Il risultato viene mostrato nella stessa pagina all'interno di un box. La risposta può essere:
- Conferma del caricamento
- Errore




## Deployment

## Sintesi Finale e Nuova Architettura

In questo sprint si sono implementati i componenti: [led](#analisi-del-problema--led) e [webgui](#analisi-del-problema--webgui). Grazie al primo, è diventato possibile segnalare la presenza di un malfunzionamento del sonar, grazie al secondo è diventato possibile visualizzare lo stato degli slot e il peso complessivo dei container nel deposito.

L'architettura del sistema risultante da questo sprint è definisce il nuovo macrocontesto della webgui.

### Servizio principale

![arch3](./arch3.png)

### Dispositivi di I/O

![iodevicesarch](./iodevicesarch.png)

### WebGui

![]() //immagine ancora da fare sulla webgui

## Tempo Impiegato e Ripartizione del Lavoro

## Tempo Impiegato

Lo sprint ha richiesto poche ore in più del previsto.
Avevamo previsto di procedere al ritmo di uno sprint alla settimana, e così è stato.

### Ripartizione del Lavoro

Non c'è nessuna differenza tra la ripartizione del lavoro in questo sprint e la ripartizione del lavoro nel precedente.

Come previsto, tutti i membri del gruppo hanno partecipato attivamente a tutte le fasi dello sviluppo. Questa modalità organizzativa si è rivelata particolarmente soddisfacente, poiché le principali difficoltà riscontrate durante gli sprint hanno riguardato soprattutto la fase di analisi e progettazione, più che quella di implementazione.  

In questo contesto, si è dimostrato molto efficace affrontare le problematiche attraverso sessioni di brainstorming collettivo. È infatti raro che un singolo componente riesca a cogliere da solo tutte le sfaccettature di una tematica complessa, mentre il confronto tra punti di vista diversi porta spesso alla sintesi di soluzioni condivise, in grado di soddisfare l’intero team.  

Particolarmente utile si è rivelata la necessità di esporre e argomentare le proprie idee fin dalle prime fasi di analisi: il confronto verbale permette di individuare tempestivamente eventuali errori o fraintendimenti, e assicura che il gruppo proceda in maniera coerente, evitando che si consolidino interpretazioni discordanti che potrebbero compromettere il lavoro futuro.