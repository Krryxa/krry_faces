<%@page import="net.sf.json.JSONObject"%>
<%@page import="net.sf.json.JSONArray"%>
<%@ page language="java" import="java.io.File" pageEncoding="utf-8"%>
<%@page import="java.math.BigDecimal"%>
<%@ page import="java.util.*" %>
<%

	String path = request.getContextPath();
	int port = request.getServerPort();
	String basePath = null; 
	if(port==80){
		basePath = request.getScheme()+"://"+request.getServerName()+path;
	}else{
		basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
	}
	pageContext.setAttribute("basePath", basePath);
	
	
	Object str = request.getAttribute("face");
	JSONObject json = JSONObject.fromObject(str);
	JSONArray faces = json.getJSONArray("faces");
	StringBuffer strBuff = new StringBuffer();
	//识别出人脸的个数
	int length = faces.size();
	
	//识别出多少个人脸，就是循环多少次
	for(int i = 0;i < length;i++){
	
		JSONObject face = faces.getJSONObject(i);  //拿到第i+1个人脸部信息
		JSONObject attribute = face.getJSONObject("attributes");//拿到脸部属性
		
		//年龄
		JSONObject age = attribute.getJSONObject("age");
		int ageValue = age.getInt("value");
		
		//性别
		JSONObject gender = attribute.getJSONObject("gender");
		String sex = gender.getString("value");
		if(sex.equals("Male")) sex = "男";
		else sex = "女";
		
		//人种
		JSONObject ethnicity = attribute.getJSONObject("ethnicity");
		String races = ethnicity.getString("value");
		if(races.equals("ASIAN")) races = "亚洲人";
		else if(races.equals("WHITE")) races = "白人";
		else races = "黑人";
		
		//微笑程度
		JSONObject smile = attribute.getJSONObject("smile");
		String smileRange = String.format("%.2f",smile.getDouble("value"));
		
		//表情
		JSONObject emotion = attribute.getJSONObject("emotion");
		Map<String,Double> mapp = new TreeMap<String,Double>();
		//装配表情信息到map
		double sadness = emotion.getDouble("sadness");
		mapp.put("伤心", sadness);
		double neutral = emotion.getDouble("neutral");
		mapp.put("平静", neutral);
		double disgust = emotion.getDouble("disgust");
		mapp.put("厌恶", disgust);
		double anger = emotion.getDouble("anger");
		mapp.put("愤怒", anger);
		double happiness = emotion.getDouble("happiness");
		mapp.put("高兴", happiness);
		double surprise = emotion.getDouble("surprise");
		mapp.put("惊讶", surprise);
		double fear = emotion.getDouble("fear");
		mapp.put("恐惧", fear);
		
		//利用list取最大值
		List<Double> listmap = new ArrayList<Double>();
		for(String key:mapp.keySet()){
			listmap.add(mapp.get(key));
		}
		//取到最大值
		double valueMax = Collections.max(listmap);
		//根据map的value获取map的key  
		String emotionMax = "";  
		for (Map.Entry<String, Double> entry : mapp.entrySet()) {  
			if(valueMax == entry.getValue()){  
				emotionMax = entry.getKey();  
			}
		}
		
		//颜值分数
		JSONObject beauty = attribute.getJSONObject("beauty");
		String beautys = "";
		if(sex.equals("男")) beautys = String.format("%.2f",beauty.getDouble("male_score"));
		else beautys = String.format("%.2f",beauty.getDouble("female_score"));
		
		//面部状态
		JSONObject skinstatus = attribute.getJSONObject("skinstatus");
		Map<String,String> mapSkin = new TreeMap<String,String>();
		//装配信息到map
		double health = skinstatus.getDouble("health");
		mapSkin.put("健康指数", String.format("%.2f",health));
		double stain = skinstatus.getDouble("stain");
		mapSkin.put("色斑指数", String.format("%.2f",stain));
		double acne = skinstatus.getDouble("acne");
		mapSkin.put("青春痘指数", String.format("%.2f",acne));
		double dark_circle = skinstatus.getDouble("dark_circle");
		mapSkin.put("黑眼圈指数", String.format("%.2f",dark_circle));
		
		strBuff.append("<br>");
		strBuff.append("<span>年龄：").append(ageValue).append("岁</span>");
		strBuff.append("<span>性别：").append(sex).append("</span>");
		strBuff.append("<br>").append("<br>");
		strBuff.append("<span>人种：").append(races+"</span>");
		strBuff.append("<span>微笑程度：").append(smileRange+"%</span>");
		strBuff.append("<br>").append("<br>");
		strBuff.append("<span>表情：").append(emotionMax+"</span>");
		strBuff.append("<span>颜值分数：").append(beautys+"</span>");
		strBuff.append("<br>").append("<br>");
		int counts = 0;
		for(String key:mapSkin.keySet()){
			counts++;
			strBuff.append("<span>"+key+"：").append(mapSkin.get(key)+"</span>");
			if(counts % 2 == 0) strBuff.append("<br>").append("<br>");
		}
		     
		strBuff.append("<br>");
		     
	}
      
      

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title> Java开发人脸特征识别系统  --krry</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

	<style type="text/css">
     	*{padding:0px;margin:0px;}
     	body{background: #FFF;background:url("images/78788.jpg");background-size:cover;}
     	.a_btn{text-decoration:none;font-family:"幼圆";background:#fff;transition:.6s;color:#f60;cursor:pointer;width:100px;height:40px;border-radius:10px;display: inline-block;text-align:center;line-height:40px;}
		.aa{position: absolute;left: 48px;top: 31px;}
		.a_btn:hover{background:red;color:#fff;transition:.6s;}
     	.clear{clear:both;}
	     .title{margin-top: 25px;font-size:48px;font-family:"幼圆";color: #ff7272;text-shadow: 1px 1px 3px #ff3737;text-align:center;line-height:45px;}
	     .content{margin:0 auto;width:900px;}
     	 .content .img{float:left;margin-top:30px;}
     	 .content .img img{border-radius:10px;}
	     .content .describe{margin-top:30px;width:500px;color:#000;font-family:"幼圆";font-size:18px;float:right;}
		 .content .describe span{width: 190px;display: inline-block;}
	</style>

  </head>
  
  <body>
  		<div class="aa"><a class="a_btn" href="${basePath}">返回</a></div>
  		<div class="title">人脸识别结果</div>
  		<div class="content">
	        <div class = "img">
	           <img style="float:left" width="350" src="${imgSrcPage}" />
	        </div>
	        <div style="float:right" class="describe"><%=strBuff.toString() %></div>
	        <div class="clear"></div>
        </div>
  </body>
</html>
