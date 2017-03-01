
package com.games.job.common.utils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
abstract public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    public static final String EMPTY_JSON_OBJECT = "{}";
    public static final String EMPTY_JSON_ARRAY = "[]";
    private static final JavaType javaTypeMapStringObject;
    private static final JavaType javaTypeMapStringMap;
    private static final JavaType javaTypeMapStringString;

    static {
        javaTypeMapStringObject = constructParametricType(Map.class, String.class, Object.class);
        javaTypeMapStringMap = constructParametricType(Map.class, String.class, Map.class);
        javaTypeMapStringString = constructParametricType(Map.class, String.class, String.class);
    }

    public static Map<String, Object> convertJsonObjectToMap(String request) {
        if (StringUtils.isEmpty(request)) {
            return Collections.emptyMap();
        }
        return fromJson(request);
    }

    public static boolean isEmptyJsonObject(String json) {
        return json != null && StringUtils.equals(json.trim(), EMPTY_JSON_OBJECT);
    }
    public static boolean isEmptyJsonArray(String json){
        return json != null && StringUtils.equals(json.trim(), EMPTY_JSON_ARRAY);
    }

    public static Map<String, Object> fromJson(String content) {
        return fromJson(content, javaTypeMapStringObject);
    }

    public static Map<String, Map> fromJsonToMapStringMap(String content) {
        return fromJson(content, javaTypeMapStringMap);
    }

    public static Map<String, String> fromJsonToMapStringString(String content) {
        return fromJson(content, javaTypeMapStringString);
    }

    public static <K, V> Map<K, V> fromJsonToMap(String content, Class<K> keyType, Class<V> valueType) {
        JavaType javaType = constructParametricType(Map.class, keyType, valueType);
        return fromJson(content, javaType);
    }

    public static <T> List<T> fromJsonToList(String content, Class<T> listType) {
        return fromJson(content, constructParametricType(List.class, listType));
    }

    public static <T> T fromJson(String content, Class<T> valueType) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        try {
            ObjectMapper mapper = JsonObjectMapper.OBJECT_MAPPER;
            return mapper.readValue(content, valueType);
        } catch (Exception ex) {
            return null;
        }
    }

    public static <T> T fromJson(String content, JavaType valueType) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        try {
            ObjectMapper mapper = JsonObjectMapper.OBJECT_MAPPER;
            return mapper.readValue(content, valueType);
        } catch (Exception ex) {
            return null;
        }
    }

    public static String toJsonPretty(Object value) {
        try {
            ObjectMapper mapper = JsonObjectMapper.OBJECT_MAPPER;
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (Exception ex) {
            logger.error("toJsonPretty failed: " + ex.getMessage(), ex);
            return null;
        }
    }

    public static String toJson(Object value) {
        try {
            ObjectMapper mapper = JsonObjectMapper.OBJECT_MAPPER;
            return mapper.writeValueAsString(value);
        } catch (Exception ex) {
            logger.error("toJson failed: " + ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * Convert a pojo object to map. If map object passed in, do nothing.
     *
     * @param obj object to be converted
     * @return map object, return null if null obj specified or in case of any error.
     * @since 1.17.1
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> safeConvertObjectToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Map) {
            return (Map) obj;
        }
        try {
            ObjectMapper mapper = JsonObjectMapper.OBJECT_MAPPER;
            return mapper.convertValue(obj, javaTypeMapStringObject);
        } catch (Exception ex) {
            logger.error("Failed to convert {} to java.util.Map, return null", obj.getClass().getName());
            return null;
        }
    }

    /**
     * Convert map to a pojo object.
     *
     * @param map  map to be converted
     * @param type value type
     * @param <T>  value type
     * @return pojo object
     * @since 1.17.1
     */
    @SuppressWarnings("unchecked")
    public static <T> T safeConvertMapToObject(Map<String, Object> map, Class<T> type) {
        if (type == null) {
            logger.error("Target type is required, return null");
            return null;
        }
        if (map == null) {
            return null;
        }
        if (type == map.getClass()) {
            return (T) map;
        }
        try {
            ObjectMapper mapper = JsonObjectMapper.OBJECT_MAPPER;
            return mapper.convertValue(map, type);
        } catch (Exception ex) {
            logger.error("Failed to convert java.util.Map to {}, return null", type.getName());
            return null;
        }
    }

    private static JavaType constructParametricType(Class<?> parametrized, Class<?>... parameterClasses) {
        ObjectMapper mapper = JsonObjectMapper.OBJECT_MAPPER;
        return mapper.getTypeFactory().constructParametrizedType(parametrized, parametrized, parameterClasses);
    }
}
