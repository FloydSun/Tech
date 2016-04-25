package com.project.speed.handler.server;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import com.project.speed.handler.Handler;
import com.project.speed.request.Request;
import com.project.speed.rule.NamingRule;
import com.project.speed.rule.OptionRule;
import com.project.speed.util.CodeUtil;
import com.project.speed.util.FileUtil;
import com.project.speed.util.SQLScriptUtil;
import com.project.speed.util.SQLScriptUtil.TableField;


//entity <-i | -n> <component Name> <table name>
public class EntityHandler extends Handler {

	static Pattern classPattern = Pattern.compile("(Entity)$");   
	
	private boolean validateRequest(Request req){
		boolean bRet = true;
		if (Request.ENTITY.equals(req.getType())){
			if (req.getArgs().size() >= 2){
				if (!NamingRule.validatePackage(req.getArgs().get(1))){
					System.out.println("class name 非法");
					bRet = false;
				}
				
				if (!NamingRule.validateClass(req.getArgs().get(2))){
					System.out.println("table name 非法");
					bRet = false;
				}
				
//				if (req.getArgs().size() >= 4){
//					File f = new File(req.getArgs().get(3));
//					if (!f.exists() || !f.isFile()){
//						System.out.println("非法脚本文件");
//						bRet = false;
//					}
//				}
				
			}else{
				System.out.println("参数过少");
				bRet = false;
			}
		}else{
			bRet = false;
		}
		return bRet;
	}
	
	@Override
	public boolean onHandle(Request req) {
		if (validateRequest(req)){
			

			
			String totalClassName = NamingRule.getEntityName(req.getArgs().get(1));
			String path = NamingRule.getEntityPath(req.getArgs().get(1)) + ".java";
			
			try {
				File file = new File(path);
				if (!file.exists()) {
					FileUtil.copyResFile("/res/entity.java", path);
				}
				
				String text = FileUtil.readText(path, "utf-8");
				text = CodeUtil.setTableName(text, req.getArgs().get(2));
				text = CodeUtil.setClassName(text, NamingRule.getClassName(totalClassName));
				text = CodeUtil.setPackageName(text, NamingRule.getPackageName(totalClassName));
				boolean isFrame = "-f".equals(req.getArgs().get(0));
				if (isFrame){
					text = CodeUtil.addImport(text,  OptionRule.getFramePackage() + ".model.entity.AbstractReadWriteEntity");
					text = CodeUtil.addImport(text, "javax.persistence.Id");
					text = CodeUtil.addImport(text, "javax.persistence.GeneratedValue");
					text = CodeUtil.addImport(text, "javax.persistence.GenerationType");
					text = CodeUtil.addImport(text, "javax.persistence.Column");
					text = CodeUtil.setExtendsName(text, "AbstractReadWriteEntity");
					text = CodeUtil.addMember(text, 
						"\t@Id\r\n"+
						"\t@GeneratedValue(strategy = GenerationType.AUTO)\r\n" +
						"\t@Column(name = \"id\")\r\n" +
						"\tpublic int getId() {\r\n" +
						"\t\treturn super.getId();\r\n" +
						"\t}");
					
					String daoPath = NamingRule.getDaoPath(req.getArgs().get(1)) + ".java";
					String textDao = FileUtil.readText(daoPath, "utf-8");
					textDao = CodeUtil.addImport(textDao, OptionRule.getFramePackage() + ".model.dao.AbstractReadWriteDao");
					textDao = CodeUtil.setExtendsName(textDao, "AbstractReadWriteDao<" + NamingRule.getClassName(totalClassName) + ">");
					textDao = CodeUtil.addImport(textDao, totalClassName);
					FileUtil.setText(daoPath, textDao, "utf-8");
					
					String daoPathImpl = NamingRule.getDaoPath(req.getArgs().get(1)) + "Impl.java";
					String textDaoImpl = FileUtil.readText(daoPathImpl, "utf-8");
					textDaoImpl = CodeUtil.addImport(textDaoImpl,  OptionRule.getFramePackage() + ".model.dao.AbstractReadWriteDaoImpl");
					textDaoImpl = CodeUtil.addImport(textDaoImpl, totalClassName);
					textDaoImpl = CodeUtil.setExtendsName(textDaoImpl, "AbstractReadWriteDaoImpl<" + NamingRule.getClassName(totalClassName) + ">");
					textDaoImpl = CodeUtil.setEntityManager(textDaoImpl, "public void setEntityManager(EntityManager entityManager) {\r\n" +
						"\t\tsuper.setEntityManager(entityManager);\r\n" +
					"\t}");
					FileUtil.setText(daoPathImpl, textDaoImpl, "utf-8");
				}
				
				
//				if (req.getArgs().size() >= 4){
//					String script = FileUtil.readText(req.getArgs().get(3), "utf-8");
//					List<TableField> fields = SQLScriptUtil.getTableFields(script, req.getArgs().get(2));
//					for (TableField fd : fields){
//						if (isFrame && "id".equals(fd.getName())){
//							
//						}else{
//							if ("int".equalsIgnoreCase(fd.getType())){
//								text = CodeUtil.addMember(text, 
//										"\tInteger " + fd.getName() + ";\r\n" +
//										"\tpublic Integer get" + fd.getName().substring(0, 1).toUpperCase() + fd.getName().substring(1) + "(){\r\n" +
//										"\t\t return " + fd.getName() + ";\r\n" +
//										"\t}\r\n" + 
//										"\tpublic Integer set" + fd.getName().substring(0, 1).toUpperCase() + fd.getName().substring(1) + "(Integer " + fd.getName() +"){\r\n" +
//										"\t\t this." + fd.getName() + " = " + fd.getName() + ";\r\n" +
//										"\t}");
//							}else if("numeric".equalsIgnoreCase(fd.getType())){
//								text = CodeUtil.addMember(text, 
//										"\tDouble " + fd.getName() + ";\r\n" +
//										"\tpublic Double get" + fd.getName().substring(0, 1).toUpperCase() + fd.getName().substring(1) + "(){\r\n" +
//										"\t\t return " + fd.getName() + ";\r\n" +
//										"\t}\r\n" + 
//										"\tpublic Double set" + fd.getName().substring(0, 1).toUpperCase() + fd.getName().substring(1) + "(Double " + fd.getName() +"){\r\n" +
//										"\t\t this." + fd.getName() + " = " + fd.getName() + ";\r\n" +
//										"\t}");
//							}else if("varchar".equalsIgnoreCase(fd.getType())){
//								text = CodeUtil.addMember(text, 
//										"\tInteger " + fd.getName() + ";\r\n" +
//										"\tpublic String get" + fd.getName().substring(0, 1).toUpperCase() + fd.getName().substring(1) + "(){\r\n" +
//										"\t\t return " + fd.getName() + ";\r\n" +
//										"\t}\r\n" + 
//										"\tpublic String set" + fd.getName().substring(0, 1).toUpperCase() + fd.getName().substring(1) + "(String " + fd.getName() +"){\r\n" +
//										"\t\t this." + fd.getName() + " = " + fd.getName() + ";\r\n" +
//										"\t}");
//							}
//						}
//					}
//				}
				
				FileUtil.setText(path, text, "utf-8");

			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

}
