package org.andulir.parser;

import org.andulir.utils.XMLUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Type;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BasicDataParserTest {

    private BasicDataParser basicDataParser;
    private Element testElement;

    @BeforeEach
    void setUp() {
        basicDataParser = new BasicDataParser();
        // 创建一个测试用的 XML Element
        Document document = DocumentHelper.createDocument();
        testElement = document.addElement("root");
    }

    @Test
    void testParseData() {
        // 创建一个测试用的 Type
        Type testType = String.class;

        // 使用 Mockito 来模拟静态方法调用
        try (MockedStatic<XMLUtils> xmlUtils = Mockito.mockStatic(XMLUtils.class)) {
            // 执行测试方法
            basicDataParser.parseData(testType, testElement);

            // 验证 XMLUtils.addTypeMapping 是否被正确调用
            xmlUtils.verify(() ->
                            XMLUtils.addTypeMapping(
                                    eq(testElement),
                                    eq(testType.getTypeName())
                            ),
                    times(1)
            );
        }
    }

    @Test
    void testParseDataWithNullType() {
        // 测试传入 null 类型的情况
        try (MockedStatic<XMLUtils> xmlUtils = Mockito.mockStatic(XMLUtils.class)) {
            basicDataParser.parseData(null, testElement);

            // 验证当类型为 null 时的行为
            xmlUtils.verify(() ->
                            XMLUtils.addTypeMapping(
                                    any(Element.class),
                                    eq(null)
                            ),
                    times(1)
            );
        }
    }

    @Test
    void testParseDataWithNullElement() {
        // 测试传入 null element 的情况
        Type testType = String.class;

        try (MockedStatic<XMLUtils> xmlUtils = Mockito.mockStatic(XMLUtils.class)) {
            basicDataParser.parseData(testType, null);

            // 验证当 element 为 null 时的行为
            xmlUtils.verify(() ->
                            XMLUtils.addTypeMapping(
                                    eq(null),
                                    eq(testType.getTypeName())
                            ),
                    times(1)
            );
        }
    }
}