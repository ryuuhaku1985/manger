package com.jdxl.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author CodeGen
 * @date 2019-08-14 16:42:21
 */
@TableName("sys_sms")
public class SysSmsEntity implements Serializable {


	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 
	 */
	private String mobile;
	/**
	 * 
	 */
	private String code;
	/**
	 * 
	 */
	private String content;
	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 0 - 成功
1 - 失败
	 */
	private Integer status;

	/**
	 * 设置：
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * 获取：
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * 设置：
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置：
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 获取：
	 */
	public String getContent() {
		return content;
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
	 * 设置：0 - 成功
1 - 失败
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：0 - 成功
1 - 失败
	 */
	public Integer getStatus() {
		return status;
	}
}
