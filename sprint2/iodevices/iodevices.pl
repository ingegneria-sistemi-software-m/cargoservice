%====================================================================================
% iodevices description   
%====================================================================================
event( measurement, measurement(CM) ). %le misurazioni sono in CM e sono sempre > 0
event( container_arrived, container_arrived(X) ). %evento che avvisa della presenza di un container presso l'IO-port
event( container_absent, container_absent(X) ). %evento che avvisa dell'assenza di un container presso l'IO-port
event( interrompi_tutto, interrompi_tutto(X) ). %evento che avvisa di un malfunzionamento del sonar e che quindi costringe a interrompre tutte le attività
event( riprendi_tutto, riprendi_tutto(X) ). %evento che avvisa del ripristino del sonar e che quindi permette di continuare tutte le attività
%====================================================================================
context(ctx_iodevices, "localhost",  "TCP", "8001").
 qactor( measure_processor, ctx_iodevices, "it.unibo.measure_processor.Measure_processor").
 static(measure_processor).
  qactor( sonar_listener, ctx_iodevices, "it.unibo.sonar_listener.Sonar_listener").
 static(sonar_listener).
