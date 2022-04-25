package com.approvalSystem.vo;

public class Document {
    private Integer docKey;
    private Integer docTypeKey;
    private String docTypeName;
    private String title;
    private String contents;
    private Integer creUserKey;
    private String creUserName;
    private String creDate;
    private String state;
    private String approvalDate;

    public Integer getDocKey() {
        return docKey;
    }

    public void setDocKey(Integer docKey) {
        this.docKey = docKey;
    }

    public Integer getDocTypeKey() {
        return docTypeKey;
    }

    public void setDocTypeKey(Integer docTypeKey) {
        this.docTypeKey = docTypeKey;
    }

    public String getDocTypeName() {
        return docTypeName;
    }

    public void setDocTypeName(String docTypeName) {
        this.docTypeName = docTypeName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Integer getCreUserKey() {
        return creUserKey;
    }

    public void setCreUserKey(Integer creUserKey) {
        this.creUserKey = creUserKey;
    }

    public String getCreUserName() {
        return creUserName;
    }

    public void setCreUserName(String creUserName) {
        this.creUserName = creUserName;
    }

    public String getCreDate() {
        return creDate;
    }

    public void setCreDate(String creDate) {
        this.creDate = creDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(String approvalDate) {
        this.approvalDate = approvalDate;
    }
}
