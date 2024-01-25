package com.example.javaspringlearn;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Authorization {
    private static volatile Authorization instance;
    private Boolean status = false;
    private Users user;

    public static Authorization getInstance() {
        Authorization localInstance = instance;
        if (localInstance == null) {
            synchronized (Authorization.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Authorization();
                }
            }
        }
        return localInstance;
    }
}
