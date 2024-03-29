package com.gui.claim;

import java.util.List;
import java.util.Vector;

import com.autoinsure.model.bom.Claim;
import com.autoinsure.service.claim.ClaimService;
import com.framework.bom.ServiceLocator;
import com.gui.BaseAction;

/**
 * 
 * @author mahendra
 * 
 * @date 4 Feb 2012
 */
public class ClaimAction extends BaseAction {
	private Vector claimList;

	@Override
	protected void executeAction(String actionCommand) throws Exception {
		ClaimService claimService = (ClaimService) ServiceLocator.getInstance().getBean("claimService");
		if ("Save".equals(actionCommand)) {
			if (treeMap != null) {
				treeMap.put("clientId", getClientId());
				claimService.saveClaim((Claim) treeMap.get("claim"));
			}
		} else if ("Search".equals(actionCommand)) {
			List list = claimService.searchClaims(input);
			if (list != null) {
				claimList.clear();
				claimList.addAll(list);
			}
		} else if ("View".equals(actionCommand)) {
			Claim claim = (Claim) getSelectedObj().firstElement();
			addActionData(treeMap);
		}

	}

	public Vector getClaimList() {
		return claimList;
	}

	public void setClaimList(Vector claimList) {
		this.claimList = claimList;
	}

}
