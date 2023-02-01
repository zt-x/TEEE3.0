package com.teee.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teee.dao.*;
import com.teee.domain.course.Course;
import com.teee.domain.course.CourseUser;
import com.teee.domain.course.UserCourse;
import com.teee.domain.user.UserInfo;
import com.teee.domain.work.Work;
import com.teee.domain.work.WorkSubmit;
import com.teee.domain.work.WorkSubmitContent;
import com.teee.project.ProjectCode;
import com.teee.project.ProjectRole;
import com.teee.service.CourseService;
import com.teee.utils.*;
import com.teee.vo.Result;
import com.teee.vo.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
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
    @Autowired
    WorkDao workDao;
    @Autowired
    WorkSubmitDao workSubmitDao;
    @Autowired
    WorkSubmitContentDao workSubmitContentDao;

    @Value("${path.file.files}")
    private String filePath;

    @Value("${path.file.temps}")
    private String tempPath;

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
    public Result delCourse(int cid) {
        courseDao.deleteById(cid);
        courseUserDao.deleteById(cid);
        List<Work> works = workDao.selectList(new LambdaQueryWrapper<Work>().eq(Work::getCid, cid));
        for (Work work : works) {
            List<WorkSubmit> workSubmits = workSubmitDao.selectList(new LambdaQueryWrapper<WorkSubmit>().eq(WorkSubmit::getWid, work.getId()));
            for (WorkSubmit workSubmit : workSubmits) {
                workSubmitDao.deleteById(workSubmit.getSid());
                workSubmitContentDao.deleteById(workSubmit.getSid());
            }
        }
        return new Result("删除成功!");
    }

    //TODO 2 编辑课程信息

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

    private void addStuToCourse(int cid, Long uid) {
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
        }catch (Exception e){
            throw new BusinessException(ProjectCode.CODE_EXCEPTION_BUSSINESS, "添加课程给用户时出错", e);
        }
    }
    private void addCourseToUser(Long uid, int cid) {
        int new_ = 0;
        try{
            UserCourse userCourse =  userCourseDao.selectById(uid);
            if(userCourse == null){
                userCourse = new UserCourse(uid, "[]");
                new_ = 1;
            }
            ArrayList<Integer> cids = new ArrayList<>();
            String[] split = userCourse.getCid().replace("[", "").replace("]", "").split(",");
            if(!"".equals(split[0])){
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
        }catch (Exception e){
            throw new BusinessException(ProjectCode.CODE_EXCEPTION_BUSSINESS, "为课程添加用户时出错", e);
        }

    }

    @Override
    public Result getUsers(int cid) {
        List<Work> works = workDao.selectList(new LambdaQueryWrapper<Work>().eq(Work::getCid, cid));
        List<Integer> wids = new ArrayList<>();
        for (Work work : works) {
            wids.add(work.getId());
        }
        String uids = courseUserDao.selectById(cid).getUid();
        if(uids != null){
            ArrayList<String> arrayList = TypeChange.str2arrl(uids);
            JSONArray jarr = new JSONArray();
            // 遍历学生
            for (String s : arrayList) {
                UserInfo userInfo = userInfoDao.selectById(Long.valueOf(s));
                JSONObject ret = new JSONObject();
                if(userInfo == null){
                    userInfo = new UserInfo();
                }
                ret.put("uid", userInfo.getUid());
                ret.put("username", userInfo.getUname());
                ret.put("avatar", userInfo.getAvatar());
                List<WorkSubmit> workSubmits = workSubmitDao.selectList(new LambdaQueryWrapper<WorkSubmit>().eq(WorkSubmit::getUid, userInfo.getUid()));
                float avarage = 0;
                int fwn = 0;
                for (WorkSubmit workSubmit : workSubmits) {
                    if(wids.contains(workSubmit.getWid())){
                        avarage += workSubmit.getScore();
                        fwn++;
                    }
                }
                avarage = avarage/ (workSubmits.size() == 0?1:workSubmits.size());
                ret.put("workAverageScore", avarage);
                ret.put("finishWorkNum", fwn);

                jarr.add(ret);
            }
            return new Result(ProjectCode.CODE_SUCCESS, TypeChange.jarr2str(jarr), "获取用户成功");
        }else{
            log.warn("异常的查询：查询课程用户时，courseUser表中未记录该课程");
            return new Result(ProjectCode.CODE_SUCCESS, "[]", "您的班级还没有学生加入哦~");
        }
    }

    @Override
    public Result removeUserFromCourse(Long uid, JSONObject jo) {
        int cid = (Integer) jo.get("cid");
        // TODO 3 需要完成 学生提交作业 功能后
        // 从Course_user表移除
        CourseUser courseUser = courseUserDao.selectById(cid);
        MyAssert.notNull(courseUser, "该学生已不在这个班级啦！");
        ArrayList<Long> uids = new ArrayList<>();
        String[] split = courseUser.getUid().replace("[", "").replace("]", "").split(",");
        if(!split[0].equals("")){
            for (String s : split) {
                if(!s.equals(uid.toString())){
                    uids.add(Long.valueOf(s.trim()));
                }
            }
        }
        courseUser.setUid(uids.toString());
        // 从UserCourse表删除
        UserCourse userCourse = userCourseDao.selectById(uid);
        MyAssert.notNull(userCourse, "未在学生表中找到该学生，可能还未选课 ...");
        ArrayList<Integer> cids = new ArrayList<>();
        split = userCourse.getCid().replace("[", "").replace("]", "").split(",");
        if(!"".equals(split[0])){
            for (String s : split) {
                if(!s.equals(String.valueOf(cid))){
                    cids.add(Integer.valueOf(s.trim()));
                }
            }
        }

        // 删除该生提交作业的记录
        LambdaQueryWrapper<WorkSubmit> eq = new LambdaQueryWrapper<WorkSubmit>().eq(WorkSubmit::getUid, uid);
        List<WorkSubmit> workSubmits = workSubmitDao.selectList(eq);
        for (WorkSubmit workSubmit : workSubmits) {
            workSubmitDao.deleteById(workSubmit.getSid());
            workSubmitContentDao.deleteById(workSubmit.getSid());
        }
        return new Result(ProjectCode.CODE_SUCCESS, null, "已将该学生移除班级啦~");
    }

    @Override
    public Result getCourses(String token, int page) {
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
                for (int i = (page-1)*9; i<(Math.min(page * 9, cids.length)); i++) {
                    cids[i] = cids[i].replaceAll(" ", "");
                    course = courseDao.selectById(Integer.valueOf(cids[i]));
                    if(course == null){
                        continue;
                    }
                    packageCourse(courses, course);
                }
                JSONObject ret = new JSONObject();
                ret.put("current", page);
                ret.put("pages", cids.length/9+1);
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

    @Override
    public Result getCourseInfo(int cid) {
        Course course = courseDao.selectOne(new LambdaQueryWrapper<Course>().eq(Course::getCid, cid));
        MyAssert.notNull(course, "您查询的课程不存在哦！👀");
        JSONObject o = (JSONObject) JSONObject.toJSON(course);
        o.put("tname", userInfoDao.selectById(o.getLong("tid")).getUname());
        o.put("UserCount", TypeChange.str2arrl(courseUserDao.selectById(cid).getUid()).size());
        o.put("WorksCount", TypeChange.str2arrl(course.getWorks()).size());
        o.put("ExamsCount", TypeChange.str2arrl(course.getExams()).size());
        return new Result(ProjectCode.CODE_SUCCESS, o.toJSONString(), "获取课程信息成功！");
    }

    @Override
    public Result getWorks(int cid) {
        LambdaQueryWrapper<Work> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Work::getCid, cid);
        List<Work> works = workDao.selectList(lqw);
        JSONArray jsonArray = new JSONArray();
        // 装配
        for (Work work : works) {
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(work);
            jsonArray.add(jsonObject);
        }
        return new Result(ProjectCode.CODE_SUCCESS, jsonArray, "获取成功");
    }

    @Override
    public Result getWorks_(int cid, int page, int isExam) {
        LambdaQueryWrapper<Work> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Work::getCid, cid).eq(Work::getIsExam, isExam);
        Page page1 = workDao.selectPage(new Page(page, 7), lqw);
        List<Work> works = page1.getRecords();
        JSONArray jsonArray = new JSONArray();
        // 装配
        for (Work work : works) {
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(work);
            jsonArray.add(jsonObject);
        }
        return new Result(Math.toIntExact(page1.getPages()), jsonArray, "获取成功");
    }

    @Override
    public Result getAnnouncements(int cid) {
        return null;
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

    @Override
    public File packageFile(int wid) {
        String tmpFilePath = tempPath+File.separator + "downloadZipTemp" + File.separator + wid;
        File tmpdir = new File(tmpFilePath);
        if(tmpdir.isDirectory()){
            log.info("wid文件夹存在, 删除");
            FileUtil.delFile(tmpdir);
        }else{

        }
        List<WorkSubmit> workSubmits = workSubmitDao.selectList(new LambdaQueryWrapper<WorkSubmit>().eq(WorkSubmit::getWid, wid));
        for (WorkSubmit sw : workSubmits) {
            log.info("获取work");
            Integer submitId = sw.getSid();
            WorkSubmitContent swc = workSubmitContentDao.selectById(submitId);
            String files = swc.getFiles();
            ArrayList<String> file_list = TypeChange.str2arrl(files);
            for (int i = 1; i <= file_list.size(); i++) {
                // 第 i 题
                log.info("  获取第" + i + "题");
                ArrayList<String> ans_file = TypeChange.str2arrl(file_list.get(i-1), ",");
                // 第 i1 个附件
                for (int i1 = 0; i1 < ans_file.size(); i1++) {
                    log.info("    获取第" + i1+1 + "个文件");
                    String fileName = ans_file.get(i1);
                    File src = new File(filePath+File.separator + fileName);
                    String substring = fileName.substring(fileName.lastIndexOf("_")+1);
                    String fileOriginName = substring.substring(substring.lastIndexOf("_")+1);
                    File dst = new File( tmpFilePath +File.separator + sw.getUid()+"_" + sw.getUname() +"_第"+i+"题_" + fileOriginName);
                    if(!dst.getParentFile().isDirectory()){
                        dst.getParentFile().mkdirs();
                    }
                    try {
                        FileCopyUtils.copy(src,dst);
                    } catch (IOException e) {
                        e.printStackTrace();
                        log.error("存在损坏的文件" + e.getMessage());
                    }
                }
            }
        }
        try {
            return FileUtil.fileToZip(tmpFilePath, tempPath+File.separator + "downloadZipTemp" + File.separator,wid+".zip");
        } catch (IOException e) {
            log.error("打包时出现异常 ...");
            e.printStackTrace();
            return null;
        }
    }
}
