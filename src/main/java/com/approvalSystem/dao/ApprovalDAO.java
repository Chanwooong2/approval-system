package com.approvalSystem.dao;

import com.approvalSystem.vo.ApprovalMgmt;
import com.approvalSystem.vo.Document;
import com.approvalSystem.vo.DocumentType;
import com.approvalSystem.vo.User;
import com.approvalSystem.vo.sc.SCDocument;

import java.util.List;

public interface ApprovalDAO {
	List<User> getUserList(String id, String pw);
	List<DocumentType> getDocTypeList();
	List<Document> getDocumentList(SCDocument scDocument);
	List<Document> getDocumentArchive(Integer userKey);

	Integer saveDocument(Document document);
	void saveApprovalMgmtList(List<ApprovalMgmt> approvalMgmtList);

	void saveMappingDocApproval(Integer docKey);
	void updateMappingDocApproval(Integer docKey, Boolean deleteYn);

	void updateDocument(Document document);
	void updateApprovalMgmt(ApprovalMgmt approvalMgmt);

	Integer getRemainApproverCnt(Integer docKey);
}
