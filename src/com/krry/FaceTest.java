package com.krry;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.net.ssl.SSLException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class FaceTest {
	
	public static void main(String[] args) throws Exception{
		
        File file = new File("/Users/krry/Desktop/psb (1).jpeg");
		byte[] buff = getBytesFromFile(file);
		String url = "https://api-cn.faceplusplus.com/facepp/v3/detect";
        HashMap<String, String> map = new HashMap<String, String>();
        HashMap<String, byte[]> byteMap = new HashMap<String, byte[]>();
        map.put("api_key", "15N9eBF_ieE-N2nvI7oK_A9anh2meuUS");
        map.put("api_secret", "cBnvsIAo8VaOj53lXCWlpuOSd0NrsFif");
		map.put("return_landmark", "1");
        map.put("return_attributes", "gender,age,smiling,headpose,facequality,blur,eyestatus,emotion,ethnicity,beauty,mouthstatus,eyegaze,skinstatus");
        byteMap.put("image_file", buff);
        try{
            byte[] bacd = post(url, map, byteMap);
            String str = new String(bacd);
            JSONObject json = JSONObject.fromObject(str);
            JSONArray faces = json.getJSONArray("faces");
            
            
            for(int i = 0;i < faces.size();i++){
              	 
                JSONObject face = faces.getJSONObject(i);  //拿到i下标所代表的脸部信息
                JSONObject attribute = face.getJSONObject("attributes");
                
                //年龄
                JSONObject age = attribute.getJSONObject("age");
                int ageValue = age.getInt("value");
                
                //性别
                JSONObject gender = attribute.getJSONObject("gender");
                String sex = gender.getString("value");
                if(sex.equals("Male")) sex = "男";
                else sex = "女";
                
                //种族
                JSONObject ethnicity = attribute.getJSONObject("ethnicity");
                String races = ethnicity.getString("value");
                System.out.println(races);
                
                //微笑程度
                JSONObject smile = attribute.getJSONObject("smile");
                String smileRange = String.format("%.2f",smile.getDouble("value"));
                
                //表情
                JSONObject emotion = attribute.getJSONObject("emotion");
                Map<String,Float> mapp = new TreeMap<String,Float>();
                //装配表情信息到map
                float sadness = Float.parseFloat(emotion.getString("sadness"));
                mapp.put("伤心", sadness);
                float neutral = Float.parseFloat(emotion.getString("neutral"));
                mapp.put("平静", neutral);
                float disgust = Float.parseFloat(emotion.getString("disgust"));
                mapp.put("厌恶", disgust);
                float anger = Float.parseFloat(emotion.getString("anger"));
                mapp.put("愤怒", anger);
                float happiness = Float.parseFloat(emotion.getString("happiness"));
                mapp.put("高兴", happiness);
                float surprise = Float.parseFloat(emotion.getString("surprise"));
                mapp.put("惊讶", surprise);
                float fear = Float.parseFloat(emotion.getString("fear"));
                mapp.put("恐惧", fear);
                
                //利用list取最大值
                List<Float> listmap = new ArrayList<Float>();
                for(String key:mapp.keySet()){
              	  listmap.add(mapp.get(key));
                }
                //取到最大值
                float valueMax = Collections.max(listmap);
                //根据map的value获取map的key  
                String emotionMax = "";  
                for (Map.Entry<String, Float> entry : mapp.entrySet()) {  
                    if(valueMax == entry.getValue()){  
                  	  emotionMax = entry.getKey();  
                    }
                }
                
                //颜值分数
                JSONObject beauty = attribute.getJSONObject("beauty");
                String beautys = "";
                if(sex.equals("男")) beautys = beauty.getString("male_score");
                else beautys = beauty.getString("female_score");
                
                //面部状态
                JSONObject skinstatus = attribute.getJSONObject("skinstatus");
                Map<String,Float> mapSkin = new TreeMap<String,Float>();
                //装配信息到map
                float health = Float.parseFloat(skinstatus.getString("health"));
                mapSkin.put("健康", health);
                float stain = Float.parseFloat(skinstatus.getString("stain"));
                mapSkin.put("色斑", stain);
                float acne = Float.parseFloat(skinstatus.getString("acne"));
                mapSkin.put("青春痘", acne);
                float dark_circle = Float.parseFloat(skinstatus.getString("dark_circle"));
                mapSkin.put("黑眼圈", dark_circle);
           }
            
            
            
            
            System.out.println(str);
        }catch (Exception e) {
        	e.printStackTrace();
		}
	}
	
	private final static int CONNECT_TIME_OUT = 30000;
    private final static int READ_OUT_TIME = 50000;
    private static String boundaryString = getBoundary();
    
    protected static byte[] post(String url, HashMap<String, String> map, HashMap<String, byte[]> fileMap) throws Exception {
        HttpURLConnection conne;
        URL url1 = new URL(url);
        conne = (HttpURLConnection) url1.openConnection();
        conne.setDoOutput(true);
        conne.setUseCaches(false);
        conne.setRequestMethod("POST");
        conne.setConnectTimeout(CONNECT_TIME_OUT);
        conne.setReadTimeout(READ_OUT_TIME);
        conne.setRequestProperty("accept", "*/*");
        conne.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundaryString);
        conne.setRequestProperty("connection", "Keep-Alive");
        conne.setRequestProperty("user-agent", "Mozilla/4.0 (compatible;MSIE 6.0;Windows NT 5.1;SV1)");
        DataOutputStream obos = new DataOutputStream(conne.getOutputStream());
        Iterator iter = map.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry<String, String> entry = (Map.Entry) iter.next();
            String key = entry.getKey();
            String value = entry.getValue();
            obos.writeBytes("--" + boundaryString + "\r\n");
            obos.writeBytes("Content-Disposition: form-data; name=\"" + key
                    + "\"\r\n");
            obos.writeBytes("\r\n");
            obos.writeBytes(value + "\r\n");
        }
        if(fileMap != null && fileMap.size() > 0){
            Iterator fileIter = fileMap.entrySet().iterator();
            while(fileIter.hasNext()){
                Map.Entry<String, byte[]> fileEntry = (Map.Entry<String, byte[]>) fileIter.next();
                obos.writeBytes("--" + boundaryString + "\r\n");
                obos.writeBytes("Content-Disposition: form-data; name=\"" + fileEntry.getKey()
                        + "\"; filename=\"" + encode(" ") + "\"\r\n");
                obos.writeBytes("\r\n");
                obos.write(fileEntry.getValue());
                obos.writeBytes("\r\n");
            }
        }
        obos.writeBytes("--" + boundaryString + "--" + "\r\n");
        obos.writeBytes("\r\n");
        obos.flush();
        obos.close();
        InputStream ins = null;
        int code = conne.getResponseCode();
        try{
            if(code == 200){
                ins = conne.getInputStream();
            }else{
                ins = conne.getErrorStream();
            }
        }catch (SSLException e){
            e.printStackTrace();
            return new byte[0];
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[4096];
        int len;
        while((len = ins.read(buff)) != -1){
            baos.write(buff, 0, len);
        }
        byte[] bytes = baos.toByteArray();
        ins.close();
        return bytes;
    }
    private static String getBoundary() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < 32; ++i) {
            sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-".charAt(random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".length())));
        }
        return sb.toString();
    }
    private static String encode(String value) throws Exception{
        return URLEncoder.encode(value, "UTF-8");
    }
    
    public static byte[] getBytesFromFile(File f) {
        if (f == null) {
            return null;
        }
        try {
            FileInputStream stream = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b)) != -1)
                out.write(b, 0, n);
            stream.close();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
        }
        return null;
    }
}