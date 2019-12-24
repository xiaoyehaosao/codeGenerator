package com.xyhs.tools;



import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ljp
 * @apiNote
 * @date 15:13 2019/12/23
 **/
public class CodeGeneratorTest {


    public enum TemplateEnum {
        /**
         * 优惠券使用范围(1:平台通用类,2:店铺通用类,3:品类通用类,4:SPU使用类)
         */
        XML("xml", 1), ENTITY("entity", 2), MAPPER("mapper", 3), SERVICE("service", 4), CONTROLLER("controller", 4),SERVICE_IMPL("serviceImpl", 4);

        private int type;
        private String name;

        TemplateEnum(String name, int type) {
            this.name = name;
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public String getUpName() {
            return upFirstChart(name);
        }

        public static TemplateEnum getEnumByValue(String value){
            if (StringUtils.isEmpty(value)){
                return null;
            }
            int last = value.lastIndexOf("/");
            int first =  value.indexOf(".");
            value = value.substring(last+1,first);
            for (TemplateEnum enums : TemplateEnum.values()) {
                if (value.equalsIgnoreCase(enums.getName())) {
                    return enums;
                }
            }
            return null;
        }

    }

    /**
     * 数据表
     */
    private  static String[] tables = new String[]{"delivery_address_info"};


    private static String  upFirstChart(String name){
        String firstChar = name.substring(0,1);
        firstChar = firstChar.toUpperCase();
        return  firstChar + name.substring(1,name.length());
    }

    private static String getSuffix(TemplateEnum type,Boolean isUp,PackageConfig pc ){

        switch (type){
            case XML :
                return isUp ? TemplateEnum.XML.getUpName() : TemplateEnum.XML.getName();
            case ENTITY :
                String entity = pc.getEntity();
                if(!StringUtils.isEmpty(entity)){
                    return isUp ? "" : entity;
                }
                return isUp ? "" : TemplateEnum.ENTITY.getName();
            case MAPPER :
                String mapper = pc.getMapper();
                if(!StringUtils.isEmpty(mapper)){
                    return isUp ?  upFirstChart(mapper) : mapper;
                }
                return isUp ?  upFirstChart(mapper): TemplateEnum.MAPPER.getName();
            case SERVICE :
                String service = pc.getService();
                if(!StringUtils.isEmpty(service)){
                    return isUp ? upFirstChart(service) : service;
                }
                return isUp ? TemplateEnum.SERVICE.getUpName() : TemplateEnum.SERVICE.getName();
            case CONTROLLER :
                String controller = pc.getController();
                if(!StringUtils.isEmpty(controller)){
                    return isUp ? upFirstChart(controller) : controller;
                }
                return isUp ? TemplateEnum.CONTROLLER.getUpName() : TemplateEnum.CONTROLLER.getName();
            case SERVICE_IMPL :
               /* String serviceImpl = pc.getServiceImpl();
                if(!StringUtils.isEmpty(serviceImpl)){
                    return isUp ? upFirstChart(serviceImpl) : serviceImpl;
                }*/
                return isUp ? TemplateEnum.SERVICE_IMPL.getUpName() :  pc.getServiceImpl();
            default:
                return "";
        }

    }



    private static AutoGenerator initMpg(CodeGeneratorDTO generatorDTO){
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setAuthor(generatorDTO.getFileAuth());
        gc.setOpen(false);
        gc.setSwagger2(generatorDTO.getSwagger());
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(generatorDTO.getDataBaseUrl());
        dsc.setDriverName(generatorDTO.getDriverName());
        dsc.setUsername(generatorDTO.getUserName());
        dsc.setPassword(generatorDTO.getPassWord());
        mpg.setDataSource(dsc);

        // 包配置
        return mpg;
    }
    private static PackageConfig initPackage(CodeGeneratorDTO generatorDTO){
        PackageConfig pc = new PackageConfig();
        pc.setParent(generatorDTO.getParent());
        pc.setEntity(generatorDTO.getDomain());
        return pc;
    }

    /**
     * 添加模板配置
     * @param focList 配置队列
     * @param templatePath 模板路径
     * @param filePath 输出文件路径
     * @param pc 包配置
     * @return 配置队列
     */
    private static List<FileOutConfig> addFocList(List<FileOutConfig> focList, String templatePath, String filePath, PackageConfig pc ){

        if(StringUtils.isEmpty(templatePath)){
            return new ArrayList<>();
        }
        String suffixPath;
        String upFileSuffix = "";
        String fileType ;
        if(templatePath.contains(TemplateEnum.XML.getName())){
            suffixPath = "/src/main/resources/mapper";
            fileType = StringPool.DOT_XML;
        }else {
            suffixPath = "/src/main/java/";
            String parent = pc.getParent();
            parent = parent.replace(".","/");
            suffixPath = suffixPath + parent;
            TemplateEnum templateEnum = TemplateEnum.getEnumByValue(templatePath);
            if(templateEnum == null){
                return null;
            }
            String path =  getSuffix(templateEnum,false,pc);
            suffixPath =suffixPath + "/"+path;
            suffixPath = suffixPath.replace(".","/");

            upFileSuffix = getSuffix(templateEnum,true,pc);
            fileType = StringPool.DOT_JAVA;
        }

        // 自定义配置会被优先输出
        String finalSuffixPath = suffixPath;
        String finalUpFileSuffix = upFileSuffix;
        String finalFileType = fileType;
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                if(templatePath.contains("service.java.vm")){
                    tableInfo.setServiceName( tableInfo.getEntityName()+finalUpFileSuffix);
                }
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return  filePath + finalSuffixPath+ "/" + tableInfo.getEntityName() + finalUpFileSuffix + finalFileType;
            }
        });
        return focList;
    }


    private static void templateConfig(AutoGenerator mpg, PackageConfig pc,CodeGeneratorDTO generatorDTO){
        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        // 如果模板引擎是 velocity
        String xmlTemplatePath = "/templates/mapper.xml.vm";
        String mapperTemplatePath = "/templates/mapper.java.vm";
        String entityTemplatePath = "/templates/entity.java.vm";
        String serviceTemplatePath = "/templates/service.java.vm";
        String serviceImplTemplatePath = "/templates/serviceImpl.java.vm";
        String controllerTemplatePath = "/templates/controller.java.vm";
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        addFocList(focList,xmlTemplatePath,generatorDTO.getDaoModulePath(),pc);
        addFocList(focList,mapperTemplatePath,generatorDTO.getDaoModulePath(),pc);
        addFocList(focList,entityTemplatePath,generatorDTO.getServiceModulePath(),pc);

        addFocList(focList,serviceTemplatePath, generatorDTO.getServiceModulePath(),pc);
        addFocList(focList,serviceImplTemplatePath,generatorDTO.getServiceImplModulePath(),pc);
        addFocList(focList,controllerTemplatePath,generatorDTO.getControllerModulePath(),pc);

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        templateConfig.setEntity(null);
        templateConfig.setController(null);
        templateConfig.setServiceImpl(null);
        templateConfig.setService(null);
        templateConfig.setMapper(null);
        mpg.setTemplate(templateConfig);
    }

    public static void main(String[] args) {
        CodeGeneratorDTO generatorDTO = new CodeGeneratorDTO();
        generatorDTO.setDataBaseUrl("jdbc:mysql://182.61.42.210:3306/basecenter?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
        generatorDTO.setDriverName("com.mysql.cj.jdbc.Driver");
        generatorDTO.setUserName("root");
        generatorDTO.setPassWord("Ljp10061087@");
        generatorDTO.setParent("com.xyhs.b2c");
        generatorDTO.setModuleName("tradecenter");
        generatorDTO.setModulePath("F:/xyhs");

        // 代码生成器
        AutoGenerator mpg = initMpg(generatorDTO);
        PackageConfig pc = initPackage(generatorDTO);
        mpg.setPackageInfo(pc);
        templateConfig(mpg, pc,generatorDTO);
        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setSuperEntityClass("com.xyhs.b2c.domain.BaseEntity");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 写于父类中的公共字段
        strategy.setSuperEntityColumns("id");
        strategy.setInclude(tables);
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        mpg.execute();
    }

    public static void generateModuleCode( CodeGeneratorDTO generatorDTO){

        AutoGenerator mpg = initMpg(generatorDTO);
        PackageConfig pc = initPackage(generatorDTO);
        mpg.setPackageInfo(pc);
        templateConfig(mpg, pc,generatorDTO);
        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setSuperEntityClass(generatorDTO.getBaseDomain());
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 写于父类中的公共字段
        strategy.setSuperEntityColumns("id");
        strategy.setInclude(generatorDTO.getTables());
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        mpg.execute();
    }

}
