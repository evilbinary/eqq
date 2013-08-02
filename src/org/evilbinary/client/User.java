/*
 * Create By EvilBinary СE
 * 2011-10-20
 * rootntsd@gmail.com
 */
package org.evilbinary.client;

public class User {
	protected String flag;
	protected String uin;
	protected String categories;
	
	protected String markname;
	protected String vipLevel;
	protected String isVip;
	protected String nick;
	protected String face;
	//get_online_buddy
	protected String status;
	protected String clientType;
	//
	protected String province;
	protected String gender;
	protected String country;
	protected String city;
	
	//group mark info
	protected String card;
	protected String mflag;
	
	public User(){
		
	}
	public User(String flag,String uin,String categories){
		this.flag=flag;
		this.uin=uin;
		this.categories=categories;
	}
	public void print(){
		System.out.print("uin:"+uin);
		System.out.print(" nick:"+nick);
		System.out.print(" categories:"+categories);
		System.out.println(" markname:"+markname);
	}
	public void printDetail(){
		System.out.println("flag="+flag);
		System.out.println("uin="+uin);
		System.out.println("categories="+categories);
		System.out.println("markname="+markname);
		System.out.println("vipLevel="+vipLevel);
		System.out.println("isVip="+isVip);
		System.out.println("nick="+nick);
		System.out.println("face="+face);
		System.out.println("status="+status);
		System.out.println("clientType="+clientType);
		System.out.println("province="+province);
		System.out.println("gender="+gender);
		System.out.println("country="+country);
		System.out.println("city="+city);
		System.out.println("card="+card);
		System.out.println("mflag="+mflag);
	}
		
	public String getCard() {
		return card;
	}
	public void setCard(String card) {
		this.card = card;
	}
	public String getMflag() {
		return mflag;
	}
	public void setMflag(String mflag) {
		this.mflag = mflag;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getClientType() {
		return clientType;
	}
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getUin() {
		return uin;
	}

	public void setUin(String uin) {
		this.uin = uin;
	}

	public String getCategories() {
		return categories;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	public String getMarkname() {
		return markname;
	}

	public void setMarkname(String markname) {
		this.markname = markname;
	}

	public String getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(String vipLevel) {
		this.vipLevel = vipLevel;
	}

	public String getIsVip() {
		return isVip;
	}

	public void setIsVip(String isVip) {
		this.isVip = isVip;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}
	
}
