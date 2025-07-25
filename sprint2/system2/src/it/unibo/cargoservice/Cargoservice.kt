/* Generated by AN DISI Unibo */ 
package it.unibo.cargoservice

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import it.unibo.kactor.sysUtil.createActor   //Sept2023
//Sept2024
import org.slf4j.Logger
import org.slf4j.LoggerFactory 
import org.json.simple.parser.JSONParser
import org.json.simple.JSONObject


//User imports JAN2024

class Cargoservice ( name: String, scope: CoroutineScope, isconfined: Boolean=false, isdynamic: Boolean=false ) : 
          ActorBasicFsm( name, scope, confined=isconfined, dynamically=isdynamic ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		//IF actor.withobj !== null val actor.withobj.name» = actor.withobj.method»ENDIF
		
				var Cur_prod_PID = -1
				var Cur_prod_weight = -1
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outblue("$name | STARTS")
						
									// inizializzo lo stato 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="wait_request", cond=doswitch() )
				}	 
				state("wait_request") { //this:State
					action { //it:State
						CommUtils.outblue("$name | waiting for request")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t00",targetState="get_prod_weight",cond=whenRequest("load_product"))
				}	 
				state("get_prod_weight") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("load_product(PID)"), Term.createTerm("load_product(PID)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 Cur_prod_PID = payloadArg(0).toInt()  
								CommUtils.outblue("$name | checking with productservice for the weight of PID: $Cur_prod_PID")
								request("getProduct", "product($Cur_prod_PID)" ,"productservice" )  
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t01",targetState="check_prod_answer",cond=whenReply("getProductAnswer"))
				}	 
				state("check_prod_answer") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("product(JSonString)"), Term.createTerm("product(JSonString)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
												val jsonStr = payloadArg(0)
												Cur_prod_weight = main.java.Product.getJsonInt(jsonStr, "weight")
								CommUtils.outblue("$name | the weight of PID is: $Cur_prod_PID")
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="reserve_slot", cond=doswitchGuarded({ Cur_prod_weight > 0  
					}) )
					transition( edgeName="goto",targetState="get_weight_fail", cond=doswitchGuarded({! ( Cur_prod_weight > 0  
					) }) )
				}	 
				state("get_weight_fail") { //this:State
					action { //it:State
						 
									val Causa = "'Non è stato possibile recuperare il peso di PID: $Cur_prod_PID in quanto non registrato dentro a productservice.'"
						CommUtils.outred("$name | $Causa")
						answer("load_product", "load_refused", "load_refused($Causa)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="wait_request", cond=doswitch() )
				}	 
				state("reserve_slot") { //this:State
					action { //it:State
						CommUtils.outblue("$name | checking with hold if a reservation with (PID: $Cur_prod_PID, KG: $Cur_prod_weight) is possible")
						request("reserve_slot", "reserve_slot($Cur_prod_weight)" ,"hold" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t02",targetState="load_cargo",cond=whenReply("reserve_slot_success"))
					transition(edgeName="t03",targetState="reserve_slot_fail",cond=whenReply("reserve_slot_fail"))
				}	 
				state("reserve_slot_fail") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("reserve_slot_fail(CAUSA)"), Term.createTerm("reserve_slot_fail(CAUSA)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
												val Causa = payloadArg(0) 
												val CausaMsg = "'Non è stato possibile prenotare uno slot per (PID: $Cur_prod_PID, KG: $Cur_prod_weight). \n\tCausa: $Causa'"
								CommUtils.outred("$name | $CausaMsg")
								answer("load_product", "load_refused", "load_refused($CausaMsg)"   )  
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="wait_request", cond=doswitch() )
				}	 
				state("load_cargo") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("reserve_slot_success(SLOT)"), Term.createTerm("reserve_slot_success(SLOT)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
												val Reserved_slot = payloadArg(0)
								CommUtils.outgreen("$name | product (PID: $Cur_prod_PID, KG: $Cur_prod_weight) is going to be placed in slot: $Reserved_slot")
								answer("load_product", "load_accepted", "load_accepted($Reserved_slot)"   )  
								CommUtils.outblue("$name | waiting for load completion")
								request("handle_load_operation", "handle_load_operation($Reserved_slot)" ,"cargorobot" )  
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t04",targetState="load_request_done",cond=whenReply("load_operation_complete"))
				}	 
				state("load_request_done") { //this:State
					action { //it:State
						CommUtils.outgreen("$name | product (PID: $Cur_prod_PID, KG: $Cur_prod_weight) successfully loaded!")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="wait_request", cond=doswitch() )
				}	 
			}
		}
} 
