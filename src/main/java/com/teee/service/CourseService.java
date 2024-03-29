package com.teee.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.teee.domain.course.Course;
import com.teee.vo.Result;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * @author Xu ZhengTao
 * @version 3.0
 */
public interface CourseService {
    Result getCourses(String token, int page, String criteria);
    /**
     * 获取待批改的TODO列表
     * */
    Result getCoursesTodo(String token);
    Result createCourse(String token, Course course);
    Result delCourse(int cid);
    Result editCourse(Course course);
    Result addUsers(JSONArray users, Integer cid);
    Result addCourse(String token, JSONObject jo);

    Result getCourseInfo(int cid);
    Result getUsers(int cid);
    Result getWorks(int cid);
    Result getWorks_(int cid, int page, int isExam);
    Result getAnnouncements(int cid);
    Result removeUserFromCourse(Long uid, JSONObject jo);
    Result getLastExamStatistics(int cid);
    Result getFiveWorksAvg(String token, int cid);
    Result downloadUserInfo(int cid, HttpServletResponse resp) throws UnsupportedEncodingException;
    File packageFile(int wid);
}
