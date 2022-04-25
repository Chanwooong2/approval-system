package com.approvalSystem.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.approvalSystem.vo.ApprovalMgmt;
import com.approvalSystem.vo.Document;
import com.approvalSystem.vo.DocumentType;
import com.approvalSystem.vo.User;
import com.approvalSystem.vo.sc.SCDocument;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class ApprovalDAOImpl implements ApprovalDAO {
	@Resource(name="MainSession")
	private final SqlSession session;
	private final String mapper = "mapper.postgresql.mapper.";

	public ApprovalDAOImpl(SqlSession session) {
		this.session = session;
	}

	@Override
	public List<User> getUserList(String id, String pw) {
		Map<String, Object> param = new HashMap<>();
		param.put("id", id);
		param.put("pw", pw);

		return session.selectList(mapper+"getUserList", param);
	}

	@Override
	public List<DocumentType> getDocTypeList() {
		return session.selectList(mapper+"getDocTypeList");
	}

	@Override
	public List<Document> getDocumentList(SCDocument scDocument) {
		return session.selectList(mapper+"getDocumentList", scDocument);
	}

	@Override
	public List<Document> getDocumentArchive(Integer userKey) {
		Map<String, Object> param = new HashMap<>();
		param.put("userKey", userKey);

		return session.selectList(mapper+"getDocumentArchive", param);
	}

	@Override
	public Integer getRemainApproverCnt(Integer docKey) {
		Map<String, Object> param = new HashMap<>();
		param.put("docKey", docKey);

		return session.selectOne(mapper+"getRemainApproverCnt", param);
	}

	@Override
	public Integer saveDocument(Document document) {
		session.insert(mapper+"saveDocument", document);
		return document.getDocKey();
	}

	@Override
	public void saveApprovalMgmtList(List<ApprovalMgmt> approvalMgmtList) {
		Map<String, Object> param = new HashMap<>();
		param.put("approvalMgmtList", approvalMgmtList);

		session.insert(mapper+"saveApprovalMgmtList", param);
	}

	@Override
	public void saveMappingDocApproval(Integer docKey) {
		Map<String, Object> param = new HashMap<>();
		param.put("docKey", docKey);

		session.insert(mapper+"saveMappingDocApproval", param);
	}

	@Override
	public void updateMappingDocApproval(Integer docKey, Boolean deleteYn) {
		Map<String, Object> param = new HashMap<>();
		param.put("docKey", docKey);
		param.put("deleteYn", deleteYn);

		session.update(mapper+"updateMappingDocApproval", param);
	}

	@Override
	public void updateDocument(Document document) {
		session.insert(mapper+"updateDocument", document);
	}

	@Override
	public void updateApprovalMgmt(ApprovalMgmt approvalMgmt) {
		session.insert(mapper+"updateApprovalMgmt", approvalMgmt);
	}
}
