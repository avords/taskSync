/**
 * 
 */
package cn.com.flaginfo.db;

import java.io.IOException;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.Executor;

import org.apache.log4j.Logger;

import cn.com.flaginfo.util.SystemMessage;

/**
 * TODO
 *
 * @author guobin.liu
 * 2015年9月7日
 */
public class JDBCMessage {
    private final static Logger logger = Logger.getLogger(SystemMessage.class);
    private static Properties properties = new Properties();
    
    //本地配置文件
    private static final String BUNDLE_NAME = "jdbc";

    static{
        //开启本地读取模式
        ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME);
        properties = new Properties();
        Enumeration<String> e = bundle.getKeys();
        while(e.hasMoreElements()){
            String key = e.nextElement();
            properties.put(key, bundle.getString(key));
        }
        
    }
    private static Properties getPropertiesForString(String configInfo){
        Properties p = new Properties();
        try {
            p.load(new StringReader(configInfo));
            if(logger.isDebugEnabled()){
                Set<Entry<Object,Object>> set = properties.entrySet();
                Map allMap = new HashMap();
                for(Entry e:set){
                    logger.debug(e.getKey()+"==>"+e.getValue());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }
    
    public static String getString(String key) {
        return properties.getProperty(key);
    }
}
