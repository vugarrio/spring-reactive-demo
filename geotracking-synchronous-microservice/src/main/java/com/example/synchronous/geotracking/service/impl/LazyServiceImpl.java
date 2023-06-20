package com.example.synchronous.geotracking.service.impl;

import com.example.synchronous.geotracking.service.LazyService;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LazyServiceImpl implements LazyService {
    @Override
    public String sleep(long time) {
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        return "Sleep " + time + " seconds";
    }

}
