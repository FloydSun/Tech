package com.project.speed.handler;

import java.io.File;
import java.io.IOException;

import com.project.speed.request.Request;
import com.project.speed.rule.NamingRule;
import com.project.speed.rule.OptionRule;
import com.project.speed.util.CodeUtil;
import com.project.speed.util.FileUtil;


//entity <-i | -n> <class name> <table name>
public class EntityHandler extends Handler {

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
			String path = OptionRule.getBasePath() + "/" + req.getArgs().get(1).replace(".", "/") + ".java";
			
			try {
				File file = new File(path);
				if (!file.exists()) {
					FileUtil.copyResFile("/res/entity.java", path);
				}
				
				String text = FileUtil.readText(path, "utf-8");
				text = CodeUtil.setTableName(text, req.getArgs().get(2));
				text = CodeUtil.setClassName(text, NamingRule.getClassName(req.getArgs().get(1)));
				text = CodeUtil.setPackageName(text, NamingRule.getPackageName(req.getArgs().get(1)));
				if ("-i".equals(req.getArgs().get(0))){
					text = CodeUtil.setExtendsName(text, "AbstractReadWriteEntity");
					text = CodeUtil.addMember(text, "@Id\r\n@GeneratedValue(strategy = GenerationType.AUTO)\r\n@Column(name = \"id\")\r\npublic int getId() {\r\nreturn super.getId();\r\n}");
					String daoName = NamingRule.getDaoName(req.getArgs().get(1));
					String daoPath = OptionRule.getBasePath() + "/" + daoName.replace(".", "/") + ".java";
					String textDao = FileUtil.readText(daoPath, "utf-8");
					textDao = CodeUtil.setExtendsName(textDao, "AbstractReadWriteDao<" + NamingRule.getClassName(req.getArgs().get(1)) + ">");
					textDao = CodeUtil.addImport(textDao, req.getArgs().get(1));
					FileUtil.setText(daoPath, textDao, "utf-8");
					String daoPathImpl = OptionRule.getBasePath() + "/" + daoName.replace(".", "/") + "Impl.java";
					String textDaoImpl = FileUtil.readText(daoPathImpl, "utf-8");
					textDaoImpl = CodeUtil.addImport(textDaoImpl, req.getArgs().get(1));
					textDaoImpl = CodeUtil.setExtendsName(textDaoImpl, "AbstractReadWriteDaoImpl<" + NamingRule.getClassName(req.getArgs().get(1)) + ">");
					textDaoImpl = CodeUtil.setEntityManager(textDaoImpl, "public void setEntityManager(EntityManager entityManager) {\r\n" +
						"super.setEntityManager(entityManager);\r\n" +
					"}");
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
