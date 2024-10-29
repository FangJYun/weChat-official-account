package com.example.demo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.ShellRunner;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * mybatis Generator
 *
 * @author fangjiayun@nnuo.com
 * @date 2020-03-10 14:25
 **/
@Slf4j
public class MybatisGenerator extends PluginAdapter {


    public static void main(String[] args) {
        generate();
    }

    public static void generate() {
        String config = Objects.requireNonNull(
                MybatisGenerator.class.getClassLoader().getResource("generator/generator-config.xml"))
                .getFile();
        String[] arg = {"-configfile", config, "-overwrite"};
        ShellRunner.main(arg);
    }

    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        topLevelClass.addJavaDocLine("/**\n"
                + " * <p>\n"
                + " * "+introspectedTable.getRemarks()+"\n"
                + " * </p>\n"
                + " *\n"
                + " * @author fangjiayun\n"
                + " * @since "+f.format(new Date())+"\n"
                + " */");
        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass,
                                       IntrospectedColumn introspectedColumn,
                                       IntrospectedTable introspectedTable,
                                       ModelClassType modelClassType) {
        String classAnnotation =
                "@ApiModel(value=\"" + topLevelClass.getType().getShortName() + "\")";
        if (!topLevelClass.getAnnotations().contains(classAnnotation)) {
            topLevelClass.addAnnotation(classAnnotation);
        }
        String classLombokAnnotation = "@Data";
        if (!topLevelClass.getAnnotations().contains(classLombokAnnotation)) {
            topLevelClass.addAnnotation(classLombokAnnotation);
        }
        String apiModelAnnotationPackage = this.properties.getProperty("apiModelAnnotationPackage");
        String apiModelPropertyAnnotationPackage = this.properties
                .getProperty("apiModelPropertyAnnotationPackage");
        String lombokDataAnnotationPackage = this.properties
                .getProperty("lombokDataAnnotationPackage");
        if (null == apiModelAnnotationPackage) {
            apiModelAnnotationPackage = "io.swagger.annotations.ApiModel";
        }
        if (null == apiModelPropertyAnnotationPackage) {
            apiModelPropertyAnnotationPackage = "io.swagger.annotations.ApiModelProperty";
        }
        if (null == lombokDataAnnotationPackage) {
            lombokDataAnnotationPackage = "lombok.Data";
        }
        topLevelClass.addImportedType(apiModelAnnotationPackage);
        topLevelClass.addImportedType(apiModelPropertyAnnotationPackage);
        topLevelClass.addImportedType(lombokDataAnnotationPackage);
        field.addAnnotation("@ApiModelProperty(value=\"" + introspectedColumn.getRemarks() +
                "\",name=\"" + introspectedColumn.getJavaProperty() +
                "\")");
        return super
                .modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable,
                        modelClassType);
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass,
                                              IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable,
                                              ModelClassType modelClassType) {
        return false;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass,
                                              IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable,
                                              ModelClassType modelClassType) {
        return false;
    }

    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientInsertSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }
}
