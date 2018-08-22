package com.example.lfs.androidstudy.Demo;

import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by lfs on 2018/8/20.
 * 参照: http://www.trinea.cn/android/hashmap-loop-performance/
 */

public class MapTraversing {
    HashMap<String, Object> m_mapDatas = new HashMap<>();

    public MapTraversing() {

        for (int i = 0; i < 5; ++i) {
            m_mapDatas.put("key" + i, "value" + i);
        }

        traverseByEntrySet();
        traverseByIterator();
        traverseByKey();
    }

    public void traverseByEntrySet() {
        Log.i("Map_Traverse", "[traverseByEntrySet] begin");
        for (Map.Entry<String, Object> entry : m_mapDatas.entrySet()) {
            Log.i("Map_Traverse", "key: " + entry.getKey().toString() + ", value: " + entry.getValue().toString());
        }
        Log.i("Map_Traverse", "[traverseByEntrySet] end");
    }

    public void traverseByIterator() {
        Log.i("Map_Traverse", "[traverseByIterator] begin");
        Iterator<Map.Entry<String, Object> > iter = m_mapDatas.entrySet().iterator();
        Map.Entry<String, Object> entry;
        while (iter.hasNext()) {
            entry = iter.next();
            Log.i("Map_Traverse", "key: " + entry.getKey().toString() + ", value: " + entry.getValue().toString());
        }
        Log.i("Map_Traverse", "[traverseByIterator] end");
    }

    public void traverseByKey() {
        Log.i("Map_Traverse", "[traverseByKey] begin");
        String value;
        for (String key : m_mapDatas.keySet()) {
            value = m_mapDatas.get(key).toString();
            Log.i("Map_Traverse", "key: " + key + ", value: " + value);
        }
        Log.i("Map_Traverse", "[traverseByKey] begin");
    }
}
