package cn.com.flaginfo.support;


/**
 * 用户信息dto
 * @author Rain
 *
 */
public class UserInfo {
	
	private Long userId;
	private String userName;
	private String loginIp;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	@Override
	public String toString() {
		return "UserInfo [userId=" + userId + ", userName=" + userName + ", loginIp=" + loginIp + "]";
	}
 
}
