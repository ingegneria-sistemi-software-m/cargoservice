# Sprint 0

## Indice
- [Obiettivi](#obiettivi)
- [Requisiti](#requisiti)
- [Vocabolario](#vocabolario)
- [Macrocomponenti](#macrocomponenti)
- [Architettura di Riferimento](#architettura-di-riferimento)
- [Piano di Test](#piano-di-test)
- [Piano di Lavoro](#piano-di-lavoro)
- [Team di Lavoro e Attività Specifiche](#team-di-lavoro-e-attività-specifiche)

## Obiettivi
L'obiettivo prinicipale dello sprint 0 è analizzare i [requisiti](../requisiti/) forniti dal committente riguardo al progetto **TemaFinale25** eliminando eventuali ambiguità relative ai termini utilizzati e formalizzando quest'ultimi con dei componenti software (che possono essere già sviluppati o ancora da sviluppare).  

Una volta formalizzati i requisiti si procederà con la definizione di una prima **architettura logica generale del sistema** che sarà il riferimento iniziale per lo sprint 1.  

Questa architettura iniziale sarà costituita dai **macrocomponenti principali** (bounded context) deducibili dal documento dei requisiti, e dalle **interazioni** tra quest'ultimi sotto forma di messaggi (mappa di contesto).  

L'architettura logica sarà anche corredata di un relativo piano di test iniziale.

Terzo e ultimo obiettivo dello sprint 0 sarà quello di definire un **piano di lavoro** in cui si definirà il numero e gli obiettivi degli sprint necessari al completamento del sistema.  

Inoltre, in questa sezione si deciderà come suddividere il lavoro tra i membri del gruppo, ed eventualmente, quali sprint potranno essere realizzati in parallelo.

## INFO A CASO
- disambuiguiamo i termini 
- diciamo quali componenti sono da sviluppare
- quali sono da formalizzare con un pezzo di software già in nostro possesso
- cosa è un attore, cosa è un POJO, più eventuali attributi di entità
- **pezzo relativo alla mappa e wenv praticamente da copiare e incollare**
- ricorda di linkare alla repo del corso i progetti che si possono riciclare

## Requisiti
I requisiti sono descritti dal committente nel documento: [documento dei requisiti](../requisiti/). 



## Vocabolario
E' stato ritenuto più opportuno presentare i termini del dominio in un formato discorsivo, prolisso e ridondante, per garantire non solo che i termini non siano ambigui, ma anche che per il lettore sia difficile malinterpretare.

Questo si contrappone al più comune approccio, in cui il vocabolario è un mero elenco puntato, nel quale è possibile che una mancata disambiguazione, possa evolvere in errori considerevoli durante lo sviluppo.

Ci riserviamo la possibilità di ricondurci al classico vocabolario-elenco nel caso dovessero sorgere complicazioni inaspettate, sicuri del fatto che l'eventuale conversione sarà semplice.




Un **_Differential Drive Robot (DDR)_** è un tipo di robot fisico e mobile che si muove grazie a due ruote motrici indipendenti, solitamente affiancate sullo stesso asse, che gli consentono di avanzare, arretrare o ruotare su sé stesso. All'interno del sistema il DDR è il supporto fisico che viene comandato dal _cargorobot_ per implementarne le azioni nel mondo reale. 




Il **_deposito/stiva (Hold)_** rappresenta il workspace del _cargorobot_: lo scenario entro cui è confinato ed in cui svolge i suoi **interventi di carico**.

È di forma rettangolare e sono presenti alcune aree notevoli e fisse che è bene segnalare con un nome univoco:
- La **_home_** è la postazione da cui il _cargorobot_ parte e a cui il _cargorobot_ fa ritorno al termine di ogni intervento.
- Gli **_slot_** sono le cinque postazioni in cui _container_ vengono depositati. Notare che **queste postazioni sono distinte dalle _laydown-positions_** siccome il _cargorobot_ scarica il _container_ che sta trasportando davanti a se. 
    - I requisiti specificano che lo slot numero 5 è sempre occupato e quindi si riduce ad un ostacolo. 
- Le **_laydown-position_** sono le postazioni in cui il _cargorobot_ si posiziona per effettuare l'operazione di deposito. Ogni _laydown-position_ è associata ad uno _slot_ e richiede che il _cargorobot_ sia orientato verso quest'ultimo per effettuare la sua operazione di deposito.  
- La **_IO-Port_** indica la posizione dove verranno depositati i _container_ in arrivo. Similmente agli _slot_, alla _IO-port_ è associata una _pickup-position_ che ha una posizione distinta. 
- La **_pickup-position_** è la postazione in cui il _cargorobot_ si posiziona per recuperare il _container_ arrivato alla _IO-Port_. Il _cargorobot_ deve essere rivolto verso _l'IO-port_ per caricare il _container_.

Le aree appena elencate sono **attributi** dell'entità _deposito_ e possono essere modellate come POJO in quanto entità passive. Inoltre, il deposito ha come ulteriore attributo **_MaxLoad_** che definisce il peso massimo trasportabile dalla nave.

Non sarebbe irragionevole immaginare di rappresentare il _deposito_ come un POJO che aggrega gli attributi appena descritti, ma si è ritenuto più opportuno modellarlo come un attore per **separare la logica dei dati dalla logica di buisness** (responsabilità di _cargoservice_) seguendo il Single Responsability Principle.

Si è detto che il _deposito_ è l'area di lavoro del _cargorobot_: si noti, a tal proposito che il tentativo di descrivere il _deposito_, e gli elementi che lo compongono, inevitabilmente, conduce a una prima vaga descrizione dei processi che vi si svolgono (**interventi di carico**). Naturalmente, questo dipende dalla scelta, deliberata, di utilizzare gli elementi del _deposito_ per descriverlo. Questo crea nel _deposito_ una dipendenza forte dai suoi elementi, che riteniamo ragionevole, perchè non è particolarmente realistico, che una delle estensioni future preveda un _deposito_ senza questi elementi notevoli al suo interno.





### NB: i requisiti sono ambigui nello specificare se la registrazione riguarda i singolo container o i prodotti come categoria (container diversi possono contenere lo stesso prodotto ma magari in quantità, e quindi peso, diverse). Bisogna chiedere al prof. per adesso ho lasciato l'opzione più probabile ovvero quella dei prodotti 
Un **_container_** è un recipiente di dimensioni predefinite che contiene un determinato _prodotto_ da trasportare. Ogni **richiesta di carico** è sempre associata ad un container, e quest'ultimi vengono sempre recuperati dalla _IO-Port_ per poi essere trasportati dal _cargorobot_ dentro a uno degli _slot_ liberi.

I **_prodotti_** sono merci di qualsiasi tipo sempre trasportate all'interno di un _container_. Ogni _prodotto_, viene sempre **registrato** dentro a _productservice_ prima di essere caricato. 

**_productservice_** è un servizio utilizzato per registrare i _prodotti_ che devono essere caricati all'interno del _deposito_ dentro ad un database. L'operazione di registrazione consiste nello specificare il peso del prodotto da registrare, successivamente _productservice_ restituirà l'identificatore unico (PID) del prodotto appena registrato.

Abbiamo quindi che un prodotto è una entità passiva modellabile come un POJO che ha come attributi:
- un peso: valore reale
- ed un PID: valore intero > 0

Il committente fornisce già tutto il software necessario per l'implementazione di _productservice_ e dei _prodotti_. In particolare il componente che implementa le funzionalità di _productservice_ è un **attore** che si chiama (sfortunatamente) [cargoservice](./link). Questo è un nome infelice in quanto coincide con quello di un altro macrocomponente del sistema; da ora in poi ci si riferirà al software che il committente ha fornito con _productservice_.

I messaggi con cui si può interagire con _productservice_ sono i seguenti.

```
    Request createProduct : product(String)                    
    Reply   createdProduct: productid(ID) for createProduct   
        
    Request deleteProduct  : product( ID ) 
    Reply   deletedProduct : product(String) for deleteProduct

    Request getProduct : product( ID )  
    Reply   getProductAnswer: product( JSonString ) for getProduct 
    
    Request getAllProducts : dummy( ID )
    Reply   getAllProductsAnswer: products(  String ) for getAllProducts 
```







Un **_cargorobot_** è un robot **logico** capace, **sotto richiesta**, di: muoversi liberamente all'interno del _deposito_, recuperare un container dall'_IO-port_, posizionare un _container_ precendentemente recuperato in uno degli _slot_ liberi all'interno del _deposito_. È opportuno modellare il _cargorobot_ come **attore** in quanto è un componente attivo che ha un proprio flusso di controllo.

**Il committente fornisce del software per la modellazione del _cargorobot_**. In particolare:
- l'ambiente virtuale [WEnv](./sprint0.md) (aggiusta link) che simula un _DDR_ e la _stiva_ della nave in cui il _cargorobot_ dovrà operare
- un componente software chiamato [basicrobot](./sprint0.md) (aggiusta link) che permette di governare un _DDR_ virtuale all'interno di _WEnv_ 

L'ambiente virtuale **_WEnv_** modella la tipica stiva della nave in cui dovrà andare ad operare il _cargorobot_ e include un simulatore di _DDR_.

Il _DDR_ all'interno di _WEnv_ è un **robot inscrivibile in un cerchio di raggio R** che può compiere solamente 4 mosse:
- andare avanti per un certo periodo di tempo
- andare indietro per un certo periodo di tempo
- ruotare a destra di 90°
- ruotare a sinistra di 90°

Il committente fornisce il tempo necessario al DDR per andare avanti o indietro di **una distanza pari alla sua dimensione**. Si ha quindi a disposizione anche una quinta mossa elementare chiamata **step** che corrisponde ad un passo del DDR lungo 2R.

Il committente ha anche specificato che lo **step fornisce un unità di misura per le dimensioni del deposito**. Il deposito pertanto può essere modellato come una **griglia rettangolare composto da HHxHW (Hold Height per Hold Width) celle**. Ogni cella è un quadrato con lato pari all'unità di misura robotica, ovvero 2R. 

Basandosi sull'unità di misura robotica, il committente fornisce anche una mappa del _deposito_ (utilizzata dal _basicrobot_) e le coordinate di tutte le aree notevoli al suo interno. L'origine del sistema di riferimento è l'angolo in alto a sinistra che coincide con la _home_.

In seguito la mappa fornita dal committente e le coordinate delle posizioni notevoli. Nella mappa:
- gli '1' indicano una cella libera
- le 'X' indicano un ostacolo
- la 'r' indica la posizione del robot

```
    r,  1,  1,  1,  1,  1,  1, 
    1,  1,  X,  X,  1,  1,  1, 
    1,  1,  1,  1,  X,  1,  1, 
    1,  1,  X,  X,  1,  1,  1, 
    1,  1,  1,  1,  1,  1,  1, 
    X,  X,  X,  X,  X,  X,  X, 
```
// non c'è bisogno di indicare il direzionamento in quanto ho specificato sopra
- Home:              Hold.Home = (0,0)
- Slots:             Hold.Slots = {(2,1), (3,1), (2,3), (3,3), (4,2)}
- Laydown-positions: Hold.LaydownPositions = {(1,1), (1,3), (4,1), (4,3)}
- IO-port:           Hold.IoPort = (0,5)
- Pickup-position:   Hold.PickupPosition = (0,4)

Indicando nella mappa fornita le coordinate specificate otteniamo la seguente mappa logica.

```text
    H,  1,  1,  1,  1,  1,  1, 
    r,  L1, S1, S2, L2, 1,  1, 
    1,  1,  1,  1,  S5, 1,  1, 
    1,  L3, S3, S4, L4, 1,  1, 
    P,  1,  1,  1,  1,  1,  1, 
    IO, X,  X,  X,  X,  X,  X,   
```


![Wenv](../requisiti/scene.jpg)


Il **_basicrobot_** fornito dal committente è un **attore** esecutore di comandi con cui è possibile governare il _DDR_ all'interno di _WEnv_. Il _basicrobot_ modella quindi un _DDR_ e permette di effettuare singole mosse o **sequenze di mosse**, a seguito di messaggi di richiesta. Più nel dettaglio, il basicrobot incorpora un **planner** grazie al quale è in grado di produrre la sequenza di mosse necessarie per posizionare il _DDR_ a una determinata coordinata, con un preciso direzionamento.

In seguito, i messaggi con cui è possibile interagire con il _basicrobot_.

```
    Dispatch cmd       	: cmd(MOVE)         
    Dispatch end       	: end(ARG)         
    
    Request step       : step(TIME)	
    Reply stepdone     : stepdone(V)                 for step
    Reply stepfailed   : stepfailed(DURATION, CAUSE) for step

    Event  sonardata   : sonar( DISTANCE ) 	   
    Event obstacle     : obstacle(X) 
    Event info         : info(X)    

    Request  doplan     : doplan( PATH, STEPTIME )
    Reply doplandone    : doplandone( ARG )    for doplan
    Reply doplanfailed  : doplanfailed( ARG )  for doplan

    Dispatch setrobotstate: setpos(X,Y,D) //D =up|down!left|right

    Request engage        : engage(OWNER, STEPTIME)	
    Reply   engagedone    : engagedone(ARG)    for engage
    Reply   engagerefused : engagerefused(ARG) for engage

    Dispatch disengage    : disengage(ARG)

    Request checkowner    : checkowner(CALLER)
    Reply checkownerok    : checkownerok(ARG)      for checkowner
    Reply checkownerfailed: checkownerfailed(ARG)  for checkowner
    
    Event alarm           : alarm(X)
    Dispatch nextmove     : nextmove(M)
    Dispatch nomoremove   : nomoremove(M)
    
    Dispatch setdirection : dir( D )  //D =up|down!left|right

    //Inglobamento endosimbitico di robotpos
    Request moverobot    :  moverobot(TARGETX, TARGETY)  
    Reply moverobotdone  :  moverobotok(ARG)                    for moverobot
    Reply moverobotfailed:  moverobotfailed(PLANDONE, PLANTODO) for moverobot
    
    //Richieste di info 
    Request getrobotstate : getrobotstate(ARG)
    Reply robotstate      : robotstate(POS,DIR)  for getrobotstate

    Request getenvmap     : getenvmap(X)
    Reply   envmap        : envmap(MAP)  for getenvmap
```

Vi è un **abstraction gap** tra i requisiti del _cargorobot_ e quelli soddisfatti dal _basicrobot_; quest'ultimo **non è in grado caricare/scaricare container**. Il committente ha però specificato che nel sistema da sviluppare sarà sufficiente essere in grado di muovere il _DDR_ virtuale dentro a _WEnv_, **le operazioni di carico e scarico dei container di _cargorobot_ possono essere ridotte a delle stampe o noop**. Il _basicrobot_ fornito è quindi sufficiente nonostante non sia in grado di caricare/scaricare container per limitazione dell'ambiente virtuale _WEnv_.

Si noti a questo proposito la relazione tra i componenti _cargorobot_ e _basicrobot_. _cargorobot_ risulta essere un componente **interfaccia** che permette al resto del sistema di pilotare un DDR robot. Il _basicrobot_ risulta essere una **implementazione** di questa interfaccia con cui si pilota la specifica tipologia di _DDR_ simulata da _WEnv_. In una futura estensione del sistema, se si desidererà sostituire _WEnv_ con un _DDR_ reale (con cui si può anche ), sarà sufficiente progettare un nuovo componente in grado di comandare gli attuatori di questo _DDR_ rispettando l'interfaccia imposta da _cargorobot_. Successivamente, bisognerà modificare _cargorobot_ in modo tale che utilizzi questo nuovo componente come implementazione della sua interfaccia al posto di _basicrobot_. **Il resto del sistema rimarrà invariato** ma in questo modo funzionerà anche nel mondo fisico e non solo in quello virtuale. 

Per questi motivi, sara fondamentale prevedere nel _cargorobot_ anche le operazioni di carico/scarico dei container anche se non sarà necessario implementarle.







Il **_Sonar_** è un componente attivo che, tenendo traccia delle misurazioni periodiche che effettua, è in grado di rilevare la presenza o l'assenza di un _container_ presso l'_IO-port_. In quanto componente attivo è opportuno modellare il _sonar_ con un **attore**. 

Il _sonar_ è infine caratterizzato da una costante chiamata **_DFREE_** che definisce:
- la distanza massima con cui una misurazione può essere interpretata: "come assenza di container" (DFREE/2)
- la distanza massima con cui una misurazione può essere considerata valida (DFREE)

Siccome _DFREE_ rappresenta una distanza è opportuno modellarlo come un valore rele maggiore di zero.

Un componente strettamente associato al _sonar_ è il **_Led_**. I requisiti speicificano che in caso di misurazioni del _sonar_ riconducibili a malfunzionamenti di quest'ultimo, il led **deve essere acceso da _cargoservice_** in modo da notificare la presenza del malfunzionamento. Questo è un comportamento **passivo** e pertanto modellabile tramite un POJO. Tuttavia, siccome è presumibile che il led fisico da accendere risieda in un nodo fisico distinto rispetto a quello di _cargoservice_, è più opportuno modellare _led_ come un **attore** parte di un sistema distribuito.   





Il **_Cargoservice_** è il macrocomponente principale che implementa il core-buisness del sistema. Si occupa di orchestrare gli altri componenti del sistema per decidere:
- se accettare o rifiutare una richiesta di carico
- quale slot libero associare alla richiesta che si sta servendo
- dove e quando spostare il _cargorobot_
- di interrompere tutte le attività quando si accorge di un malfunzionamento del _sonar_

Modellare il cargoservice come **attore** è ancora più opportuno rispetto ai casi precedenti in quanto oltre ad essere un componente attivo, è anche un componente **reattivo** agli eventi del _sonar_.


// bisogna capire che cosa intende il prof con stato del deposito
Infine, la **dynamically updated web GUI** è la pagina web che mostra graficamente e in tempo reale, lo stato del deposito. Si noti che non è previsto di poter visualizzare i container, ne le informazioni relative ai prodotti al loro inerno.









## Macrocomponenti (la roba tra parentesi quadre serve solo per fare mente locale, non va lasciata)
//Il sistema deve essere distribuito su N nodi computazionali diversi???
(Proposta:
potenzialmente ognuno dei macrocomponenti del sistema può essere distribuito su un nodo fisico distinto, tuttavia, decidere il grado di distribuzione del sitema durante lo sprint 0 risulta prematuro, per questo motivo si rimanda questa decisione dopo aver effettuato le analisi del problema nei prossimi sprint.

Fanno eccezione i componenti che modellano un'entità reale, il deployment di quest'ultimi va per forza effettuato sul nodo fisico in cui sono presenti i sensori/attuatori da comandare. Per questo motivo si può già dallo sprint 0 affermare che _sonar_ e _led_ risiederanno su un nodo fisico distinto rispetto al resto del sistema (anche lo stesso). 

In futuro, anche il componente che comanderà il _DDR_ fisico (sostituendo l'attuale _basicrobot_) dovrà per gli stessi motivi essere distribuito su un nodo fisico a se stante.
)


Segue l'elenco dei macrocomponenti software del sistema:

- cargoservice (orchestratore che comunica con praticamente tutti)
    - [riceve eventi riguardo alla presenza/assenza di container da sonarservice]
    - [manda query a product service per recuperare il peso del prodotto da caricare]
    - [comunica con holdservice per recuperare il prossimo slot libero e per occuparlo]
- productservice
    - [registra i prodotti]
    - [riceve richieste di registrazione da clienti esterni al sistema]
    - [riceve query da cargoservice per recuperare il peso relativo ad un prodotto che deve caricare]
- cargorobot
- basicrobot
- WEnv
- sonar 
    - [invia aggiornamento sulla presenza o meno del container al cargoservice]
- led
    - [viene acceso da cargoservice quando il sonar emette una evento riconducibile ad un malfunzionamento]
- hold
    - [comunica con cargoservice per fornire il prossimo slot libero e per aggiornare il suo stato  ]
- web-gui
    - [riceve gli aggiornamenti sullo stato del deposito da holdservice]


quelli da sviluppare sono:
- cargoservice
- cargorobot
- sonar
- led
- hold
- webgui


## Architettura di Riferimento

### Messaggi
I messaggi costituiscono la base fondamentale della comunicazione nel modello distribuito ad attori adottato.  

I requisiti non specificano la maggior parte delle interazioni tra i componenti del sistema, di conseguenza ci si limita definire solamente i messaggi che modellano le interazioni espresse esplicitamente. Le interazioni rimanenti verranno discusse e modellate durante le fasi di analisi del problema nei successivi sprint.  


```text
    <inserire qui request e response>
```

```text
    <inserire qui request e response>
```

### Diagramma dell'Architettura

Il seguente diagramma rappresenta l'architettura iniziale di riferimento per lo sprint 1.

![Errore Caricamento Diagramma Architettura Riferimento](architettura-riferimento.png)

## Piano di Test

E' disponibile il [riferimanto al codice dei test](./nonloso).

### Test di Sistema

Il test di sistema è un collaudo interno che in questa prima fase ha il preciso compito di confermare il corretto funzionamento della rete e delle interazioni via messaggi attraverso di essa dei vari componenti (mock in questa prima fase).  
Questo obbliga, inevitabilmente, a riflettere sulle interazioni tra i componenti del sistema, prima di realizzarli.

Scenario di test 1: richiesta di intervento di carico accettata da cargoservice

```text
    <inserire qui il codice dello es. richiesta di carico con peso massimo già raggiunto>
```

Scenario di test 2: richiesta di intervento di carico rifiutata a causa di altro intervento di carico in corso

Scenario di test 3: richiesta di intervento di carico rifiutata a causa della mancanza di slot libero

Scenario di test 4: richiesta di intervento di carico rifiutata a causa dell'assenza del prodotto nel db gestito da productservice

Scenario di test 5: richiesta di intervento di carico rifiutata a causa del peso eccessivo del container

## Piano di Lavoro

Oltre a questo sprint 0 iniziale, dedicato all'impostazione del progetto, nel nostro processo Scrum abbiamo previsto tre sprint operativi:
1. Sprint 1
    - cargoservice (corebuisness del sistema)
    - cargorobot
1. Sprint 2
    - sonar
    - hold
1. Sprint 3
    - led
    - web-gui

| Numero sprint             | Data inizio (indicativa)  | Data fine (indicativa)    | Lavoro Stimato Totale (h) |
|---------------------------|---------------------------|---------------------------|---------------------------|
| Sprint 1                  | 07/07/2025                | 11/07/2025                | 30                        |
| Sprint 2                  | 14/07/2025                | 18/07/2025                | 20                        |
| Sprint 3                  | 21/07/2025                | 25/08/2025                | 20                        |

La pianificazione temporale costituisce un riferimento per il team.  
Sono comunque contemplate variazioni, nel limite del ragionevole, purché non compromettano il ritmo generale del lavoro.  
Eventuali modifiche significative potranno essere apportate solo in presenza di esigenze straordinarie, cambiamenti rilevanti durante il percorso progettuale, o situazioni tali da non poter essere ignorate.  

**La presentazione finale è prevista, indicativamente, in data 31 luglio 2025.**

## Team di Lavoro e Attività Specifiche

| <img src="team/pietro.jpg" width="150"/> | <img src="team/kevin.jpg" width="150"/> | <img src="team/andrea.jpg" width="150"/> |
|-----------------------|-----------------------|-----------------------|
| Bertozzi Pietro       | Koltraka Kevin        | La Rocca Andrea       |
| Redazione estensiva del documento "chronicle" dello sprint 0 | Impostazione iniziale e raccolta del materiale di riferimento (sconfitta della sindrome del foglio bianco) | Correzione a posteriori del Vocabolario |

Buona parte dello studio al fine di decidere come formalizzare e rappresentare i requisiti è stato discusso in gruppo.  
Le scelte sulla architettura del sistema e l'organizzazione in macrocomponenti, nonche l'organizzaizone dei tempi di sviluppo, sono state interamente di gruppo.