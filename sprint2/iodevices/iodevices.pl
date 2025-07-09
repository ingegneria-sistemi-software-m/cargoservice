%====================================================================================
% iodevices description   
%====================================================================================
event( measurement, measurement(CM) ). %le misurazioni sono in CM e sono sempre > 0
%====================================================================================
context(ctx_iodevices, "localhost",  "TCP", "8001").
 qactor( sonarsimul, ctx_iodevices, "it.unibo.sonarsimul.Sonarsimul").
 static(sonarsimul).
  qactor( sonarlistener, ctx_iodevices, "it.unibo.sonarlistener.Sonarlistener").
 static(sonarlistener).
