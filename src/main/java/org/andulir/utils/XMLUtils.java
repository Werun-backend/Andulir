package org.andulir.utils;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
public class XMLUtils {

    /*  xml文件格式
<aTest>
    <controllerMapping name="com.andulir.controller.TestController">
        <methodMapping name="test" status="2">
            <parameterMapping>
                <typeMapping>
                    <name>java.util.List&lt;com.andulir.controller.User&gt;</name>
                    <genericsMapping>
                        <typeMapping>
                            <name>com.andulir.controller.User</name>
                            <attributeMapping>
                                <typeMapping>
                                    <name>java.lang.String</name>
                                </typeMapping>
                                <typeMapping>
                                    <name>java.lang.String</name>
                                </typeMapping>
                                <typeMapping>
                                    <name>com.andulir.controller.User1</name>
                                    <attributeMapping>
                                        <typeMapping>
                                            <name>java.lang.Integer</name>
                                        </typeMapping>
                                        <typeMapping>
                                            <name>java.util.List&lt;java.lang.Integer&gt;</name>
                                            <genericsMapping>
                                                <typeMapping>
                                                    <name>java.lang.Integer</name>
                                                </typeMapping>
                                            </genericsMapping>
                                        </typeMapping>
                                    </attributeMapping>
                                </typeMapping>
                                <typeMapping>
                                    <name>java.util.Date</name>
                                </typeMapping>
                            </attributeMapping>
                        </typeMapping>
                    </genericsMapping>
                    <value>[{"name":"unlpVPdpxl","password":"3gfFVrxRyT","user1":{"integer":482706871,"list":[1664289742,1695992765,83605263,2058302144,1405561780]},"date":"2024-04-01T12:09:42.196+00:00"},{"name":"BkSiai4vAq","password":"sPDSTaYqEE","user1":{"integer":730295237,"list":[1972469971,1385152103,347125236,2109886458,1090042104]},"date":"2024-04-01T12:09:42.197+00:00"},{"name":"tZ3qpifkfS","password":"0oz_OzEKGv","user1":{"integer":875145769,"list":[164758244,1201554584,1389985343,1623168781,1245175018]},"date":"2024-04-01T12:09:42.198+00:00"},{"name":"geLhwE5Ekn","password":"U85tbCF9hp","user1":{"integer":334017606,"list":[1617920538,1033903769,1584460213,923489401,1974727827]},"date":"2024-04-01T12:09:42.199+00:00"},{"name":"Rq4BbyDaJn","password":"xS5NRlbABF","user1":{"integer":870916813,"list":[1156802735,1258239336,1987564550,1086998438,625813767]},"date":"2024-04-01T12:09:42.200+00:00"}]</value>
                    <value>[{"name":"xy5oKPhlZn","password":"R8syTgRQOY","user1":{"integer":1393101300,"list":[1905690013,63475114,1729317625,1509695998,156541128]},"date":"2024-04-01T12:09:42.221+00:00"},{"name":"Yo9uKrys0p","password":"9jSmvRjV0S","user1":{"integer":1333699137,"list":[699448710,5893526,1985242388,1337884773,1335871702]},"date":"2024-04-01T12:09:42.222+00:00"},{"name":"3U38xWl5q_","password":"WtTCAIUDvd","user1":{"integer":1036265010,"list":[1137478221,927619360,5731438,464153964,313636219]},"date":"2024-04-01T12:09:42.222+00:00"},{"name":"SSkttTSzpt","password":"MMDG27JCBJ","user1":{"integer":410961796,"list":[520275809,1210589417,258177185,1277217926,566056391]},"date":"2024-04-01T12:09:42.223+00:00"},{"name":"oKt5oD98SA","password":"CfsZs_oTKe","user1":{"integer":1504153069,"list":[108960699,321205562,109433437,1164202919,1811251953]},"date":"2024-04-01T12:09:42.224+00:00"}]</value>
                </typeMapping>
                <typeMapping>
                    <name>java.lang.Integer</name>
                    <value>649693351</value>
                    <value>1359726955</value>
                </typeMapping>
            </parameterMapping>
        </methodMapping>
    </controllerMapping>
</aTest>
     */

    public static void addBasicValue(Element typeMapping,String value) {
        typeMapping.addElement("value").setText(value);
    }

    public static Element addAttributeMapping(Element typeMapping) {
        Element attributeMapping = typeMapping.addElement("attributeMapping");
        return attributeMapping;
    }

    public static Element addGenericsMapping(Element typeMapping) {
        Element genericsMapping = typeMapping.addElement("genericsMapping");
        return genericsMapping;
    }
    public static Element addGenericsMapping(Element typeMapping,String paramType) {
        Element genericsMapping = typeMapping.addElement("genericsMapping");
        Element typeMapping1 = addTypeMapping(genericsMapping, paramType);
        return typeMapping1;
    }
    public static Element addTypeMapping(Element parameterMapping,String paramType) {
        Element typeMapping = parameterMapping.addElement("typeMapping");
        typeMapping.addElement("name").setText(paramType);
        return typeMapping;
    }


    public static Element addParameterMapping(Element methodMapping) {
        Element parameterMapping = methodMapping.addElement("parameterMapping");
        return parameterMapping;
    }

    public static Element addMethodMapping(Element controller,String methodName,String methodStatus) {
        Element methodMapping = controller.addElement("methodMapping");
        methodMapping.addAttribute("name",methodName);
        methodMapping.addAttribute("status",methodStatus);
        return methodMapping;
    }

    public static Element addControllerMapping(Element root,String clazz) {
        Element controllerMapping = root.addElement("controllerMapping");
        controllerMapping.addAttribute("name",clazz);
        return controllerMapping;
    }
    public static Element initXmlFile(Document document,File file) {
        //若已存在xml文件则不创建
        if (file.exists()) {
            log.info("atest.xml文件已存在!");
            return null;
        }
        Element rootElement = document.addElement("aTest");
        writeXML(document,file);
        return rootElement;
    }
    //将一个 XML 文档写入到指定的文件
    public static void writeXML(Document document,File file) {
        OutputFormat of = OutputFormat.createPrettyPrint();
        of.setEncoding("UTF-8");

        try {
            XMLWriter writer = new XMLWriter(new FileOutputStream(file), of);
            writer.write(document);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
