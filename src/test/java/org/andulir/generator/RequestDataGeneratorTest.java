package org.andulir.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RequestDataGeneratorTest {

    @Mock
    private PodamFactory podamFactory;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private RequestDataGenerator requestDataGenerator;

    private Element testElement;

    @BeforeEach
    void setUp() {
        // 创建测试用的 XML Element
        Document document = DocumentHelper.createDocument();
        testElement = document.addElement("root");
    }

    @Test
    void testGenerateRandomData_Success() throws Exception {
        // 准备测试数据
        String typeName = "java.lang.String";
        String mockValue = "test value";
        String expectedJson = "\"test value\"";

        // 配置 mock 行为
        when(podamFactory.manufacturePojo(String.class)).thenReturn(mockValue);
        when(objectMapper.writeValueAsString(mockValue)).thenReturn(expectedJson);

        // 执行测试
        String result = requestDataGenerator.generateRandomData(typeName, testElement);

        // 验证结果
        assertEquals(expectedJson, result);
        verify(podamFactory).manufacturePojo(String.class);
        verify(objectMapper).writeValueAsString(mockValue);
    }

    @Test
    void testGenerateRandomData_ComplexObject() throws Exception {
        // 准备测试数据
        String typeName = "org.andulir.model.TestModel";
        TestModel mockModel = new TestModel();
        String expectedJson = "{\"field\":\"value\"}";

        // 配置 mock 行为
        when(podamFactory.manufacturePojo(TestModel.class)).thenReturn(mockModel);
        when(objectMapper.writeValueAsString(mockModel)).thenReturn(expectedJson);

        // 执行测试
        String result = requestDataGenerator.generateRandomData(typeName, testElement);

        // 验证结果
        assertEquals(expectedJson, result);
        verify(podamFactory).manufacturePojo(TestModel.class);
        verify(objectMapper).writeValueAsString(mockModel);
    }

    @Test
    void testGenerateRandomData_ClassNotFoundException() {
        // 准备测试数据
        String invalidTypeName = "org.invalid.NonExistentClass";

        // 执行测试并验证异常
        assertThrows(ClassNotFoundException.class, () ->
                requestDataGenerator.generateRandomData(invalidTypeName, testElement)
        );

        // 验证 mock 方法没有被调用
        verify(podamFactory, never()).manufacturePojo(any());

    }

    @Test
    void testGenerateRandomData_JsonProcessingException() throws Exception {
        // 准备测试数据
        String typeName = "java.lang.String";
        String mockValue = "test value";

        // 配置 mock 行为
        when(podamFactory.manufacturePojo(String.class)).thenReturn(mockValue);
        when(objectMapper.writeValueAsString(mockValue))
                .thenThrow(new JsonProcessingException("JSON processing error") {});

        // 执行测试并验证异常
        assertThrows(JsonProcessingException.class, () ->
                requestDataGenerator.generateRandomData(typeName, testElement)
        );

        // 验证方法调用
        verify(podamFactory).manufacturePojo(String.class);
        verify(objectMapper).writeValueAsString(mockValue);
    }

    @Test
    void testGenerateRandomData_NullTypeName() {
        // 执行测试并验证异常
        assertThrows(NullPointerException.class, () ->
                requestDataGenerator.generateRandomData(null, testElement)
        );

        // 验证 mock 方法没有被调用
        verify(podamFactory, never()).manufacturePojo(any());

    }

    // 用于测试的内部类
    private static class TestModel {
        private String field;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }
    }
}