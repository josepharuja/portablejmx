package org.leedsmet.observe.meter;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Simple, ordered set of metric tags. Backed by a LinkedHashMap for stable iteration order.
 */
public final class Tags {
    private final Map<String, String> tags;

    private Tags(Map<String, String> tags) {
        this.tags = Collections.unmodifiableMap(tags);
    }

    public static Tags empty() { return new Tags(new LinkedHashMap<>()); }

    /** Create tags from alternating key,value pairs. */
    public static Tags of(String... keyValues) {
        Objects.requireNonNull(keyValues, "keyValues");
        if (keyValues.length % 2 != 0) {
            throw new IllegalArgumentException("Tags.of requires an even number of arguments, got " + Arrays.toString(keyValues));
        }
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < keyValues.length; i += 2) {
            String k = Objects.requireNonNull(keyValues[i], "tag key");
            String v = String.valueOf(keyValues[i + 1]);
            map.put(k, v);
        }
        return new Tags(map);
    }

    public Map<String, String> asMap() { return tags; }
}
