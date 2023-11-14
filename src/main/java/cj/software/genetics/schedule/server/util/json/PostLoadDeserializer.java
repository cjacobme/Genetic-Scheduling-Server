package cj.software.genetics.schedule.server.util.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;

import java.io.IOException;

public class PostLoadDeserializer extends DelegatingDeserializer {

    private final transient BeanDescription beanDescription;

    public PostLoadDeserializer(JsonDeserializer<?> delegate, BeanDescription beanDescription) {
        super(delegate);
        this.beanDescription = beanDescription;
    }

    @Override
    protected JsonDeserializer<?> newDelegatingInstance(JsonDeserializer<?> newDelegatee) {
        return new PostLoadDeserializer(newDelegatee, beanDescription);
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        Object result = super.deserialize(p, ctxt);
        try {
            callAnnotatedMethods(result);
        } catch (Exception e) {
            throw new IOException(e);
        }
        return result;
    }

    private void callAnnotatedMethods(Object deserializedObject) throws Exception {
        AnnotatedClass annotatedClass = beanDescription.getClassInfo();
        Iterable<AnnotatedMethod> methods = annotatedClass.memberMethods();
        for (AnnotatedMethod method : methods) {
            if (method.hasAnnotation(PostLoad.class)) {
                method.callOn(deserializedObject);
            }
        }
    }
}
