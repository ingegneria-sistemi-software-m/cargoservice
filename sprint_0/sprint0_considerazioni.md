Il documento (‘chronicle’) relativo a ciascuno SPRINT deve contenere:
1. Un link al testo dei requisiti del committente
2. Un link allo sprint precedente (se esiste)
3. Una immagine della architettura al termine dello sprint precedente (se esiste) detta ARCHITETTURA INIZIALE DI RIFERIMENTO
4. Un chiara definizione del GOAL dello Sprint
5. Un piano di testing relativo al Testing (User Acceptance Test)


Obiettivi sprint0:
0. analizzare i requisiti... ok, ma cosa significa?

1. allinearsi con il committente sui requisiti
    - disambiguando i termini e formalizzandoli con un componente software
2. definire una macro-architettura del sistema (**dai soli requisiti**)
    - definendo i bounded contexts
    - le loro interazioni (messaggi)
    - **test plan per il bounded context**
    - i componenti software da scrivere
        - relativamente a quest'ultimi, vedere se sono da scrivere oppure si ha già qualcosa con un abstraction gap appropriato
3. definire un piano di lavoro
    - definendo l'obiettivo del prossimi sprint
        - ovvero quale sottoinsieme dei requisiti quest'ultimi affrontano
    - distribuendo il lavoro tra i membri del gruppo


**Inoltre**
- cosa è un attore, cosa è un POJO
- attributi e invarianti di ogni entità del sistema
- quali parti del sistema possono essere distribuite e quali no


## analisi requisiti

### bounded contexts
- quali interazioni tra i contesti?
    - esclusivamente deducibili dai requisiti

### formalizzazione termini e requisiti

- modello delle macro parti del sistema (sistema logico di riferimento)
    - quali componenti sono già forniti
    - quali sono quelli da sviluppare


## piano di lavoro


## modello delle macro-parti del sistema