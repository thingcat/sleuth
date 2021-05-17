package com.sleuth.core.script.sample;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.core.script.Yuan;
import com.sleuth.core.script.parser.ScriptRefactorer;
import com.sleuth.core.script.parser.SyntaxParser;

/** 脚本重构
 * 
 * @author Jonse
 * @date 2020年12月10日
 */
public class RefactorerTest {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	static Yuan user() throws IOException {
		byte[] bytes = Files.readAllBytes(Paths.get("G:\\workspace\\yuan\\User.yuan"));
		String script = new String(bytes);
		
		//构建脚本解释器
		SyntaxParser syntaxParser = new SyntaxParser(script);
		//构建脚本重构器
		ScriptRefactorer scriptRefactorer = new ScriptRefactorer(syntaxParser);
		if (syntaxParser.getIncludes() != null && syntaxParser.getIncludes().length > 0) {
			//导入其他的事件元
			//scriptRefactorer.include(yuan);
			
		}
		return scriptRefactorer.refactorer();
	}
	
	static Yuan sample(Yuan yuan) throws IOException {
		byte[] bytes = Files.readAllBytes(Paths.get("G:\\workspace\\yuan\\Sample.yuan"));
		String script = new String(bytes);
		
		//构建脚本解释器
		SyntaxParser syntaxParser = new SyntaxParser(script);
		//构建脚本重构器
		ScriptRefactorer scriptRefactorer = new ScriptRefactorer(syntaxParser);
		if (syntaxParser.getIncludes() != null && syntaxParser.getIncludes().length > 0) {
			//导入其他的事件元
			scriptRefactorer.include(yuan);
		}
		return scriptRefactorer.refactorer();
	}

	//脚本名称提取正则表达式
	static String args = "[\\s*|a-zA-Z|\\s*]+(\\,[\\s*|a-zA-Z|\\s*]+)*";
	static final String name_regex = "^function(\\s+)([a-zA-Z]*)(\\s*)\\("+args+"\\)(\\s*)\\{";
	static final Pattern name_regex_pattern = Pattern.compile(name_regex);
	
	public static void main(String[] args) throws IOException {
//		Yuan userYuan = user();
//		String jsText = userYuan.getJsscript();
//		System.out.println(jsText);
//		Matcher matcher = name_regex_pattern.matcher(jsText);
//		System.out.println(matcher.groupCount());
//		if (matcher.find()) {
//			System.out.println(matcher.group());
//		}
		
//		Yuan sampleYuan = sample(userYuan);
//		System.out.println(sampleYuan.getJsscript());
		
		StringBuffer json = new StringBuffer("[");
		List<String> lines = Files.readAllLines(Paths.get("G:\\sleep_datas.01-05.txt"));
		lines.forEach(line->{
			int begin = line.indexOf("2A01700");
			int end = line.lastIndexOf("0A") + 1;
			String obj = "\""+line.substring(begin, end) + "\"";
			json.append(obj).append(",");
		});
		json.deleteCharAt(json.length() - 1);
		json.append("]");
		System.out.println(json.toString());
		
	}
	
}
