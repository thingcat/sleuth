package com.sleuth.core.script.parser;

import java.util.HashMap;
import java.util.Map;

import com.sleuth.core.script.Fnbody;
import com.sleuth.core.script.Include;
import com.sleuth.core.script.Yuan;
import com.sleuth.core.script.exception.ScriptSyntaxException;

/** 脚本重构
 * 
 * @author Jonse
 * @date 2020年12月11日
 */
public class ScriptRefactorer {
	
	static final String EOF_FN = "function ";
	static final String EOF_BRACES_LEFT = " {\n";
	static final String EOF_BRACES_RIGHT = "}\n";
	
	static final String EOF_LINE_BREAK = "\n";
	
	private final Map<String, Yuan> joinInclude = new HashMap<String, Yuan>();
	
	private final SyntaxParser syntaxParser;
	
	public ScriptRefactorer(SyntaxParser syntaxParser) {
		this.syntaxParser = syntaxParser;
	}
	
	/** 导入其他的yuan
	 * 
	 * @param yuan
	 */
	public void include(Yuan yuan) {
		String name = yuan.getFnbody().getName();
		this.joinInclude.put(name, yuan);
	}
	
	/** 开始重构
	 * 
	 * @return Yuan
	 */
	public Yuan refactorer() {
		
		Fnbody fnbody = this.syntaxParser.getFnbody();
		Include[] includes = this.syntaxParser.getIncludes();
		
		//构建JS脚本，需要加入的函数体
		StringBuilder joinFn = new StringBuilder();
		//重构变量
		StringBuilder joinVar = new StringBuilder();
		if (includes != null && includes.length > 0) {
			for (Include include : includes) {
				Yuan joinYuan = this.joinInclude.get(include.getName());
				if (joinYuan == null) {
					throw new ScriptSyntaxException("Undefined function '"+include.getName()+"'");
				}
				
				joinFn.append(EOF_FN);
				joinFn.append(joinYuan.getFnbody().getName());
				joinFn.append(EOF_BRACES_LEFT);
				joinFn.append(joinYuan.getJsscript());
				joinFn.append(EOF_BRACES_RIGHT);
				joinFn.append(EOF_LINE_BREAK);
				
				joinVar.append("var ").append(include.getAlias()).append(" = new ").append(include.getName()).append("();\n");
				
			}
		}
		
		StringBuilder jsscript = new StringBuilder();
		jsscript.append(joinFn);
		
		jsscript.append(EOF_LINE_BREAK);
		jsscript.append(joinVar);
		jsscript.append(fnbody.getBody());
		
		Yuan yuan = new Yuan();
		yuan.setFnbody(fnbody);
		yuan.setIncludes(includes);
		yuan.setJsscript(jsscript.toString().replace("\n", ""));
		yuan.setSource(this.syntaxParser.getSource());
		
		return yuan;
	}
	
}
