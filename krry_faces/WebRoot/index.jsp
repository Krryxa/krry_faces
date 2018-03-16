<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%

	Object ermsg = request.getSession().getAttribute("error");
	StringBuffer strBuff = new StringBuffer();
	if(ermsg != null){
		//清除作用域
		request.getSession().removeAttribute("error");
		strBuff.append("loading('"+ermsg+"',4)");
	}
%>

<!doctype html>
<html>
    <head>
	   <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
	   <meta name="keywords" content="关键词,关键词">
	   <meta name="description" content="">
	   <link rel="stylesheet" type="text/css" href="css/sg.css" />
	   <link rel="stylesheet" type="text/css" href="css/sg/css/animate.css" />
	   <link rel="stylesheet" type="text/css" href="css/sg/css/sg.css" />
	   
	   <title> Java开发人脸特征识别系统  --krry</title>
	   <style type="text/css">
	       *{padding:0px;margin:0px;}
	       body{background:url("images/5.jpg");background-size:cover;font-size:18px;font-family:"微软雅黑";color:#666;}
	       img{border:none;}
	       h1{font-size:24px;}
	       .title{margin-top: 30px;font-size:48px;font-family:"幼圆";color: #ff7272;text-shadow: 1px 1px 3px #ff3737;text-align:center;line-height:45px;}
		   .box{margin:100px auto;text-align:center;}
	       .box .b_btn{width:100px;height:40px;background:#fff;transition:.6s;font-size:16px;font-family:"微软雅黑";display:block;margin:54px auto;text-align:center;
	                   cursor:pointer;line-height:40px;text-decoration:none;color:#F60;border:1px;border-radius:12px}
	   	   .box .b_btn:hover{background:red;color:#fff;transition:.6s;}
	   	   .busbu{top:0;background:rgba(0, 0, 0, 0.2);display:none;width:100%;height:100%;position:absolute;z-index:1000;}
	   </style>
	</head>

<body>
   <!--action  提交的路径  method:post(打包传输) get(明文传输) enctype(采用二进制)-->
   <form action="show" method="post" enctype="multipart/form-data">
   <div class="box">
       <div class="title">人脸特征识别系统 --krry</div>
	   <input type="file" id="fileChoice" accept="image/jpeg,image/png" style="display:none" name="files"/>
	   <a class="b_btn" href="javascript:void(0);" onclick="choice()">选择照片</a>
       <input type="button" value="提交解析" class="b_btn" id="but"/>
       <input type="submit" class="sub_btn" style="display:none;"/> 
   </div>
   </form>
   <div class="busbu"></div>
 	<script src="js/jquery-1.11.3.min.js"></script>
	<script src="js/sg.js"></script>
	<script src="js/sgutil.js"></script>
	<script type="text/javascript" src="css/sg/tz_util.js"></script>
	<script type="text/javascript" src="css/sg/sg.js"></script>
	<script type="text/javascript">
	<%=strBuff.toString() %>
    //选择照片
	function choice(){
	   //js 弱类型  快速定位
	   var choose = document.getElementById("fileChoice");
	   choose.click();
	}
    $("#but").click(function(){
    	if($("#fileChoice").val()){//如果选择了照片
    		$(".sub_btn").click();
    		$(".busbu").show();
    		loading("正在解析",900);//再展示
    	}else{
    		$.tmDialog.alert({open:"top",content:"请先选择一张照片",title:"Krry温馨提醒"});
    	}
    });
      
</script>
</body>

</html>