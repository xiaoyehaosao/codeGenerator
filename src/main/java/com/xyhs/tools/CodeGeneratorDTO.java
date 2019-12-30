package com.xyhs.tools;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ljp
 * @apiNote
 * @date 15:12 2019/12/23
 **/
@Data
public class CodeGeneratorDTO  implements Serializable {

    private static final long serialVersionUID = 5767294264567514356L;

    /**
     * 文件的作者，注释中的author
     */
    private String fileAuth;

    /**
     * 数据库url
     */

    private  String dataBaseUrl;

    /**
     * 数据库驱动
     */

    private String driverName;

    /**
     * 数据库用户名
     */
    private String userName;

    /**
     * 数据库密码
     */
    private String passWord;

    /**
     * 是否添加swagger配置
     */
    private Boolean isSwagger;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 模块路径
     */
    private String modulePath;

    /**
     * dao-module的绝对路径
     */
    private String daoModulePath;

    /**
     * service-module的绝对路径
     */
    private String serviceModulePath;

    /**
     * serviceImpl-module的绝对路径
     */
    private String serviceImplModulePath;

    /**
     * controller - module的绝对路径
     */
    private String controllerModulePath;

    /**
     * api - module的绝对路径
     */
    private String exportModulePath;

    /**
     * 共用的包名 如： com.xyhs.b2c
     */
    private String parent;

    /**
     * PO 名称
     */
    private String domain;

    /**
     * 数据库表名
     */
    private String [] tables;

    /**
     * 基础基类---需要手动创建
     */
    private String baseDomain;



    public Boolean getSwagger() {
        if(isSwagger == null){
            return false;
        }
        return isSwagger;
    }

    public String getFileAuth() {
        if(StringUtils.isEmpty(fileAuth)){
            return "ljp";

        }
        return fileAuth;
    }

    public String getDaoModulePath() {
        if(StringUtils.isEmpty(daoModulePath)){
            return modulePath+"/"+moduleName+"/"+ moduleName +"-dao";
        }
        return daoModulePath;
    }

    public String getServiceModulePath() {
        if(StringUtils.isEmpty(serviceModulePath)){
            return modulePath+"/"+moduleName+"/"+ moduleName +"-service";
        }
        return serviceModulePath;
    }

    public String getServiceImplModulePath() {
        if(StringUtils.isEmpty(serviceImplModulePath)){
            return modulePath+"/"+moduleName+"/"+ moduleName +"-service";
        }
        return serviceImplModulePath;
    }

    public String getControllerModulePath() {
        if(StringUtils.isEmpty(controllerModulePath)){
            return modulePath+"/"+moduleName+"/"+ moduleName +"-web";
        }
        return controllerModulePath;
    }

    public String getExportModulePath() {
        if(StringUtils.isEmpty(exportModulePath)){
            return modulePath+"/"+moduleName+"/"+ moduleName +"-export";
        }
        return exportModulePath;
    }

    public String getDomain() {
        if(StringUtils.isEmpty(domain)){
            return"entity";
        }
        return domain;
    }
}
