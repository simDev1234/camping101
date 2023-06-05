package com.camping101.beta.util;

import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;

public class RandomCode {

    public static String createRandomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String createRandomEightString() {
        return RandomStringUtils.randomAlphanumeric(8);
    }

}
