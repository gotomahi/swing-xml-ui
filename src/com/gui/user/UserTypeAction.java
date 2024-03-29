package com.gui.user;

import java.util.Vector;

import com.autoinsure.model.bom.Client;
import com.framework.bom.ServiceLocator;
import com.gui.BaseAction;
import com.service.bom.user.UserService;

public class UserTypeAction extends BaseAction {
	private Vector userTypeList = new Vector();

	@Override
	protected void executeAction(String actionCommand) throws Exception {
		String retPage = "UserType";
		if ("Search".equals(actionCommand)) {
			UserService userService = (UserService) ServiceLocator.getInstance().getBean("userService");
			userTypeList = new Vector(userService.getUserTypes(input));
		} else if ("New".equals(actionCommand)) {
			userTypeList.add(new Client());
		} else if ("Delete".equals(actionCommand)) {
		}		
	}

	public Vector getUserTypeList() {
		return userTypeList;
	}

	public void setUserTypeList(Vector userTypeList) {
		this.userTypeList = userTypeList;
	}
}
