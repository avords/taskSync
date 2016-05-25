package cn.com.flaginfo.support;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import cn.com.flaginfo.db.DBCommitInterceptor;
import cn.com.flaginfo.proxy.ManagerInterceptor;

/**
 * 类工厂，所有的Manager都通过这个方法获得
 * @author Rain
 *
 */
public class ManagerFactory {
	
	private static Map<String,Object> beanMap = new HashMap<String,Object>();
	
	public static <T> T getInstance(Class beanClazz){
		 T t = (T)beanMap.get(beanClazz.getName());
		 if(t==null){
			 Enhancer en = new Enhancer();
			 en.setSuperclass(beanClazz);
			 en.setCallback(new DBCommitInterceptor());
//			 en.setCallback(new ManagerInterceptor());
//			 en.setCallbackFilter(new CallbackFilter() {
//				@Override
//				public int accept(Method arg0) {
//					return 0;
//				}
//			});
			 t = (T)en.create();
			 beanMap.put(beanClazz.getName(),t);
		 }
		 return t;
	}
	
}
