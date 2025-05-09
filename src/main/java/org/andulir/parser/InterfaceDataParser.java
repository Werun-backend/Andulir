package org.andulir.parser;

import lombok.extern.slf4j.Slf4j;
import org.andulir.annotation.ATest;
import org.andulir.exception.AndulirSystemException;
import org.andulir.utils.TypeUtils;
import org.andulir.utils.XMLUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Set;
@Component
@Slf4j
public class InterfaceDataParser {
    @Autowired
    private DataParser basicDataParser;
    @Autowired
    private DataParser listDataParser;
    @Autowired
    private DataParser requestDataParser;
    @Autowired
    private Document document;
    @Autowired
    private File file;

    private Element root;

    //这个方法的作用是扫描带有注解的方法,并获取其参数列表
    public void conversionInterfaceInformation(String scanPackage) {

        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(RestController.class));

        Set<BeanDefinition> beanDefinitions = provider.findCandidateComponents(scanPackage);
        for (BeanDefinition beanDefinition : beanDefinitions) {
            Class<?> clazz = null;
            try {
                clazz = Class.forName(beanDefinition.getBeanClassName());
            } catch (ClassNotFoundException e) {
                throw new AndulirSystemException("扫描注册类" + clazz + "异常！" + e.getMessage());
            }
            Element controllerMapping = XMLUtils.addControllerMapping(root, clazz.getName());
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                // 处理加了注解的方法
                if (method.isAnnotationPresent(ATest.class)) {
                    ATest annotation = method.getAnnotation(ATest.class);
                    Element methodMapping = XMLUtils.addMethodMapping(controllerMapping, method.getName(), Integer.toString(annotation.value()));
                    try {
                        getMethodParameterTypesAndGenerateXML(method,methodMapping);
                    } catch (ClassNotFoundException e) {
                        throw new AndulirSystemException(clazz + "类中" + method.getName() + "方法的参数列表解析和生成xml文件异常！" + e.getMessage());
                    }
                    log.info("搜索到ATest注解方法[{}]，接口参数解析完成。",method.getName());
                }
            }
        }
    }

    //这个方法的作用是获取方法的参数类型列表并且生成xml
    public void getMethodParameterTypesAndGenerateXML(Method method, Element methodMapping) throws ClassNotFoundException {
        Element parameterMapping = XMLUtils.addParameterMapping(methodMapping);
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            //如果是基本数据类型
            if (TypeUtils.isBasicType(parameter)) {
                basicDataParser.parseData(parameter.getType(), parameterMapping);
                XMLUtils.writeXML(document,file);
            } else if (List.class.isAssignableFrom(parameter.getType())) { //如果是list类型
                listDataParser.parseData(parameter.getParameterizedType(), parameterMapping);
                XMLUtils.writeXML(document,file);
            } else { //如果是自定义请求类
                requestDataParser.parseData(parameter.getType(), parameterMapping);
                XMLUtils.writeXML(document,file);
            }
        }
    }

    public boolean initXML(){
        boolean isNewFile = !file.exists();
        root = XMLUtils.initXmlFile(document,file);
        return isNewFile; // 返回true表示文件是新建的
    }


}
