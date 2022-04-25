package com.approvalSystem.controller;

import com.approvalSystem.service.ApprovalService;
import com.approvalSystem.vo.ApprovalMgmt;
import com.approvalSystem.vo.Document;
import com.approvalSystem.vo.DocumentType;
import com.approvalSystem.vo.User;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/approval")
public class ApprovalController {
    private final ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }


    /**
     * 로그인 조회
     * parameter : id, pw
     * return : result, msg, userInfo
     */
    @PostMapping(value = "/login")
    public Map<String, Object> login(@RequestBody String param){
        Map<String, Object> retData = new HashMap<>();
        JSONObject jsonParam = JSONObject.fromObject(param);
        boolean result = false;
        boolean missParamYn = false;
        String msg = "";

        String id = null;
        String pw = null;
        User userInfo;

        if(jsonParam.has("id")){
            id = jsonParam.getString("id");
        }else{
            missParamYn = true;
            msg = "ID - null";
        }
        if(jsonParam.has("pw")){
            pw = jsonParam.getString("pw");
        }else{
            missParamYn = true;
            msg = "PW - null";
        }

        if( !missParamYn ){
            userInfo = approvalService.loginCheck(id, pw);
            if(userInfo != null){
                result = true;
                retData.put("userInfo", userInfo);
            }
        }

        retData.put("result", result);
        retData.put("msg", msg);

        return retData;
    }


    /**
     * 문서 조회 - OUTBOX / INBOX
     * parameter : type, userKey
     * return : result, msg, documentList
     */
    @PostMapping(value = "/getDocumentList")
    public Map<String, Object> getDocumentList(@RequestBody String param){
        Map<String, Object> retData = new HashMap<>();
        JSONObject jsonParam = JSONObject.fromObject(param);
        boolean result = false;
        boolean missParamYn = false;
        String msg = "";

        Integer userKey = null;
        String type = null;

        if(jsonParam.has("userKey")){
            userKey = jsonParam.getInt("userKey");
        }else{
            missParamYn = true;
            msg = "userKey - null";
        }

        if(jsonParam.has("type")){
            type = jsonParam.getString("type");
        }else{
            missParamYn = true;
            msg = "document list type - null";
        }

        if( !missParamYn ){
            List<Document> documentList = null;
            if(type.equals("INBOX")){
                documentList = approvalService.getInboxList(userKey);
            }else if(type.equals("OUTBOX")){
                documentList = approvalService.getOutboxList(userKey);
            }

            result = true;
            retData.put("documentList", documentList);
        }

        retData.put("result", result);
        retData.put("msg", msg);

        return retData;
    }

    /**
     * 문서 조회 - ARCHIVE
     * parameter : userKey
     * return : result, msg, documentList
     */
    @PostMapping(value = "/getDocumentArchive")
    public Map<String, Object> getDocumentArchive(@RequestBody String param){
        Map<String, Object> retData = new HashMap<>();
        JSONObject jsonParam = JSONObject.fromObject(param);
        boolean result = false;
        boolean missParamYn = false;
        String msg = "";

        Integer userKey = null;

        if(jsonParam.has("userKey")){
            userKey = jsonParam.getInt("userKey");
        }else{
            missParamYn = true;
            msg = "userKey - null";
        }

        if( !missParamYn ){
            List<Document> documentList = approvalService.getDocumentArchive(userKey);

            result = true;
            retData.put("documentList", documentList);
        }

        retData.put("result", result);
        retData.put("msg", msg);

        return retData;
    }


    /**
     * 문서 타입 조회
     * return : docTypeList
     */
    @GetMapping(value = "/getDocTypeList")
    public Map<String, Object> getDocTypeList(){
        Map<String, Object> retData = new HashMap<>();

        List<DocumentType> docTypeList = approvalService.getDocTypeList();
        retData.put("docTypeList", docTypeList);

        return retData;
    }
    /**
     * 사용자 목록 조회
     * return : userList
     */
    @GetMapping(value = "/getAllUserList")
    public Map<String, Object> getAllUserList(){
        Map<String, Object> retData = new HashMap<>();

        List<User> userList = approvalService.getAllUserList();
        retData.put("userList", userList);

        return retData;
    }

    /**
     * 문서 생성
     * parameter :
     *  userKey, title, docTypeKey, contents
     *  approverList( [{approver, priority}] )
     */
    @PostMapping(value = "/saveDocument")
    public Map<String, Object> saveDocument(@RequestBody String param){
        Map<String, Object> retData = new HashMap<>();
        JSONObject jsonParam = JSONObject.fromObject(param);
        String msg = "";

        Document document = new Document();
        document.setCreUserKey(jsonParam.getInt("creUserKey"));
        document.setTitle(jsonParam.getString("title"));
        document.setDocTypeKey(jsonParam.getInt("docTypeKey"));
        document.setContents(jsonParam.getString("contents"));

        JSONArray approverList = jsonParam.getJSONArray("approverList");
        List<ApprovalMgmt> approvalMgmtList = new ArrayList<>();

        for (Object obj : approverList) {
            JSONObject reqObj = (JSONObject) obj;

            ApprovalMgmt tempObj = new ApprovalMgmt();
            tempObj.setPriority(reqObj.getInt("priority"));
            tempObj.setApprover(reqObj.getInt("approver"));

            approvalMgmtList.add(tempObj);
        }

        approvalService.saveDocument(document, approvalMgmtList);

        retData.put("msg", msg);

        return retData;
    }


    /**
     * 문서 승인 or 거절 - update
     * parameter : userKey, ApprovalMgmt
     * return : msg, documentList
     */
    @PostMapping(value = "/doConfirm")
    public Map<String, Object> doConfirm(@RequestBody String param){
        Map<String, Object> retData = new HashMap<>();
        JSONObject jsonParam = JSONObject.fromObject(param);
        String msg = "";

        ApprovalMgmt approvalMgmt = (ApprovalMgmt)JSONObject.toBean(jsonParam, ApprovalMgmt.class);
        approvalMgmt.setState("C");

        approvalService.doConfirm(approvalMgmt);

        // 화면에 문서리스트 갱신
        List<Document> documentList = approvalService.getInboxList(approvalMgmt.getApprover());

        retData.put("documentList", documentList);
        retData.put("msg", msg);

        return retData;
    }
    /**
     * 문서 승인 or 거절 - update
     * userKey, ApprovalMgmt
     * return : msg, documentList
     */
    @PostMapping(value = "/doReject")
    public Map<String, Object> doReject(@RequestBody String param){
        Map<String, Object> retData = new HashMap<>();
        JSONObject jsonParam = JSONObject.fromObject(param);
        String msg = "";

        ApprovalMgmt approvalMgmt = (ApprovalMgmt)JSONObject.toBean(jsonParam, ApprovalMgmt.class);
        approvalMgmt.setState("R");

        approvalService.doReject(approvalMgmt);

        // 화면에 문서리스트 갱신
        List<Document> documentList = approvalService.getInboxList(approvalMgmt.getApprover());

        retData.put("documentList", documentList);
        retData.put("msg", msg);

        return retData;
    }
}
