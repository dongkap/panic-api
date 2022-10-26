package com.dongkap.common.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JsonUtils {

    ObjectMapper objectMapper;
    ObjectWriter objectWriter;

    public JsonUtils() {
        this.objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /**
     * Convert JSON formatted String Into Java POJO obj
     *
     * @param json Source string
     * @param cls  Class type of returned obj
     * @return Object Convert result from JSON formatted String to POJO obj
     * @throws IOException
	 */
    public <T> Object fromJson(String json, Class<T> cls) throws IOException {
        return objectMapper.readValue(json, cls);
    }

    public <T> Object fromMap(Map<String, Object> map, Class<T> cls) {
        return this.objectMapper.convertValue(map, cls);
    }

    @SuppressWarnings("unchecked")
    public <T> Object fromListMap(List<Map<String, Object>> list,
                                  @SuppressWarnings("rawtypes") TypeReference typeReference) {
        return this.objectMapper.convertValue(list, typeReference);
    }

    public <T> List<T> jsonToListOfObj(Class<?> typeKey, String json) {
        List<T> listo = null;
        try {
            listo = objectMapper.readValue(json,
                    TypeFactory.defaultInstance().constructCollectionType(List.class, typeKey));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return listo;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Map<String, Object> jsonToMapOfObject(String json) {
        Map<String, Object> mapto = new HashMap();
        try {
            mapto = objectMapper.readValue(json, new TypeReference<>() {
			});
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return mapto;
    }

    /**
     * Convert an POJO obj into JSON formatted String
     *
     * @param obj
     * @return {@link String} of json
     */
    public String objToJson(Object obj) {
        String json = "";
        try {
            json = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
        }
        return json;
    }

    @SuppressWarnings("rawtypes")
    public <T> List<T> castCollection(List srcList, Class<T> clas) {
        List<T> list = new ArrayList<T>();
        for (Object obj : srcList) {
            if (obj != null && clas.isAssignableFrom(obj.getClass()))
                list.add(clas.cast(obj));
        }
        return list;
    }

}