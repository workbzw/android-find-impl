package com.workbzw.lib.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;
import com.workbzw.lib.base.annotation.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class ServiceProcessor extends AbstractProcessor {
    private Elements elementUtils;
    private Messager messager;
    private Filer filer;
    private Types typeUtils;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        elementUtils = env.getElementUtils();
        filer = env.getFiler();
        messager = env.getMessager();
        typeUtils = env.getTypeUtils();
        messager.printMessage(Diagnostic.Kind.WARNING, "ActionProcess init(---)");
    }

    @Override
    public Set<String> getSupportedOptions() {
        return super.getSupportedOptions();
    }

    /**
     * 添加可识别注解
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> annotationSet = new HashSet<>();
        String annotation = Service.class.getCanonicalName();
        annotationSet.add(annotation);
        return annotationSet;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {

        if (set.isEmpty())
            return false;

        messager.printMessage(Diagnostic.Kind.WARNING, "-----start process-----");
        Set<? extends Element> services = roundEnv.getElementsAnnotatedWith(Service.class);

        /*组件注解的全路径*/
        String iServicePath = "com.workbzw.lib.base.IService";
        String iRoutingTablePath = "com.workbzw.lib.base.IRoutingTable";

        TypeElement iServiceType = elementUtils.getTypeElement(iServicePath);
        TypeElement iRoutingTableType = elementUtils.getTypeElement(iRoutingTablePath);

        /*routeTable()方法传入值 -> Map<String,<Class<? extends IService>>*/
        TypeMirror IServiceTypeMirror = iServiceType.asType();
        ParameterizedTypeName mapType = ParameterizedTypeName.get(ClassName.get(Map.class)
                , ClassName.get(String.class)
                , ParameterizedTypeName.get(
                        ClassName.get(Class.class)
                        , WildcardTypeName.subtypeOf(ClassName.get(iServiceType))));

        /*构建方法名为routeTable 的方法*/
        ParameterSpec spec = ParameterSpec.builder(mapType, "table").build();
        MethodSpec.Builder builder = MethodSpec.methodBuilder("insertInto")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(spec);

        String packageNameGenerate = "com.workbzw.android.router.service";
        /*包名*/
        String packageName = elementUtils.getPackageOf((Element) services.toArray()[0]).getQualifiedName().toString();
        int index = packageName.lastIndexOf(".");
        String lastName = packageName.substring(index + 1, packageName.length());
        /*生成的类名*/
        String createdClassName = "Router$$Table$$" + lastName;
        /*循环创建方法体*/
        for (Element element : services) {
            /*生成统一的包结构 plugin解析时统一处理*/
            messager.printMessage(Diagnostic.Kind.WARNING, "packageName:" + packageName);

            /*当前被注解的class的类名*/
            String serviceImplClassName = element.getSimpleName().toString();
            messager.printMessage(Diagnostic.Kind.WARNING, "className:" + serviceImplClassName);

            /*获取该class的所实现的所有接口*/
            List<? extends TypeMirror> interfaces = ((TypeElement) element).getInterfaces();
            for (int i = 0; i < interfaces.size(); i++) {
                /*遍历接口 找到继承IService的XXXService服务*/
                TypeMirror service = interfaces.get(i);
                /*限定必须有接口XXXService继承于IService 实现类实现XXXService 否则路由表不予识别*/
                boolean isIServiceAbstract = typeUtils.isSubtype(service, IServiceTypeMirror);
                /*如果该接口继承自IService*/
                if (isIServiceAbstract) {
                    /*获取抽象接口*/
                    TypeName serviceInterface = ClassName.get(service);
                    /*获取实现类*/
                    String mapValue = packageName + "." + serviceImplClassName;
                    TypeElement serviceElement = elementUtils.getTypeElement(mapValue);
                    ClassName serviceImpl = ClassName.get(serviceElement);
                    /*在 Router$$Table 类的 routeTable() 方法中 增加一句 table.put("com.xxx.XXXService",XXXServiceImpl.class)*/
                    builder.addStatement("table.put($S,$T.class)", serviceInterface, serviceImpl);
                }
            }
        }
        /*构建方法返回值*/
        MethodSpec methodSpec = builder
                .addStatement("return")
                .build();
        /*构建带有insertInto() 方法的类*/
        TypeSpec clazz = TypeSpec.classBuilder(createdClassName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ClassName.get(iRoutingTableType))
                .addMethod(methodSpec)
                .build();
        JavaFile javaFile = JavaFile.builder(packageNameGenerate, clazz).build();
        try {
            javaFile.writeTo(System.out);
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}