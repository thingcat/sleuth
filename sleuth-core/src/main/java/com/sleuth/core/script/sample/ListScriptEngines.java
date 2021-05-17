package com.sleuth.core.script.sample;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.core.script.variable.GlobalAPIVariable;

public class ListScriptEngines {
	
	public static String script;
	
	static {
		Path path = Paths.get("G:\\workspace\\yuan\\Sample.yuan");
		byte[] bytes;
		try {
			bytes = Files.readAllBytes(path);
			script = new String(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
     *  使用Script实现java接口
     * @throws Exception 
     */
    public static void runnableImpl() throws Exception{
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        
        // String里定义一段JavaScript代码脚本
        String script = "function run() { print('run called'); }";
        // 执行这个脚本
        engine.eval(script);
        
        // 从脚本引擎中获取Runnable接口对象（实例）. 该接口方法由具有相匹配名称的脚本函数实现。
        Invocable inv = (Invocable) engine;
        // 在上面的脚本中，我们已经实现了Runnable接口的run()方法
        Runnable runnable = inv.getInterface(Runnable.class);
        
        // 启动一个线程运行上面的实现了runnable接口的script脚本
        Thread thread = new Thread(runnable);
        thread.start();
    }
	
	public static void test() throws Exception {
		
		final Logger logger = LoggerFactory.getLogger(ListScriptEngines.class);
		
		ScriptEngineManager engineManager = new ScriptEngineManager();
		ScriptEngine scriptEngine = engineManager.getEngineByName("javascript");
		scriptEngine.put("aaa", 100000);
		scriptEngine.put("log", LoggerFactory.getLogger("CONSOLE"));
		
//		if (scriptEngine instanceof Compilable) {
//			Compilable compEngine = (Compilable)scriptEngine;
//			CompiledScript script = compEngine.compile("function test(a) {print(aaa); return a;} test(10000);");
//			System.out.println(script.eval());
//		}
		System.out.println(scriptEngine.eval("var bb = 123; function test(a) {print(bb); log.info('111111111'); return a;} test(10000);"));
	}
	
	public static void scriptVar() throws ScriptException {
		ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        engine.put("api", new GlobalAPIVariable());
        engine.eval("print(api.require('111111111').getName());");
	}
	
	public static void test(String[] args) throws Exception {
	
		Path path = Paths.get("G:\\workspace\\sample.yuan");
		Stream<String> stream = Files.lines(path);
		StringBuilder text = new StringBuilder();
		stream.forEach(line->{
			text.append(line);
		});
		stream.close();
		
		ScriptEngineManager engineManager = new ScriptEngineManager();
		ScriptEngine scriptEngine = engineManager.getEngineByName("javascript");
		scriptEngine.put("log", LoggerFactory.getLogger(ListScriptEngines.class));
		if (scriptEngine instanceof Compilable) {
			Compilable compEngine = (Compilable)scriptEngine;
			CompiledScript script = compEngine.compile(text.toString().replace("yuan", "function"));
			System.out.println(script.eval());
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		String script = "function Atest(j) {var i = 100;return j;};function Btest() {var atest = new Atest();print(atest.get());}";
		ScriptEngineManager engineManager = new ScriptEngineManager();
		ScriptEngine scriptEngine = engineManager.getEngineByName("javascript");
		scriptEngine.put("log", LoggerFactory.getLogger(ListScriptEngines.class));
		scriptEngine.eval(script.toString());
		
		if (scriptEngine instanceof Invocable) {
			Invocable invoke = (Invocable) scriptEngine;
			Object result = invoke.invokeFunction("Atest", 23);
//			Object object = scriptEngine.get("Btest");
//			Object result = invoke.invokeMethod(object, null);
			System.out.println(result);
		}
		
		/*
		ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        // 打印全局变量 "x"
        engine.put("x", "hello word!!");
        engine.eval("print(x);");
        // 上面的代码会打印"hello word！！"
        
        // 现在，传入另一个不同的script context
        ScriptContext context = new SimpleScriptContext();
        //新的Script context绑定ScriptContext的ENGINE_SCOPE
        Bindings bindings = context.getBindings(ScriptContext.ENGINE_SCOPE);
        
        // 增加一个新变脸到新的范围 engineScope 中
        bindings.put("x", "word hello!!");
        // 执行同一个脚本 - 但这次传入一个不同的script context
        engine.eval("print(x);", bindings);
        engine.eval("print(x);");
		*/
	}
	
}
