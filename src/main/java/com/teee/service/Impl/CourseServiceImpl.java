package com.teee.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.teee.dao.CourseDao;
import com.teee.dao.CourseUserDao;
import com.teee.dao.UserCourseDao;
import com.teee.dao.UserInfoDao;
import com.teee.domain.course.Course;
import com.teee.domain.course.CourseUser;
import com.teee.domain.course.UserCourse;
import com.teee.exception.BusinessException;
import com.teee.project.ProjectCode;
import com.teee.project.ProjectRole;
import com.teee.service.CourseService;
import com.teee.util.JWT;
import com.teee.util.MyAssert;
import com.teee.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    UserCourseDao userCourseDao;
    @Autowired
    CourseDao courseDao;
    @Autowired
    UserInfoDao userInfoDao;
    @Autowired
    CourseUserDao courseUserDao;

    @Override
    public Result createCourse(String token, Course course) {
        // 1、 从token获取用户ID， 从Body中获取cid
        Long tid = JWT.getUid(token);
        // 3、写Course表\写teacher_course表\ 写Course_User表
        course.setTid(tid);
        courseDao.insert(course);
        courseUserDao.insert(new CourseUser(course.getCid(),""));
        return new Result(ProjectCode.CODE_SUCCESS, course.getCid(), "创建成功！课程ID为" + course.getCid());
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
        // 分权限
        int role = JWT.getRole(token);
        if(role == ProjectRole.ADMIN.ordinal()){
            return new Result(ProjectCode.CODE_SUCCESS, "suc");
        }else if(role == ProjectRole.TEACHER.ordinal()){
            JSONArray courses = new JSONArray();
            Course course;
            new JSONObject();
            JSONObject courseJson = null;
            try{
                UserCourse userCourse = userCourseDao.selectById(JWT.getUid(token));
                if(userCourse == null){
                    return new Result(ProjectCode.CODE_SUCCESS_NoCourse, null, "您还没有选课~");
                }
                String[] cids = userCourse.getCid().replace("[", "").replace("]", "").split(",");
                for (String cid: cids) {
                    cid = cid.replaceAll(" ", "");
                    course = courseDao.selectById(Integer.valueOf(cid));
                    courseJson = (JSONObject) JSONObject.toJSON(course);
                    courseJson.put("Name", course.getCname());
                    courseJson.put("id", course.getCid());
                    courseJson.put("TeacherName", userInfoDao.selectById(course.getTid()).getUname());
                    courseJson.put("College", course.getCollege());
                    courseJson.put("Time", course.getStartTime() + " - " + course.getEndTime());
                    courseJson.put("IMG", course.getBanner());
                    courseJson.put("status", course.getStatus());
                    courses.add(courseJson);
                }
                return new Result(ProjectCode.CODE_SUCCESS, courses, "suc");
            }catch(NullPointerException npe){
                npe.printStackTrace();
                throw new BusinessException(ProjectCode.CODE_EXCEPTION_BUSSINESS, "获取课程时发生了一点错误 ...", npe);
            }
        }else if(role == ProjectRole.STUDENT.ordinal()){
            return new Result(ProjectCode.CODE_SUCCESS, "suc");
        }else{
            throw new BusinessException(ProjectCode.CODE_EXCEPTION_BUSSINESS,"😣获取身份失败 ... ");
        }
    }

    public boolean addCourseToUser(Long uid, int cid) {
        int flag = 0;
        try{
            UserCourse userCourse =  userCourseDao.selectById(uid);
            if(userCourse == null){
                userCourse = new UserCourse(uid, "[]");
                flag = 1;
            }
            ArrayList<Integer> cids = new ArrayList<>();
            String[] split = userCourse.getCid().replace("[", "").replace("]", "").split(",");
            if(!split[0].equals("")){
                for (String s : split) {
                    cids.add(Integer.valueOf(s.trim()));
                }
            }
            if(!cids.contains(cid)){
                cids.add(cid);
            }
            userCourse.setCid(cids.toString());
            if(flag == 1){
                userCourseDao.insert(userCourse);
            }else{
                userCourseDao.updateById(userCourse);
            }
            return true;
        }catch (Exception e){
            return false;
        }

    }
}
