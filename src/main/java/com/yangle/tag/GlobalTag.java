package com.yangle.tag;

import com.yangle.util.GuavaDataCache;
import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * Created by yangle on 2017/10/12.
 */

@Component
public class GlobalTag implements TemplateDirectiveModel{
    @Autowired
    private GuavaDataCache guavaDataCache;
    @Override
    public void execute(Environment environment, Map map, TemplateModel[] templateModels, TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
    if(map.containsKey("key")&&map.get("key")!=null){
        String key= map.get("key").toString();
        String value= (String) guavaDataCache.query(key);
        DefaultObjectWrapperBuilder builder=new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);
        environment.setVariable(key,builder.build().wrap(value));
//        templateDirectiveBody.render(environment.getOut());
    }
    }
}
