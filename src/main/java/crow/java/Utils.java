/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crow.java;

import clojure.lang.Keyword;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import static java.util.stream.Collectors.toMap;

/**
 *
 * @author t_yano
 */
public final class Utils {

    private Utils() {
    }

    public static Map<Keyword,Object> toKeywordMap(Map<String,Object> m) {
        return Optional.ofNullable(m)
            .map(attr -> attr.entrySet().stream().collect(
                toMap(
                    e -> Keyword.intern(e.getKey()),
                    e -> e.getValue())))
            .orElse(Collections.emptyMap());
    }

    public static Map<String,Object> toNameMap(Map<Keyword,Object> m) {
        return Optional.ofNullable(m)
                .map(attr -> attr.entrySet().stream().collect(
                    toMap(
                        e -> e.getKey().getName(),
                        e -> e.getValue())))
                .orElse(Collections.emptyMap());
    }
}
