package com.teee.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import com.teee.util.validator;
import com.teee.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xu ZhengTao
 * @version 3.0
 */
@Service
@Slf4j
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
        course.setBanner(userInfoDao.selectById(tid).getAvatar());
        if(!validator.isThisDateValid(course.getStartTime(), "yyyy-MM-dd")){
            course.setStartTime(null);
        }
        if(!validator.isThisDateValid(course.getEndTime(), "yyyy-MM-dd")){
            course.setEndTime(null);
        }
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
        Long uid = JWT.getUid(token);
        cid = Integer.parseInt(jo.getString("cid"));
        MyAssert.isTrue(isCourseExist(cid),"课程不存在");
        try{
            addStuToCourse(cid, uid);
            addCourseToUser(uid, cid);
            return new Result(ProjectCode.CODE_SUCCESS, null, "课程 " + courseDao.selectOne(new LambdaQueryWrapper<Course>().eq(Course::getCid, cid)).getCname() + " 已添加至你的库中啦");
        }catch (Exception e){
            throw new BusinessException(ProjectCode.CODE_EXCEPTION_BUSSINESS, "传入数据异常",e);
        }
    }

    private Integer addStuToCourse(int cid, Long uid) {
        int new_ = 0;
        try{
            CourseUser courseUser = courseUserDao.selectById(cid);
            if(courseUser == null){
                courseUser = new CourseUser(cid, "[]");
                new_ = 1;
            }
            ArrayList<Long> uids = new ArrayList<Long>();
            String[] split = courseUser.getUid().replace("[", "").replace("]", "").split(",");
            if(!split[0].equals("")){
                for (String s : split) {
                    uids.add(Long.valueOf(s.trim()));
                }
            }
            if(!uids.contains(uid)){
                uids.add(Long.valueOf(uid));
            }
            courseUser.setUid(uids.toString());
            if(new_ == 1){
                courseUserDao.insert(courseUser);
            }else{
                courseUserDao.updateById(courseUser);
            }
            return ProjectCode.CODE_SUCCESS;
        }catch (Exception e){
            throw new BusinessException(ProjectCode.CODE_EXCEPTION_BUSSINESS, "添加课程给用户时出错", e);
        }
    }
    private Integer addCourseToUser(Long uid, int cid) {
        int new_ = 0;
        try{
            UserCourse userCourse =  userCourseDao.selectById(uid);
            if(userCourse == null){
                userCourse = new UserCourse(uid, "[]");
                new_ = 1;
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
            if(new_ == 1){
                userCourseDao.insert(userCourse);
            }else{
                userCourseDao.updateById(userCourse);
            }
            return ProjectCode.CODE_SUCCESS;
        }catch (Exception e){
            throw new BusinessException(ProjectCode.CODE_EXCEPTION_BUSSINESS, "为课程添加用户时出错", e);
        }

    }

    @Override
    public Result removeCourse(String token, JSONObject jo) {
        int cid = (Integer) jo.get("cid");
        return null;
    }

    @Override
    public Result getCourses(String token, int page) {
        log.info("page = " + page);
        // 分权限
        int role = JWT.getRole(token);
        if(role == ProjectRole.ADMIN.ordinal()){
            return new Result(ProjectCode.CODE_SUCCESS, "suc");
        }else if(role == ProjectRole.TEACHER.ordinal()){
            JSONArray courses = new JSONArray();
            new JSONObject();
            JSONObject courseJson = null;
            IPage<Course> page1 = courseDao.selectPage(new Page(page, 9), new LambdaQueryWrapper<Course>().eq(Course::getTid, JWT.getUid(token)));
            List<Course> coursesList = page1.getRecords();
            for (Course course : coursesList) {
                packageCourse(courses, course);
            }
            JSONObject ret = new JSONObject();
            ret.put("current", page1.getCurrent());
            ret.put("pages", page1.getPages());
            ret.put("courses", courses);
            return new Result(ProjectCode.CODE_SUCCESS, ret, "suc");
        }else if(role == ProjectRole.STUDENT.ordinal()){
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
                // TODO 需要测试
                for (int i = (page-1)*9; i<(Math.min(page * 9, cids.length)); i++) {
                    cids[i] = cids[i].replaceAll(" ", "");
                    course = courseDao.selectById(Integer.valueOf(cids[i]));
                    packageCourse(courses, course);
                }
                JSONObject ret = new JSONObject();
                ret.put("current", page);
                ret.put("pages", cids.length/9);
                ret.put("courses", courses);
                return new Result(ProjectCode.CODE_SUCCESS, ret, "suc");
            }catch(NullPointerException npe){
                npe.printStackTrace();
                throw new BusinessException(ProjectCode.CODE_EXCEPTION_BUSSINESS, "获取课程时发生了一点错误 ...", npe);
            }
        }else{
            throw new BusinessException(ProjectCode.CODE_EXCEPTION_BUSSINESS,"😣获取身份失败 ... ");
        }
    }

    private void packageCourse(JSONArray courses, Course course) {
        JSONObject courseJson;
        courseJson = new JSONObject();
        courseJson.put("cname", course.getCname());
        courseJson.put("cid", course.getCid());
        courseJson.put("tname", userInfoDao.selectById(course.getTid()).getUname());
        courseJson.put("college", course.getCollege());
        courseJson.put("time", course.getStartTime() + " - " + course.getEndTime());
        courseJson.put("banner", course.getBanner());
        courseJson.put("status", course.getStatus());
        courses.add(courseJson);
    }

    private boolean isCourseExist(int cid){
        if(courseDao.selectCount(new LambdaQueryWrapper<Course>().eq(Course::getCid, cid))>0){
            return true;
        }else{
            return false;
        }
    }
    //public boolean addCourseToUser(Long uid, int cid) {
    //    int flag = 0;
    //    try{
    //        UserCourse userCourse =  userCourseDao.selectById(uid);
    //        if(userCourse == null){
    //            userCourse = new UserCourse(uid, "[]");
    //            flag = 1;
    //        }
    //        ArrayList<Integer> cids = new ArrayList<>();
    //        String[] split = userCourse.getCid().replace("[", "").replace("]", "").split(",");
    //        if(!split[0].equals("")){
    //            for (String s : split) {
    //                cids.add(Integer.valueOf(s.trim()));
    //            }
    //        }
    //        if(!cids.contains(cid)){
    //            cids.add(cid);
    //        }
    //        userCourse.setCid(cids.toString());
    //        if(flag == 1){
    //            userCourseDao.insert(userCourse);
    //        }else{
    //            userCourseDao.updateById(userCourse);
    //        }
    //        return true;
    //    }catch (Exception e){
    //        return false;
    //    }
    //
    //}
}
