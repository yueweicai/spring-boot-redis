package com.orilore.controller;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.orilore.util.RedisDAO;

/**
 * 用户控制器
 * @author yue
 */
@RestController
public class UserCtrl {
	@Resource
	private RedisDAO redis;
	/**
	 * 用户登录方法
	 * @param name
	 * @param pwd
	 * @param request
	 * @param response
	 * @return boolean
	 */
	@RequestMapping("/login.do")
	public boolean login(String name,String pwd,HttpServletRequest request,HttpServletResponse response){
		if(name.equals(pwd)){
			HttpSession session = request.getSession();
			String id = session.getId();
			//将会话ID存入redis数据库
			redis.insert(id, name);
			//将会话ID通过响应头传输给客户端
			response.setHeader("token", id);
			return true;
		}else{
			return false;
		}
	}
}
