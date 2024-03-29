package com.gui.policy;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.ResourceLoader;
import com.autoinsure.model.bom.PolicyDocument;
import com.autoinsure.service.policy.PolicyService;
import com.framework.bom.ServiceLocator;

/**
 * 
 * @author mahendra
 * 
 * @date 28 Jan 2012
 */
public class PolicyDocumentAction extends PlanAction {
	private String filePath;
	private String fileName;
	private String description;

	@Override
	public void init(Map data) throws Exception {
		Map criteria = new HashMap();
		PolicyService policyService = (PolicyService) ServiceLocator.getInstance().getBean("policyService");
	}

	@Override
	protected void executeAction(String actionCommand) throws Exception {
		Map policyMap = (Map) treeMap.get("policyMap");
		if ("Add".equals(actionCommand)) {
			PolicyDocument policyDocument = new PolicyDocument();
			policyDocument.setDescription(description);
			policyDocument.setUploadedDate(new Date());
			policyDocument.setFileData(ResourceLoader.getInstance().getFileData(filePath));
			Vector documentsList = (Vector) policyMap.get("documents");
			if (documentsList == null) {
				documentsList = new Vector();
				policyMap.put("documents", documentsList);
			}
			documentsList.add(policyDocument);
		}
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
