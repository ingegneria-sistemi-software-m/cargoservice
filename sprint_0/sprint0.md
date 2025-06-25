# Sprint 0

## Obiettivi
L'obiettivo prinicipale dello sprint 0 è analizzare i [requisiti](../requisiti.md) forniti dal committente riguardo al progetto **TemaFinale25** eliminando eventuali ambiguità relative ai termini utilizzati e formalizzando quest'ultimi con dei componenti software (già sviluppati o da sviluppare). 

Una volta formalizzati i requisiti si procederà con la definizione di una prima **architettura logica generale del sistema** che sarà il riferimento iniziale per lo sprint 1. Questa architettura iniziale sarà costituita dai **macro-componenti principali** (bounded context) deducibili dal documento dei requisiti, e dalle **interazioni** tra quest'ultimi sotto forma di messaggi (mappa di contesto). L'architettura logica sarà anche corredata di un relativo piano di test iniziale.

Terzo e ultimo obiettivo dello sprint 0 sarà quello di definire un **piano di lavoro** in cui si definirà il numero e gli obiettivi degli sprint necessari al completamento del sistema. Inoltre, in questa sezione si deciderà come suddividere il lavoro tra i membri del gruppo e quali sprint potranno essere realizzati in parallelo.







## Requisiti
I requisiti sono descritti dal committente nel documento: [documento dei requisiti](../requisiti.md) 

## Analisi dei requisiti
- disambuiguiamo i termini 
- diciamo quali componenti sono da sviluppare
- quali sono da formalizzare con un pezzo di software già in nostro possesso
- cosa è un attore, cosa è un POJO, più eventuali attributi di entità
- ricorda anche di formalizzare quelli che saranno i macrocomponenti del sistema
- **pezzo relativo alla mappa e wenv praticamente da copiare e incollare**
- ricorda di linkare alla repo del corso i progetti che si possono riciclare


In seguito la fomalizzazione dei termini trovati all'interno del documento dei requisiti.


#### Differential Drive Robot (DDR)
robot **fisico** capace di muoversi utilizzando due ruote motrici indipendenti che gli consentono di avanzare, arretrare o ruotare su sé stesso. 
- All'interno del sistema il DDR verrà utilizzato come supporto fisico per l'implementazione del _cargorobot_.


#### Hold (deposito)
- slots
- IO-Port
- teoricamente POJO ma anche ATTORE per thread-safety


#### Cargo Robot
robot **logico** capace, **sotto richiesta**, di: muoversi liberamente all'interno del _deposito_, recuperare un container dall'_IO-port_, posizionare un container precendentemente recuperato in uno degli _slot_ liberi all'interno del _deposito_

**Il committente fornisce del software per la modellazione del _cargorobot_**. In particolare:
- l'ambiente virtuale [WEnv](./sprint0.md) (aggiusta link) che simula la stiva di una nave in cui il _cargorobot_ dovrà operare
- un componente software chiamato [basicrobot](./sprint0.md) (aggiusta link) che permette di governare un DDR virtuale all'interno di WEnv 

**dettagli WEnv**
// parla della mappa

**dettagli basicrobot**
// Il robot è un oggetto di dimensioni finite, inscrivibile in un cerchio di diametro D (unità robotica) ed esegue movimenti a velocità costante.

// Il basicrobot fornito dal committente è un puro esecutore di comandi, con cui il robot può effettuare singole mosse o sequenze di mosse, a seguito di messaggi di richiesta.

```
listone messaggi con cui si può interagire con il basicrobot 
```


#### Product (prodotto)
- racchiusi all'interno di un container e posizionati all'interno di uno slot
- peso
- id


#### Product Service






#### Sonar











### Wenv | ambiente virtuale per la simulazione del cargorobot  
Il committente fornisce un ambiente virtuale che modella il deposito della nave (**hold**) e il movimento **cargorobot** al suo interno [[wenv]](link a documentazione repo prof per wenv).

...




## Macro-componenti



## Architettura logica generale del sistema

![diagramma architettura logica](non_esiste_ancora.png)

### messaggi




## Piano di test

vari scenari... 
- es. richiesta di carico con peso massimo già raggiunto









## Piano di lavoro






## Team
link vari e (molto importante) le nostre foto