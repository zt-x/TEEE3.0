package com.teee.controller.Project.Annoation;

import com.teee.controller.Project.ProjectRole;

import java.lang.annotation.*;

/**
 * @author Xu ZhengTao
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RoleCheck {
    public ProjectRole role();
}