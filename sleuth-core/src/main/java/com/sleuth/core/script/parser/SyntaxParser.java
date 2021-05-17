package com.sleuth.core.script.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;

import com.sleuth.core.script.Fnbody;
import com.sleuth.core.script.Include;

/** 语法解析
 * 
 * @author Jonse
 * @date 2020年12月11日
 */
public class SyntaxParser extends ScriptSyntax {
	
	static final Pattern pattern = Pattern.compile("\"(.*?)\"");
	
	private final String[] lines;
	private final String source;
	
	public SyntaxParser(String source) {
		this.source = source;
		this.lines = this.slipt(source);
	}

	/** 获取函数信息
	 * 
	 * @return
	 */
	public Fnbody getFnbody() {
		Fnbody fnbody = new Fnbody();
		fnbody.setName(this.fetchName(this.lines));
		fnbody.setVersion(this.fetchVersion(this.lines));
		fnbody.setBody(this.fetchBody(this.lines));
		return fnbody;
	}
	
	/** 获取导入的函数
	 * 
	 * @return
	 */
	public Include[] getIncludes() {
		String[] includes = this.fetchIncludes(this.lines);
		if (includes != null && includes.length > 0) {
			Include[] list = {};
			for (String include : includes) {
				Matcher matcher = pattern.matcher(include);
				if (matcher.find()) {
					//引用函数名称
					String object = matcher.group().replace("\"", "");
					//变量名
					String alias = include.split("as")[1].replace(";", "").trim();
					list = ArrayUtils.add(list, new Include(object, alias));
				}
			}
			return list;
		}
		return null;
	}

	/** 脚本源代码
	 * 
	 * @return
	 */
	public String getSource() {
		return source;
	}
	
}
