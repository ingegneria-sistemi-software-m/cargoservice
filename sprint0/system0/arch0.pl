%====================================================================================
% arch0 description   
%====================================================================================
request( load_product, load_product(PID) ). %la richiesta di carico di un container che arriva a cargoservice: contiene il PID del prodotto
reply( load_accepted, load_accepted(SLOT) ). %%for load_product | la richiesta di carico può essere accettata, con restituzione dello slot assegnato al container
reply( load_refused, load_refused(CAUSA) ). %%for load_product | la richiesta di carico può essere rifiutata per vari motivi (peso, slot occupati, PID inesistente)
%====================================================================================
context(ctx_cargoservice, "localhost",  "TCP", "8000").
 qactor( cargoservice, ctx_cargoservice, "it.unibo.cargoservice.Cargoservice").
 static(cargoservice).
  qactor( cargorobot, ctx_cargoservice, "it.unibo.cargorobot.Cargorobot").
 static(cargorobot).
  qactor( basicrobot, ctx_cargoservice, "it.unibo.basicrobot.Basicrobot").
 static(basicrobot).
  qactor( productservice, ctx_cargoservice, "it.unibo.productservice.Productservice").
 static(productservice).
  qactor( hold, ctx_cargoservice, "it.unibo.hold.Hold").
 static(hold).
  qactor( webgui, ctx_cargoservice, "it.unibo.webgui.Webgui").
 static(webgui).
  qactor( sonar, ctx_cargoservice, "it.unibo.sonar.Sonar").
 static(sonar).
  qactor( led, ctx_cargoservice, "it.unibo.led.Led").
 static(led).
  qactor( external_client, ctx_cargoservice, "it.unibo.external_client.External_client").
 static(external_client).
