package main.java.caller;

import it.unibo.kactor.sysUtil;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.ConnectionFactory;

public class CallerTcp  {
    protected String name = "callerTcp";
    protected Interaction commChannel;

    public CallerTcp(String storageramAddr) {
    	if( storageramAddr == null ) {
    		commChannel = ConnectionFactory.createClientSupport23(ProtocolType.tcp, "localhost", "8000");
    	}else {
    		//CommUtils.outgreen(name + " |  commChannel host= storageram " + storageramAddr  );
    	    commChannel = ConnectionFactory.createClientSupport23(ProtocolType.tcp, storageramAddr, "8000");	
    	}
    	 CommUtils.outblue(name + " | connected on " + commChannel);
	}


	public void activate() throws Exception {
		CommUtils.outblue(name + " | send request");
	 	IApplMessage getreq = CommUtils.buildRequest (name, "load_product","load_product(17)", "cargoservice");
        IApplMessage answer = commChannel.request(getreq);  //raise exception
        CommUtils.outgreen(name + " | response" + answer);
 		CommUtils.outgreen(name + " | response: " + answer.msgContent());	
	}
	

//	public void delete(int k) throws Exception {
//     	IApplMessage deletecmd = CommUtils.buildDispatch(name, "deleteItem", "item("+k+")", "storagevolatile");
//        commChannel.forward(deletecmd);
//        CommUtils.outcyan(name + " | sends delete cmd   " + deletecmd);
//	}
	
	 
	public static void main(String[] args) throws Exception{
		CallerTcp caller = new CallerTcp("localhost");
		caller.activate();
	}
}
