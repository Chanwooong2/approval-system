<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="shortcut icon" href="#">
    <title>Service Check</title>
    <script type="text/javascript" src="/resources/jQuery/jquery-3.6.0.min.js"></script>
</head>

<body style="display: flex;">
    <div>
        <h5>로그인 조회</h5>
        <Button type="button" onclick="doLogin();">doLogin</Button>
        <br>
        <h5>문서조회 - OUTBOX</h5>
        <Button type="button" onclick="getDocumentListOUTBOX();">getDocumentListOUTBOX</Button>
        <br>
        <h5>문서조회 - INBOX</h5>
        <Button type="button" onclick="getDocumentListINBOX();">getDocumentListINBOX</Button>
        <br>
        <h5>문서조회 - ARCHIVE</h5>
        <Button type="button" onclick="getDocumentArchive();">getDocumentArchive</Button>
        <br>
        <h5>문서 타입 조회</h5>
        <Button type="button" onclick="getDocTypeList();">getDocTypeList</Button>
        <br>
        <h5>사용자 목록 조회</h5>
        <Button type="button" onclick="getAllUserList();">getAllUserList</Button>
        <br>
        <h5>문서 생성</h5>
        <Button type="button" onclick="saveDocument();">saveDocument</Button>
        <br>
        <h5>문서 승인</h5>
        <Button type="button" onclick="doConfirm();">doConfirm</Button>
        <br>
        <h5>문서 거절</h5>
        <Button type="button" onclick="doReject();">doReject</Button>
        <br>
    </div>
    <div id="DebugScreen" style="margin-left:20px;padding-left:20px;border-left:solid 1px #dcdcdc;"></div>
</body>

<script>
    const g_userKey = 2;
    function doLogin() {
        console.log("doLogin start");
        let url = "/approval/login";
        let param = {};

        param.id = "mcw0219";
        param.pw = "admin";

        $.ajax({
            url : url,
            type : 'POST',
            dataType : 'json',
            data : JSON.stringify(param),
            contentType : 'application/json',
            success : function(data) {
                $("#DebugScreen").empty().append(JSON.stringify(data));
            },
            error : function(data) {
                console.log("ajax Error")
            }
        });
    }
    function getDocumentListOUTBOX() {
        console.log("getDocumentListOUTBOX start");
        getDocumentList("OUTBOX");
    }
    function getDocumentListINBOX() {
        console.log("getDocumentListINBOX start");
        getDocumentList("INBOX");
    }
    function getDocumentList(type) {
        let url = "/approval/getDocumentList";
        let param = {};

        param.userKey = g_userKey;
        param.type = type;

        $.ajax({
            url : url,
            type : 'POST',
            dataType : 'json',
            data : JSON.stringify(param),
            contentType : 'application/json',
            success : function(data) {
                if(data.result){
                    $("#DebugScreen").empty().append(JSON.stringify(data));
                }
            },
            error : function(data) {
                console.log("ajax Error")
            }
        });
    }


    function getDocumentArchive() {
        console.log("getDocumentArchive start");
        let url = "/approval/getDocumentArchive";
        let param = {};

        param.userKey = g_userKey;

        $.ajax({
            url : url,
            type : 'POST',
            dataType : 'json',
            data : JSON.stringify(param),
            contentType : 'application/json',
            success : function(data) {
                if(data.result){
                    $("#DebugScreen").empty().append(JSON.stringify(data));
                }
            },
            error : function(data) {
                console.log("ajax Error")
            }
        });
    }
    function getDocTypeList() {
        console.log("getDocTypeList start");
        let url = "/approval/getDocTypeList";

        $.ajax({
            url : url,
            type : 'GET',
            dataType : 'json',
            contentType : 'application/json',
            success : function(data) {
                $("#DebugScreen").empty().append(JSON.stringify(data));
            },
            error : function(data) {
                console.log("ajax Error")
            }
        });
    }
    function getAllUserList() {
        console.log("getAllUserList start");
        let url = "/approval/getAllUserList";

        $.ajax({
            url : url,
            type : 'GET',
            dataType : 'json',
            contentType : 'application/json',
            success : function(data) {
                $("#DebugScreen").empty().append(JSON.stringify(data));
            },
            error : function(data) {
                console.log("ajax Error")
            }
        });
    }

    function saveDocument() {
        console.log("saveDocument start");
        let url = "/approval/saveDocument";
        let param = {};

        param.creUserKey = g_userKey;
        param.title = "새로운제목";
        param.docTypeKey = 3;
        param.contents = "내용 이번엔 있습니다.";

        param.approverList = new Array();

        let temp = new Object();
        temp.priority = 0;
        temp.approver = 3;
        param.approverList.push(temp);

        temp = new Object();
        temp.priority = 1;
        temp.approver = 2;
        param.approverList.push(temp);

        temp = new Object();
        temp.priority = 2;
        temp.approver = 1;
        param.approverList.push(temp);

        $.ajax({
            url : url,
            type : 'POST',
            dataType : 'json',
            data : JSON.stringify(param),
            contentType : 'application/json',
            success : function(data) {
                $("#DebugScreen").empty().append(JSON.stringify(data));
            },
            error : function(data) {
                console.log("ajax Error")
            }
        });
    }
    function doConfirm() {
        console.log("doConfirm start");
        let url = "/approval/doConfirm";
        let param = {};

        param.docKey = 11;
        param.opinion = "승인 의견입니다";
        param.approver = 4;

        $.ajax({
            url : url,
            type : 'POST',
            dataType : 'json',
            data : JSON.stringify(param),
            contentType : 'application/json',
            success : function(data) {
                $("#DebugScreen").empty().append(JSON.stringify(data));
            },
            error : function(data) {
                console.log("ajax Error")
            }
        });
    }
    function doReject() {
        console.log("doReject start");
        let url = "/approval/doReject";
        let param = {};

        param.docKey = 3;
        param.opinion = "거절 의견입니다";
        param.approver = 2;

        $.ajax({
            url : url,
            type : 'POST',
            dataType : 'json',
            data : JSON.stringify(param),
            contentType : 'application/json',
            success : function(data) {
                $("#DebugScreen").empty().append(JSON.stringify(data));
            },
            error : function(data) {
                console.log("ajax Error")
            }
        });
    }
</script>

</html>
