package cn.master.backend.util;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.Collections;

/**
 * @author create by 11's papa on 2022/12/27-14:08
 */
public class AutoGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/alexstrasza?serverTimezone=GMT%2B8&useSSL=FALSE", "root", "admin")
                .globalConfig(builder -> {
                    builder.author("11's papa") // 设置作者
                            .commentDate("yyyy-MM-dd")   // 注释日期
                            .outputDir(System.getProperty("user.dir") + "/backend/src/main/java") // 指定输出目录
                            .disableOpenDir();
                })
                .packageConfig(builder -> {
                    builder.parent("cn.master.backend") // 设置父包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + "/backend/src/main/resources/mappers")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("sys_user") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_") // 设置过滤表前缀
                            // Service 策略配置
                            .serviceBuilder().formatServiceFileName("%sService").formatServiceImplFileName("%sServiceImpl")
                            .entityBuilder().idType(IdType.ASSIGN_ID).enableLombok().enableTableFieldAnnotation()
                            .naming(NamingStrategy.underline_to_camel)  //数据库表映射到实体的命名策略：下划线转驼峰命
                            .columnNaming(NamingStrategy.underline_to_camel)    //数据库表字段映射到实体的命名策略：下划线转驼峰命
                            .controllerBuilder().formatFileName("%sController").enableRestStyle() // 开启RestController注解
                            .mapperBuilder().enableBaseResultMap().superClass(BaseMapper.class).formatMapperFileName("%sMapper").formatXmlFileName("%sMapper");
                })

                .execute();
    }
}
