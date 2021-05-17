package com.sleuth.core.script;

import javax.script.ScriptEngine;

import com.sleuth.core.script.actuator.AxParams;
import com.sleuth.core.script.actuator.InputResult;
import com.sleuth.core.script.actuator.OutputResult;

/** 脚本执行器
 * 
 * 每个脚本都包含输入、输出、触发三个函数
 * 
 * @author Jonse
 * @date 2020年12月11日
 */
public interface ScriptActuator {
	
	/** 输入执行
	 * 
	 * @param engine
	 * @param yuan
	 * @param params
	 * @return
	 */
	public abstract InputResult input(ScriptEngine engine, Yuan yuan, AxParams entries, String[] inputs);
	
	/** 触发执行
	 * 
	 * @param engine
	 * @param yuan
	 * @param entries
	 * @param outputs
	 * @return
	 */
	public abstract Object trigger(ScriptEngine engine, Yuan yuan, AxParams entries, OutputResult[] outputs);
	
	/** 输出执行
	 * 
	 * @param yuan
	 * @return
	 */
	public abstract void output(ScriptEngine engine, Yuan yuan, OutputResult result);
}
