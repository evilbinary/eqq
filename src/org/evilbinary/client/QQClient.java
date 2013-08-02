/*
 * Create By EvilBinary 小E
 * 2011-10-20
 * rootntsd@gmail.com
 */
package org.evilbinary.client;

import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.evilbinary.web.Native2Ascii;
import org.evilbinary.web.Web;
import org.json.JSONException;

public class QQClient extends Thread {

	private String userId;

	private String userPassword;

	private Web web;

	private boolean needVerify;

	private String verifycode;

	private String verifycodehex;

	private String clientid;

	private String index;

	private String port;

	private String vfwebqq;

	private String psessionid;

	private List<String> cookies;

	private FileConfigure config;

	private Logger messageLog;

	private Logger debugLog;

	private Logger clientLog;

	private int buddyMessageId;

	private int groupMessageId;

	private Map<String, Group> groups; // gid,group

	private Map<String, User> friends; // uin,user

	private List<Category> categories;

	private Queue<Message> messages;

	private Map<String, String> session; // uin,sgid

	private boolean isFirst;

	private String appid;

	private String webqqUrl;

	private String proxyUrl;

	private String hostUrl;

	private String eName;

	private Random rnd;

	private boolean msgOn;

	private MessageParse parse;

	private MessageParser parser;
	public static String GID;
	public static String UID;
	
	public QQClient() {
		eName = "eqq";
		hostUrl = "http://d.web2.qq.com/";
		webqqUrl = "http://web.qq.com/";
		proxyUrl = "http://d.web2.qq.com/proxy.html?v=20110331002&callback=2";
		config = new FileConfigure();
		config.parseFile();
		messageLog = new Logger("message.log");
		debugLog = new Logger("debug.log");
		debugLog.setEcho(false);
		clientLog = new Logger("client.log");
		clientLog.setEcho(false);
		web = new Web();
		web.setAceeptCharset("gb2312");
		web.setReferer(proxyUrl);

		userId = "";
		userPassword = "";

		appid = "1003903";
		needVerify = false;
		clientid = "10792";
		Random r = new Random();
		buddyMessageId = (r.nextInt(1000) + 4000);
		groupMessageId = r.nextInt(1000000);
		// groupMessageId = 22820001;
		isFirst = true;
		groups = new HashMap<String, Group>();
		friends = new HashMap<String, User>();
		messages = new LinkedList<Message>();
		session = new HashMap<String, String>();
		categories = new ArrayList<Category>();
		rnd = new Random();
		msgOn = true;
		parse = new MessageParse(groups, friends, messages, session);
		parser = new MessageParser();
	}

	public QQClient(String uid, String pwd, String name) {
		this();
		hostUrl = "http://d.web2.qq.com/";
		webqqUrl = "http://web.qq.com/";
		proxyUrl = "http://d.web2.qq.com/proxy.html?v=20110331002&callback=2";
		config = new FileConfigure();
		config.parseFile();
		messageLog = new Logger("message"+uid+".log");
		debugLog = new Logger("debug.log");
		clientLog = new Logger("client.log");
		web = new Web();
		web.setAceeptCharset("gb2312");
		web.setReferer(proxyUrl);

		userId = uid;
		userPassword = pwd;

		appid = "1003903";
		needVerify = false;
		clientid = "10792";
		Random r = new Random();
		buddyMessageId = (r.nextInt(1000) + 4000);
		groupMessageId = r.nextInt(1000000);
		// groupMessageId = 22820001;
		isFirst = true;
		groups = new HashMap<String, Group>();
		friends = new HashMap<String, User>();
		messages = new LinkedList<Message>();
		session = new HashMap<String, String>();
		categories = new ArrayList<Category>();
		rnd = new Random();
		eName = name;
		clientLog.log("机器名是：" + eName);
		msgOn = true;
		parse = new MessageParse(groups, friends, messages, session);
		parser = new MessageParser();
	}
	public void checkProxy(){
		clientLog.log("检测代理情况.");
		String proxy=this.config.get("proxy");
		if("true".equals(proxy)){
			clientLog.log("启动代理.");
			String host=this.config.get("proxy_host");
			String port=this.config.get("proxy_port");
			String usr=this.config.get("proxy_username");
			String pwd=this.config.get("proxy_password");
			initProxy(host,port,usr,pwd);
		}else{
			clientLog.log("未启动代理.");
		}
	}
	private void initProxy(String host, String port, final String username,
			final String password) {
		Authenticator.setDefault(new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username,
						new String(password).toCharArray());
			}
		});
		System.setProperty("http.proxyPort",port);
		System.setProperty("http.proxyHost", host);
	}

	private String getMD5Verify(String number, String verify, String verifycodehex) {
		String code = "";
		QQMD5 md = new QQMD5();
		// code = md.md5(md.md5_3(number) + verify); //old version
		code = md.encodePassword(number, verify, verifycodehex);
		return code;
	}

	private void checkVerify(String str) {
		String regex = "ptui_checkVC\\('(.*?)','(.*?)','(.*?)'\\);";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		if (m.find()) {
			// System.out.println(m.group(0) + " " + m.group(1) + " " +
			// m.group(2)+" "+m.group(3));
			if ("0".equals(m.group(1))) {
				// System.out.println("不需要验证码");
				clientLog.log("不需要验证码.");
				verifycode = m.group(2);
				verifycodehex = m.group(3);
				needVerify = false;
			} else {
				// System.out.println("需要验证码");
				clientLog.log("需要验证码.");
				web.setStrUrl("http://captcha.qq.com/getimage");
				String[][] params = { { "uin", userId }, { "vc_type", "" } };
				web.setParameters(params);
				web.clearBuffer();
				web.setContentType("image/jpeg");
				web.getImage();
				web.saveImage("verify.jpg");
				needVerify = true;
				// System.out.println(web.getContentType() +
				// web.getWebContent());
			}
		} else {
			debugLog.warn("没找到匹配验证码.");
		}
	}

	private String getVerifyCode() {
		System.out.println("请输入验证码:");
		Scanner input = new Scanner(System.in);
		return input.nextLine();
	}

	private String getVerifySession(String cookie) {
		String s = "";
		try {
			String regex;
			if (!needVerify) {
				regex = "ptvfsession=(.*?);";
			} else {
				regex = "verifysession=(.*?);";
			}
			// System.out.println(regex);
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(cookie);

			if (m.find()) {
				// System.out.println(m.group(0) + " " + m.group(1));
				s = m.group(1);

			} else {
				// System.out.println("faile.");
				debugLog.warn("验证会话没找到匹配.");
			}
			return s;
		} catch (Exception e) {
			debugLog.fatal("验证会话匹配异常." + e.getMessage());
			return s;
		}
	}

	private void loginReslultProcess(String result) {
		try {
			String regex = "ptuiCB\\('(.*?)','(.*?)','(.*?)','0','(.*?)'\\);";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(result);
			if (m.find()) {
				// System.out.println(m.group(0) + " " + m.group(1));
				int code = Integer.parseInt(m.group(1));
				// System.out.println(m.group(4));

			} else {
				// System.out.println("loginReslultProcess faile.");
				debugLog.warn("登陆没找到验证匹配.");
			}
		} catch (Exception e) {
			debugLog.fatal("登陆验证匹配结果异常." + e.getMessage());

		}
	}

	private String getContentByRegex(String regex, String content) {
		String result = "";
		try {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(content);

			if (m.find()) {
				// System.out.println(m.group(0) + " " + m.group(1));
				result = m.group(1);

			} else {
				// System.out.println("loginReslultProcess faile.");
				debugLog.warn("内容匹配失败." + regex + " " + content);
			}
			return result;
		} catch (Exception e) {
			debugLog.fatal("内容匹配异常." + e.getMessage());
			return result;

		}

	}

	public boolean login() {
		try {
			clientLog.log("登陆检查.");
			String num = String.valueOf(Math.random());
			web.setStrUrl(config.get("check_url"));
			String params[][] = { { "uin", userId }, { "appid", appid }, { "r", num },
					{ "enable_qlogin", "0" } };
			web.setParameters(params);
			web.getWeb();
			String result = new String(web.getWebContent().getBytes(), "gb2312");
			checkVerify(web.getWebContent());
			clientLog.log("准备登陆.");
			web.setStrUrl(config.get("login_url"));
			if (needVerify) {
				verifycode = getVerifyCode();
			}
			userPassword = getMD5Verify(userPassword, verifycode, verifycodehex);
			debugLog.debug("verifycode=" + verifycode + " p=" + userPassword);
			String verifysession = getVerifySession(web.getCookies().get(0));
			String p[][] = {
					{ "u", userId },
					{ "p", userPassword },
					{ "verifycode", verifycode },
					{ "webqq_type", "10" },
					{ "remember_uin", "1" },
					{ "login2qq", "1" },
					{ "aid", "1003903" },
					{ "u1",
							"http%3A%2F%2Fweb2.qq.com%2Floginproxy.html%3Flogin2qq%3D1%26webqq_type%3D10" },
					{ "h", "1" }, { "ptredirect", "0" }, { "ptlang", "2052" }, { "from_ui", "1" },
					{ "pttype", "1" }, { "dumy", "" }, { "fp", "loginerroralert" },
					{ "action", "5-36-828019" }, { "mibao_css", "m_webqq" }, { "t", "2" },
					{ "g", "1 HTTP/1.1 " } };

			web.setReferer(hostUrl);
			web.setParameters(p);
			web.clearBuffer();
			web.getWeb(web.getCookies());
			loginReslultProcess(web.getWebContent());
			clientLog.log("开始登陆.");
			cookies = new ArrayList<String>(web.getCookies());
			web.setStrUrl("http://d.web2.qq.com/channel/login2");
			String ptwebqq = getContentByRegex("ptwebqq=(.*?);\t*", cookies.get(0));
			String ptcz = getContentByRegex("ptcz=(.*?);\t*", cookies.get(2));
			String cookiesstr = "";
			for (int i = 0; i < cookies.size(); i++) {
				cookiesstr += cookies.get(i);
			}
			String skey = getContentByRegex("skey=(.*?);\t*", cookiesstr);
			String v;
			if (!needVerify) {
				v = "verifysession=" + verifysession + ";";
			} else {
				v = "verifysession=" + verifysession + ";";
			}
			cookies.add(v);
			web.clearParamters();
			String postData = "{\"status\":\"online\",\"ptwebqq\":\"" + ptwebqq
					+ "\",\"passwd_sig\":\"\",\"clientid\":\"" + clientid
					+ "\",\"psessionid\":null}";
			postData = "r=" + URLEncoder.encode(postData) + "&clientid=" + clientid;
			web.setPostData(postData);
			web.clearBuffer();
			web.setContentType("application/x-www-form-urlencoded");
			web.postWeb(cookies);
			String content = web.getWebContent();
			psessionid = getContentByRegex("\"psessionid\":\"(.*?)\",*", content);
			vfwebqq = getContentByRegex("\"vfwebqq\":\"(.*?)\",*", content);
			port = getContentByRegex("\"port\":(.*?),", content);
			index = getContentByRegex("\"index\":(.*?),", content);
			// System.out.println("vfwebqq="+vfwebqq);
			cookies.add("vfwebqq=" + vfwebqq + ";");
			cookies.add("psessionid=" + psessionid + ";");
			cookies.add("port=" + port + ";");
			cookies.add("index=" + index + ";");
			clientLog.log("登陆成功.");
			return true;
		} catch (Exception e) {
			debugLog.debug("登陆异常." + e.getMessage());
			return false;
		}

	}

	public void getGroupNameListMask() {
		try {
			// System.out.println("getGroupNameListMask");
			clientLog.log("获取分组信息.");
			web.setStrUrl("http://s.web2.qq.com/api/get_group_name_list_mask2");
			String postData = config.get("get_group_name_list_mask");
			postData = postData.replaceAll("vfwebqqvalue", vfwebqq);
			// System.out.println(postData);
			// System.out.println(URLEncoder.encode(postData));
			postData = "r=" + URLEncoder.encode(postData);
			web.setContentType("application/x-www-form-urlencoded");
			web.setPostData(postData);
			web.clearBuffer();
			web.postWeb(cookies);
			parse.parseGroupNameListMask(web.getWebContent());
		} catch (Exception e) {
			debugLog.debug("分组列表异常." + e.getMessage());
		}
	}

	public void getGroupInfo(String gcodes) {
		clientLog.log("getGroupInfo");
		web.clearBuffer();
		web.clearParamters();
		web.setStrUrl("http://s.web2.qq.com/api/get_group_info");
		String[][] params = { { "gcode", "[" + gcodes + "]" }, { "retainKey", "memo,gcode" },
				{ "vfwebqq", vfwebqq }, { "t", "1321595171715" } };
		web.setParameters(params);
		web.getWeb(cookies);
		web.printWeb();

	}

	public void getGroupInfoExt(String gcode) {
		if ("".equals(gcode)) {
			gcode = findGroupGcodByName("FC原创游戏开发-进阶群");
		}
		// getGroupInfo(gcode);
		clientLog.log("getGroupInfoExt");
		web.clearBuffer();
		web.clearParamters();
		web.setStrUrl("http://s.web2.qq.com/api/get_group_info_ext2");
		String[][] params = { { "gcode", gcode }, { "vfwebqq", vfwebqq }, { "t", "1321595171715" } };
		web.setParameters(params);
		web.setReferer(this.proxyUrl);
		// web.printOpenGetUrl();
		web.getWeb(cookies);
		// System.out.println(web.getReferer());
		// web.printWeb();
		parse.parseGroupInfoExt(web.getWebContent(), findGroupGidByCode(gcode));
		// printGroupsUsers();

	}

	private void printCookies(List<String> c) {
		for (String s : c) {
			System.out.println(s);
		}
	}

	public void sendBuddyMessageContent(String uin, String message) {
		message = parse.genContent(message);
		sendBuddyMessage(uin, message);
	}

	public void sendBuddyMessage(String uin, String message) {
		try {
			// System.out.println("send_buddy_msg");
			clientLog.log("send_buddy_msg");
			web.setStrUrl("http://d.web2.qq.com/channel/send_buddy_msg2");
			String msg = "send_buddy_msg";
			String postData = config.get(msg);
			// String friendUin = findFriendUinByNick("小E");
			// String o = "friendUin=" + friendUin + " msgid=" + buddyMessageId;
			// System.out.println(o);
			postData = postData.replace("tovalue", uin);
			postData = postData.replace("contentvalue", message);
			String font = config.get("font_fixedsys");
			// System.out.println(font);
			postData = postData.replace("fontvalue", font);
			postData = postData.replace("msg_idvalue", String.valueOf(buddyMessageId));
			postData = postData.replace("clientidvalue", clientid);
			postData = postData.replace("psessionidvalue", psessionid);
			// System.out.println(postData);
			postData = "r=" + URLEncoder.encode(postData, "utf-8");

			web.setContentType("application/x-www-form-urlencoded");
			web.setPostData(postData);
			web.clearBuffer();
			web.postWeb(cookies);
			web.printWeb();
			buddyMessageId++;
			isFirst = false;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			debugLog.fatal(e.getMessage());
		}
	}

	public void sendGroupMessage() {
		try {
			// System.out.println("send group msg");
			clientLog.log("send group msg");
			String gid = findGroupGidByName("FC原创游戏开发-进阶群");
			sendGroupMessage(gid, groupMessageId + "嘎嘎");

		} catch (Exception e) {
			// System.out.println(e.getMessage());
			debugLog.fatal(e.getMessage());
		}
	}

	public void sendGroupMessage(String msg) {
		try {
			// System.out.println("send group msg");
			clientLog.log("send group msg");
			String gid = findGroupGidByName("FC原创游戏开发-进阶群");
			sendGroupMessage(gid, msg);

		} catch (Exception e) {
			// System.out.println(e.getMessage());
			debugLog.fatal(e.getMessage());
		}
	}

	public void sendGroupMessageContent(String gid, String msg) {
		msg = parse.genContent(msg);
		sendGroupMessage(gid, msg);
	}

	public void sendGroupMessage(String gid, String msg) {
		try {
			// System.out.println("send group msg");
			clientLog.log("send group msg");
			web.setReferer(proxyUrl);
			web.setStrUrl("http://d.web2.qq.com/channel/send_qun_msg2");
			web.clearBuffer();
			String postData = config.get("send_group_msg");
			postData = postData.replace("group_uinvalue", gid);
			postData = postData.replace("contentvalue", msg);
			String font = config.get("font_fixedsys");
			postData = postData.replace("fontvalue", font);
			postData = postData.replace("msg_idvalue", String.valueOf(groupMessageId));
			postData = postData.replace("clientidvalue", clientid);
			postData = postData.replace("psessionidvalue", psessionid);
			postData = "r=" + URLEncoder.encode(postData, "utf-8");
			// System.out.println(postData);
			web.setContentType("application/x-www-form-urlencoded");
			web.setPostData(postData);
			web.postWeb(cookies);
			web.printWeb();
			groupMessageId++;
			// printCookies(cookies);
			isFirst = false;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			debugLog.fatal(e.getMessage());
		}
	}

	public void getFriendInfo(String uin) {
		clientLog.log("获取好友信息.");
		web.setStrUrl("http://s.web2.qq.com/api/get_friend_info2");
		web.clearParamters();
		web.clearBuffer();
		String params[][] = { { "tuin", uin }, { "verifysession", "" }, { "code", "" },
				{ "vfwebqq", vfwebqq }, { "t", "1321262302730" } };
		web.setParameters(params);
		web.setContentType("text/html");
		// web.printOpenGetUrl();
		web.getWeb(cookies);
		// web.printWeb();
	}

	public void getMyInfo() {
		// System.out.println("getFriendInfo");
		getFriendInfo(userId);
	}

	public void getUserFriends() {
		try {
			clientLog.log("获取好友列表.");
			web.setStrUrl("http://s.web2.qq.com/api/get_user_friends2");
			web.clearBuffer();
			String postData = config.get("get_user_friend");
			// System.out.println(postData);
			// config.print();
			// web.printOpenPostUrl();
			postData = postData.replaceAll("vfwebqqvalue", vfwebqq);
			// System.out.println(URLEncoder.encode(postData));
			postData = "r=" + URLEncoder.encode(postData);
			web.setContentType("application/x-www-form-urlencoded");
			web.setPostData(postData);
			web.postWeb(cookies);
			//web.printWeb("GBK");
			// parse.parseUserFriends(web.getWebContent());
			String content = web.getWebContent();
			Message msg = parser.parseMessageResultObject(content);
			// System.out.println(msg.getResult());
			UserFriendMessage userFriendMsg = parser.parseUserFriends(msg.getResult());
			// System.out.println(userFriendMsg.friends);
			// System.out.println();
			// System.out.println(userFriendMsg.categories);
			// System.out.println();
			// System.out.println(userFriendMsg.info);
			// System.out.println();
			// System.out.println(userFriendMsg.markNames);
			// System.out.println();
			//			
			parser.parseFriends(userFriendMsg.friends, friends);
			parser.parseFriendCategories(userFriendMsg.categories, categories);
			parser.parseFriendsInfo(userFriendMsg.info, friends);
			parser.parseFriendsMarkName(userFriendMsg.markNames, friends);
			parser.parseFriendsVipInfo(userFriendMsg.vipinfo, friends);
			// printFriends();
		} catch (JSONException e) {
			debugLog.debug("获取好友列表JSON 解析异常." + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			debugLog.debug("获取好友列表异常." + e.getMessage());
		}

	}

	public void ping() {
		// System.out.println("ping");
		clientLog.log("ping");
		web.setStrUrl("http://ps.qq.com/kl/ping");
		web.clearBuffer();
		web.clearParamters();
		web.setParameters("sid=" + clientid);
		web.getWeb(cookies);
		// web.printWeb();
	}

	public void as() {
		// System.out.println("as");
		clientLog.log("as");
		web.setStrUrl("http://ps.qq.com/kl/as");
		web.clearBuffer();
		web.clearParamters();
		web.setParameters("sid=" + clientid + "&aid=50&s=1");
		web.getWeb(cookies);
		// web.printWeb();
	}

	public void getLog() {
		// System.out.println("getlog");
		clientLog.log("getlog");
		// web.setStrUrl("http://tj.qstatic.com/getlog");
		// web.clearBuffer();
		// web.clearParamters();
		// web.setParameters("app2=" + userId + "&aid=50&s=1");
		// web.getWeb(cookies);
		// web.printWeb();
	}

	public void getMessageTip() {
		// System.out.println("getMessageTip");
		clientLog.log("getMessageTip");
		web.setStrUrl("http://web.qq.com/web2/get_msg_tip");
		web.setReferer(webqqUrl);
		web.clearParamters();
		web.clearBuffer();
		web.setParameters("uin=&tp=1&id=0&retype=1&rc=685&lv=3&t=1321264584374");
		web.getWeb(cookies);
		// web.printWeb();
	}

	public void allowAndAdd(String account, String gid, String mname) {
		try {
			System.out.println("allowAndAdd");
			clientLog.log("allowAndAdd");
			web.setStrUrl("http://s.web2.qq.com/api/allow_and_add2");
			web.setReferer(webqqUrl);
			String msg = "allow_and_add";
			String postData = config.get(msg);
			postData = postData.replace("accountvalue", account);
			postData = postData.replace("gidvalue", gid);
			postData = postData.replace("mnamevalue", mname);
			postData = postData.replace("vfwebqqvalue", vfwebqq);
			postData = "r=" + URLEncoder.encode(postData, "utf-8");
			web.setContentType("application/x-www-form-urlencoded");
			web.setPostData(postData);
			web.clearBuffer();
			web.postWeb(cookies);
			// web.printWeb();
			System.out.print(postData);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getC2CMessageSig(String uin, String id) {
		System.out.println("getC2CMessageSig");
		clientLog.log("getC2CMessageSig");
		web.setStrUrl("http://d.web2.qq.com/channel/get_c2cmsg_sig2");
		web.setReferer(webqqUrl);
		web.clearParamters();
		web.clearBuffer();
		web.setParameters("id=" + id + "&to_uin=" + uin + "&service_type=0&clientid=" + clientid
				+ "&psessionid=" + psessionid);
		web.getWeb(cookies);
		// String s=Native2Ascii.ascii2Native(web.getWebContent());
		// web.printWeb();
		// System.out.println(s);
	}

	public byte[] getFace(String uin) {
		return getFace(uin, 0);
	}

	public byte[] getFace(String uin, int cache) {
		System.out.println("getFace");
		clientLog.log("getFace");
		web.setStrUrl("http://face1.qun.qq.com/cgi/svr/face/getface");
		web.setReferer(webqqUrl);
		web.clearParamters();
		web.clearBuffer();
		web.setParameters("cache=" + cache + "&type=1&fid=0&uin=" + uin + "&vfwebqq=" + vfwebqq);
		// web.printOpenGetUrl();
		web.clearImageBuffer();
		web.getImage(cookies);
		// web.saveImage("img/"+uin+".jpg");
		return web.getBuffer();
	}

	public void sendSessionMessage(String uin, String gsig, String message) {
		try {
			System.out.println("sendSessionMessage");
			clientLog.log("sendSessionMessage");
			web.setStrUrl("http://d.web2.qq.com/channel/get_c2cmsg_sig2");
			web.setReferer(webqqUrl);

			String msg = "send_session_msg";
			String postData = config.get(msg);
			postData = postData.replace("tovalue", uin);
			postData = postData.replace("group_sigvalue", gsig);
			postData = postData.replace("contentvalue", message);
			String font = config.get("font_fixedsys");
			postData = postData.replace("fontvalue", font);
			postData = postData.replace("msg_idvalue", String.valueOf(buddyMessageId));
			postData = postData.replace("clientidvalue", clientid);
			postData = postData.replace("psessionidvalue", psessionid);
			postData = "r=" + URLEncoder.encode(postData, "utf-8");
			web.setContentType("application/x-www-form-urlencoded");
			web.setPostData(postData);
			web.clearBuffer();
			web.postWeb(cookies);
			web.printWeb();
			System.out.print(postData);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getBingSearch(String search) {
		String result = "";
		List<String> results = new ArrayList<String>();
		String tmp;
		try {
			web.clearBuffer();
			web.clearParamters();
			web.setStrUrl("http://cn.bing.com/search");
			web.setParameters("q=" + search);
			web.setContentType("txt/html");
			web.getWeb();
			// web.printOpenGetUrl();
			String regex = "<p>(.*?)</p>";
			result = web.getWebContent();
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(result);
			while (m.find()) {
				// System.out.println(m.group(0) + " " + m.group(1));
				result = m.group(1);
				results.add(result);
			}
			// int count=0;
			// for (String s : results) {
			// if (s.indexOf("我") >= 0 || s.indexOf("你") >= 0||s.indexOf("你们")
			// >= 0||s.indexOf("人") >= 0) {
			// result = s;
			// count++;
			// }
			// }
			// if(count==0){
			// result=results.get(0);
			// }
			result = results.get(new Random().nextInt(results.size()));
			result = result.replaceAll("</?[^>]+>", "");
			result = result.replace("&nbsp;", "");
			result = result.replace("&gt;", "");
			result = result.replace("&quot;", "");
			result = result.replace("&lt;", "");
			result = result.replaceAll("&#[0-9]*;", "");
			result = result.replaceAll("最佳答案", "");
			result = result.replace(".", "");
			result = result.replace("\"", "‘");
			result = result.replace("'", "‘");
			result = result.replaceAll("\\s*|\t|\r|\n", "");
			// System.out.println(result);
			// System.out.println(result);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return result;

	}

	private void printGroupMessage(GroupMessage gmsg) {
		Group group = groups.get(gmsg.getFromUin());
		Map<String, User> users = group.getUsers();
		if (users.size() <= 0) {
			getGroupInfoExt(group.getCode());
		}
		User u = users.get(gmsg.getSendUin());
		String nick = "";
		if (u != null) {
			if (u.getCard() != null) {
				nick += u.getCard();
			}
			nick += u.getNick();
		}
		GID=group.getGid();
		System.out.println("#From Group: " + group.getName() + " gid: " + group.getGid());
		System.out.println("  " + nick + ": " + gmsg.getContent());
		messageLog.log("#From Group: " + group.getName() + " gid: " + group.getGid());
		messageLog.log("  " + nick + ": " + gmsg.getContent());
	}

	private void printBuddyMessage(BuddyMessage bm) {
		// bm.print();
		String name = friends.get(bm.getFromUin()).getNick();
		if (name == null) {
			name = "陌生人";
			name += " fromUin:" + bm.getFromUin();
		}
		UID=bm.getFromUin();
		System.out.println("#Message From: " + name + " fromUin: " + bm.getFromUin());
		System.out.println("  " + bm.getContent());
		messageLog.log("#Message From: " + name + " fromUin: " + bm.getFromUin());
		messageLog.log("  " + bm.getContent());
		// autoSendBuddyMessage(bm.getFromUin(), bm.getContent());
	}

	public void processMessage() {
		try {
			Message m = messages.poll();
			if (m == null) {
				System.out.println("message is null");
				return;
			}
			// m.print();
			Vector<String[]> result = parser.parseMessagePollType(m.getResult());
			for (String s[] : result) {
				// System.out.println(s[0]);
				// System.out.println(s[1]);
				if ("group_message".equals(s[0])) {
					GroupMessage gmsg = parser.parseGroupMessage(s[1]);
					printGroupMessage(gmsg);
				} else if ("buddies_status_change".equals(s[0])) {
					System.out.println(s[0]);
					System.out.println(s[1]);

				} else if ("message".equals(s[0])) {
					// System.out.println("#" + s[1]);
					BuddyMessage bm = parser.parseBuddyMessage(s[1]);
					printBuddyMessage(bm);

				} else if ("sess_message".equals(s[0])) {
					// System.out.println("#" + s[1]);
					SessionMessage sm = parser.parseSessionMessage(s[1]);
					printSessionMessage(sm);

				} else if ("system_message".equals(s[0])) {
					// System.out.println("#" + s[1]);
					SystemMessage sm = parser.parseSystemMessage(s[1]);
					if (("verify_required").equals(sm.getType())) {
						System.out.println("verify_required");
						System.out.println("验证消息：" + sm.getMessage());
						String gid = "0";
						String mname = "";
						this.allowAndAdd(sm.getAccount(), gid, mname);
					}

				} else if ("buddylist_change".equals(s[0])) {
					System.out.println("#" + s[1]);

				} else {
					System.out.println("type:" + s[0]);
					System.out.println("value:" + s[1]);

				}

			}
		} catch (JSONException e) {
			debugLog.debug("消息解析JSON异常." + e.getMessage());
		} catch (Exception e) {
			debugLog.debug("消息解析异常." + e.getMessage());
		}

	}

	private void printSessionMessage(SessionMessage sm) {
		String name = "";
		// getFriendInfo(sm.getFromUin());
		getSingleLongNick(sm.getRuin());
		System.out.println("#Message From: " + name + " 内容:" + sm.getContent());
		if (session.get(sm.getFromUin()) == null) {
			getC2CMessageSig(sm.getFromUin(), sm.getId());
			String ret[] = parse.parseReturnResult(web.getWebContent());
			// System.out.println(ret[0]);
			// System.out.println(ret[1]);
			String rr[] = parse.parseC2CMessage(ret[1]);
			System.out.println(rr[0]);
			System.out.println(rr[1]);
			System.out.println(rr[2]);
			session.put(sm.getFromUin(), rr[1]);
		}

	}

	public void test() {
		Message m = messages.poll();
		if (m == null) {
			System.out.println("message is null");
			return;
		}
		System.out.println("result:" + m.getResult());

		Vector<String[]> results = parse.parseMessages(m.getResult());
		System.out.println("lenth:" + results.size());
		for (int i = 0; i < results.size(); i++) {
			String result[] = results.get(i); // poll_type ,value
			System.out.println(result[0]);
			System.out.println(result[1]);

			if ("group_message".equals(result[0])) {
				GroupMessage gmsg = new GroupMessage();
				String r[] = parse.parseGroupMessage(result[1]);
				gmsg.setMessageId(r[0]);
				gmsg.setFromUin(r[1]);
				gmsg.setToUin(r[2]);
				gmsg.setMessageId2(r[3]);
				gmsg.setMessageType(r[4]);
				gmsg.setReplyIp(r[5]);
				gmsg.setGroupCode(r[6]);
				gmsg.setSendUin(r[7]);
				gmsg.setSeq(r[8]);
				gmsg.setTime(r[9]);
				gmsg.setInfoSeq(r[10]);
				gmsg.setContent(r[11]);
				String fc[] = parse.parseContent(gmsg.getContent());
				gmsg.setFont(fc[0]);
				gmsg.setContent(fc[1]);
				// System.out.println(gmsg.getContent());
				// GroupMessage gmsg = parseGroupMessage1(m.getValue());
				String msg = Native2Ascii.ascii2Native(gmsg.getContent());
				Group group = groups.get(gmsg.getFromUin());

				Map<String, User> users = group.getUsers();
				if (users.size() <= 0) {
					getGroupInfoExt(group.getCode());
				}
				User u = users.get(gmsg.getSendUin());
				String nick = "";
				if (u != null) {
					if (u.getCard() != null) {
						nick += u.getCard();
					}
					nick += u.getNick();
				}
				System.out.println("#From Group: " + group.getName() + " " + nick + ": " + msg);
				//				
				if (msg.indexOf(eName.toLowerCase()) >= 0 || msg.indexOf(eName.toUpperCase()) >= 0) {
					autoSendGroupMessage(gmsg.getFromUin(), msg);
				}

			} else if ("buddies_status_change".equals(result[0])) {
				System.out.println("buddies_status_change");
				System.out.println(result[1]);

			} else if ("message".equals(result[0])) {
				BuddyMessage bm = new BuddyMessage();
				// System.out.println("VV:"+m.getValue());
				String r[] = parse.parseBuddyMessage(result[1]);
				bm.setMessageId(r[0]);
				bm.setFromUin(r[1]);
				bm.setToUin(r[2]);
				bm.setMessageId2(r[3]);
				bm.setMessageType(r[4]);
				bm.setReplyIp(r[5]);
				bm.setTime(r[6]);
				bm.setContent(r[7]);
				String fc[] = parse.parseContent(bm.getContent());
				bm.setFont(fc[0]);
				bm.setContent(fc[1]);
				String msg = Native2Ascii.ascii2Native(bm.getContent());
				String name = friends.get(bm.getFromUin()).getNick();
				System.out.println("#Message From: " + name + " 内容:" + msg);
				autoSendBuddyMessage(bm.getFromUin(), msg);

			} else if ("sess_message".equals(result[0])) {
				SessionMessage sm = new SessionMessage();
				String r[] = parse.parseSessionMessage(result[1]);
				sm.setMessageId(r[0]);
				sm.setFromUin(r[1]);
				sm.setToUin(r[2]);
				sm.setMessageId2(r[3]);
				sm.setMessageType(r[4]);
				sm.setReplyIp(r[5]);
				sm.setTime(r[6]);
				sm.setId(r[7]);
				sm.setRuin(r[8]);
				sm.setServiceType(r[9]);
				sm.setFlags(r[10]);
				sm.setContent(r[11]);
				String fc[] = parse.parseContent(sm.getContent());
				sm.setContent(fc[1]);
				sm.setFont(fc[0]);
				String msg = Native2Ascii.ascii2Native(sm.getContent());
				String name = "";
				// getFriendInfo(sm.getFromUin());
				getSingleLongNick(sm.getRuin());
				System.out.println("#Message From: " + name + " 内容:" + msg);

				if (session.get(sm.getFromUin()) == null) {
					getC2CMessageSig(sm.getFromUin(), sm.getId());
					String ret[] = parse.parseReturnResult(web.getWebContent());
					// System.out.println(ret[0]);
					// System.out.println(ret[1]);
					String rr[] = parse.parseC2CMessage(ret[1]);
					System.out.println(rr[0]);
					System.out.println(rr[1]);
					System.out.println(rr[2]);
					session.put(sm.getFromUin(), r[1]);
				}
				sendSessionMessage(sm.getFromUin(), session.get(sm.getFromUin()), parse
						.genContent("tesgt"));
			} else if ("system_message".equals(result[0])) {
				System.out.println(result[0]);
				// System.out.println(m.getValue());
				String[] r = parse.parseSystemMessage(result[1]);
				// System.out.println(r[0]);
				// System.out.println(r[1]);
				// System.out.println(r[2]);
				// System.out.println(r[3]);
				if (r[1].equals("verify_required")) {
					System.out.println("verify_required");
					String[] vinfo = parse.parseVerifyRequired(r[3]);
					String account = vinfo[1];
					System.out.println("验证消息：" + vinfo[2]);
					String gid = "0";
					String mname = "";
					this.allowAndAdd(account, gid, mname);
				}

			} else if ("buddylist_change".equals(result[0])) {
				System.out.println(result[0]);

			} else {
				System.out.println("type:" + result[0]);
				System.out.println("value:" + result[1]);

			}

		}

	}

	private void autoSendBuddyMessage(String uin, String msg) {
		msg = msg.replace("小E", "");
		msg = msg.substring(1, msg.length() - 2);
		msg = msg.trim();
		sendBuddyMessageContent(uin, "嘎嘎");
		// System.out.println("send#" + msg);
		// if (msg.indexOf("你是") >= 0) {
		// sendBuddyMessageContent(uin, "我是" + eName + "机器人。");
		// } else if (msg.indexOf("好么") >= 0) {
		// sendBuddyMessageContent(uin, "不好。");
		// } else if (msg.length() <= 1) {
		// sendBuddyMessageContent(uin, ".......");
		// } else if (msg.indexOf("ee") >= 0) {
		// sendBuddyMessageContent(uin, "干嘛？");
		// } else if (msg.indexOf("蛋疼") >= 0) {
		// sendBuddyMessageContent(uin, "我比你疼。");
		// } else if (msg.indexOf("呃") >= 0) {
		// sendBuddyMessageContent(uin, "你好无像很聊。");
		// } else if (msg.indexOf("++") >= 0) {
		// sendBuddyMessageContent(uin, "。。。。");
		// } else if (msg.indexOf("几岁") >= 0) {
		// sendBuddyMessageContent(uin, "额，切，");
		// } else if (msg.indexOf("机器人") >= 0) {
		// sendBuddyMessageContent(uin, "你也是。");
		// } else if (msg.indexOf("hello") >= 0) {
		// sendBuddyMessageContent(uin, ".....");
		// } else if (msg.indexOf("主人") >= 0) {
		// sendBuddyMessageContent(uin, "我是自己的主人。");
		// } else if (msg.indexOf("吃了么") >= 0) {
		// sendBuddyMessageContent(uin, "吃了。");
		// } else if (msg.indexOf("webqq协议") >= 0) {
		// sendBuddyMessageContent(uin, config.get("send_buddy_msg"));
		// } else {
		// if (msg.indexOf("face") >= 0) {
		//
		// } else {
		// if (1 == 1) {
		// msg = msg.replaceAll("[.,。！？!?]", "");
		//
		// System.out.println("#Search: " + msg);
		//
		// msg = URLEncoder.encode("我" + msg);
		// String s = getBingSearch(msg);
		// if (s.indexOf("RTL:false") > 0) {
		// s = eName + "，出错拉";
		// }
		// System.out.println("#Result: " + s);
		// int i = 0;
		// if (s.length() > 10) {
		//
		// i = s.indexOf('。');
		// if (i <= 0) {
		// i = s.indexOf('.');
		// if (i <= 0) {
		// i = s.length();
		// }
		//
		// }
		//
		// s = s.substring(0, i);
		// s += "。";
		// }
		// s = s.replaceAll("[“’'\"]", "");
		// System.out.println(s);
		// String ss[] = s.split("。");
		// for (String txt : ss) {
		// if (txt.length() > 10) {
		// if (s.indexOf(",") >= 0) {
		// String sss[] = txt.split(",");
		// for (String t : sss) {
		// sendBuddyMessage(uin, genContent(t));
		// if (msgOn) {
		// System.out.println(t);
		// }
		// }
		// } else if (s.indexOf("，") >= 0) {
		// String sss[] = txt.split("，");
		// for (String t : sss) {
		// sendBuddyMessage(uin, genContent(t));
		// if (msgOn) {
		// System.out.println(t);
		// }
		// }
		// } else if (s.indexOf("、") >= 0) {
		// String sss[] = txt.split("、");
		// for (String t : sss) {
		// sendBuddyMessage(uin, genContent(t));
		// if (msgOn) {
		// System.out.println(t);
		// }
		// }
		// } else {
		// int sendsize = 8;
		// // for(int
		// // x=0;x<s.length();x+=sendsize){
		// sendBuddyMessage(uin, genContent(s.substring(0, sendsize)));
		// if (msgOn) {
		// System.out.println(s.substring(0, sendsize));
		// }
		// // sendsize=rnd.nextInt(10)+6;
		// // }
		// }
		// } else {
		// sendBuddyMessage(uin, genContent(txt));
		// if (msgOn) {
		// System.out.println(txt);
		// }
		// }
		//
		// }
		//
		// messageLog.log(s);
		// messageLog.log(msg);
		// }
		// }
		//
		// }

	}

	private void autoSendGroupMessage(String uin, String msg) {
		msg = msg.replace("小E", "");
		msg = msg.substring(1, msg.length() - 2);
		msg = msg.trim();
		if (msgOn) {
			System.out.println("#" + msg);
		}
		if (msg.indexOf("你是") >= 0) {
			sendGroupMessage(uin, "我是" + eName + "机器人。");
		} else if (msg.indexOf("好么") >= 0) {
			sendGroupMessage(uin, "不好。");
		} else if (msg.length() <= 1) {
			sendGroupMessage(uin, ".......");
		} else if (msg.indexOf("ee") >= 0) {
			sendGroupMessage(uin, "干嘛？");
		} else if (msg.indexOf("蛋疼") >= 0) {
			sendGroupMessage(uin, "我比你疼。");
		} else if (msg.indexOf("呃") >= 0) {
			sendGroupMessage(uin, "你好无像很聊。");
		} else if (msg.indexOf("++") >= 0) {
			sendGroupMessage(uin, "。。。。");
		} else if (msg.indexOf("几岁") >= 0) {
			sendGroupMessage(uin, "额，切，");
		} else if (msg.indexOf("机器人") >= 0) {
			sendGroupMessage(uin, "你也是。");
		} else if (msg.indexOf("hello") >= 0) {
			sendGroupMessage(uin, ".....");
		} else if (msg.indexOf("主人") >= 0) {
			sendGroupMessage(uin, "我是自己的主人。");
		} else if (msg.indexOf("吃了么") >= 0) {
			sendGroupMessage(uin, "吃了。");
		} else if (msg.indexOf("webqq协议") >= 0) {
			sendGroupMessage(uin, config.get("send_buddy_msg"));
		} else {
			if (msg.indexOf("face") >= 0) {

			} else {
				if (1 == 1 || groups.get(uin).getName().equals("FC原创游戏开发-进阶群")) {
					msg = msg.replaceAll("[.,。！？!?]", "");
					if (msgOn) {
						System.out.println("Search... " + msg);
					}
					msg = URLEncoder.encode("我" + msg);
					String s = getBingSearch(msg);
					if (s.indexOf("RTL:false") > 0) {
						s = eName + "，出错拉";
					}
					System.out.println("Result=" + s);

					int i = 0;
					if (s.length() > 10) {

						i = s.indexOf('。');
						if (i <= 0) {
							i = s.indexOf('.');
							if (i <= 0) {
								i = s.length();
							}

						}

						s = s.substring(0, i);
						s += "。";
					}
					s = s.replaceAll("[“’'\"]", "");
					System.out.println(s);
					String ss[] = s.split("。");
					for (String txt : ss) {
						if (txt.length() > 10) {
							if (s.indexOf(",") >= 0) {
								String sss[] = txt.split(",");
								for (String t : sss) {
									sendGroupMessage(uin, parse.genContent(t));
									if (msgOn) {
										System.out.println(t);
									}
								}
							} else if (s.indexOf("，") >= 0) {
								String sss[] = txt.split("，");
								for (String t : sss) {
									sendGroupMessage(uin, parse.genContent(t));
									if (msgOn) {
										System.out.println(t);
									}
								}
							} else if (s.indexOf("、") >= 0) {
								String sss[] = txt.split("、");
								for (String t : sss) {
									sendGroupMessage(uin, parse.genContent(t));
									if (msgOn) {
										System.out.println(t);
									}
								}
							} else {
								int sendsize = 8;
								// for(int
								// x=0;x<s.length();x+=sendsize){
								sendGroupMessage(uin, parse.genContent(s.substring(0, sendsize)));
								if (msgOn) {
									System.out.println(s.substring(0, sendsize));
								}
								// sendsize=rnd.nextInt(10)+6;
								// }
							}
						} else {
							sendGroupMessage(uin, parse.genContent(txt));
							if (msgOn) {
								System.out.println(txt);
							}
						}

					}

					messageLog.log(s);
					messageLog.log(msg);
				}
			}

		}

	}

	public void test1() {
		// String test = new String(config.get("test_msg"));
		// test = Native2Ascii.ascii2Native(test);
		// System.out.println(test);
		// clientLog.test();
		String uin = findFriendUinByNick("小E");
		System.out.println("uin=" + uin);
		sendBuddyMessage(uin, parse.genContent("嘎嘎") + "," + parse.genFace(111));
		uin = findGroupGidByName("webqq3.0研究");
		System.out.println("uin=" + uin);
		sendGroupMessage(uin, parse.genContent("嘎嘎E") + "," + parse.genFace(111));

	}

	public void init() {
		try {
			clientLog.log("初始化.");
			this.checkProxy();
			this.login();
			this.getMyInfo();
			this.getUserFriends();
			this.getGroupNameListMask();
			// this.getLog();
			this.getOnlineBuddies();
			this.getDiscuzListNew();

			// getRecentList();
			// getSingelLongNick
			this.getMessageTip();
			// this.sendBuddyMessage();
			// printCookies(cookies);
			// sendBuddyMessage();
			// sendGroupMessage();
			// getMessageTip();
		} catch (Exception e) {
			debugLog.fatal("初始化异常." + e.getMessage());
			System.exit(0);
		}
	}

	public void run() {
		try {
			while (true) {
				poll();
				processMessage();
				// sleep(4000);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			debugLog.fatal("线程异常." + e.getMessage());
		}
	}

	public void poll() {
		try {
			// System.out.println("poll");
			clientLog.log("拉取消息.");
			web.setStrUrl("http://d.web2.qq.com/channel/poll2");
			String postData = config.get("poll");
			postData = postData.replaceAll("clientidvalue", clientid);
			postData = postData.replaceAll("psessionidvalue", psessionid);
			// System.out.println(URLEncoder.encode(postData));
			postData = "r=" + URLEncoder.encode(postData);
			web.setContentType("application/x-www-form-urlencoded");
			web.setPostData(postData);
			web.clearBuffer();
			web.postWeb(cookies);
			String content = web.getWebContent();
			if (content.trim().length() > 0) {
				// System.out.println("parseMessageResult");
				// System.out.println("#" + content);
				// Message message = parser.parseMessageResultArray(content);
				// messages.add(message);
				messages.addAll(parser.parseMessagesResult(content));
			} else {
				Message message = new Message();
				messages.add(message);
			}
		} catch (Exception e) {
			debugLog.fatal("拉取消息异常." + e.getMessage());
			e.printStackTrace();
		}

	}

	public void logout() {
		// System.out.println("logOut");
		clientLog.log("注销.");
		web.setStrUrl("http://d.web2.qq.com/channel/logout2");
		web.clearParamters();
		web.clearBuffer();
		web.setParameters("ids=&clientid=" + clientid + "&psessionid=" + psessionid);
		web.getWeb(cookies);
		// web.printWeb();
		// String postData = config.get("log_out");
		// postData = postData.replaceAll("appidvalue", appid);
		// postData = postData.replaceAll("vfwebqqvalue", vfwebqq);
		// web.setReferer(webqqUrl);
		// web.setContentType("application/x-www-form-urlencoded");
		// web.setPostData(postData);
		// web.clearBuffer();
		// web.postWeb(cookies);
		// web.printWeb();
	}

	public void getDiscuzListNew() {
		clientLog.log("getDiscuzListNew");
		web.setStrUrl("http://d.web2.qq.com/channel/get_discu_list_new2");
		web.clearParamters();
		web.clearBuffer();
		web.setParameters("clientid=" + clientid + "&psessionid=" + psessionid);
		web.getWeb(cookies);
		// web.printWeb();
	}

	public void getSingleLongNick(String uin) {
		clientLog.log("getSingleLongNick");
		web.setStrUrl("http://s.web2.qq.com/api/get_single_long_nick2");
		web.clearParamters();
		web.clearBuffer();
		web.setParameters("tuin=" + uin + "&vfwebqq=" + vfwebqq);
		web.getWeb(cookies);
		// web.printWeb();
	}

	public void getOnlineBuddies() throws JSONException {
		clientLog.log("获取在线好友.");
		web.setStrUrl("http://d.web2.qq.com/channel/get_online_buddies2");
		web.clearParamters();
		web.clearBuffer();
		web.setParameters("clientid=" + clientid + "&psessionid=" + psessionid);
		web.getWeb(cookies);
		Message msg = parser.parseMessageResultArray(web.getWebContent());
		parser.parseOnlineBuddies(msg.getResult(), friends);
		// web.printWeb();
	}

	// parse xxx

	// private void parseMessage(String messages) {
	// System.out.println(messages);
	// String regex =
	// "\"retcode\":(.*?),\"result\":\\[\\{\"poll_type\":\"(.*?)\",\"value\":\\{(.*?)\\}\\]\\}";
	// Pattern p = Pattern.compile(regex);
	// Matcher m = p.matcher(messages);
	// while (m.find()) {
	// Message msg = new Message(m.group(1), m.group(2), m.group(3));
	// synchronized (messages) {
	// this.messages.offer(msg);
	// }
	// }
	// }

	private String findFriendUinByNick(String nick) {
		String uin = "";
		Set<Entry<String, User>> set = friends.entrySet();
		Iterator<Entry<String, User>> ite = set.iterator();
		while (ite.hasNext()) {
			Entry<String, User> en = ite.next();
			if (nick.equals(en.getValue().getNick())) {
				uin = en.getValue().getUin();
			}
		}
		return uin;
	}

	private String findGroupGidByName(String name) {
		String gid = "";
		Set<Entry<String, Group>> set = groups.entrySet();
		Iterator<Entry<String, Group>> ite = set.iterator();
		while (ite.hasNext()) {
			Entry<String, Group> en = ite.next();
			if (name.equals(en.getValue().getName())) {
				gid = en.getValue().getGid();
			}
		}
		return gid;
	}

	private String findGroupGidByCode(String code) {
		String gid = "";
		Set<Entry<String, Group>> set = groups.entrySet();
		Iterator<Entry<String, Group>> ite = set.iterator();
		while (ite.hasNext()) {
			Entry<String, Group> en = ite.next();
			if (code.equals(en.getValue().getCode())) {
				gid = en.getValue().getGid();
			}
		}
		return gid;
	}

	private String findGroupGcodByGid(String gid) {
		String gcode = "";
		Set<Entry<String, Group>> set = groups.entrySet();
		Iterator<Entry<String, Group>> ite = set.iterator();
		while (ite.hasNext()) {
			Entry<String, Group> en = ite.next();
			if (gid.equals(en.getValue().getGid())) {
				gcode = en.getValue().getCode();
			}
		}
		return gcode;
	}

	private String findGroupGcodByName(String name) {
		String gcode = "";
		Set<Entry<String, Group>> set = groups.entrySet();
		Iterator<Entry<String, Group>> ite = set.iterator();
		while (ite.hasNext()) {
			Entry<String, Group> en = ite.next();
			if (name.equals(en.getValue().getName())) {
				gcode = en.getValue().getCode();
			}
		}
		return gcode;
	}

	private Group findGroupByGid(String gid) {
		return groups.get(gid);
	}

	public void printGroups() {
		Set<Entry<String, Group>> set = groups.entrySet();
		Iterator<Entry<String, Group>> ite = set.iterator();
		while (ite.hasNext()) {
			Entry<String, Group> en = ite.next();
			en.getValue().print();
		}
	}

	public void printGroupsUsers() {
		Set<Entry<String, Group>> set = groups.entrySet();
		Iterator<Entry<String, Group>> ite = set.iterator();
		while (ite.hasNext()) {
			Entry<String, Group> en = ite.next();
			en.getValue().printGroupUsers();
		}
	}

	public void printFriends() {
		Set<Entry<String, User>> set = friends.entrySet();
		Iterator<Entry<String, User>> ite = set.iterator();
		while (ite.hasNext()) {
			Entry<String, User> en = ite.next();
			en.getValue().print();
		}
	}

	public void echo(String state) {
		if ("on".equals(state)) {
			this.msgOn = true;
			clientLog.setEcho(true);
		} else if ("off".equals(state)) {
			this.msgOn = false;
			clientLog.setEcho(false);
		}
	}

	public Map<String, Group> getGroups() {
		return groups;
	}

	public void setGroups(Map<String, Group> groups) {
		this.groups = groups;
	}

	public Map<String, User> getFriends() {
		return friends;
	}

	public void setFriends(Map<String, User> friends) {
		this.friends = friends;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
}
