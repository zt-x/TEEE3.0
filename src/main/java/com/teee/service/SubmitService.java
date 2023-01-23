package com.teee.service;

import com.teee.vo.Result;

public interface SubmitService {
    Result SubmitWork(String token, int wid, String ans, String files);
    Result getAllSubmitByWorkId(int wid);
    Result setSubmitScore(int subid, String score);
    Result getSubmitSummary(int subid);
    Result getSubmitContentBySid(int sid);
    Result getSubmitByWorkId(String token, int wid);
}


