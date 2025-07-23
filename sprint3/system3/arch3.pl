%====================================================================================
% arch3 description   
%====================================================================================
request( load_product, load_product(PID) ). %la richiesta di carico di un container che arriva a cargoservice: contiene il PID del prodotto
reply( load_accepted, load_accepted(SLOT) ). %%for load_product | può essere accettata, con restituzione dello slot assegnato al container
reply( load_refused, load_refused(CAUSA) ). %%for load_product | può essere rifiutata per vari motivi (peso, slot occupati, PID inesistente)
request( getProduct, product(ID) ).
reply( getProductAnswer, product(JSonString) ).  %%for getProduct
request( reserve_slot, reserve_slot(WEIGHT) ). %richiesta verso hold per prenotare uno slot. Contiene il peso del prodotto da caricare
reply( reserve_slot_success, reserve_slot_success(SLOT) ). %%for reserve_slot | se la richiesta è soddisfacibile, hold restituisce il nome/id dello slot prenotato
reply( reserve_slot_fail, reserve_slot_fail(CAUSA) ). %%for reserve_slot | fallisce se il peso supera MaxLoad oppure se non c'è uno slot libero
request( get_hold_state, get_hold_state(X) ). %richiesta verso hold per conoscere lo stato iniziale del deposito.Contiene il peso attuale e lo stato degli slot
reply( hold_state, hold_state(JSonString) ). %%for get_hold_state | risposta verso gui da parte di hold con le informazioni interne del deposito
event( hold_update, hold_update(JSonString) ). %evento che avvisa di un cambiamento nello stato interno di hold
request( handle_load_operation, handle_load_operation(SLOT) ). %richiesta verso cargorobot per l'intervento di carico
reply( load_operation_complete, load_operation_complete(OK) ). %%for handle_load_operation | l'intervento di carico non può fallire quindi prevediamo solo una risposta
event( container_arrived, container_arrived(X) ). %evento che avvisa della presenza di un container presso l'IO-port
event( container_absent, container_absent(X) ). %evento che avvisa dell'assenza di un container presso l'IO-port
event( interrompi_tutto, interrompi_tutto(X) ). %evento che avvisa di un malfunzionamento del sonar e che quindi costringe a interrompre tutte le attività
event( riprendi_tutto, riprendi_tutto(X) ). %evento che avvisa del ripristino del sonar e che quindi permette di continuare tutte le attività
dispatch( continue, continue(X) ).
dispatch( cmd, cmd(MOVE) ).
dispatch( end, end(ARG) ).
request( step, step(TIME) ).
reply( stepdone, stepdone(V) ).  %%for step
reply( stepfailed, stepfailed(DURATION,CAUSE) ).  %%for step
event( sonardata, sonar(DISTANCE) ).
event( obstacle, obstacle(X) ).
event( info, info(X) ).
request( doplan, doplan(PATH,STEPTIME) ).
reply( doplandone, doplandone(ARG) ).  %%for doplan
reply( doplanfailed, doplanfailed(ARG) ).  %%for doplan
dispatch( setrobotstate, setpos(X,Y,D) ).
request( engage, engage(OWNER,STEPTIME) ).
reply( engagedone, engagedone(ARG) ).  %%for engage
reply( engagerefused, engagerefused(ARG) ).  %%for engage
dispatch( disengage, disengage(ARG) ).
request( checkowner, checkowner(CALLER) ).
reply( checkownerok, checkownerok(ARG) ).  %%for checkowner
reply( checkownerfailed, checkownerfailed(ARG) ).  %%for checkowner
event( alarm, alarm(X) ).
dispatch( nextmove, nextmove(M) ).
dispatch( nomoremove, nomoremove(M) ).
dispatch( setdirection, dir(D) ).
request( moverobot, moverobot(TARGETX,TARGETY) ).
reply( moverobotdone, moverobotok(ARG) ).  %%for moverobot
reply( moverobotfailed, moverobotfailed(PLANDONE,PLANTODO) ).  %%for moverobot
request( getrobotstate, getrobotstate(ARG) ).
reply( robotstate, robotstate(POS,DIR) ).  %%for getrobotstate
request( getenvmap, getenvmap(X) ).
reply( envmap, envmap(MAP) ).  %%for getenvmap
%====================================================================================
context(ctx_cargoservice, "localhost",  "TCP", "8000").
context(ctx_iodevices, "rpi.local",  "TCP", "8001").
context(ctx_basicrobot, "127.0.0.1",  "TCP", "8020").
context(ctx_productservice, "127.0.0.1",  "TCP", "8111").
 qactor( basicrobot, ctx_basicrobot, "external").
  qactor( productservice, ctx_productservice, "external").
  qactor( cargoservice, ctx_cargoservice, "it.unibo.cargoservice.Cargoservice").
 static(cargoservice).
  qactor( cargorobot, ctx_cargoservice, "it.unibo.cargorobot.Cargorobot").
 static(cargorobot).
  qactor( hold, ctx_cargoservice, "it.unibo.hold.Hold").
 static(hold).
  qactor( external_client, ctx_cargoservice, "it.unibo.external_client.External_client").
 static(external_client).
