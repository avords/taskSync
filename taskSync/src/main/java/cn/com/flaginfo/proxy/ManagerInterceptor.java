package cn.com.flaginfo.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

/**
 * Manager拦截器<br/>
 * 可执行多个配置的Advice<br/>
 * 添加Advice到List中即可自动执行
 * 
 * @author Rain
 * 
 */
public class ManagerInterceptor implements MethodInterceptor {

	private Logger logger = Logger.getLogger(getClass());

	@Override
	public Object intercept(Object object, Method method, Object[] o,
			MethodProxy proxy) throws Throwable {

		Object result = null;
		long start = System.currentTimeMillis();
		result = proxy.invokeSuper(object, o);

		if (System.currentTimeMillis() - start > 1000) {
			String methodKey = method.getDeclaringClass().getName() + "@"
					+ method.getName();
			logger.info("method slowly=" + methodKey + " cost time:"
					+ (System.currentTimeMillis() - start) + " ms");
		}

		return result;
	}
}
