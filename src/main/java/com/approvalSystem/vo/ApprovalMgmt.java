package com.approvalSystem.vo;

public class ApprovalMgmt {
    private Integer docKey;
    private Integer priority;
    private Integer approver;
    private String state;

    private String opinion;

    public Integer getDocKey() {
        return docKey;
    }

    public void setDocKey(Integer docKey) {
        this.docKey = docKey;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getApprover() {
        return approver;
    }

    public void setApprover(Integer approver) {
        this.approver = approver;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }
}
