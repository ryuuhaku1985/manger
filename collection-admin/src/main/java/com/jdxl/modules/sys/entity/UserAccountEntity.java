package com.jdxl.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户账户表
 * 
 * @author CodeGen
 * @date 2018-09-21 15:24:19
 */
@TableName("tb_user_account")
public class UserAccountEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 商户ID
	 */
	private String mchId;
	/**
	 * 商户账户ID
	 */
	private String mchAccountId;
	/**
	 * 用户ID
	 */
	private String userId;
	/**
	 * 用户账户ID
	 */
	private String userAccountId;
	/**
	 * 余额
	 */
	private Long balance;
	/**
	 * 冻结金额
	 */
	private Long frozenAmount;
	/**
	 * 是否可用1可用0不可用
	 */
	private Integer userAccountStatus;
	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 
	 */
	private Date updateTime;

	/**
	 * 设置：
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：商户ID
	 */
	public void setMchId(String mchId) {
		this.mchId = mchId;
	}
	/**
	 * 获取：商户ID
	 */
	public String getMchId() {
		return mchId;
	}
	/**
	 * 设置：商户账户ID
	 */
	public void setMchAccountId(String mchAccountId) {
		this.mchAccountId = mchAccountId;
	}
	/**
	 * 获取：商户账户ID
	 */
	public String getMchAccountId() {
		return mchAccountId;
	}
	/**
	 * 设置：用户ID
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户ID
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * 设置：用户账户ID
	 */
	public void setUserAccountId(String userAccountId) {
		this.userAccountId = userAccountId;
	}
	/**
	 * 获取：用户账户ID
	 */
	public String getUserAccountId() {
		return userAccountId;
	}
	/**
	 * 设置：余额
	 */
	public void setBalance(Long balance) {
		this.balance = balance;
	}
	/**
	 * 获取：余额
	 */
	public Long getBalance() {
		return balance;
	}
	/**
	 * 设置：冻结金额
	 */
	public void setFrozenAmount(Long frozenAmount) {
		this.frozenAmount = frozenAmount;
	}
	/**
	 * 获取：冻结金额
	 */
	public Long getFrozenAmount() {
		return frozenAmount;
	}
	/**
	 * 设置：是否可用1可用0不可用
	 */
	public void setUserAccountStatus(Integer userAccountStatus) {
		this.userAccountStatus = userAccountStatus;
	}
	/**
	 * 获取：是否可用1可用0不可用
	 */
	public Integer getUserAccountStatus() {
		return userAccountStatus;
	}
	/**
	 * 设置：
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
}
