package com.workbzw.plugin.router.visitor

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class RoutingTableClassVisitor extends ClassVisitor {
    private String className
    private ClassVisitor classVisitor

    RoutingTableClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM6, classVisitor)
        this.classVisitor = classVisitor
    }

    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces)
        className = name
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        println("ClassVisitor visitMethod name-------" + name)
        MethodVisitor method = classVisitor.visitMethod(access, name, descriptor, signature, exceptions)
        if (name.equals("register"))
            return new RoutingTableMethodVisitor();
        return method
    }

    @Override
    void visitEnd() {
        super.visitEnd()
    }
}