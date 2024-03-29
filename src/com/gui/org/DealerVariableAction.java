package com.gui.org;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import com.autoinsure.model.bom.Dealer;
import com.autoinsure.model.bom.DealerVariables;
import com.autoinsure.model.bom.Product;
import com.autoinsure.service.bom.common.VersionManager;
import com.autoinsure.service.bom.product.ProductManager;
import com.autoinsure.service.price.PriceService;
import com.framework.bom.ServiceLocator;
import com.gui.BaseAction;
import com.service.bom.dealer.DealerService;
import com.service.bom.org.OrgService;

/**
 * 
 * @author mahendra
 * 
 * @date 23 Apr 2011
 */
public class DealerVariableAction extends BaseAction {
	private Dealer dealer;
	private Vector organisations;

	public void init(Map data) throws Exception {
		OrgService orgService = (OrgService) ServiceLocator.getInstance().getBean("orgService");
		Map criteria = new HashMap(1);
		criteria.put("client.clientId", getClientId());
		organisations = new Vector(orgService.getOrganisations(criteria));
		DealerService dealerService = (DealerService) ServiceLocator.getInstance().getBean("dealerService");
		dealer = (Dealer) getSessionValue("Dealer");
		//dealerService.initialize(dealer, "dealerVariables");
		Map products = null;//ProductManager.getInstance().getProductMap(getClientId());
		Iterator itr = products.keySet().iterator();
		while (itr.hasNext()) {
			Product product = (Product) products.get(itr.next());
			boolean exists = false;
			for (DealerVariables dlrVariables : dealer.getDealerVariables()) {
				if (dlrVariables.getProduct().getProductId() == product.getProductId()) {
					exists = true;
					dlrVariables.setSelected(true);
				}
			}
			if (!exists) {
				PriceService priceService = (PriceService) ServiceLocator.getInstance().getBean("priceService");
				criteria = new HashMap();
				criteria.put("productId", product.getProductId());
				long clientProdId = 0;//ProductManager.getInstance().getClientProductId(getClientId(),product.getProductId());
				long version = VersionManager.getInstance().getCurrPriceVersion(clientProdId);
				criteria.put("versionId", version);
				DealerVariables dealerVariables = new DealerVariables();
				dealerVariables.setDealer(dealer);
				dealerVariables.getDynamicData().put("DealerVariableList",
						new Vector(priceService.getRateGroups(criteria)));
				dealerVariables.setProduct(product);
				dealerVariables.setSelected(false);
				dealer.getDealerVariables().add(dealerVariables);
			}
		}
	}

	@Override
	public void executeAction(String actionCommand) throws Exception {
		DealerService dealerService = (DealerService) ServiceLocator.getInstance().getBean("dealerService");
		for (int i = 0; i < dealer.getDealerVariables().size(); i++) {
			DealerVariables dlrVariable = (DealerVariables) dealer.getDealerVariables().get(i);
			if (!dlrVariable.isSelected() && dlrVariable.getStatus() == dlrVariable.NEW) {
				dealer.getDealerVariables().remove(i);
			} else if (!dlrVariable.isSelected() && dlrVariable.getStatus() == dlrVariable.MODIFIED) {
				dlrVariable.setStatus(dlrVariable.DELETE);
			}
			dlrVariable.setVersion(dlrVariable.getVersion() + 1);
		}
		dealerService.persistDealerVariable(dealer.getDealerVariables());
	}

	public Dealer getDealer() {
		return dealer;
	}

	public void setDealer(Dealer dealer) {
		this.dealer = dealer;
	}

	public Vector getOrganisations() {
		return organisations;
	}

	public void setOrganisations(Vector organisations) {
		this.organisations = organisations;
	}

}
