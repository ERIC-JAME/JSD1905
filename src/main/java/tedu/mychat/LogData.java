package tedu.mychat;

public class LogData {
	private String account;
	private String password;
	private String nickName;
	private int age;
	private String mood;
	public LogData(String account, String password, String nickName, int age,String friends) {
		super();
		this.account = account;
		this.password = password;
		this.nickName = nickName;
		this.age = age;
		this.mood=mood;
	}
	
	public LogData(String[] s){
		this.account = s[0];
		this.password = s[1];
		this.nickName = s[2];
		this.age = Integer.parseInt(s[3]);
		this.mood=s[4];
	}
	
	public LogData(String s){
		String[] strs=s.split(",");
		this.account = strs[0];
		this.password = strs[1];
		this.nickName = strs[2];
		this.age = Integer.parseInt(strs[3]);
		this.mood=strs[4];
	}
	public String getMood() {
		return mood;
	}

	public void setMood(String mood) {
		this.mood = mood;
	}

	@Override
	public String toString() {
		return account + "," + password + ","
				+ nickName + "," +age+","+mood ;
	}
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}

	public boolean equals(Object o) {
		if(o==this){
			return true;
		}
		if(o==null){
			return false;
		}
		if(o instanceof LogData){
			LogData log=(LogData)o;
			if(log.account==this.account&&log.password==this.password){
				return true;
			}
		}
		return false;
	}
	
	
}