package org.evilbinary.client;
import java.io.Console;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	/**
	 * @param args
	 */
	public static String getFormhash(String contents) {
		String myRegex = "<input\\s*type=\"hidden\"\\s*name=\"formhash\"\\s*value=\"(.*?)\"\\s*\\/>";
		System.out.println("Reg:" + myRegex);
		Pattern p = Pattern.compile(myRegex);

		Matcher m = p.matcher(contents);
		if (m.find()) {
			int count = m.groupCount();
			System.out.println("Count:" + count + " success.");
			System.out.println(m.group(0) + " formhash:" + m.group(1));
			return m.group(1);

		} else {
			System.out.println("faile.");
		}
		return "";

	}

	

	public static int executeCommand(QQClient client, String cmd) {
		int code = 0;
		try {

			if ("lg".equals(cmd)) {
				client.printGroups();
			} else if ("echo on".equals(cmd)) {
				client.echo("on");
				System.out.println("on");

			} else if ("echo off".equals(cmd)) {
				System.out.println("off");
				client.echo("off");
			} else if ("lf".equals(cmd)) {
				client.printFriends();
			} else if ("lgu".equals(cmd)) {
				client.printGroupsUsers();
			} else if ("pause".equals(cmd)) {
				// client.printFriends();
				client.stop();
			} else if ("ping".equals(cmd)) {
				client.ping();
			} else if ("poll".equals(cmd)) {
				client.poll();
			} else if ("resume".equals(cmd)) {
				client.resume();
			} else if ("f".equals(cmd)) {
				String msg = new String(System.console().readLine("input msg:"));
				client.sendGroupMessage(msg);
			} else if ("sg".equals(cmd)) {

				// String gid = new
				// String(System.console().readLine("input gid:"));
				// String msg = new
				// String(System.console().readLine("input msg:"));
				String gid = new Scanner(System.in).nextLine();
				String msg = new Scanner(System.in).nextLine();
				client.sendGroupMessageContent(gid, msg);
			} else if ("sgu".equals(cmd)) {
				String gid = new Scanner(System.in).nextLine();
				String msg = new Scanner(System.in).nextLine();
				client.sendGroupMessage(gid, MessageParse.genFace(msg));
			} else if ("sf".equals(cmd)) {
				// String fid = new
				// String(System.console().readLine("input fid:"));
				// String msg = new
				// String(System.console().readLine("input msg:"));
				String fid = new Scanner(System.in).nextLine();
				String msg = new Scanner(System.in).nextLine();
				client.sendBuddyMessageContent(fid, msg);
			} else if ("tip".equals(cmd)) {
				client.getMessageTip();
			} else if ("test".equals(cmd)) {
				// String gcode = "";
				// client.getGroupInfoExt(gcode);
				client.test1();

			} else if ("quit".equals(cmd)) {
				client.logout();
				client.stop();
				// client.destroy();
				client.interrupted();

				code = -1;
			} else if ("rg".equals(cmd)) {
				String message = new Scanner(System.in).nextLine();
				if (client.GID != null)
					client.sendBuddyMessageContent(client.GID, message);
			} else if ("rf".equals(cmd)) {
				String message = new Scanner(System.in).nextLine();
				if (client.UID != null)
					client.sendBuddyMessageContent(client.UID, message);
			}
			return code;
		} catch (Exception ex) {
			ex.printStackTrace();
			return code;
			
		}
		
	}

	public static void consol(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: java -jar eqq.jar -u帐号 -p");
		} else if (args.length == 2) {
			if (args[0].indexOf("-u") >= 0 && args[1].indexOf("-p") >= 0) {
				String qqnumber = args[0].substring(2, args[0].length());
				Console console = System.console();
				if (console == null) {
					System.out.println("不可获得控制台。");
					QQClient qq = new QQClient("QQ账号", "密码", "ee");
					qq.echo("on");
					qq.init();
					qq.echo("off");
					qq.start();
					String name = new String(console.readLine("机器名：ee"));
					int code = 0;
					while (code != -1) {
						System.out.print("eqq$");
						String cmd = new String(console.readLine());
						code = executeCommand(qq, cmd);
					}
				} else {
					String qqpwd = new String(console.readPassword("密码:"));
					qqpwd.trim();
					String name = new String(console.readLine("机器名："));
					name = name.trim();
					QQClient qq = new QQClient(qqnumber, qqpwd, name);
					qq.echo("on");
					qq.init();
					qq.echo("off");
					qq.start();
					int code = 0;
					while (code != -1) {
						System.out.print("eqq$");
						String cmd = new String(console.readLine());
						code = executeCommand(qq, cmd);
					}
				}
			} else {

			}

		} else {
			System.out.println("exit.");
		}

	}

	public static void main(String[] args) {
		try {
			consol(args);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
