package com.example.test.utils;

import com.example.test.exception.ApiException;
import com.example.test.protocol.ErrorCode;
import lombok.experimental.UtilityClass;
import org.jooq.lambda.tuple.Tuple2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import static java.lang.Math.log;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;
import static java.util.Collections.shuffle;

@UtilityClass
public class CommonUtils {

    public static <T> void validateByPredicate(T data, Predicate<T> errorCondition, ErrorCode errorCode) {
        if (errorCondition.test(data)) {
            throw new ApiException(errorCode);
        }
    }

    public static boolean isPowerOfTwo(int number) {
        return number > 0 && ((number & (number - 1)) == 0);
    }

    public static BigDecimal generateRandomBigDecimal() {
        BigDecimal min = ZERO;
        BigDecimal max = TEN.multiply(TEN);

        BigDecimal randomBigDecimal = min.add(BigDecimal.valueOf(Math.random()).multiply(max.subtract(min)));
        return randomBigDecimal.setScale(2, HALF_UP);
    }

    public static int log2(int x) {
        return (int) (log(x) / log(2));
    }

    public static String getStageName(int stage) {
        switch (stage) {
            case 0:
                return "Final";
            case 1:
                return "Semi-final";
            default:
                return String.format("1/%s final", (int) Math.pow(2, stage));
        }
    }
}
