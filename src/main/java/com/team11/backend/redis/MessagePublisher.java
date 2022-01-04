package com.team11.backend.redis;

public interface MessagePublisher {
    void publish(String message);
}
