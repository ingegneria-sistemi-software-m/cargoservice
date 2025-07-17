%====================================================================================
% webgui description   
%====================================================================================
event( hold_update, hold_update(JSonString) ).
%====================================================================================
context(ctx_webgui, "localhost",  "TCP", "8050").
context(ctx_cargoservice, "127.0.0.1",  "TCP", "8000").
 qactor( hold, ctx_cargoservice, "external").
  qactor( webgui, ctx_webgui, "it.unibo.webgui.Webgui").
 static(webgui).
