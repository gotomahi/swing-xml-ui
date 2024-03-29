package com.gui.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.autoinsure.model.bom.User;
import com.framework.bom.ServiceLocator;
import com.gui.BaseAction;
import com.service.bom.dealer.DealerService;
import com.service.bom.emp.EmpService;
import com.service.bom.org.OrgService;
import com.service.bom.user.UserService;

/**
 * 
 * @author mahendra
 * 
 * @date 23 Apr 2011
 */
public class UserCreateEditAction extends BaseAction {
	protected Vector organisations;
	protected Vector userTypes;
	protected Vector dealers = new Vector();
	protected Vector employees = new Vector();
	protected User user;

	public void init(Map data) throws Exception {
		user = (User)getSessionValue("CreateEditUser");
		
		OrgService orgService = (OrgService) ServiceLocator.getInstance().getBean("orgService");
		Map criteria = new HashMap(1);
		criteria.put("client.clientId", getClientId());
		organisations = new Vector(orgService.getOrganisations(criteria));

		UserService userService = (UserService) ServiceLocator.getInstance().getBean("userService");
		criteria = new HashMap(1);
		userTypes = new Vector(userService.getUserTypes(criteria));
	}

	@Override
	public void executeAction(String actionCommand) throws Exception {
		String retPage = "User";
		if ("organisation".equals(actionCommand)) {
			User user = (User) this.getSelectedObj().get(0);
			dealers = getDealers(user.getOrganisation().getOrgId());
		} else if ("dealer".equals(actionCommand)) {
			User user = (User) this.getSelectedObj().get(0);
			employees = getEmployees(user.getOrganisation().getOrgId());
		}		
	}

	private Vector getDealers(long orgId) {
		DealerService dealerService = (DealerService) ServiceLocator.getInstance().getBean("dealerService");
		Map criteria = new HashMap(1);
		criteria.put("orgId", orgId);
		Vector dealers = new Vector(dealerService.getDealers(criteria));
		return dealers;
	}

	private Vector getEmployees(long dealerId) {
		EmpService empService = (EmpService) ServiceLocator.getInstance().getBean("empService");
		Map criteria = new HashMap(1);
		criteria.put("dealerId", dealerId);
		Vector employees = new Vector(empService.getEmployees(criteria));
		return employees;
	}

	public Vector getOrganisations() {
		return organisations;
	}

	public void setOrganisations(Vector organisations) {
		this.organisations = organisations;
	}

	public Vector getUserTypes() {
		return userTypes;
	}

	public void setUserTypes(Vector userTypes) {
		this.userTypes = userTypes;
	}

	public Vector getDealers() {
		return dealers;
	}

	public void setDealers(Vector dealers) {
		this.dealers = dealers;
	}

	public Vector getEmployees() {
		return employees;
	}

	public void setEmployees(Vector employees) {
		this.employees = employees;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
