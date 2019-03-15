package com.krry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *  FileUpLoad
 * @author krry
 *
 */
@WebServlet(name="fileUp",urlPatterns={"/show"})
public class FileUpLoad extends HttpServlet{
	
	private static final long serialVersionUID = 1L;

	@Override
	public void service(HttpServletRequest request,HttpServletResponse response) throws IOException{
		//判断是不是文件上传，根据请求包里面的内容是否为二进制提交的上传的文件 true/false
		boolean isMultPart = ServletFileUpload.isMultipartContent(request);
		if(isMultPart){
			//创建文件上传工厂
			FileOutputStream out = null;
			InputStream in = null;
			try {
	     		FileItemFactory factory = new DiskFileItemFactory();
		     	//创建容器文件类，负责处理上传的文件数据
		    	ServletFileUpload upload = new ServletFileUpload(factory);
	     		//解析上传文件,其实就是解析表单传过来的数据
				List<FileItem> fileItems = upload.parseRequest(request);
				//只需要拿一张图片
				in = fileItems.get(0).getInputStream();
				//获取源文件名
				String Ofilename = fileItems.get(0).getName();
				//拿到后缀名
				String ext = Ofilename.substring(Ofilename.indexOf("."), Ofilename.length());
				//拿到一个目录，绝对地址
				String path =request.getSession().getServletContext().getRealPath("/");
				//生成一个唯一的名字
				String fileName = UUID.randomUUID().toString() + ext;
				//定义上传目录
				String dirPath = path+"/upload";
				File dirFile = new File(dirPath);
				//如果此文件夹不存在
				if(!dirFile.exists()){
					dirFile.mkdirs();//创建此文件夹
				}
				//配置文件路径
				String filePath = path+"/upload/"+fileName;
				//输出流
				out = new FileOutputStream(filePath);
				//边读取边编写 字节流
				byte[] b = new byte[1024];
				int length = 0;
				while((length = in.read(b)) != -1){  //读到字节数组里面去
					out.write(b,0,length);   //从0开始写，写你读了多少
				}
				//放在request作用域里
				request.setAttribute("imgSrc", filePath);
				request.setAttribute("imgSrcPage", "upload/"+fileName);
				//请求转发，转发到另一个servlet
				request.getRequestDispatcher("showImage").forward(request, response);
			} catch (Exception e) {
				
				e.printStackTrace();
			}finally{
			    out.close();
				in.close();
			}
		}
	}

}
