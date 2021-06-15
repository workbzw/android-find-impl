package com.workbzw.plugin.router.visitor

import jdk.internal.org.objectweb.asm.Opcodes
import org.objectweb.asm.MethodVisitor

class RoutingTableMethodVisitor extends MethodVisitor {
    private String className
    private String methodName
    private MethodVisitor methodVisitor

    RoutingTableMethodVisitor(MethodVisitor methodVisitor, String className, String methodName) {
        super(org.objectweb.asm.Opcodes.ASM6, methodVisitor)
        this.className = className
        this.methodName = methodName
        this.methodVisitor = methodVisitor
    }

    @Override
    void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack + 4, maxLocals)
    }

    @Override
    void visitInsn(int opcode) {
        if ((opcode >= Opcodes.IRETURN) && opcode <= Opcodes.RETURN) {
            methodVisitor.visitLdcInsn()
            methodVisitor.visitMethodInsn(
                    org.objectweb.asm.Opcodes.INVOKESTATIC
                    , 'com.workbzw.lib.base.ServiceManager'
                    , 'insertInto'
                    , "(Ljava/lang/String;)V"
                    , false

            )
        }
        super.visitInsn(opcode)
    }
}