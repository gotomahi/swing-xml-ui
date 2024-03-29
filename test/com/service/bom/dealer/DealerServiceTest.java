package com.service.bom.dealer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import com.autoinsure.model.bom.Dealer;

/**
 * 
 * @author mahendra
 * 
 * @date 25 Sep 2011
 */
public class DealerServiceTest {
	private DealerService dealerService;

	
	public void testDealerProducts() {
		Map criteria = new HashMap();
		criteria.put("dealerId", 1);
		List result = dealerService.getDealerProducts(criteria);
	}

	public void testGetDealers() {
		Map criteria = new HashMap();
		criteria.put("organisation.client.clientId", 1);
		List result = dealerService.getDealers(criteria);
		System.out.println(result);
	}

	public void testLoadDealer() {
		Dealer dealer = new Dealer();
		dealer.setDealerId(1);
		try {
			//dealerService.load(dealer);
		} catch (Exception e) {

		}
		Assert.assertTrue(dealer != null);
	}

	public void testGetDealerVariables() {
		Map criteria = new HashMap();
		criteria.put("dealer.dealerId", 1);
		List result = dealerService.getDealerVariables(criteria);
		System.out.println(result);
	}

	
	protected void tearDown() throws Exception {
		dealerService = null;
	}
}
