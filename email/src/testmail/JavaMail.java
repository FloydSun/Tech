package testmail;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class JavaMail {
	public void SendEmailTest() {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.163.com");
		props.put("mail.smtp.auth", "true"); // 允许smtp校验
		Session sendMailSession = Session.getInstance(props, null);

		try {
			Transport transport = sendMailSession.getTransport("smtp");
			// 连接你的QQ，注意用户名和密码必须填写正确，否则权限得不到
			transport.connect("smtp.163.com", "sun.sunshine@163.com",
					"4827122712248");
			Message newMessage = new MimeMessage(sendMailSession);

			// 设置mail主题
			String mail_subject = "更改邮件发送人测试";
			newMessage.setSubject(mail_subject);

			// 设置发信人地址
			// String strFrom = "sun.sunshine@163.com";
			// strFrom = new String(strFrom.getBytes(), "iso-8859-1");
			Address addrFrom = new InternetAddress("sun.sunshine@163.com");
			newMessage.setFrom(addrFrom);
			// Address addressFrom[] = { new
			// InternetAddress("617334015@qq.com"),new
			// InternetAddress("goodnight0002@163.com") };
			// 改变发件人地址
			// newMessage.addFrom(addressFrom);
			// 设置收件人地址
			Address addressTo[] = { new InternetAddress("sun.sunshine@163.com") };
			newMessage.setRecipients(Message.RecipientType.TO, addressTo);

			// 设置mail正文
			newMessage.setSentDate(new java.util.Date());
			String mail_text = "java实现邮件发送！";

			// 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
			Multipart multipart = new MimeMultipart();

			// 添加邮件正文
			BodyPart contentPart = new MimeBodyPart();
			//contentPart.setText("附件传输test");
			
			String html = "<html><head>" +
			"<meta http-equiv=\"X-UA-Compatible\" content=\"IE=8\">" +
			"<title>测试</title>" +
			"<body>" +
				"<div style=\"text-align: center;\">" +
					"<div class=\"header\">" +
						"<div>" +
							"<table>" +
								"<tbody><tr>" +
									"<td style=\"padding-top: 0px\"><img src=\"cid:image\" width=\"100%\" border=\"0\" height=\"130px\"></td>" +
								"</tr>" +
								"<tr>" +
									"<td style=\"padding-top: 0px\"><img src=\"cid:image2\" width=\"100%\" border=\"0\" height=\"130px\"></td>" +
								"</tr>" +
							"</tbody></table>" +
						"</div>" +
					"</div>" +
					"<div style=\"height: 80px;\"></div>" +
					"<div class=\"title\" style=\"text-align: center;\">" +
						"<h1 style=\"color: #170A69; font-size: 300%\">" +
							"<b>测试</b>" +
						"</h1>" +
					"</div>" +
			"</body></html>";
			
			contentPart.setContent(html, "text/html;charset=UTF-8");
			multipart.addBodyPart(contentPart);

			
			BodyPart messageBodyPart = new MimeBodyPart();
	        DataSource fds = new FileDataSource
	          ("D:\\login_logo.png");
	        messageBodyPart.setDataHandler(new DataHandler(fds));
	        messageBodyPart.setHeader("Content-ID","<image>");

	        // add it
	        multipart.addBodyPart(messageBodyPart);
			
	        messageBodyPart = new MimeBodyPart();
	        fds = new FileDataSource
	          ("D:\\login_logo - 副本.png");
	        messageBodyPart.setDataHandler(new DataHandler(fds));
	        messageBodyPart.setHeader("Content-ID","<image2>");

	        // add it
	        multipart.addBodyPart(messageBodyPart);
			
			
			// 添加附件的内容
			File attachment = new File("D://aaaa.xls");
			BodyPart attachmentBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(attachment);
			attachmentBodyPart.setDataHandler(new DataHandler(source));

			// 网上流传的解决文件名乱码的方法，其实用MimeUtility.encodeWord就可以很方便的搞定
			// 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
			// sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
			// messageBodyPart.setFileName("=?GBK?B?" +
			// enc.encode(attachment.getName().getBytes()) + "?=");

			// MimeUtility.encodeWord可以避免文件名乱码
			attachmentBodyPart.setFileName(MimeUtility.encodeWord(attachment
					.getName()));
			multipart.addBodyPart(attachmentBodyPart);

			// newMessage.setText(mail_text);
			newMessage.setContent(multipart);
			newMessage.saveChanges(); // 保存发送信息
			transport.sendMessage(newMessage,
					newMessage.getRecipients(Message.RecipientType.TO)); // 发送邮件

			transport.close();
			
			System.out.println("发送成功!");
		} catch (Exception e) {
			System.out.println("发送失败！");
			System.out.println(e);
		}

	}

	public static void main(String args[]) throws Exception {
		JavaMail SEmail = new JavaMail();
		SEmail.SendEmailTest();
	}
}