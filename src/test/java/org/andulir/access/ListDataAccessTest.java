package org.andulir.access;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.andulir.entity.ParameterizedTypeImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListDataAccessTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ListDataAccess listDataAccess;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testConvertRandomData2Obj_SimpleList() throws Exception {
        // 准备测试数据
        String typeName = "java.util.List<java.lang.String>";
        String jsonValue = "[\"value1\", \"value2\"]";
        List<String> expectedList = Arrays.asList("value1", "value2");

        // 配置 mock 行为
        when(objectMapper.readValue(eq(jsonValue), any(TypeReference.class)))
                .thenReturn(expectedList);

        // 执行测试
        Object result = listDataAccess.convertRandomData2Obj(typeName, jsonValue);

        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof List);
        assertEquals(expectedList, result);
        verify(objectMapper).readValue(eq(jsonValue), any(TypeReference.class));
    }

    @Test
    void testConvertRandomData2Obj_NestedList() throws Exception {
        // 准备测试数据
        String typeName = "java.util.List<java.util.List<java.lang.Integer>>";
        String jsonValue = "[[1,2],[3,4]]";
        List<List<Integer>> expectedList = Arrays.asList(
                Arrays.asList(1, 2),
                Arrays.asList(3, 4)
        );

        // 配置 mock 行为
        when(objectMapper.readValue(eq(jsonValue), any(TypeReference.class)))
                .thenReturn(expectedList);

        // 执行测试
        Object result = listDataAccess.convertRandomData2Obj(typeName, jsonValue);

        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof List);
        assertEquals(expectedList, result);
        verify(objectMapper).readValue(eq(jsonValue), any(TypeReference.class));
    }

    @Test
    void testConvertRandomData2Obj_CustomObjectList() throws Exception {
        // 准备测试数据
        String typeName = "java.util.List<org.andulir.access.TestModel>";
        String jsonValue = "[{\"field\":\"value1\"},{\"field\":\"value2\"}]";
        List<TestModel> expectedList = Arrays.asList(
                new TestModel("value1"),
                new TestModel("value2")
        );

        // 配置 mock 行为
        when(objectMapper.readValue(eq(jsonValue), any(TypeReference.class)))
                .thenReturn(expectedList);

        // 执行测试
        Object result = listDataAccess.convertRandomData2Obj(typeName, jsonValue);

        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof List);
        assertEquals(expectedList, result);
        verify(objectMapper).readValue(eq(jsonValue), any(TypeReference.class));
    }

    @Test
    void testConvertRandomData2Obj_ClassNotFoundException() {
        // 准备测试数据
        String typeName = "java.util.List<org.nonexistent.Class>";
        String jsonValue = "[]";

        // 执行测试并验证异常
        assertThrows(ClassNotFoundException.class, () ->
                listDataAccess.convertRandomData2Obj(typeName, jsonValue)
        );


    }

    @Test
    void testConvertRandomData2Obj_JsonProcessingException() throws Exception {
        // 准备测试数据
        String typeName = "java.util.List<java.lang.String>";
        String invalidJson = "invalid json";

        // 配置 mock 行为
        when(objectMapper.readValue(eq(invalidJson), any(TypeReference.class)))
                .thenThrow(new JsonProcessingException("Invalid JSON") {});

        // 执行测试并验证异常
        assertThrows(JsonProcessingException.class, () ->
                listDataAccess.convertRandomData2Obj(typeName, invalidJson)
        );

        // 验证 objectMapper 被调用
        verify(objectMapper).readValue(eq(invalidJson), any(TypeReference.class));
    }

    @Test
    void testConvertRandomData2Obj_NullInputs() {
        // 测试 null typeName
        assertThrows(NullPointerException.class, () ->
                listDataAccess.convertRandomData2Obj(null, "[]")
        );

        // 测试 null value
        assertThrows(NullPointerException.class, () ->
                listDataAccess.convertRandomData2Obj("java.util.List<java.lang.String>", null)
        );
    }

    // 用于测试的内部类
    private static class TestModel {
        private String field;

        public TestModel(String field) {
            this.field = field;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            TestModel testModel = (TestModel) obj;
            return field.equals(testModel.field);
        }

        @Override
        public int hashCode() {
            return field.hashCode();
        }
    }
}