package com.xyhs.tools;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * @author ljp
 * @apiNote
 * @date 11:52 2019/12/25
 **/
public class SingleModuleCodeGeneratorTest {

    /**
     * 数据表
     */
    private  static String[] tables = new String[]{"delivery_address_info"};

    /**
     * 基类需要手动创建 -----也可以不初始化
     */
    private static final String baseEntity ="com.xyhs.b2c.domain.BaseEntity";

    /**
     * 路径中在src.main.java之后的包命
     */
    private static final String afterJavaPackageName ="com.xyhs.b2c";


    public void codeGenerator()  {
        String path2 =System.getProperty("user.dir");
        System.out.println(path2);
        // 代码生成器
        AutoGenerator mpg  = initDataBase();
        initDaoCode(mpg);
        execute(mpg);
    }

    /**
     * 初始化数据库
     * @return 代码生成器
     */
    private static AutoGenerator initDataBase(){
        AutoGenerator mpg  = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("ljp");
        gc.setOpen(false);
        gc.setEnableCache(false);
        gc.setSwagger2(true);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://182.61.42.210:3306/basecenter?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("Ljp10061087@");
        mpg.setDataSource(dsc);
        return mpg;
    }

    /**
     * Dao层代码生成
     * @param mpg 代码生成器
     */
    private static void initDaoCode(AutoGenerator mpg){
        // 包配置
        PackageConfig pc = new PackageConfig();
        //  pc.setModuleName("dao");
        pc.setParent(afterJavaPackageName);
        pc.setMapper("dao");
        pc.setEntity("domain");
        pc.setXml("xml");
        mpg.setPackageInfo(pc);
    }


    private static void execute(AutoGenerator mpg){

        // 自定义配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setSuperEntityClass(baseEntity);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 写于父类中的公共字段
        String[] array = tables;
        strategy.setInclude(array);
        strategy.setControllerMappingHyphenStyle(true);
        mpg.setStrategy(strategy);
        mpg.execute();
    }


    public static void main(String[] args) {

    }
}
