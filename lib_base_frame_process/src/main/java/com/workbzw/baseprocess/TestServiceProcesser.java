package com.workbzw.baseprocess;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;
import com.workbzw.baseframe.IService;
import com.workbzw.baseframe.annotation.Service;

import java.io.IOException;
import java.util.HashSet;
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
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class TestServiceProcesser extends AbstractProcessor {
    private ProcessingEnvironment env;
    private Elements elementUtils;
    private Messager messager;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        this.env = env;
        elementUtils = env.getElementUtils();
        filer = env.getFiler();
        messager = env.getMessager();
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
        env.getMessager().printMessage(Diagnostic.Kind.WARNING, "-----start process-----");
        Set<? extends Element> services = roundEnv.getElementsAnnotatedWith(Service.class);
        for (Element element : services) {
            String packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
            messager.printMessage(Diagnostic.Kind.WARNING, "packageName:" + packageName);

            String className = element.getSimpleName().toString();
            messager.printMessage(Diagnostic.Kind.WARNING, "className:" + className);
            String createdClassName = className + "$$table";


//            String annotationPath = IService.class.getPackage() + IService.class.getCanonicalName();
            String annotationPath = "com.workbzw.baseframe.IService";

            TypeElement IServiceType = elementUtils.getTypeElement(annotationPath);


            ParameterizedTypeName mapTypeName = ParameterizedTypeName.get(ClassName.get(Map.class)
                    , ClassName.get(String.class)
                    , ParameterizedTypeName.get(
                            ClassName.get(Class.class)
                            , WildcardTypeName.subtypeOf(ClassName.get(IServiceType))));

//            ParameterizedTypeName.get(ClassName.get(Map.class)
//            ,ClassName.get(String.class)
//            ,ClassName.get(IServiceType));

            ParameterSpec spec = ParameterSpec.builder(mapTypeName, "table").build();


            MethodSpec method = MethodSpec.methodBuilder("routeTable")
                    .returns(Map.class)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addParameter(spec)
                    .addStatement("return table")
                    .build();

            TypeSpec clazz = TypeSpec.classBuilder(createdClassName)
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(method)
                    .build();

            JavaFile javaFile = JavaFile.builder(packageName, clazz).build();
            try {
                javaFile.writeTo(System.out);
                javaFile.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static TypeMirror getActor(Service annotation) {
        try {
//            annotation.name(); // this should throw
        } catch (MirroredTypeException mte) {
            return mte.getTypeMirror();
        }
        return null; // can this ever happen ??
    }
}