package org.andulir.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.andulir.entity.ParameterizedTypeImpl;
import org.andulir.exception.AndulirSystemException;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.jemos.podam.api.PodamFactory;

import java.lang.reflect.Type;


@Component
@Slf4j
public class ListDataGenerator implements DataGenerator {
    @Autowired
    private PodamFactory podamFactory;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String generateRandomData(String typeName, Element typeMapping){
        try {
            Class<?> listClass = null;
            String className = null;
            Type genericsType = null;
            int count = 0;
            listClass = Class.forName("java.util.List");
            className = typeName.substring(typeName.indexOf('<') + 1, typeName.length() - 1);
            while (className.contains("java.util.List")) {
                count++;
                className = className.substring(className.indexOf('<') + 1, className.length() - 1);
            }

            genericsType = Class.forName(className);
            for (int i = 0; i < count; i++) {
                genericsType = new ParameterizedTypeImpl(new Type[]{genericsType}, null, listClass);
            }
            Object value = podamFactory.manufacturePojo(listClass, genericsType);
            return objectMapper.writeValueAsString(value);
        } catch (ClassNotFoundException e) {
            log.error("未找到类: {}", typeName, e);
            throw new AndulirSystemException("未找到类: " + typeName + e.getMessage());
        } catch (JsonProcessingException e) {
            log.error("处理类型为: {} 的JSON失败", typeName, e);
            throw new AndulirSystemException("处理JSON失败，类型为：" + typeName + e.getMessage());
        } catch (Exception e) {
            log.error("产生类型为: {} 的随机数据时失败", typeName, e);
            throw new AndulirSystemException("产生随机数据时失败" + e.getMessage());
        }
    }
}
