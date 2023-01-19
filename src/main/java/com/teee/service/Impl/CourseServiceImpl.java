package com.teee.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.teee.domain.course.Course;
import com.teee.exception.BusinessException;
import com.teee.project.ProjectCode;
import com.teee.service.CourseService;
import com.teee.vo.Result;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService {
    @Override
    public Result createCourse(String token, Course course) {
        System.out.println(course);
        return null;
    }

    @Override
    public Result delCourse(int cid) {return null;}

    @Override
    public Result editCourse(Course course) {return null;}

    @Override
    public Result addCourse(String token, JSONObject jo) {
        int cid;
        try{
            cid = (Integer) jo.get("cid");
        }catch (Exception e){
            throw new BusinessException(ProjectCode.CODE_EXCEPTION_BUSSINESS, "传入数据异常");
        }
        return null;
    }

    @Override
    public Result removeCourse(String token, JSONObject jo) {
        int cid = (Integer) jo.get("cid");
        return null;
    }

    @Override
    public Result getCourses(String token) {
        return null;
    }
}
