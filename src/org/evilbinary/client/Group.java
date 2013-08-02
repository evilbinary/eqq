package org.evilbinary.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class Group {
	// basic
	private String flag;
	private String name;
	private String gid;
	private String code;
	//
	private String ginfo;
	private String cards;
	private String mflag;
	private Map<String, User> users; // qqnumber,user

	public Group() {
		flag = "";
		name = "";
		gid = "";
		code = "";
		users = new HashMap<String, User>();
	}

	public Group(String flag, String name, String gid, String code) {
		this.gid = gid;
		this.name = name;
		this.code = code;
		this.flag = flag;
		users = new HashMap<String, User>();
	}

	public void putUser(String muin, User user) {
		users.put(muin, user);
	}

	public void printGroupUsers() {
		Set<Entry<String, User>> set = users.entrySet();
		Iterator<Entry<String, User>> ite = set.iterator();
		while (ite.hasNext()) {
			Entry<String, User> en = ite.next();
			en.getValue().print();
		}
	}

	public User getUser(String muin) {
		return users.get(muin);
	}

	public String getGinfo() {
		return ginfo;
	}

	public void setGinfo(String ginfo) {
		this.ginfo = ginfo;
	}

	public String getCards() {
		return cards;
	}

	public void setCards(String cards) {
		this.cards = cards;
	}

	public String getMflag() {
		return mflag;
	}

	public void setMflag(String mflag) {
		this.mflag = mflag;
	}

	public Map<String, User> getUsers() {
		return users;
	}

	public void setUsers(Map<String, User> users) {
		this.users = users;
	}

	public void print() {
		System.out.print("gid:" + gid);
		System.out.print(" name:" + name);
		System.out.print(" code:" + code);
		System.out.println(" flag:" + flag);
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
