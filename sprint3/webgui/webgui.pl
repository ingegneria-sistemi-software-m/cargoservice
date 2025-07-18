%====================================================================================
% webgui description   
%====================================================================================
request( get_hold_state, get_hold_state(X) ).
reply( hold_state, hold_state(JSonString) ).  %%for get_hold_state
request( reserve_slot, reserve_slot(WEIGHT) ). %richiesta verso hold per prenotare uno slot. Contiene il peso del prodotto da caricare
reply( reserve_slot_success, reserve_slot_success(SLOT) ). %%for reserve_slot | se la richiesta è soddisfacibile, hold restituisce il nome/id dello slot prenotato
reply( reserve_slot_fail, reserve_slot_fail(CAUSA) ). %%for reserve_slot | fallisce se il peso supera MaxLoad oppure se non è presente uno slot libero
event( hold_update, hold_update(JSonString) ).
%====================================================================================
context(ctx_webgui, "localhost",  "TCP", "8050").
 qactor( webgui, ctx_webgui, "it.unibo.webgui.Webgui").
 static(webgui).
  qactor( hold_mock, ctx_webgui, "it.unibo.hold_mock.Hold_mock").
 static(hold_mock).
  qactor( hold_updater, ctx_webgui, "it.unibo.hold_updater.Hold_updater").
 static(hold_updater).
