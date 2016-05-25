package cn.com.flaginfo.db;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.log4j.Logger;

/**
 * DB提交
 * @author Rain
 *
 */
public class DBCommitInterceptor implements MethodInterceptor {
	
	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	public Object intercept(Object object, Method method, Object[] o,
			MethodProxy proxy) throws Throwable {
		
		Object result = null;
		long start = System.currentTimeMillis();
		String methodKey =method.getDeclaringClass().getName()+"@"+method.getName();
		try {
			TransactionHolder.push(methodKey);
			result = proxy.invokeSuper(object, o);
			TransactionHolder.pull();
			if(TransactionHolder.isEmpty()){
				ConnectionHolder.commit();
				//ConnectionHolder.colseAndRemove();
				logger.info("cost time:"+methodKey+"==>"+(System.currentTimeMillis()-start)+"ms");
			}
			if(System.currentTimeMillis()-start>500){
				logger.info("****"+methodKey+" cost time:"+(System.currentTimeMillis()-start)+"ms. The method is more slowly ****");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ConnectionHolder.rollback();
			TransactionHolder.remove();
			ConnectionHolder.colseAndRemove();
			throw new RuntimeException(e);
		}
		
		return result;
	}

}
