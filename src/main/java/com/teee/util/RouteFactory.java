package com.teee.util;

import com.alibaba.fastjson.JSONObject;

public class RouteFactory {
    public JSONObject getRouterObject(String name, String path, String component, String icon, boolean show){
        JSONObject router = new JSONObject();
        router.put("name", name);
        router.put("path", path);
        router.put("component", component);
        router.put("icon", icon);
        router.put("show", show?1:0);
        return router;
    }
}
