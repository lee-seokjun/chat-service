var stompClient = null;

function setConnected(connected) {

    if (connected) {
        $("#conversation").show();
        $("#chatMessage").show();
        $("#chatSend").show();
        $("#disconnect").show();
    }
    else {
        $("#conversation").hide();
        $("#chatMessage").hide();
        $("#chatSend").hide();
        $("#disconnect").hide();
    }
//    $("#greetings").html("");
}



function connect() {
    var socket = new SockJS('/websocket/' +$('#chatId').val());
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        // stompClient.subscribe('/topic/greetings', function (greeting) {
        //     console.log(greeting+'  ok')
        //
        //     showGreeting(JSON.parse(greeting.body).content);
        // });
        stompClient.subscribe('/topic/chat', function (chat) {
            console.log(chat+'  ok')
            showChat(JSON.parse(chat.body));
        });
    });
}


function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
    $('#chatting').hide()
}

function sendName() {
    console.log('sendName')

    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#userName").val()}));
}

function sendChat() {
    console.log('sendChat')
    messageJson = {};
    messageJson.chatId = $('#chatId').val();
    messageJson.senderId = $('#userName').val();
    messageJson.message = $('#chatMessage').val();

    stompClient.send("/app/chat", {}, JSON.stringify(messageJson));
    $("input[name=chatMessage]").val("");
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}
function showChat(chat) {
    if(chat.chatId==$("#chatId").val()){


    var li = chat.senderId==$("#userName").val()?  $('<li>'+chat.message +'</li>').css("text-align","right") : $('<li>'+chat.senderName +' <br/></li>').append($('<span>'+chat.message +'</span>'))
    $("#greetings").append(li);

    }
}
function getMessage(){
    $.ajax({
        url: 'http://localhost:6060/messages/'+$("#chatId").val(),
        type: 'GET',
        headers:{
            contentType :'application/x-www-form-urlencoded;',
            userId:$('#userName').val(),
            token:$('#token').val()
        }
        ,success: function (res) {
            $("#greetings").empty()
            res.forEach(
                chat=>
                {
                    var li = chat.senderId==$("#userName").val()?  $('<li>'+chat.message +'</li>').css("text-align","right") : $('<li>'+chat.senderName +' <br/></li>').append($('<span>'+chat.message +'</span>'))
                    // var li = v.senderId=='test' ?  $('<li>'+v.message +'</li>').css("text-align","right") : $('<li>'+v.senderId +' <br/></li>').append($('<span>'+v.message +'</span>'))
                    $("#greetings").append(li);
                }
            )

        },
        error : function(err){
            console.log('error');

        }
    });
}
function postMessageCheck(event)
{
    console.log("SPECTRA porstMessage method ::: ",event);


    var vData;
    try
    {
        vData = JSON.parse(event.data);
        console.log(vData)
        if(vData.func =="connect")
        {
            console.log("chatID : ",vData.chatId)
            console.log("userId : ",vData.userId)
            postMethodStart(vData.chatId,vData.userId,vData.token)
        }else
        {
            disconnect()
        }



    }catch(e)
    {
        return;
    }

}
function postMethodStart(chatId,userId,token){

    $("#userName").val(userId)
    $("#chatId").val(chatId)
    $("#token").val(token)
    console.log($("#userName").val())
    goToRoom()

}
$(function () {

    window.addEventListener("message", postMessageCheck, false);

    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
    $( "#chatSend" ).click(function(){
            if($("input[name=chatMessage]").val()!="")
                sendChat();
            $("input[name=chatMessage]").val("");
        }

    );
    $("#startChat").click(function(){
        postMethodStart('chatId','userId')
    })
    $("#createRoom").click(function(){
        var json = {
            // "chatMemberIds" :["친구1","친구2"],
            "title" :$("#roomName").val()
        };
        console.log(json)

        $.ajax({
            url: 'http://localhost:6060/chat',
            type: 'POST',
            Accept :'*/*',
            headers:{
                contentType :'application/x-www-form-urlencoded;',
                userId:$('#userName').val(),
                token:$('#token').val()
            },
            data : JSON.stringify(json),
            success: function (res) {
                console.log(res)
                showChattingRoom();
            },
            error : function(err){
                console.log('error');

            }
        });
        $("#roomName").val("");
    });
    $("#chatId").on('change',function(){
        $("#greetings").empty()
    })

    $("#chatMessage").on("keydown",function(evt){
        if(evt.keyCode==13){	//enter키가 눌려진 경우
            if($("input[name=chatMessage]").val()!="")
                sendChat();
            $("input[name=chatMessage]").val("");//등록한 메세지 지움

        }
    })
})

var ws;


function getRoom(){
    $('#myroomList').empty();
    $.ajax({
        url: 'http://localhost:6060/chat',
        type: 'GET',
        headers:{
            contentType :'application/x-www-form-urlencoded;',
            userId:$('#userName').val(),
            token:$('#token').val()
        },
        success: function (res) {
            res.forEach(
                v =>
                {
                    $('#myroomList').append($('<li id="'+v.chatId+'">'  +v.title +' </li>').append($('<button >들어가기</button>').attr('chatId',v.chatId).attr('title',v.title).on('click',goToRoom)) )
                }
            )
        },
        error : function(err){
            console.log('error');
        }
    });
}



function showChattingRoom(){
    $.ajax({
        url: 'http://localhost:6060/chat',
        type: 'GET',
        headers:{
            contentType :'application/x-www-form-urlencoded;',
            userId:$('#userName').val(),
            token:$('#token').val()
        },
        success: function (res) {
            $('#myroomList').empty()
            res.forEach(
                v =>
                {
                    $('#myroomList').append($('<li id="'+v.chatId+'">'  +v.title +' </li>').append($('<button >들어가기</button>').attr('chatId',v.chatId).attr('title',v.title).on('click',goToRoom)) )
                }
            )
        },
        error : function(err){
            console.log('error');

        }
    });
}

function goToRoom(){
    if (stompClient !== null) {
        disconnect()
    }
    //$('#chatId').val($(this).attr('chatId'))
    $('#chatting').show();
    getMessage();
    connect()
}

function login(){
    console.log('login')

    $("#roomAdder").show()
    $(this).attr('disabled',true)
    getRoom()
}