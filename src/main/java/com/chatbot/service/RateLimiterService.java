package com.chatbot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    private static final Logger logger = LoggerFactory.getLogger(RateLimiterService.class);

    // Max 10 requests per minute per IP
    private static final int MAX_REQUESTS = 10;
    private static final long TIME_WINDOW_MS = 60_000; // 1 minute

    // IP -> [requestCount, windowStartTime]
    private final Map<String, long[]> ipRequestMap = new ConcurrentHashMap<>();

    public boolean isAllowed(String ipAddress) {
        long now = Instant.now().toEpochMilli();
        ipRequestMap.compute(ipAddress, (ip, data) -> {
            if (data == null || (now - data[1]) > TIME_WINDOW_MS) {
                return new long[]{1, now};
            }
            data[0]++;
            return data;
        });

        long[] data = ipRequestMap.get(ipAddress);
        boolean allowed = data[0] <= MAX_REQUESTS;

        if (!allowed) {
            logger.warn("Rate limit exceeded for IP: {}", ipAddress);
        }

        return allowed;
    }

    public long getRequestCount(String ipAddress) {
        long[] data = ipRequestMap.get(ipAddress);
        return data != null ? data[0] : 0;
    }
}