package cn.shopping.etass_back;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.shopping.etass_back.mapper")
public class EtassBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(EtassBackApplication.class, args);
    }

}
