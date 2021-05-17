package com.sleuth.core.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

public class CopyUtil {
	
	final static Logger logger = LoggerFactory.getLogger(CopyUtil.class);
	
	final static String serialVerID = "serialVersionUID";
	
	public static <T> List<T> copyList(List<?> sources, Class<T> target) {
		List<T> result = new ArrayList<T>(sources.size());
		try {
			for (Object t : sources) {
				T object = target.newInstance();
				BeanUtils.copyProperties(t, object);
				result.add(object);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public static <T> T copyProperty(Object source, Class<T> target) {
		T object = null;
		if (source != null) {
			try {
				object = target.newInstance();
				BeanUtils.copyProperties(source, object);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return object;
	}
	
	public static <T> List<T> copyList2(List<?> sources, Class<T> target) {
		Assert.notNull(sources, "Source must not be null");
		Assert.notNull(target, "Target must not be null");
		
		List<T> result = new ArrayList<T>(sources.size());
		try {
			PropertyDescriptor[] targetPds = forClass(target);
			PropertyDescriptor[] superPds = forSuperClass(target);
			for (Object source : sources) {
				if (source != null) {
					T targetObjct = target.newInstance();
					copyValue(targetPds, source, targetObjct);
					copyValue(superPds, source, targetObjct);
					result.add(targetObjct);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public static <T> T copyProperty2(Object source, Class<T> target) {
		
		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");
		T targetObjct = null;
		try {
			targetObjct = target.newInstance();
			PropertyDescriptor[] targetPds = forClass(target);
			copyValue(targetPds, source, targetObjct);
			
			//拷贝父类属性
			PropertyDescriptor[] superPds = forSuperClass(target);
			if (superPds != null && superPds.length > 0) {
				copyValue(superPds, source, targetObjct);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return targetObjct;
	}
	
	private static <T> void copyValue(PropertyDescriptor[] targetPds, Object source, 
			T targetObject) throws Exception {
		for (PropertyDescriptor targetPd : targetPds) {
			if (targetPd != null && !serialVerID.equals(targetPd.getName())) {
				Method writeMethod = targetPd.getWriteMethod();
				if (writeMethod != null) {
					PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
					if (sourcePd != null) {
						Method readMethod = sourcePd.getReadMethod();
						if (readMethod != null) {
							try {
								readMethod.setAccessible(true);
								Object value = readMethod.invoke(source);
								writeMethod.setAccessible(true);
								writeMethod.invoke(targetObject, value);
							} catch (Exception e) {
								logger.error("Could not copy property '" + 
										targetPd.getName() + "' from source to target", e);
							}
						}
						
					}
				}
			}
		}
	}
	
	private static PropertyDescriptor getPropertyDescriptor(Class<?> sourceClass, 
			String targetFileName) throws IntrospectionException {
		return new PropertyDescriptor(targetFileName, sourceClass);
	}
	
	/** 读取类的属性
	 * 
	 * @param beanClass
	 * @return
	 * @throws IntrospectionException
	 */
	private static PropertyDescriptor[] forClass(Class<?> beanClass) throws IntrospectionException {
		PropertyDescriptor[] propertys = null;
		Field[] fields = beanClass.getDeclaredFields();
		if (fields != null && fields.length > 0) {
			propertys = new PropertyDescriptor[fields.length];
		}
		int temp = 0;
		for (Field field : fields) {
			if (!field.getName().equals(serialVerID)) {
				String fieldName = field.getName();
				boolean exists = false;
				try {
					exists = beanClass.getDeclaredField(fieldName) != null ? true : false;
				} catch (Exception e) {
				}
				
				if (exists) {
					propertys[temp] = new PropertyDescriptor(fieldName, beanClass);
				}
				temp++;
			}
		}
		return propertys;
	}
	
	/** 读取父类的属性
	 * 
	 * @param beanClass
	 * @return
	 * @throws IntrospectionException
	 */
	private static PropertyDescriptor[] forSuperClass(Class<?> beanClass) throws Exception {
		PropertyDescriptor[] propertys = null;
		Class<?> superclass = beanClass.getSuperclass();//父类
		Field[] fields = superclass.getDeclaredFields();
		if (fields != null && fields.length > 0) {
			propertys = new PropertyDescriptor[fields.length];
		}
		int temp = 0;
		for (Field field : fields) {
			if (!field.getName().equals(serialVerID)) {
				String fieldName = field.getName();
				boolean exists = false;
				try {
					exists = beanClass.getDeclaredField(fieldName) != null ? true : false;
				} catch (Exception e) {
				}
				if (exists) {
					propertys[temp] = new PropertyDescriptor(fieldName, superclass);
				}
				temp++;
			}
		}
		return propertys;
	}

}
