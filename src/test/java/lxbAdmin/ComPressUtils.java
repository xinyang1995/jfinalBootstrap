package lxbAdmin;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.jfinal.log.Log;
import com.lxb.util.MD5Util;
import com.tinify.Source;
import com.tinify.Tinify;

/**
 * Tinypng压缩指定jpg/png图片工具类
 */
public class ComPressUtils {

	private static Log log=Log.getLog(ComPressUtils.class);
	
	//配置文件路径（存储被压缩图片的信息，键值对保存，键：图片路径，值：压缩后图片MD5加密）
	public static final String FILE_NAME = "keyvalue.properties";//要创建的文件名
    public static final String FILE_PATH = "C:/Users/xiubin/Desktop/pic/";//文件指定存放的路径
    
    //搜索压缩图片的目录
	private static final String IMGPATH="C:/Users/xiubin/Desktop/pic";
	
	public static void main(String[] args) throws IOException {
		
		//加载配置文件信息，获取已被压缩的图片
		Map<String, Object> compressImgMap=initConfigFile();
		
		//搜索压缩的图片
		List<String> result=new ArrayList<>();//存储要压缩的图片路径
		log.info("准备搜索目标文件");
		File file=new File(IMGPATH);
		getNeedCompressImg(file,result);
		log.info("搜索目标文件结束");
		for (String s : result) {
			log.info("搜索结果"+s);
		}
		
		//压缩搜索到的图片替换旧图片
		compressImg(result,compressImgMap);
		
	}
	
	public static byte[] getBytes(String filePath){  
        byte[] buffer = null;  
        try {  
            File file = new File(filePath);  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
            byte[] b = new byte[1000];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return buffer;  
    }

	/**
	 * 压缩搜索到的图片替换旧图片
	 * @param result
	 * @throws IOException 
	 */
	private static void compressImg(List<String> result,Map<String, Object> compressImgMap) throws IOException {
		
		Properties prop = new Properties();
		
		log.info("准备压缩目标图片");
		Tinify.setKey("Y5k06VoPIVGt6eLUxhivOxBCNwaDclv2");
			
		Source source;
		File imgFile;
		String byteStr;
		FileOutputStream oFile = new FileOutputStream(FILE_PATH+FILE_NAME,true);//true表示追加打开
		byte[] byteArray;
		for (String s : result) {
			try {
				log.info("开始压缩目标图片："+s);
				//获取旧图片的绝对路径和名称
				imgFile=new File(s);
				String path=imgFile.getAbsolutePath();
				//先判断图片是否已被压缩过
				if (!compressImgMap.containsKey(s)) {//未被压缩
					//压缩
					source = Tinify.fromFile(s);
					source.toFile(path);
					
					//存储信息到配置文件
					imgFile=new File(s);//新图片
					byteArray=getBytes(s);
					byteStr=MD5Util.byteArrayToHexString(byteArray);
					prop.setProperty(path, MD5Util.MD5Encode(byteStr));
					log.info("图片"+s+"压缩成功");
				}else {//配置文件有该路径，判断对应的图片是否被压缩的图片
					String oldSign=compressImgMap.get(s).toString();
					imgFile=new File(s);//新图片
					byteArray=getBytes(s);
					byteStr=MD5Util.byteArrayToHexString(byteArray);
					String newSign=MD5Util.MD5Encode(byteStr);
					if (oldSign.equals(newSign)) {
						log.info("图片"+s+"已被压缩，无需重复压缩");
					}else {
						//压缩
						source = Tinify.fromFile(s);
						source.toFile(path);
						log.info("图片"+s+"压缩成功");
						//存储信息到配置文件
						prop.setProperty(path, MD5Util.MD5Encode(byteStr));
					}
				}
				
			} catch (Exception e) {
				log.info("图片"+s+"压缩失败，"+e.getMessage());
			}
		}
		prop.store(oFile,null);
		oFile.close();
		log.info("图片压缩结束");
	}

	/**
	 * 搜索需要压缩的图片
	 * @param imgPath
	 */
	private static void getNeedCompressImg(File file,List<String> result) {
		
		//获取指定目录下当前所有文件夹或者文件对象
		File[] files=file.listFiles();
		for(int i=0;i<files.length;i++) {
			if(files[i].isDirectory()) {//文件夹，进一步遍历文件夹内容
				getNeedCompressImg(files[i],result);
			}else {
				//文件夹名
				String fileName=file.getName();    
				//文件夹里文件
				String childrenFileName=files[i].getName();//文件全名(包含扩展名)
				String name=childrenFileName.substring(0,childrenFileName.lastIndexOf("."));//文件名（不包含扩展名）
				String fileTyle=childrenFileName.substring(childrenFileName.lastIndexOf(".")+1,childrenFileName.length());//文件的扩展名
				
				if (null!=fileName&&null!=name) {
					if (fileName.equals(name)) {
						if ("jpg".equals(fileTyle)||"png".equals(fileTyle)) {
							result.add(files[i].getAbsolutePath());
						}
					}
				}
			}
		}
		
	}

	/**
	 * 加载配置文件信息
	 * @param configFilePath
	 */
	private static Map<String, Object> initConfigFile() {
		
		Map<String, Object> result=new HashMap<>();//图片路径：图片MD5加密
		String configFilePath=FILE_PATH+FILE_NAME;
		
		File folder = new File(FILE_PATH);
        //文件夹路径不存在
        if (!folder.exists() && !folder.isDirectory()) {
        	log.info("文件夹路径不存在，创建路径:" + FILE_PATH);
            folder.mkdirs();
            log.info("文件夹创建成功");
        } else {
        	log.info("文件夹路径存在:" + FILE_PATH);
        }

		//判断配置文件是否存在
		File file = new File(configFilePath);
		if (file.exists()) {
			log.info("配置文件存在，开始加载配置文件");
			Properties prop = new Properties();  
			try {
				InputStream in = new BufferedInputStream (new FileInputStream(configFilePath));//读取配置文件
				prop.load(in);     ///加载配置文件
				Iterator<String> it=prop.stringPropertyNames().iterator();
				while(it.hasNext()){
					String key=it.next();
					result.put(key, prop.getProperty(key));
				}
				in.close();
				log.info("加载配置文件成功");
			} catch (FileNotFoundException e) {
				log.info("读取配置文件失败！");
				e.printStackTrace();
			} catch (IOException e) {
				log.info("加载配置文件失败！");
				e.printStackTrace();
			}
		}else {
			log.info("配置文件不存在，正在创建配置文件！");
			try {
				file.createNewFile();
				log.info("配置文件创建成功！");
			} catch (IOException e) {
				log.info("配置文件创建失败！");
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
}
