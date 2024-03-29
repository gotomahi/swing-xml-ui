package com.gui.org;

import java.util.Vector;

import javax.swing.event.TreeSelectionEvent;

import com.autoinsure.model.bom.Client;
import com.autoinsure.service.bom.client.ClientService;
import com.framework.bom.ServiceLocator;
import com.gui.BaseAction;

/**
 * 
 * @author mahendra
 * 
 * @date 23 Apr 2011
 */
public class ClientAction extends BaseAction {
	private Vector clientList = new Vector();

	@Override
	public void executeAction(String actionCommand) throws Exception {
		String retPage = "Client";
		if ("Search".equals(actionCommand)) {
			ClientService clientService = (ClientService) ServiceLocator.getInstance().getBean("clientService");
			clientList = new Vector(clientService.getClients(input));
		} else if ("New".equals(actionCommand)) {
			clientList.add(new Client());
		} else if ("Delete".equals(actionCommand)) {
		}		
	}

	public void processTreeAction(TreeSelectionEvent e) throws Exception {

	}

	public Vector getClientList() {
		return clientList;
	}

	public void setClientList(Vector clientList) {
		this.clientList = clientList;
	}
}