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
// responsabilità e userstory di cargoservice
// - la userstory consiste nello spiegara la lista di interazione con gli altri componenti che bisogna effettuare

L’attore ColdStorage è cruciale e riassume in sè molteplici responsabilità:
- Deve essere raggiungibile dalle due GUI per gestirne le richieste e tenere nota di eventuali richieste sospese in attesa di risposta (Queued).
- Deve gestire la logica dei ticket, inglobando al suo interno una base di conoscenza persistente, che corrisponde ai ticket emessi in precedenza. In questa sede, i ticket vengono semplicisticamente descritti da numeri interi.
- Deve tenere nota dello stato della service area, inteso come posizione corrente del trolley e stato del trolley (Busy / Idle).

Il principio di singola responsabilità non risulta dunque in questo stadio rispettato. Si decide di posticipare alla fase di progettazione l'eventuale risoluzione del problema. 

Il comportamento della ColdStorage è schematicamente riassunto dal seguente diagramma degli stati, dettagliato in sede di analisi ed espresso dal codice sprint1Robot/src/analisi.qak:


// codice qak di cargoservice




### Analisi cargorobot
// responsabilità e userstory di cargorobot 

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