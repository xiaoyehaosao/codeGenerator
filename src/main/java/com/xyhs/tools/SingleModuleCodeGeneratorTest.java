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
     * 初始化数据库
     * @return 代码生成器
     */
    private static AutoGenerator initDataBase(CodeGeneratorDTO generatorDTO){
        AutoGenerator mpg  = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = generatorDTO.getModulePath()+"/"+generatorDTO.getModuleName();
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor(generatorDTO.getFileAuth());
        gc.setSwagger2(generatorDTO.getSwagger());
        mpg.setGlobalConfig(gc);
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(generatorDTO.getDataBaseUrl());
        dsc.setDriverName(generatorDTO.getDriverName());
        dsc.setUsername(generatorDTO.getUserName());
        dsc.setPassword(generatorDTO.getPassWord());
        mpg.setDataSource(dsc);
        return mpg;
    }

    /**
     * Dao层代码生成
     * @param mpg 代码生成器
     */
    private static void initDaoCode(AutoGenerator mpg,CodeGeneratorDTO generatorDTO){
        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(generatorDTO.getParent());
        mpg.setPackageInfo(pc);
    }


    private static void execute(AutoGenerator mpg,CodeGeneratorDTO generatorDTO){

        // 自定义配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setSuperEntityClass(generatorDTO.getBaseDomain());
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 写于父类中的公共字段
        strategy.setInclude(generatorDTO.getTables());
        strategy.setControllerMappingHyphenStyle(true);
        mpg.setStrategy(strategy);
        mpg.execute();
    }


    public static void main(String[] args) {

       String[] tables = new String[]{"delivery_address_info"};

        CodeGeneratorDTO generatorDTO = new CodeGeneratorDTO();
        generatorDTO.setDataBaseUrl("jdbc:mysql://182.61.42.210:3306/basecenter?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
        generatorDTO.setDriverName("com.mysql.cj.jdbc.Driver");
        generatorDTO.setUserName("root");
        generatorDTO.setPassWord("Ljp10061087@");
        generatorDTO.setParent("com.xyhs.tools");
        generatorDTO.setModuleName("codeGenerator");
        generatorDTO.setModulePath("F:/xyhs");
        generatorDTO.setTables(tables);
        generatorDTO.setBaseDomain("com.xyhs.b2c.tools.BaseEntity");

        AutoGenerator mpg  = initDataBase(generatorDTO);
        initDaoCode(mpg,generatorDTO);
        execute(mpg,generatorDTO);

    }
}
