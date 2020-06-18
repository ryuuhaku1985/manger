package com.jdxl.modules.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.jdxl.common.exception.BizException;
import com.jdxl.common.message.SysMsgInfo;
import com.jdxl.common.result.Result;
import com.jdxl.common.utils.HttpServiceUtils;
import com.jdxl.modules.sys.entity.SysSmsEntity;
import com.jdxl.modules.sys.entity.SysUserEntity;
import com.jdxl.modules.sys.form.SysLoginForm;
import com.jdxl.modules.sys.service.SysCaptchaService;
import com.jdxl.modules.sys.service.SysSmsService;
import com.jdxl.modules.sys.service.SysUserService;
import com.jdxl.modules.sys.service.SysUserTokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

/**
 * 登录相关
 */
@RestController
@Slf4j
public class SysLoginController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserTokenService sysUserTokenService;
	@Autowired
	private SysCaptchaService sysCaptchaService;

	@Autowired
	private SysSmsService sysSmsService;

	public static final String cq_url = "";
	public static final String cq_cmd = "";

	/**
	 * 验证码
	 */
	@GetMapping("captcha.jpg")
	public void captcha(HttpServletResponse response, String uuid)throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");

		//获取图片验证码
		BufferedImage image = sysCaptchaService.getCaptcha(uuid);

		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
		IOUtils.closeQuietly(out);
	}

	/**
	 * 登录
	 */
	@PostMapping("/sys/login")
	public Result login(@RequestBody SysLoginForm form)throws IOException {

		//用户信息
		SysUserEntity user = sysUserService.queryByUserName(form.getUsername());
		Integer verifytype = user.getVerify();
		log.info("login verifytype"+verifytype);

		//账号不存在、密码错误
		if(user == null || !user.getPassword().equals(new Sha256Hash(form.getPassword(), user.getSalt()).toHex())) {
			throw new BizException(SysMsgInfo.SYS0018);
		}

		//账号锁定
		if(user.getStatus() == 0){
			throw new BizException(SysMsgInfo.SYS0019);
		}

		//生成token，并保存到数据库
		Map<String, Object> resp = sysUserTokenService.createToken(user.getUserId());
		log.info("verifytype"+verifytype);
//		if (verifytype == 0){
//			resp.put("verify",1);
//		}else {
			resp.put("verify",1);
//		}
		return Result.response(resp);
	}

	@PostMapping("/sys/loginverify")
	public Result loginverify(@RequestBody SysLoginForm form)throws IOException {
		SysUserEntity sysUserEntity = getUser();
		//用户信息
		SysUserEntity user = sysUserService.queryByUserName(form.getUsername());
		Map<String, Object> resp = sysUserTokenService.createToken(user.getUserId());

		//账号不存在、密码错误
		if(user == null || !user.getPassword().equals(new Sha256Hash(form.getPassword(), user.getSalt()).toHex())) {
			throw new BizException(SysMsgInfo.SYS0018);
		}

		//账号锁定
		if(user.getStatus() == 0){
			throw new BizException(SysMsgInfo.SYS0019);
		}
		log.info("loginverify user"+user.toString());
//		//用户信息
//		Integer verifytype = user.getVerify();
//		if (verifytype == 1) {
//			//只有当用户需要验证码登录的时候，才进行这一步操作
//			String code = form.getVerifycode();
//			Map<String,Object> map = new HashMap<>();
//			map.put("mobile",form.getMobile());
////			SysSmsEntity sysSmsEntity = sysSmsService.selectbymobile(map);
////			if (null==sysSmsEntity){
////				throw new BizException(SysMsgInfo.SYS0028);
////			}
////			if (!code.equals(sysSmsEntity.getCode())){
////				throw new BizException(SysMsgInfo.SYS0025);
////			}
//		}

		return Result.response(resp);
	}

	@PostMapping("/sys/loginsendverify")
	public Result loginsendverify(@RequestBody SysLoginForm form)throws IOException {
		//用户信息
		SysUserEntity user = sysUserService.queryByUserName(form.getUsername());
		if(!user.getMobile().equals(form.getMobile())){
			throw new BizException(SysMsgInfo.SYS0027);
		}
		String mobile = form.getMobile();
		Integer verify = new Random().nextInt(9999);

		String url = cq_url;
		Map<String,String> map = new HashedMap();
		map.put("cmd",cq_cmd);

		map.put("mobile",mobile);
		map.put("is_new","");
		map.put("gzip","0");

		map.put("code",verify.toString());
		String request = null;
		try {
			//发送post请求
			request = HttpServiceUtils.post(url, map, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject result = JSONObject.parseObject(request);
		log.info("loginsendverify result"+result.toString());
		//只有当code是1 的时候是成功 其他都认为是失败
		Integer  i = (Integer) result.get("code");
		if(!i.equals(0)){
			throw new BizException(SysMsgInfo.SYS0024);
		}else {
			//插入用户验证码表中
			SysSmsEntity sysSmsEntity = new SysSmsEntity();
			sysSmsEntity.setMobile(mobile);
			sysSmsEntity.setCode(verify.toString());
			sysSmsService.insert(sysSmsEntity);
		}

		return Result.response(map);
	}


	/**
	 * 退出
	 */
	@PostMapping("/sys/logout")
	public Result logout() {
		sysUserTokenService.logout(getUserId());
		return Result.response();
	}
	
}
