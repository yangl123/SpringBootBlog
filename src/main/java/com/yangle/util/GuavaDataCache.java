package com.yangle.util;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangle on 2017/10/11.
 */
@Component
public class GuavaDataCache {
    private Map<String,Object> datas=new HashMap();
    @PostConstruct
    public void init() {
        datas.put("a", "张三");
        datas.put("b", "李四");
        datas.put("c", "王五");
    }
    @Cacheable(value="sysCache" ,key="#id + 'datas'")
    public Object query(String id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(new Date()) + " : query id is " + id);
        return  datas.get(id);
    }
    @CachePut(value="sysCache" ,key="#id + 'datas'")
    public Object put(String id, Object value) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(new Date()) + " : add data ,id is "+ id);
        datas.put(id, value);
        // data persistence
        return value;
    }
    @CacheEvict(value="sysCache" , key="#id + 'datas'")
    public void remove(String id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(new Date()) + " : remove id is "+ id + " data");
        datas.remove(id);
        // data remove
    }
}
