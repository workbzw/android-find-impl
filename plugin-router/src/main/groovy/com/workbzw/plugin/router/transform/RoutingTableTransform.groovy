package com.workbzw.plugin.router.transform

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import com.workbzw.plugin.router.utils.CodeGenerator
import com.workbzw.plugin.router.utils.ScanSetting
import com.workbzw.plugin.router.utils.ScanUtils
import org.gradle.api.Project

class RoutingTableTransform extends Transform {

    Project project
    static ArrayList<ScanSetting> registerList
    static File fileContainsInitClass;

    @Override
    String getName() {
        return "soeasy"
    }

/**
 * 输入文件类型，有CLASSES和RESOURCES
 * @return
 */
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

/**
 * 指Transform要操作内容的范围，官方文档Scope有7种类型：
 * EXTERNAL_LIBRARIES        只有外部库
 * PROJECT                   只有项目内容
 * PROJECT_LOCAL_DEPS        只有项目的本地依赖(本地jar)
 * PROVIDED_ONLY             只提供本地或远程依赖项
 * SUB_PROJECTS              只有子项目。
 * SUB_PROJECTS_LOCAL_DEPS   只有子项目的本地依赖项(本地jar)。
 * TESTED_CODE               由当前变量(包括依赖项)测试的代码
 * SCOPE_FULL_PROJECT        整个项目
 * @return
 */
    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

/**
 * 增量编译
 * @return
 */
    @Override
    boolean isIncremental() {
        return false
    }

/**
 *
 * @param transformInvocation
 * @throws TransformException* @throws InterruptedException* @throws IOException
 */
    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        //inputs中是传过来的输入流，其中有两种格式，一种是jar包格式一种是目录格式。
        def inputs = transformInvocation.getInputs()
        //获取到输出目录，最后将修改的文件复制到输出目录，这一步必须做不然编译会报错
        def outputProvider = transformInvocation.getOutputProvider()
        for (TransformInput input : inputs) {
            //处理Jar中的class文件
            for (JarInput jarInput : input.getJarInputs()) {
                File src = jarInput.getFile()

                File dest = outputProvider.getContentLocation(
                        jarInput.getName(),
                        jarInput.getContentTypes(),
                        jarInput.getScopes(),
                        Format.JAR);
                if (ScanUtils.shouldProcessPreDexJar(src.absolutePath)) {
                    ScanUtils.scanJar(src, dest)
                }
                //将修改过的字节码copy到dest，就可以实现编译期间干预字节码的目的了
                FileUtils.copyFile(jarInput.getFile(), dest);
            }

            //处理文件目录下的class文件
            for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                /*文件相关操作*/
                /************************************/

                /***********************************/

                File dest = outputProvider.getContentLocation(
                        directoryInput.getName(),
                        directoryInput.getContentTypes(),
                        directoryInput.getScopes(),
                        Format.DIRECTORY)
                //将修改过的字节码copy到dest，就可以实现编译期间干预字节码的目的了
                FileUtils.copyDirectory(directoryInput.getFile(), dest)
            }
        }

        if (fileContainsInitClass) {
            println('------registerList.size:' + registerList.size())
            registerList.each { ext ->
                println('Insert register code to file ' + fileContainsInitClass.absolutePath)
                if (ext.classList.isEmpty()) {
                    println("No class implements found for interface:" + ext.interfaceName)
                } else {
                    ext.classList.each {
                        println(it)
                    }
                    CodeGenerator.insertInitCodeTo(ext)
                }
            }
        }
    }
}




















