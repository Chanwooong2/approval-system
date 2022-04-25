<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="shortcut icon" href="#">
    <title>문서 결재 시스템</title>
    <script type="text/javascript" src="/resources/jQuery/jquery-3.6.0.min.js"></script>
</head>

<style>
    th, td {
        padding: 5px 15px;
    }
    input {
        width: 100px;
    }

</style>

<body>

    <h3>로그인</h3>
    <div style="padding: 20px; width: 500px; border:1px solid #333;">
        <label for="id"><B>ID : </B></label><input type="text" id="id">
        <label for="pw"><B>PW : </B></label><input type="password" id="pw">
        <button onclick="doLogin();">로그인</button>
    </div>

    <h3>현재 로그인 정보</h3>
    <div style="padding: 20px; width: 500px; border:1px solid #333;">
        <div><B>ID : </B><span id="dispUserId">로그인이 필요합니다.</span></div>
        <div><B>Name : </B><span id="dispUserName">로그인이 필요합니다.</span></div>
    </div>

    <h3>문서 생성</h3>
    <div class="new-doc" style="padding: 20px; width: 500px; border:1px solid #333;">
        <div><B>제목 : </B><input type="text" name="title"></div>
        <div><B>분류 : </B>
            <select name="" id="typeSelBox">
<%--                <option value="1">품의</option>--%>
<%--                <option value="2">비용</option>--%>
<%--                <option value="3">휴가</option>--%>
            </select>
        </div>
        <div><B>내용 : </B><input type="text" name="contents"></div>
        <div><B>결재자 : </B>
            <span id="approverList">
<%--                <select name="" class="approverSel">--%>
<%--                    <option value="1">문찬웅</option>--%>
<%--                    <option value="2">관리자01</option>--%>
<%--                </select>--%>
            </span>
            <button onclick="addApproverSelBox();">결재자 추가</button>
            <button onclick="initApproverSelBox()">결재자 초기화</button>
        </div>
        <button style="margin-top:30px; margin-left: 430px;" onclick="saveDocument();"><B>문서생성</B></button>
    </div>

    <h3>문서 조회</h3>
    <div style="padding: 20px; border:1px solid #333; min-width: 1080px;">
        <div><B>문서 목록 : </B>
            <select name="" id="docListSelBox">
                <option value="OUTBOX">OUTBOX</option>
                <option value="INBOX">INBOX</option>
                <option value="ARCHIVE">ARCHIVE</option>
            </select>
            <button onclick="onclickSearch();">조회</button>
        </div>
        <table style="margin-top:10px; padding: 20px; border:1px solid #333;">
            <thead>
            <tr>
                <th>분류</th>
                <th>문서제목</th>
                <th>내용</th>
                <th>작성자</th>
                <th>문서 생성 날짜</th>
                <th>결재 상태</th>
                <th>결재 날짜</th>
                <th></th>
            </tr>
            </thead>
            <tbody id="documentListBody">
            </tbody>
        </table>
    </div>
</body>

<script>
    let g_userKey;
    let g_login_yn = false;

    let g_loginUserInfo;
    let g_userList;
    let g_docTypeList;
    $(document).ready(function() {
        initData();
    });
    function initData(){
        g_userList = ${userList};
        g_docTypeList = ${docTypeList};

        let optionHtml = "";
        for(let i=0; i<g_docTypeList.length; i++){
            let typeInfo = g_docTypeList[i];
            optionHtml += "<option value=\"" + typeInfo.typeKey + "\">" + typeInfo.typeName + "</option>";
        }
        $('#typeSelBox').empty();
        $('#typeSelBox').append(optionHtml);

        initApproverSelBox();
    }
    function initView(){
        // 문서 생성 form 초기화
        $(".new-doc input[name=title]").val("");
        $(".new-doc #typeSelBox").val("1");
        $(".new-doc input[name=contents]").val("");
        initApproverSelBox();

        // 문서 조회 form 초기화
        $('#docListSelBox').val("OUTBOX");
        $('#documentListBody').empty();
    }

    function doLogin() {
        console.log("doLogin start");
        let url = "/approval/login";
        let param = {};

        param.id = $('#id').val();
        param.pw = $('#pw').val();

        if(!param.id){
            return alert("Plz input ID");
        }
        if(!param.pw){
            return alert("Plz input PW");
        }

        $.ajax({
            url : url,
            type : 'POST',
            dataType : 'json',
            data : JSON.stringify(param),
            contentType : 'application/json',
            success : function(data) {
                if (data.result) {
                    g_login_yn = true;
                    g_loginUserInfo = data.userInfo;
                    g_userKey = g_loginUserInfo.userKey;

                    displayUserInfo();
                    initView();
                    onclickSearch();
                }
            },
            error : function(data) {
                console.log("doLogin ajax Error")
            }
        });
    }
    function displayUserInfo(){
        $('#dispUserId').text(g_loginUserInfo.id);
        $('#dispUserName').text(g_loginUserInfo.userName);
    }

    function saveDocument() {
        console.log("saveDocument start");
        if(!g_login_yn) return alert("로그인 정보가 없습니다.");

        let url = "/approval/saveDocument";
        let param = {};

        param.creUserKey = g_userKey;
        param.title = $(".new-doc input[name=title]").val();
        param.docTypeKey = parseInt($(".new-doc #typeSelBox").val());
        param.contents = $(".new-doc input[name=contents]").val();


        // 같은 결재자가 없는지 확인
        let approverList = new Array();
        for(let i=0; i< $('.approverSel').length; i++){
            let ele = $('.approverSel')[i];
            approverList.push(parseInt($(ele).val()));
        }

        let checkSet = new Set(approverList);
        if(checkSet.size != approverList.length){
            return alert("같은 결재자가 2번이상 포함될 수 없습니다.");
        }

        param.approverList = new Array();
        for(let i=0; i<approverList.length; i++){
            let temp = new Object();
            temp.priority = i;
            temp.approver = approverList[i];
            param.approverList.push(temp);
        }

        $.ajax({
            url : url,
            type : 'POST',
            dataType : 'json',
            data : JSON.stringify(param),
            contentType : 'application/json',
            success : function(data) {
                initView();
                onclickSearch();
            },
            error : function(data) {
                console.log("ajax Error")
            }
        });
    }
    function addApproverSelBox(){
        let selBoxHtml = "<select name=\"\" class=\"approverSel\">";
        for(let i=0; i<g_userList.length; i++) {
            let userInfo = g_userList[i];
            selBoxHtml += "<option value=\"" + userInfo.userKey + "\">" + userInfo.userName + "</option>";
        }
        selBoxHtml += "</select>";

        $("#approverList").append(selBoxHtml);
    }
    function initApproverSelBox(){
        $("#approverList").empty();
        addApproverSelBox();
    }


    function onclickSearch(){
        if(!g_login_yn) return alert("로그인 정보가 없습니다.");

        getDocumentList($('#docListSelBox').val());
    }
    function getDocumentList(type) {
        let url;
        let param = {};

        param.userKey = g_userKey;
        if(type == "ARCHIVE"){
            url = "/approval/getDocumentArchive";
        }else{
            url = "/approval/getDocumentList";
            param.type = type;
        }

        $.ajax({
            url : url,
            type : 'POST',
            dataType : 'json',
            data : JSON.stringify(param),
            contentType : 'application/json',
            success : function(data) {
                if(data.result){
                    displayDocumentList(data.documentList);
                }
            },
            error : function(data) {
                console.log("ajax Error")
            }
        });
    }
    function displayDocumentList(documentList){
        let listType = $('#docListSelBox').val();
        let listHtml = "";
        for(let i=0; i<documentList.length; i++){
            let document = documentList[i];

            let state;
            if(document.state == "I"){
                state = "<td>진행중</td>"
            }else if(document.state == "C"){
                state = "<td style=\"color: #13b900;\">승인완료</td>"
            }else if(document.state == "R"){
                state = "<td style=\"color: #f00;\">거절</td>"
            }

            let buttonHtml = "";
            if(listType === "INBOX"){
                buttonHtml += "<input type=\"text\" class=\"opinion\" placeholder='의견 입력'>";
                buttonHtml += "<button onclick='doConfirm("+ document.docKey +")'>승인</button>";
                buttonHtml += "<button onclick='doReject("+ document.docKey +")'>거절</button>";
            }

            listHtml += "<tr data-docKey=\"" + document.docKey + "\">";
            listHtml += "	<td>" + document.docTypeName + "</td>";
            listHtml += "	<td>" + document.title + "</td>";
            listHtml += "	<td>" + document.contents + "</td>";
            listHtml += "	<td>" + document.creUserName + "</td>";
            listHtml += "	<td>" + document.creDate + "</td>";
            listHtml += state;
            listHtml += "	<td>" + document.approvalDate + "</td>";
            listHtml += "	<td>" + buttonHtml + "</td>";
            listHtml += "</tr>";
        }
        if(documentList.length == 0){
            listHtml = "<h3>데이터가 없습니다.</h3>";
        }

        $('#documentListBody').empty();
        $('#documentListBody').append(listHtml);
    }

    function doConfirm(docKey){
        console.log("doConfirm start");
        doApproval("/approval/doConfirm", docKey);
    }
    function doReject(docKey){
        console.log("doReject start");
        doApproval("/approval/doReject", docKey);
    }
    function doApproval(url, docKey) {
        let param = {};

        param.approver = g_userKey;
        param.docKey = docKey;
        param.opinion = $("tr[data-dockey="+ docKey +"] input").val();

        $.ajax({
            url : url,
            type : 'POST',
            dataType : 'json',
            data : JSON.stringify(param),
            contentType : 'application/json',
            success : function(data) {
                displayDocumentList(data.documentList);
            },
            error : function(data) {
                console.log("ajax Error")
            }
        });
    }
</script>

</html>
