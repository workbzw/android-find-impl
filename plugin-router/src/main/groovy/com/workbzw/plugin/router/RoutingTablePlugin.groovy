package com.workbzw.plugin

import com.android.build.gradle.AppExtension
import com.workbzw.plugin.router.transform.RoutingTableTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

class RoutingTablePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println("----------------------")
        println("++++++++++++++++++++++")
        println("----------------------")
        AppExtension android = project.extensions.getByType(AppExtension)
        android.registerTransform(new RoutingTableTransform())
    }
}