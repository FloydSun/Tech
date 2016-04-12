package com.project.speed.handler;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import com.project.speed.request.Request;
import com.project.speed.rule.NamingRule;
import com.project.speed.rule.OptionRule;
import com.project.speed.util.CodeUtil;
import com.project.speed.util.FileUtil;


//entity <-i | -n> <component Name> <table name>
public class EntityHandler extends Handler {

	static Pattern classPattern = Pattern.compile("(Entity)$");   
	
	private boolean validateRequest(Request req){
		boolean bRet = true;
		if (Request.ENTITY.equals(req.getType())){
			if (req.getArgs().size() >= 3){
				if (!NamingRule.validatePackage(req.getArgs().get(1))){
					System.out.println("class name 非法");
					bRet = false;
				}
				
				if (!NamingRule.validateClass(req.getArgs().get(2))){
					System.out.println("table name 非法");
					bRet = false;
				}
				
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
				if ("-f".equals(req.getArgs().get(0))){
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
				FileUtil.setText(path, text, "utf-8");

			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

}
