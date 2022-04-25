package com.approvalSystem.vo.sc;

/**
 * Search Condition for document
 */
public class SCDocument {
    private String listType;    // [ INBOX, OUTBOX ]
    private Integer creUserKey;
    private Integer approver;

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public Integer getCreUserKey() {
        return creUserKey;
    }

    public void setCreUserKey(Integer creUserKey) {
        this.creUserKey = creUserKey;
    }

    public Integer getApprover() {
        return approver;
    }

    public void setApprover(Integer approver) {
        this.approver = approver;
    }
}
