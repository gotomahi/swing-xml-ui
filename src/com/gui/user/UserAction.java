package com.gui.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.autoinsure.model.bom.Organisation;
import com.autoinsure.model.bom.User;
import com.framework.bom.ServiceLocator;
import com.gui.BaseAction;
import com.service.bom.emp.EmpService;
import com.service.bom.org.OrgService;
import com.service.bom.user.UserService;

/**
 * 
 * @author mahendra
 * 
 * @date 23 Apr 2011
 */
public class UserAction extends BaseAction {
	protected Vector userList = new Vector();
	protected Vector organisations;

	public void init(Map data) throws Exception {
		OrgService orgService = (OrgService) ServiceLocator.getInstance().getBean("orgService");
		Map criteria = new HashMap(1);
		criteria.put("client.clientId", getClientId());
		organisations = new Vector(orgService.getOrganisations(criteria));
	}

	@Override
	public void executeAction(String actionCommand) throws Exception {
		String retPage = "User";
		if ("Search".equals(actionCommand)) {
			UserService userService = (UserService) ServiceLocator.getInstance().getBean("userService");
			userList = new Vector(userService.getUsers(input));
			for (Object obj : userList) {
				User user = (User) obj;
				Vector emps = getEmployees(user.getEmployee().getDealer().getOrganisation().getOrgId());
				user.getDynamicData().put("employees", emps);
			}
		} else if ("New".equals(actionCommand)) {
			User user = new User();
			userList.add(user);
		} else if ("Delete".equals(actionCommand)) {
			Vector selObjs = getSelectedObj();
		} else if ("organisation".equals(actionCommand)) {
			User user = (User) getSelectedObj().get(0);
			user.getDynamicData().put("employees", getEmployees(user.getEmployee().getDealer().getOrganisation().getOrgId()));
		}		
	}

	protected void processItemChange(Object[] selectedObjs) throws Exception {
		Vector selObjs = getSelectedObj();
		if (selectedObjs != null && selectedObjs.length > 0 && selectedObjs[0] instanceof Organisation) {
			User user = (User) selObjs.get(0);
			user.getDynamicData().put("employees", getEmployees(((Organisation) selectedObjs[0]).getOrgId()));
		}
	}

	private Vector getEmployees(long orgId) {
		EmpService empService = (EmpService) ServiceLocator.getInstance().getBean("empService");
		Map criteria = new HashMap(1);
		criteria.put("dealer.organisation.orgId", orgId);
		Vector employees = new Vector(empService.getEmployees(criteria));
		return employees;
	}

	public Vector getUserList() {
		return userList;
	}

	public void setUserList(Vector userList) {
		this.userList = userList;
	}

	public Vector getOrganisations() {
		return organisations;
	}

	public void setOrganisations(Vector organisations) {
		this.organisations = organisations;
	}
}
