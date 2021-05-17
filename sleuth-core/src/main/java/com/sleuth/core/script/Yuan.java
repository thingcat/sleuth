package com.sleuth.core.script;

/** 交易元
 * 
 * @author Jonse
 * @date 2020年12月11日
 */
public class Yuan {
	
	private Fnbody fnbody;
	private Include[] includes;//导入的其他函数
	private String jsscript;//重构后的JS脚本，可执行
	private String source;//源代码
	
	public Fnbody getFnbody() {
		return fnbody;
	}
	public void setFnbody(Fnbody fnbody) {
		this.fnbody = fnbody;
	}
	public Include[] getIncludes() {
		return includes;
	}
	public void setIncludes(Include[] includes) {
		this.includes = includes;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getJsscript() {
		return jsscript;
	}
	public void setJsscript(String jsscript) {
		this.jsscript = jsscript;
	}
	
}
