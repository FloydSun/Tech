package com.project.speed.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeUtil {
	static Pattern classPattern = Pattern.compile("\\s+class\\s+\\S+");   
	static Pattern interfacePattern = Pattern.compile("\\s+interface\\s+\\S+");   
	static Pattern implementsPattern = Pattern.compile("\\s+implements\\s+\\S+"); 
	static Pattern extendsPattern = Pattern.compile("\\s+extends\\s+\\S+"); 
	static Pattern transactionPattern = Pattern.compile("@Transactional\\s*\\(\\s*\"\\S+\"\\s*\\)"); 
	static Pattern tablePattern = Pattern.compile("@Table\\s*\\(\\s*name\\s*=\\s*\"\\S+\"\\)"); 
	static Pattern requestPattern = Pattern.compile("@RequestMapping\\s*\\(\\s*value\\s*=\\s*\"\\S+\"\\s*\\)");
	static Pattern packagePattern = Pattern.compile("package\\s+\\S+");
	static Pattern importPattern = Pattern.compile("import\\s+\\S+");
	static Pattern dbPattern = Pattern.compile("@PersistenceContext\\(unitName\\s*=\\s*\"\\S+\"\\)");
	static Pattern entityManagerPattern = Pattern.compile("EntityManager\\s+entityManager;");
	
	public static String setEntityManager(String src, String name){
		Matcher matcher = entityManagerPattern.matcher(src);
		if (matcher.find()){
			return matcher.replaceFirst(name);
		}
		return src;
	}
	
	public static String setClassName(String src, String name){
		src = src.replace("[className]", name);
		Matcher matcher = classPattern.matcher(src);
		if (matcher.find()){
			return matcher.replaceFirst(" class " + name);
		}
		return src;
	}
	
	public static String setImplementsName(String src, String name){
		Matcher matcher = implementsPattern.matcher(src);
		if (matcher.find()){
			return matcher.replaceFirst(" implements " + name);
		}
		return src;
	}
	
	public static String setExtendsName(String src, String name){
		Matcher matcher = extendsPattern.matcher(src);
		if (matcher.find()){
			return matcher.replaceFirst(" extends " + name);
		}else{
			matcher = classPattern.matcher(src);
			if (matcher.find()){
				String cls = matcher.group(0);
				return matcher.replaceFirst(cls + " extends " + name);
			}
			matcher = interfacePattern.matcher(src);
			if (matcher.find()){
				String cls = matcher.group(0);
				return matcher.replaceFirst(cls + " extends " + name);
			}
		}
		return src;
	}
	
	public static String setInterfaceName(String src, String name){
		Matcher matcher = interfacePattern.matcher(src);
		if (matcher.find()){
			return matcher.replaceFirst(" interface " + name);
		}
		return src;
	}
	
	public static String setTransactionName(String src, String name){
		Matcher matcher = transactionPattern.matcher(src);
		if (matcher.find()){
			return matcher.replaceFirst("@Transactional(\"" + name + "\")");
		}
		return src;
	}
	
	public static String setTableName(String src, String name){
		Matcher matcher = tablePattern.matcher(src);
		if (matcher.find()){
			return matcher.replaceFirst("@Table(name = \"" + name + "\")");
		}
		return src;
	}
	
	public static String setRequestName(String src, String name){
		Matcher matcher = requestPattern.matcher(src);
		if (matcher.find()){
			return matcher.replaceFirst("@RequestMapping(value = \"" + name + "\")");
		}
		return src;
	}
	
	public static String setPackageName(String src, String name){
		Matcher matcher = packagePattern.matcher(src);
		if (matcher.find()){
			return matcher.replaceFirst("package " + name + ";");
		}
		return src;
	}
	
	public static String setDBName(String src, String name){
		Matcher matcher = dbPattern.matcher(src);
		if (matcher.find()){
			return matcher.replaceFirst("@PersistenceContext(unitName = \"" + name + "\")");
		}
		return src;
	}
	
	
	public static String addImport(String src, String name){
		Pattern impPattern = Pattern.compile("import\\s+" + name); 
		if (!impPattern.matcher(src).find()){
			Matcher matcher = importPattern.matcher(src);
			if (matcher.find()){
				return src.substring(0, matcher.start()) + 
						"import " + name + ";\r\n" + 
						src.substring(matcher.start());
			}

			matcher = packagePattern.matcher(src);
			if (matcher.find()){
				return src.substring(0, matcher.end()) + 
						"\r\nimport " + name + ";\r\n" + 
						src.substring(matcher.end());
			}
			
		}
		return src;
	}

	public static String addMember(String src, String member){   
		Matcher matcher = classPattern.matcher(src);
		if (matcher.find()){
			int open = src.indexOf("{", matcher.start());
			return src.substring(0, open + 1) + 
					"\r\n" + member + "\r\n" + 
					src.substring(open + 1);
		}
		return src;
	}
}
