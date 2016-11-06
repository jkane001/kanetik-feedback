package io.rverb.feedback.models;

import android.support.v4.util.ArrayMap;

import java.util.Map;

public class ContextData {
    private Map<String, String> contextData;

    public Map<String, String> getContextData() {
        return contextData;
    }

    public ContextData(ContextDataBuilder builder) {
        this.contextData = builder.contextData;
    }

    public static class ContextDataBuilder {
        private Map<String, String> contextData;

        public ContextDataBuilder() {
            contextData = new ArrayMap<>();
        }

        public ContextDataBuilder add(String key, boolean value) {
            contextData.put(key, Boolean.toString(value));
            return this;
        }

        public ContextDataBuilder add(String key, int value) {
            contextData.put(key, Integer.toString(value));
            return this;
        }

        public ContextDataBuilder add(String key, String value) {
            contextData.put(key, value);
            return this;
        }

        public ContextData build() {
            return new ContextData(this);
        }
    }
}
