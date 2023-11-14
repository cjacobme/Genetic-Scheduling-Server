package cj.software.util.spring;

import cj.software.util.json.PostLoadDeserializer;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanProducer {

    @Bean
    public ObjectMapper objectMapper() {
        SimpleModule simpleModule = new SimpleModule();
        BeanDeserializerModifier beanDeserializerModifier = new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDescription,
                                                          JsonDeserializer<?> originalDeserializer) {
                return new PostLoadDeserializer(originalDeserializer, beanDescription);
            }
        };
        simpleModule.setDeserializerModifier(beanDeserializerModifier);
        ObjectMapper result = new ObjectMapper();
        result.registerModule(simpleModule);
        return result;
    }
}
