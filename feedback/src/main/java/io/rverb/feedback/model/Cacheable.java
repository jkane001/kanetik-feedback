package io.rverb.feedback.model;

import java.io.Serializable;

public interface Cacheable extends Serializable {
    String getTempFileNameTag();
    Class<?> getServiceClass();
}