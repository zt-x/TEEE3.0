package com.teee.service;

import com.teee.vo.Result;

public interface SubmitService {
    Result getAllSubmitByWorkId(int wid);
    Result setSubmitScore(int subid, String score);
    Result getSubmitSummary(int wid);
    Result getSubmitContentBySid(int sid);
    Result getSubmitByWorkId(String token, int wid);
}


