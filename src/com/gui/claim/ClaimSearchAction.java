package com.gui.claim;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.autoinsure.model.bom.Claim;
import com.autoinsure.service.claim.ClaimService;
import com.framework.bom.ServiceLocator;
import com.gui.BaseAction;

/**
 * 
 * @author mahendra
 * 
 * @date 12 Feb 2012
 */
public class ClaimSearchAction extends BaseAction {
	private Vector claimList = new Vector();

	@Override
	protected void executeAction(String actionCommand) throws Exception {
		ClaimService claimService = (ClaimService) ServiceLocator.getInstance().getBean("claimService");
		if ("Search".equals(actionCommand)) {
			List claims = claimService.searchClaims(input);
			if (claims != null) {
				claimList.addAll(claims);
			}
		} else if ("Load".equals(actionCommand)) {
			Claim obj = (Claim) getSelectedObj().get(0);
			Claim claim = claimService.loadClaim(obj.getClaimId());
			Map map = new HashMap();
			map.put("claim", claim);
			addActionData(map);
		}
	}

	public Vector getClaimList() {
		return claimList;
	}

	public void setClaimList(Vector claimList) {
		this.claimList = claimList;
	}

}
