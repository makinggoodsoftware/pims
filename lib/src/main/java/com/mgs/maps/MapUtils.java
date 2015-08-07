package com.mgs.maps;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {
    public Map copy (Map from){
        Map copy = new HashMap<>();
        //noinspection unchecked
        copy.putAll(from);
        return copy;
    }
}
