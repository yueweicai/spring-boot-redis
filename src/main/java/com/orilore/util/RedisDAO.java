package com.orilore.util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
/**
 * Redis 数据访问对象
 * @author yue
 */
@Component
public class RedisDAO {
	//读取application.properties中redis配置属性
	@Value("${redis.host}")
	private String host;
	@Value("${redis.port}")
	private int port;
	@Value("${redis.timeout}")
	private int timeout;
	@Value("${redis.database}")
	private int database;
	
	//Jedis 初始化
	private Jedis redis;
	public void setRedis(){
		if(redis==null){
			this.redis = new Jedis(host,port);
			this.redis.select(database);
		}
	}
	
	/**
	 * 向redis数据库中添加键值对
	 * @param key
	 * @param value
	 */
	public void insert(String key,String value){
		this.setRedis();
		this.redis.set(key, value);
		this.redis.expire(key,timeout);
	}
	/**
	 * 从redis数据库中查询键值对
	 * @param key
	 * @return String
	 */
	public String select(String key){
		this.setRedis();
		if(this.redis.exists(key)){
			return this.redis.get(key);
		}else{
			return null;
		}
	}
	/**
	 * 更新redis数据库中键值对的有效时间
	 * @param key
	 */
	public void update(String key){
		this.setRedis();
		if(this.redis.exists(key)){
			if(this.redis.ttl(key)<timeout){
				this.redis.expire(key, timeout);
			}
		}
	}
		
}
