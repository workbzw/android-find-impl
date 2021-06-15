package com.workbzw.plugin

import com.android.build.gradle.AppExtension
import com.workbzw.plugin.router.transform.RoutingTableTransform
import com.workbzw.plugin.router.utils.ScanSetting
import org.gradle.api.Plugin
import org.gradle.api.Project

class RoutingTablePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println("----------------------")
        println("+++++++++开始++++++++++")
        println("----------------------")
        AppExtension android = project.extensions.getByType(AppExtension)
        ArrayList<ScanSetting> list = new ArrayList<>(1)
        list.add(new ScanSetting('IRouteTable'))
        RoutingTableTransform.registerList = list
        android.registerTransform(new RoutingTableTransform())
    }
}