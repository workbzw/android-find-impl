package com.workbzw.plugin.router.utils

import com.workbzw.plugin.router.transform.RoutingTableTransform
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

import java.util.jar.JarEntry
import java.util.jar.JarFile

class ScanUtils {

    static void scanJar(File jar) {
        if (jar) {
            JarFile file = new JarFile(jar)
            Enumeration entrys = file.entries()
            while (entrys.hasMoreElements()) {
                JarEntry jarEntry = entrys.nextElement()
                String name = jarEntry.getName()

                if (name.startsWith(ScanSetting.ROUTER_TABLE_CLASS_PRE)) {
                    println("========" + name + "========")
                    InputStream inputStream = file.getInputStream(jarEntry)
                    //对class文件进行读取与解析
                    ClassReader classReader = new ClassReader(inputStream)
                    //对class文件的写入
                    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                    //toByteArray方法会将最终修改的字节码以 byte 数组形式返回。
                    byte[] bytes = classWriter.toByteArray()
                    //这个地址在javac目录下
                    FileOutputStream outputStream = new FileOutputStream(jar.path)
                    outputStream.write(bytes)
                    outputStream.close()
                }
            }
        }
    }


/**
 * 自定义可删
 * @param jar
 */
    static void scanJarInput(File file) {
    }

/**
 * scan jar file
 * @param jarFile All jar files that are compiled into apk
 * @param destFile dest file after this transform
 */
    static void scanJar(File jarFile, File destFile) {
        if (jarFile) {
            def file = new JarFile(jarFile)
            Enumeration enumeration = file.entries()
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) enumeration.nextElement()
                String entryName = jarEntry.getName()
                if (entryName.startsWith(ScanSetting.ROUTER_TABLE_CLASS_PRE)) {
                    println("=========entryName.startsWith:" + entryName + "=========")
                    InputStream inputStream = file.getInputStream(jarEntry)
                    scanClass(inputStream)
                    inputStream.close()
                } else if (ScanSetting.GENERATE_TO_CLASS_FILE_NAME == entryName) {
                    println("=========entryName.ServiceManager:" + entryName + "=========")
                    // mark this jar file contains LogisticsCenter.class
                    // After the scan is complete, we will generate register code into this file
                    RoutingTableTransform.fileContainsInitClass = destFile
                }
            }
            file.close()
        }
    }

    static boolean shouldProcessPreDexJar(String path) {
        return !path.contains("com.android.support") && !path.contains("/android/m2repository")
    }

    static boolean shouldProcessClass(String entryName) {
        return entryName != null && entryName.startsWith(ScanSetting.ROUTER_CLASS_PACKAGE_NAME)
    }

/**
 * scan class file
 * @param class file
 */
    static void scanClass(File file) {
        scanClass(new FileInputStream(file))
    }

    static void scanClass(InputStream inputStream) {
        ClassReader cr = new ClassReader(inputStream)
        ClassWriter cw = new ClassWriter(cr, 0)
        ScanClassVisitor cv = new ScanClassVisitor(Opcodes.ASM5, cw)
        cr.accept(cv, ClassReader.EXPAND_FRAMES)
        inputStream.close()
    }

    static class ScanClassVisitor extends ClassVisitor {

        ScanClassVisitor(int api, ClassVisitor cv) {
            super(api, cv)
        }

        void visit(int version, int access, String name, String signature,
                   String superName, String[] interfaces) {
            super.visit(version, access, name, signature, superName, interfaces)
            interfaces.each {
                interfaceName ->
                    /*如果这个是RoutingTable 则执行*/
                    if (interfaceName == ScanSetting.ROUTING_TABLE && !ScanSetting.classList.contains(name)) {
                        println("======" + interfaceName + "======")
                        println("======" + name + "======")
                        ScanSetting.classList.add(name)
                        println("======" + ScanSetting.classList.size() + "======")
                    }
            }
        }
    }
}