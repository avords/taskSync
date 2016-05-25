package cn.com.flaginfo.support;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import cn.com.flaginfo.util.StringUtil;

/**
 * 封装httpClient方法
 * @author Rain
 *
 */
public class WebClient {
	
	private static Logger logger = Logger.getLogger(WebClient.class);
	public static HttpClient httpClient;
	
	static{
		MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager(); 
		HttpConnectionManagerParams params = connectionManager.getParams();
        params.setConnectionTimeout(60000); 
        params.setSoTimeout(120000); 
        params.setDefaultMaxConnectionsPerHost(32);
        params.setMaxTotalConnections(256);
		httpClient = new HttpClient(connectionManager);
        logger.info("httpclient init finished!");
	}
	
	/**
	 * postForm返回Map<br/>
	 * 默认接口返回值的为Json格式的字符串
	 * @param url
	 * @param formParams
	 * @return
	 */
	public Map<String,Object> postFormToMap(String url,Map<String,String> formParams){
		String result = this.postForm(url, formParams);
		if(StringUtil.isNullOrEmpty(result)){
			return null;
		}
		return JSONObject.parseObject(result, Map.class);
		
	}
	
	/**
	 * POST Form
	 * @param url
	 * @param formParams
	 * @return
	 */
	public String postForm(String url,Map<String,String> formParams){
		
		PostMethod post=new PostMethod(url);
		post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		try {
			if(formParams != null){
				Set<String> keys = formParams.keySet();
				for(String key:keys){
					String value = formParams.get(key);
					post.setParameter(key, value);
				}
			}
			int status= httpClient.executeMethod(post);
			StringBuffer strBuff = new StringBuffer();
			if(status!=HttpStatus.SC_OK){
				logger.info("上传文件失败:"+status);
				return strBuff.toString();
			}
			BufferedReader reader=new BufferedReader(new InputStreamReader(post.getResponseBodyAsStream()));
			String str="";
			while((str=reader.readLine())!=null){
				strBuff.append(str);
			}
			logger.info("upload:"+strBuff.toString());
			return strBuff.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(post!=null){
				post.releaseConnection();
			}
		}
		return null;
	}
	
	
	/**
	 * 上传文件
	 * @param url 文件地址
	 * @param parts 文件
	 * @return
	 */
	public String upload(String url,Part[] parts){
		PostMethod filePost=new PostMethod(url);//上传地址
		try {
			filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
			int status=httpClient.executeMethod(filePost);
			logger.info("status:"+filePost.getStatusLine());
			StringBuffer strBuff = new StringBuffer();
			if(status!=HttpStatus.SC_OK){
				logger.info("上传文件失败:"+status);
				return strBuff.toString();
			}
			BufferedReader reader=new BufferedReader(new InputStreamReader(filePost.getResponseBodyAsStream()));
			String str="";
			while((str=reader.readLine())!=null){
				strBuff.append(str);
			}
			logger.info("upload:"+strBuff.toString());
			return strBuff.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(filePost!=null){
				filePost.releaseConnection();
			}
		}
		return null;
	}
	
	/**
     * POST Form 可编码
     * @param url 请求地址
     * @param formParams 请求参数集合
     * @param code 编码类型 UTF-8，gbk
     * @return
     */
    public String postForm(String url,Map<String,String> formParams,String code){
        
        PostMethod post=new PostMethod(url);
        post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, code);
        try {
            if(formParams != null){
                Set<String> keys = formParams.keySet();
                for(String key:keys){
                    String value = formParams.get(key);
                    post.setParameter(key, value);
                }
            }
            int status= httpClient.executeMethod(post);
            StringBuffer strBuff = new StringBuffer();
            if(status!=HttpStatus.SC_OK){
                logger.info("请求地址失败:"+status);
                return strBuff.toString();
            }
            String result = new String(post.getResponseBody(),code);
            logger.info("response:"+result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(post!=null){
                post.releaseConnection();
            }
        }
        return null;
    }
    
}
