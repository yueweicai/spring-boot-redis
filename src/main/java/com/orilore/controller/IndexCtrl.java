package com.orilore.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 首页控制器
 * @author yue
 */
@RestController
public class IndexCtrl {
		@RequestMapping("/index.do")
		public String index(){
			return "10000";
		}
		@RequestMapping("/query.do")
		public List<String> query(){
			List<String> list = new ArrayList<String>();
			list.add("AAA");
			list.add("BBB");
			list.add("CCC");
			return list;
		}
}

