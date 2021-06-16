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
        RoutingTableTransform rtt = new RoutingTableTransform()
        android.registerTransform(rtt)
//        createNote(rtt, project)
    }

    private void createNote(RoutingTableTransform rtt, Project project) {
        if (rtt.fileContainsInitClass) {
            File rtTxt = new File(project.getRootDir() + '/RoutingTable.txt')
            if (!rtTxt.exists()) {
                rtTxt.createNewFile()
            } else {
//                rtTxt.delete()
            }
            FileWriter fileWriter = new FileWriter(rtTxt)
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)
            rtt.registerList.each { ext ->
                ext.classList.each { s ->
                    bufferedWriter.write(s)
                    bufferedWriter.newLine()
                }
            }
            bufferedWriter.close()
            fileWriter.close()
        }
    }
}