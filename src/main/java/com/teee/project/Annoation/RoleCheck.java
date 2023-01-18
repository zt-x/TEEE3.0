package com.teee.project.Annoation;

import com.teee.project.ProjectRole;

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