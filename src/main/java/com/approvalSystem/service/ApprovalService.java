package com.approvalSystem.service;

import com.approvalSystem.dao.ApprovalDAO;
import com.approvalSystem.vo.ApprovalMgmt;
import com.approvalSystem.vo.Document;
import com.approvalSystem.vo.DocumentType;
import com.approvalSystem.vo.User;
import com.approvalSystem.vo.sc.SCDocument;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApprovalService {
	private final ApprovalDAO approvalDAO;

	public ApprovalService(ApprovalDAO approvalDAO) {
		this.approvalDAO = approvalDAO;
	}

	public User loginCheck(String id, String pw){
		User resUser = null;
		List<User> userList = this.getUserList(id, pw);
		if(userList.size() == 1){
			resUser = userList.get(0);
		}
		return resUser;
	}

	public List<User> getAllUserList(){
		return approvalDAO.getUserList(null, null);
	}

	public List<Document> getInboxList(Integer approver){
		SCDocument scDocument = new SCDocument();
		scDocument.setListType("INBOX");
		scDocument.setApprover(approver);

		return this.getDocumentList(scDocument);
	}

	public List<Document> getOutboxList(Integer creUserKey){
		SCDocument scDocument = new SCDocument();
		scDocument.setListType("OUTBOX");
		scDocument.setCreUserKey(creUserKey);

		return this.getDocumentList(scDocument);
	}

	public void saveDocument(Document document, List<ApprovalMgmt> approvalMgmtList){
		Integer docKey = approvalDAO.saveDocument(document);
		for(ApprovalMgmt temp : approvalMgmtList){
			temp.setDocKey(docKey);
		}
		approvalDAO.saveApprovalMgmtList(approvalMgmtList);
		approvalDAO.saveMappingDocApproval(docKey);
	}

	public void doConfirm(ApprovalMgmt approvalMgmt){
		Integer docKey = approvalMgmt.getDocKey();
		Boolean deleteYn = null;

		// 1. update approval_management tbl
		this.updateApprovalMgmt(approvalMgmt);

		// 2. 해당 문서에 더 결재할 사람이 남아있는지 확인
		if( !remainApproverYn(docKey) ){
			// 3. if) 더이상 결재할 사람이 없다면 update documents tbl
			Document document = new Document();
			document.setDocKey(approvalMgmt.getDocKey());
			document.setState("C");

			this.updateDocument(document);

			deleteYn = true;
		}

		// 4. update m_doc_approval tbl (priority_order ++)
		approvalDAO.updateMappingDocApproval(docKey, deleteYn);
	}
	public boolean remainApproverYn(Integer docKey){
		boolean result = false;
		Integer cnt = approvalDAO.getRemainApproverCnt(docKey);
		if(cnt > 0){
			result = true;
		}
		return result;
	}

	public void doReject(ApprovalMgmt approvalMgmt){
		// 1. update approval_management tbl
		this.updateApprovalMgmt(approvalMgmt);

		// 2. update m_doc_approval tbl - 결재 프로세스 종료
		approvalDAO.updateMappingDocApproval(approvalMgmt.getDocKey(), true);

		// 3. update documents tbl
		Document document = new Document();
		document.setDocKey(approvalMgmt.getDocKey());
		document.setState("R");

		this.updateDocument(document);
	}



	// Default Method
	public List<User> getUserList(String id, String pw){
		return approvalDAO.getUserList(id, pw);
	}
	public List<Document> getDocumentList(SCDocument scDocument){
		return approvalDAO.getDocumentList(scDocument);
	}
	public List<DocumentType> getDocTypeList(){
		return approvalDAO.getDocTypeList();
	}
	public List<Document> getDocumentArchive(Integer userKey){
		return approvalDAO.getDocumentArchive(userKey);
	}
	public void updateApprovalMgmt(ApprovalMgmt approvalMgmt){
		approvalDAO.updateApprovalMgmt(approvalMgmt);
	}
	public void updateDocument(Document document){
		approvalDAO.updateDocument(document);
	}
}
