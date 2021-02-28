package cn.shopping.etass_back.mapper;

import cn.shopping.etass_back.entity.UploadFile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UploadFileMapperTest {

    @Autowired
    UploadFileMapper mapper;

    @Test
    public void getFile(){
        String[] kw = {"123","456"};
        List<Integer> fileId = mapper.getFileId(kw);
        System.out.println(fileId.size());
        List<UploadFile> file = mapper.getFile(fileId);
        System.out.println(file.size());
    }
}