package com.sleuth.core.script.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;

import com.sleuth.core.script.exception.ScriptSyntaxException;

/** 脚本解析
 * 
 * @author Jonse
 * @date 2020年12月11日
 */
public class ScriptSyntax {
	
	//版本号提取正则表达式
	static final String ver_regex = "^version\\s+\\d+(\\.\\d+)*\\;";
	static final Pattern ver_regex_pattern = Pattern.compile(ver_regex);
	
	//脚本名称提取正则表达式
	static final String name_regex = "^yuan\\s+[a-zA-Z]*[\\s+\\{]";
	static final Pattern name_regex_pattern = Pattern.compile(name_regex);
	
	//include提取正则表达式
	static final String include_regex = "^include\\s+\"[a-zA-Z0-9]*\"\\s+(as)\\s+[a-zA-A0-9]*\\;";
	static final Pattern include_regex_pattern = Pattern.compile(include_regex);
	
	static final String KEYWORD_YUAN = "yuan";
	
	static final String EOF_DOUBLE_SLASH = "//";
	static final String EOF_SLASH_DOUBLE_STAR = "/**";
	static final String EOF_SLASH_STAR = "/*";
	static final String EOF_STAR_SLASH = "*/";
	
	private int yuan_index = -1;//yuan定义开始的行数索引
	
	/** 按换行符号分割
	 * 
	 * @param script
	 * @return
	 */
	protected String[] slipt(String script) {
		String[] lines = {};
		String[] textLines = script.split("\n");//根据换行符分割
		boolean isNotes = false;//是否行注释
		//剔除注释
		for (String line : textLines) {
			String code = line.trim();
			if (!isNotes) {
				//如果是以双斜杠开头
				if (code.indexOf(EOF_DOUBLE_SLASH) == 0) {
					continue;
				} else if (code.indexOf(EOF_SLASH_DOUBLE_STAR) == 0 || code.indexOf(EOF_SLASH_STAR) == 0) {
					isNotes = true;
					if (code.lastIndexOf(EOF_STAR_SLASH) >= 0) {
						isNotes = false;
					}
				} else {
					int m = line.indexOf("//");
					if (m > -1) {
						line = line.substring(0, m);
					}
					lines = ArrayUtils.add(lines, line);
					if (this.yuan_index == -1 && line.trim().indexOf(KEYWORD_YUAN) == 0) {
						this.yuan_index = lines.length - 1;
					}
				}
			} else {
				if (code.indexOf(EOF_STAR_SLASH) == 0) {
					isNotes = false;
				}
			}
		}
		return lines;
	}
	
	/** 提取版本号
	 * 
	 * @return String
	 */
	protected String fetchVersion(String[] lines) {
		for(String line : lines) {
			Matcher matcher = ver_regex_pattern.matcher(line);
			if (matcher.groupCount() > 1) {
				throw new ScriptSyntaxException("Duplicate 'version'");
			}
			if (matcher.find()) {
				return matcher.group();
			}
		}
		throw new ScriptSyntaxException("Keyword missing 'version'");
	}
	
	/** 获取脚本名称
	 * 
	 * @return
	 */
	protected String fetchName(String[] lines) {
		for(String line : lines) {
			Matcher matcher = name_regex_pattern.matcher(line);
			if (matcher.groupCount() > 1) {
				throw new ScriptSyntaxException("Duplicate 'yuan'");
			}
			if (matcher.find()) {
				return line.replace(KEYWORD_YUAN, "").replace("{", "").trim();
			}
		}
		throw new ScriptSyntaxException("Keyword missing 'yuan'");
	}

	/** 提取导入的其他include
	 * 
	 * @param script
	 * @return String[]
	 */
	protected String[] fetchIncludes(String[] lines) {
		String[] includes = {};
		for(String line : lines) {
			Matcher matcher = include_regex_pattern.matcher(line);
			if (matcher.find()) {
				includes = ArrayUtils.add(includes, matcher.group());
			}
		}
		return includes;
	}
	
	/** 提取body内容
	 * 
	 * @param lines
	 * @return
	 */
	protected String fetchBody(String[] lines) {
		StringBuilder bodyScript = new StringBuilder();
		for (String line : lines) {
			if (line != null && !"".equals(line.trim())) {
				bodyScript.append(line.trim()).append("\n");
			}
		}
		String body = bodyScript.toString();
		int begin = body.indexOf("{");
		int end = body.lastIndexOf("}");
		return body.substring(begin + 1, end);
	}
	
}
