package com.sleuth.core.script.actuator;

import java.util.Map;
import java.util.Set;

import javax.script.Invocable;
import javax.script.ScriptEngine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sleuth.core.script.Fnbody;
import com.sleuth.core.script.ScriptActuator;
import com.sleuth.core.script.Yuan;
import com.sleuth.core.script.exception.ScriptRunException;

/** 交易元脚本执行器
 * 
 * @author Jonse
 * @date 2020年12月11日
 */
public class YuanScriptActuator implements ScriptActuator {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	static final String FN_INPUT = "input";
	static final String FN_OUTPUT = "output";
	static final String FN_TRIGGER = "trigger";
	
	@Override
	public InputResult input(ScriptEngine engine, Yuan yuan, AxParams entries, String[] inputs) {
		Fnbody fnbody = yuan.getFnbody();
		try {
			if (engine instanceof Invocable) {
				JSONObject json = new JSONObject();
				if (entries != null && entries.size() > 0) {
					JSONObject object = new JSONObject();
					while(entries.hasNext()) {
						Entry entry = entries.next();
						object.put(entry.getName(), entry.getValue());
					}
					json.put("entries", object);
				}
				json.put("inputs", inputs);
				
				engine.eval(yuan.getJsscript());
				Invocable invoke = (Invocable) engine;
				//函数执行结果
				Object result = invoke.invokeFunction(FN_INPUT, json);
				if (result == null) {
					throw new ScriptRunException("Undefined return '"+fnbody.getName()+"'");
				}
				
				InputResult iResult = new InputResult();
				//将返回结果转换为JSON对象
				JSONObject jsonResult = (JSONObject) JSONObject.toJSON(result);
				//获得输入参数
				JSONObject jsonEntries = jsonResult.getJSONObject("entries");
				if (jsonEntries != null && jsonEntries.size() > 0) {
					AxParams axParams = new AxParams();
					Set<String> keySet = jsonEntries.keySet();
					for (String key : keySet) {
						axParams.set(key, jsonEntries.get(key));
					}
					iResult.setParams(axParams);
				}
				//获取引用参数
				JSONArray jsonInputs = jsonResult.getJSONArray("inputs");
				iResult.setInputs(jsonInputs.toArray(new String[jsonInputs.size()]));
				return iResult;
			}
		} catch (Exception e) {
			logger.error("Yuan script run exception ", e);
			throw new ScriptRunException("Yuan '"+fnbody.getName()+"' script run exception, menthed 'input'");
		}
		return null;
	}

	@Override
	public Object trigger(ScriptEngine engine, Yuan yuan, AxParams entries, OutputResult[] outputs) {
		Fnbody fnbody = yuan.getFnbody();
		try {
			if (engine instanceof Invocable) {
				JSONObject json = new JSONObject();
				if (entries.size() > 0) {
					JSONObject object = new JSONObject();
					while(entries.hasNext()) {
						Entry entry = entries.next();
						object.put(entry.getName(), entry.getValue());
					}
					json.put("entries", object);
				}
				json.put("inputs", outputs);
				
				engine.eval(yuan.getJsscript());
				Invocable invoke = (Invocable) engine;
				Object result = invoke.invokeFunction(FN_TRIGGER, json);
				if ("jdk.nashorn.api.scripting.ScriptObjectMirror".equals(result.getClass().getName())) {
					@SuppressWarnings("unchecked")
					Map<Object, Object> map = (Map<Object, Object>) result;
					return JSONObject.toJSONString(map);
				}
				return result;
			}
		} catch (Exception e) {
			logger.error("Yuan script run exception ", e);
			throw new ScriptRunException("Yuan '"+fnbody.getName()+"' script run exception, menthed 'trigger'");
		}
		return null;
	}

	@Override
	public void output(ScriptEngine engine, Yuan yuan, OutputResult result) {
		Fnbody fnbody = yuan.getFnbody();
		try {
			if (engine instanceof Invocable) {
				engine.eval(yuan.getJsscript());
				Invocable invoke = (Invocable) engine;
				invoke.invokeFunction(FN_OUTPUT, JSON.toJSONString(result));
			}
		} catch (Exception e) {
			logger.error("Yuan script run exception ", e);
			throw new ScriptRunException("Yuan '"+fnbody.getName()+"' script run exception, menthed 'output'");
		}
	}
	
}
