package com.sleuth.network.message.push;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.sleuth.network.message.protocol.CmdType;

/** 是否将结果进行广播
 * 
 * @author Jonse
 * @date 2021年1月18日
 */
@Documented
@Retention (RUNTIME)
@Target(METHOD)
public @interface Broadcast {
	
	CmdType ch();//
	Source ds();//ds
	
	/** 数据源头
	 * 
	 * @author Jonse
	 * @date 2021年1月19日
	 */
	public enum Source {
		
		local("local"),
		push("push");
		
		private String source;
		
		private Source(String source) {
			this.source = source;
		}
		
		public String getSource() {
			return this.source;
		}
	}
	
}
