package com.gui.policy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.autoinsure.model.bom.Dealer;
import com.autoinsure.model.bom.DealerVariables;
import com.autoinsure.model.bom.Plan;
import com.autoinsure.model.bom.Price;
import com.autoinsure.model.bom.Product;
import com.autoinsure.model.bom.Vehicle;
import com.autoinsure.service.bom.common.VersionManager;
import com.autoinsure.service.bom.makemodel.MakeModelService;
import com.autoinsure.service.bom.product.ProductManager;
import com.autoinsure.service.plan.PlanService;
import com.autoinsure.service.policy.ProductDetailsService;
import com.framework.bom.ServiceLocator;
import com.service.bom.dealer.DealerService;

/**
 * 
 * @author mahendra
 * 
 * @date 17 Sep 2011
 */
public class PrelimAction extends PlanAction {

	@Override
	public void init(Map data) throws Exception {
		this.treeMap = data;
		String operation = (String) treeMap.get("operation");
		if (!"amendment".equals(operation)) {
			initData();
		}
		DealerService dealerService = (DealerService) ServiceLocator.getInstance().getBean("dealerService");
		Map criteria = new HashMap();
		criteria.put("organisation.client.clientId", getClientId());
		List dealers = dealerService.getDealers(criteria);
		treeMap.put("DealerList", new Vector(dealers));

	}

	private void initData() throws Exception {
		if (treeMap.get("policyMap") == null) {
			Map policyMap = new HashMap();
			policyMap.put("vehicle", new Vehicle());
			policyMap.put("lastAction", "Create");
			policyMap.put("policyStatus", "Active");
			treeMap.put("policyMap", policyMap);
		}
		if (treeMap.get("plan") == null) {
			treeMap.put("plan", new Plan());
		}
		MakeModelService mmsService = (MakeModelService) ServiceLocator.getInstance().getBean(
				"makeModelService");
		Map criteria = new HashMap();
		criteria.put("clientId", getClientId());
		List makes = mmsService.getMakes(criteria);
		treeMap.put("MakeList", new Vector(makes));
		if (makes != null && makes.size() == 1) {
			Map makeModel = new HashMap();
			makeModel.put("make", makes.get(0));
			criteria.put("make", makeModel.get("make"));
			List models = mmsService.getModels(criteria);
			treeMap.put("ModelList", new Vector(models));
			treeMap.put("ClassList", new Vector());
			if (models != null && models.size() == 1) {
				makeModel.put("model", models.get(0));
				criteria.put("make", makeModel.get("model"));
				List classes = mmsService.getModelTypes(criteria);
				treeMap.put("ClassList", new Vector(classes));
				if (classes != null && classes.size() == 1) {
					makeModel.put("clss", classes.get(0));
				}
			}
			treeMap.put("MakeModel", makeModel);
		}
	}

	@Override
	protected void executeAction(String actionCommand) throws Exception {
		Map makeModel = (Map) treeMap.get("MakeModel");
		makeModel.put("clientId", getClientId());
		MakeModelService mmsService = (MakeModelService) ServiceLocator.getInstance().getBean(
				"makeModelService");
		if ("Make".equals(actionCommand) && makeModel.get("make") != null) {
			Map criteria = new HashMap();
			criteria.put("clientId", getClientId());
			criteria.put("make", makeModel.get("make"));
			treeMap.put("ModelList", new Vector(mmsService.getModels(criteria)));
			treeMap.put("ClassList", new Vector());
		} else if ("Model".equals(actionCommand)) {
			Map criteria = new HashMap();
			criteria.put("clientId", getClientId());
			criteria.put("make", makeModel.get("make"));
			criteria.put("model", makeModel.get("model"));
			List classes = mmsService.getModelTypes(criteria);
			treeMap.put("ClassList", new Vector(classes));
		} else if ("Show Products".equals(actionCommand)) {
			updatePlanProducts();
		} else if ("Product".equals(actionCommand)) {
			updateProduct();
		} else if ("Term".equals(actionCommand)) {
			updatePolicyAmount();
		}
	}

	private void updatePlanProducts() {
		Map policyMap = (Map) treeMap.get("policyMap");
		Map productMap = (Map) treeMap.get("product");
		if (productMap == null)
			productMap = new HashMap();
		Map criteria = new HashMap();
		criteria.put("dealer.dealerId", ((Dealer) policyMap.get("dealer")).getDealerId());
		DealerService dlrService = (DealerService) ServiceLocator.getInstance().getBean("dealerService");
		List<Product> products = dlrService.getDealerProducts(criteria);
		treeMap.put("dlrVars", dlrService.getDealerVariables(criteria));
		for (Product product : products) {
			Map prodMap = (Map) productMap.get(product.getType());
			if (prodMap == null) {
				prodMap = new HashMap();
				Vector prodlist = new Vector();
				prodlist.add(product);
				prodMap.put("ProductList", prodlist);
				productMap.put(product.getType(), prodMap);
			} else if (prodMap != null) {
				Vector prodlist = (Vector) prodMap.get("ProductList");
				if (!prodlist.contains(product))
					prodlist.add(product);
			}
		}
		treeMap.put("products", productMap);
	}

	private void updatePolicyAmount() throws Exception {
		Map<String, Map> productMap = (Map<String, Map>) treeMap.get("products");
		if (productMap != null && !productMap.isEmpty()) {
			for (String prodType : productMap.keySet()) {
				Map policyProduct = (Map) productMap.get(prodType);
				Product product = (Product) policyProduct.get("product");
				if (product != null) {
					long clinetProdId =0;// ProductManager.getInstance().getClientProductId(getClientId(),product.getProductId());
					Map policyMap = (Map) treeMap.get("policyMap");
					policyMap.put("versionId", VersionManager.getInstance().getCurrVersion(clinetProdId));
					Map criteria = new HashMap();
					List<DealerVariables> dlrVars = (List<DealerVariables>) treeMap.get("dlrVars");
					for (DealerVariables dlrVar : dlrVars) {
						if (dlrVar.getProduct().getProductId() == product.getProductId()) {
							policyProduct.put("dealerVariables", dlrVar);
							criteria.put("rateGroup", dlrVar.getRateGroup());
							break;
						}
					}
					treeMap.put("clientId", getClientId());
					List<Price> prices = PlanService.getInstance().getProductPrice(treeMap, product);
					if (prices != null && prices.size() == 1) {
						policyProduct.put("coverAmount", prices.get(0).getPrice());
					} else if (prices == null || prices.isEmpty()) {

					}
				}
			}
		}
	}

	private void updateProduct() {
		Map<String, Map> productMap = (Map<String, Map>) treeMap.get("products");
		if (productMap != null && !productMap.isEmpty()) {
			for (String prodType : productMap.keySet()) {
				Map policyProduct = (Map) productMap.get(prodType);
				Product product = (Product) policyProduct.get("product");
				if (product != null) {
					ProductDetailsService proDetailsService = (ProductDetailsService) ServiceLocator.getInstance().getBean("productDetailsService");
					long clinetProdId =0;// ProductManager.getInstance().getClientProductId(getClientId(),product.getProductId());
					Map criteria = new HashMap();
					criteria.put("productId", product.productId);
					criteria.put("versionId", VersionManager.getInstance().getCurrPriceVersion(clinetProdId));
					policyProduct.put("CoverTypeList", new Vector(proDetailsService.getCoverTypes(criteria)));
					policyProduct.put("TermList", new Vector(proDetailsService.getTerms(criteria)));
				}
			}
		}
	}
}
