/**
 * Wangyin.com Inc.
 * Copyright (c) 2003-2014 All Rights Reserved.
 */
package generator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.ShellRunner;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 扩展mybaties生成代码,添加字段注释.
 *
 * @author think4ever
 * @version v 0.1 2014/11/3 17:06 Exp $$
 */
public class MySQLGeneratorPlugin extends PluginAdapter {

    /**
     * generate 方法
     */
    public static void generate() {
        String config = MySQLGeneratorPlugin.class.getClassLoader().getResource("mybatis/mybatis-generator-config.xml").getFile();
        String[] arg = {"-configfile", config, "-overwrite"};
        ShellRunner.main(arg);
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        interfaze.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Repository"));
        //interfaze.addImportedType(new FullyQualifiedJavaType("org.springframework.transaction.annotation.Transactional"));
        interfaze.addAnnotation("@Repository");
        //interfaze.addAnnotation("@Transactional");
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        generateToString(introspectedTable, topLevelClass);
        return true;
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        generateToString(introspectedTable, topLevelClass);
        return true;
    }

    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        generateToString(introspectedTable, topLevelClass);
        return true;
    }

    private void generateToString(IntrospectedTable introspectedTable, TopLevelClass topLevelClass) {
        List<Field> fields = topLevelClass.getFields();
        Map<String, Field> map = new HashMap<String, Field>();
        for (Field field : fields) {
            map.put(field.getName(), field);
        }
        List<IntrospectedColumn> columns = introspectedTable.getAllColumns();
        for (IntrospectedColumn column : columns) {
            Field f = map.get(column.getJavaProperty());
            if (f != null) {
                f.getJavaDocLines().clear();
                if (column.getRemarks() != null) {
                    f.addJavaDocLine("/** ");
                    f.addJavaDocLine("* " + column.getRemarks());
                    f.addJavaDocLine("* DB Column Name: " + column.getActualColumnName());
                    f.addJavaDocLine("*/");
                }
            }
        }
    }

    /**
     * This plugin is always valid - no properties are required
     */
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    /**
     * MySQLGeneratorPlugin main method
     *
     * @param args
     */
    public static void main(String[] args) {
        generate();
    }
}
