package org.andulir.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.andulir.exception.AndulirSystemException;
import org.andulir.utils.TypeUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.jemos.podam.api.PodamFactory;

@Component
@Slf4j
public class BasicDataGenerator implements DataGenerator {
    @Autowired
    private PodamFactory podamFactory;
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public String generateRandomData(String typeName, Element typeMapping) throws ClassNotFoundException, JsonProcessingException {
        try {
            typeName = TypeUtils.switchToPackageClass(typeName);
            Class<?> clazz = Class.forName(typeName);
            Object value = podamFactory.manufacturePojo(clazz);
            return objectMapper.writeValueAsString(value);
        } catch (ClassNotFoundException e) {
            log.error("未找到类: {}", typeName, e);
            throw new AndulirSystemException("未找到类: " + typeName + e.getMessage());
        } catch (JsonProcessingException e) {
            log.error("处理类型为：{} JSON失败", typeName, e);
            throw new AndulirSystemException("处理JSON失败: " + typeName + e.getMessage());
        } catch (Exception e) {
            log.error("产生类型为: {} 的随机数据时失败", typeName, e);
            throw new AndulirSystemException("产生随机数据时失败： " + e.getMessage());
        }
    }
}
