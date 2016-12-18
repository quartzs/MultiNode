package rhc.discribute;

import java.util.HashMap;
import java.util.Map;

public class SingleInstanceComponent extends BaseComponent{
	
	private static final Map<Class, Object> SINGLE_INSTANCE=new HashMap<Class,Object>();
	
	public SingleInstanceComponent(){
		SINGLE_INSTANCE.put(getClass(), this);
	}
	
	public static <T> T getInstance(Class<T> _class){
		T t=null;
		if(null == (t = (T) SINGLE_INSTANCE.get(_class))){
			t=createInstance(_class);
		}
		return t;
	}
	
	private static synchronized <T> T createInstance(Class<T> _class){
		T t=null;
		if(null != (t=(T) SINGLE_INSTANCE.get(_class))){
			return t;
		}
		
		try {
			t=_class.newInstance();
			SINGLE_INSTANCE.put(_class, t);
			return t;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;
	}
}
