package com.teee.service;

import com.teee.domain.Key;
import com.teee.vo.Result;

public interface KeyService {
    Result createKey(String token, Key key);
    Result deleteKey(String token, String key);
    Result useKey(String token, String key);
}
