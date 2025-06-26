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

## INFO A CASO ???
- disambuiguiamo i termini 
- diciamo quali componenti sono da sviluppare
- quali sono da formalizzare con un pezzo di software già in nostro possesso
- cosa è un attore, cosa è un POJO, più eventuali attributi di entità
- ricorda anche di formalizzare quelli che saranno i macrocomponenti del sistema
- **pezzo relativo alla mappa e wenv praticamente da copiare e incollare**
- ricorda di linkare alla repo del corso i progetti che si possono riciclare

## Requisiti

I requisiti sono descritti dal committente nel documento: [documento dei requisiti](../requisiti/). 

## Vocabolario

E' stato ritenuto più opportuno presentare i termini del dominio in un formato discorsivo, prolisso e ridondante, per garantire non solo che i termini non siano ambigui, ma anche che per il lettore sia difficile malinterpretare.
Questo si contrappone al più comune approccio, in cui il vocabolario è un mero elenco puntato, nel quale è possibile che una mancata disambiguazione, possa evolvere in errori considerevoli durante lo sviluppo.
Ci riserviamo la possibilità di ricondurci al classico vocabolario-elenco nel caso dovessero sorgere complicaizoni inaspettate, sicuri del fatto che l'eventuale conversione sarà semplice.

Un **Differential Drive Robot (DDR)** è un tipo di robot fisico e mobile che si muove grazie a due ruote motrici indipendenti, solitamente affiancate sullo stesso asse, che gli consentono di avanzare, arretrare o ruotare su sé stesso.
"All'interno del sistema il DDR verrà utilizzato come supporto fisico per l'implementazione del _cargorobot_." ???

La **stiva (Hold)** rappresenta il workspace del robot: lo scenario entro cui è confinato ed in cui svolge i suoi **interventi di carico**.
E' di forma rettangolare e sono presenti alcune aree notevoli e fisse che è bene segnalare con un nome univoco:
- La **home** è la postazione da cui il robot parte e a cui il robot fa ritorno al termine di ogni intervento.
- Gli **slots** le zone in cui vengono immagazzinati i container. Sono 5 indicano le postazioni i cui il robot può depositarei container: da notare che il container numero 5 è sempre pieno e quindi in qualche modo si riduce ad un ostacolo.
- La **IO-Port** indica la posizione dove verranno depositati i container in arrivo, e dove quindi il cargorobot li andrà a prendere per poi spostarli.
Si è detto che il deposito è l'area di lavoro del cargorobot: si noti, a tal proposito, il tentativo di descrivere il deposito, e gli elementi che lo compongono, inevitabilmente, conduce a una prima vaga descrizione dei processi che vi si svolgono (**interventi di carico**).
Naturalmente, questo dipende dalla scelta, deliberata, di utilizzare gli elementi della stiva per descriverla.  
Questo crea nella stiva una dipendenza forte dai suoi elementi, che riteniamo ragionevole, perchè non è particolarmente realistico, che una delle estensioni future preveda una stiva senza questi elementi notevoli al suo interno.

????
Non sarebbe irragionevole immaginare di rappresentare la stiva come un pojo, ma si è scelto di interpretarla come un attore per i seguenti motivi:
- thread-safety
- Single Responsability Principle: il cargorobot non gestisce la logica dei dati
- uniformità rispetto al sistema in senso ampio

Un **container** è un entità associata ad ogni richiesta di **intervento di carico** del robot, che andrà spostata dalla IO-Port ad uno degli slot liberi, e che contiene **prodotti(o merci)**.
Questi **prodotti** sono sempre all'interno di un container e sono caratterizzati da un peso e da un identificativo (pid > 0).
Ogni prodotto è **registrato** prima di essere caricato. L'operazione di **rigistrazione** consiste semplicemente nel inserimento delle caratteristiche del prodotto (ci sia aspetta quindi, prevalentemente, prodotti industriali, prodotti in serie, sempre allo stesso modo, e non artigianali ed unici).


#### Cargo Robot
robot **logico** capace, **sotto richiesta**, di: muoversi liberamente all'interno del _deposito_, recuperare un container dall'_IO-port_, posizionare un container precendentemente recuperato in uno degli _slot_ liberi all'interno del _deposito_

**Il committente fornisce del software per la modellazione del _cargorobot_**. In particolare:
- l'ambiente virtuale [WEnv](./sprint0.md) (aggiusta link) che simula la stiva di una nave in cui il _cargorobot_ dovrà operare
- un componente software chiamato [basicrobot](./sprint0.md) (aggiusta link) che permette di governare un DDR virtuale all'interno di WEnv 
    - c'è un leggero abstraction gap rispetto ai requisiti del cargorobot e quelli che soddisfa il basicrobot. Il cargorobot deve anche:
        - saper recuperare un container per poi trasportarlo
        - saper rilasciare un container
    - diventa quindi necessario estendere il basicrobot per implementare anche queste funzionalità.

**dettagli WEnv**
// parla della mappa

![Wenv](../requisiti/scene.jpg)


**dettagli basicrobot**
// Il robot è un oggetto di dimensioni finite, inscrivibile in un cerchio di diametro D (unità robotica) ed esegue movimenti a velocità costante.

// Il basicrobot fornito dal committente è un puro esecutore di comandi, con cui il robot può effettuare singole mosse o sequenze di mosse, a seguito di messaggi di richiesta.

```
listone messaggi con cui si può interagire con il basicrobot 
```

#### Product Service
- si interfaccia con un database


#### Sonar Sensor
- put in front of the io-port
- used to detect the presence of a product container when it measures a distance D, such that D < DFREE/2, during a reasonable time (e.g. 3 secs).
- attore in quanto ente attivo, che osserva le misurazioni per 3 secondi e successivamente aggiorna i suoi clienti


#### Cargoservice
- macrocomponente core buisness del sistema
- Fa da orchestrare.
- riceve le richieste di carico
    - sotto determinate condizioni le rifiuta


Infine la **dynamically updated web GUI** è la pagina web che mostra graficamente, in tempo reale, lo stato del deposito e la posizione del cargorobot al suo interno. Si noti che non è previsto di poter visualizzare i container, ne le informazioni relative ai prodotti al loro inerno.

**caro committente, come facciamo a mandare una richiesta? chi la manda? la mettiamo come possibilità dentro la gui?** ???

## Macrocomponenti

-
-
-
-
-

## Architettura di Riferimento

### Messaggi

I messaggi costituiscono la base fondamentale della comunicazione nel modello distribuito ad attori adottato.  

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

(maybe link to .kt ...)

### Test di Sistema

Il test di sistema è un collaudo interno che in questa prima fase ha il preciso compito di confermare il corretto funzionamento della rete e delle interazioni via messaggi attraverso di essa dei vari componenti (mock in questa prima fase).  
Questo obbliga, inevitabilmente, a riflettere sulle interazioni tra i componenti del sistema, prima di realizzarli.

Scenario di test 1: ...

```text
    <inserire qui il codice dello es. richiesta di carico con peso massimo già raggiunto>
```

Scenario di test 2: ...

```text
    <inserire qui il codice dello es. richiesta di carico con peso massimo già raggiunto>
```

## Piano di Lavoro

Oltre a questo sprint 0 iniziale, dedicato all'impostazione del progetto, nel nostro processo Scrum abbiamo previsto tre sprint operativi:
1. .
1. .
1. .

| Numero sprint             | Data inizio (indicativa)  | Data fine (indicativa)    | Lavoro Stimato Totale (h) |
|---------------------------|---------------------------|---------------------------|---------------------------|
| Sprint 1                  | 07/07/2025                | 11/07/2025                | nn                        |
| Sprint 2                  | 14/07/2025                | 18/07/2025                | nn                        |
| Sprint 3                  | 21/07/2025                | 25/08/2025                | nn                        |

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