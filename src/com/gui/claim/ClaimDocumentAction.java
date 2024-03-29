package com.gui.claim;

import java.util.Date;
import java.util.Map;

import com.ResourceLoader;
import com.autoinsure.model.bom.Claim;
import com.autoinsure.model.bom.ClaimDocument;

/**
 * 
 * @author mahendra
 * 
 * @date 28 Jan 2012
 */
public class ClaimDocumentAction extends ClaimAction {
	private String filePath;
	private String fileName;
	private String description;

	@Override
	public void init(Map data) throws Exception {
		filePath = null;
		fileName = null;
		description = null;
	}

	@Override
	protected void executeAction(String actionCommand) throws Exception {
		Claim claim = (Claim) treeMap.get("claim");
		if ("Add".equals(actionCommand)) {
			ClaimDocument claimDocument = new ClaimDocument();
			claimDocument.setDescription(description);
			claimDocument.setUploadedDate(new Date());
			claimDocument.setFileName(fileName);
			claimDocument.setFileData(ResourceLoader.getInstance().getFileData(filePath));
			claimDocument.setClaim(claim);
			claim.getClaimDocuments().add(claimDocument);
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
