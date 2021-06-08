package com.workbzw.baseprocess;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;
import com.workbzw.baseframe.annotation.Service;

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
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class ServiceProcesser extends AbstractProcessor {
    private ProcessingEnvironment env;
    private Elements elementUtils;
    private Messager messager;
    private Filer filer;
    private Types typeUtils;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        this.env = env;
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
        env.getMessager().printMessage(Diagnostic.Kind.WARNING, "-----start process-----");
        Set<? extends Element> services = roundEnv.getElementsAnnotatedWith(Service.class);
        String packageName = null;

        String annotationPath = "com.workbzw.baseframe.IService";

        TypeElement IServiceType = elementUtils.getTypeElement(annotationPath);

        TypeMirror IServiceTypeMirror = IServiceType.asType();
        ParameterizedTypeName mapTypeName = ParameterizedTypeName.get(ClassName.get(Map.class)
                , ClassName.get(String.class)
                , ParameterizedTypeName.get(
                        ClassName.get(Class.class)
                        , WildcardTypeName.subtypeOf(ClassName.get(IServiceType))));

        ParameterSpec spec = ParameterSpec.builder(mapTypeName, "table").build();
        MethodSpec.Builder builder = MethodSpec.methodBuilder("routeTable")
                .addModifiers(Modifier.PUBLIC)
                .returns(Map.class)
                .addParameter(spec);

        String createdClassName = "Router$$Table";

        for (Element element : services) {
            packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
            messager.printMessage(Diagnostic.Kind.WARNING, "packageName:" + packageName);

            String className = element.getSimpleName().toString();
            messager.printMessage(Diagnostic.Kind.WARNING, "className:" + className);

            List<? extends TypeMirror> interfaces = ((TypeElement) element).getInterfaces();
            for (int i = 0; i < interfaces.size(); i++) {
                TypeMirror typeMirror = interfaces.get(i);
                boolean isIServiceAbstract = typeUtils.isSubtype(typeMirror, IServiceTypeMirror);
                if (isIServiceAbstract) {

                    TypeName typeName = ClassName.get(typeMirror);

                    String mapValue = packageName + "." + className;
                    TypeElement typeElement = elementUtils.getTypeElement(mapValue);
                    ClassName impl = ClassName.get(typeElement);
                    builder.addStatement("table.put($S,$T.class)", typeName, impl);
                }
            }
        }
        MethodSpec methodSpec = builder
                .addStatement("return table")
                .build();
        TypeSpec clazz = TypeSpec.classBuilder(createdClassName)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodSpec)
                .build();
        JavaFile javaFile = JavaFile.builder(packageName, clazz).build();
        try {
            javaFile.writeTo(System.out);
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
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