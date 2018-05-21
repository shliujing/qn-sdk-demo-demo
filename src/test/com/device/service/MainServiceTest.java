package com.device.service;

import com.alibaba.fastjson.JSON;
import com.device.entity.Coordinate;
import com.device.entity.dto.CheckResultDTO;
import com.device.entity.dto.TemplateDTO;
import com.device.utils.ImageUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MainServiceTest {

    @Autowired
    private MainService mainService;
    @Autowired
    private ImageUtils imageUtils;

    //    @Test
    public void check() {
//      数据
//        {
//            "checks": [
//            {
//                "h": 56,
//                    "id": 1,
//                    "imgUrl": "muban-1-F01",
//                    "name": "F01",
//                    "tId": 1,
//                    "type": 2,
//                    "value": 0.0,
//                    "w": 64,
//                    "x": 268,
//                    "y": 433
//            },
//            {
//                "h": 61,
//                    "id": 2,
//                    "imgUrl": "muban-1-F02",
//                    "name": "F02",
//                    "tId": 1,
//                    "type": 2,
//                    "value": 0.0,
//                    "w": 76,
//                    "x": 262,
//                    "y": 597
//            },
//            {
//                "h": 135,
//                    "id": 3,
//                    "imgUrl": "muban-1-F03",
//                    "name": "F03",
//                    "tId": 1,
//                    "type": 2,
//                    "value": 0.0,
//                    "w": 139,
//                    "x": 397,
//                    "y": 339
//            }
//  ],
//            "currentImgUrl": "20180422221339",
//                "id": 1,
//                "imgUrl": "muban-1-template",
//                "name": "模板1",
//                "refs": []
//        }

//        初始参数
        String in = "{\"checks\":[{\"h\":56,\"id\":1,\"imgUrl\":\"muban-1-F01\",\"name\":\"F01\",\"tId\":1,\"type\":2,\"value\":0.0,\"w\":64,\"x\":268,\"y\":433},{\"h\":61,\"id\":2,\"imgUrl\":\"muban-1-F02\",\"name\":\"F02\",\"tId\":1,\"type\":2,\"value\":0.0,\"w\":76,\"x\":262,\"y\":597},{\"h\":135,\"id\":3,\"imgUrl\":\"muban-1-F03\",\"name\":\"F03\",\"tId\":1,\"type\":2,\"value\":0.0,\"w\":139,\"x\":397,\"y\":339}],\"currentImgUrl\":\"20180422221339\",\"id\":1,\"imgUrl\":\"muban-1-template\",\"name\":\"模板1\",\"refs\":[]}";

        TemplateDTO templateDTO = JSON.parseObject(in, TemplateDTO.class);
        CheckResultDTO result = mainService.check(templateDTO);
        System.out.println(result.isResult());
    }

    //    @Test
    public void cutImage() {

    }

    @Test
    public void getImg() throws IOException {
        String imgName = "muban-1-template";
        String filePath = imageUtils.getGalleryPath() + imgName + ".jpg";
        Path path = Paths.get(filePath);
        try {
            byte[] bytes = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();//获取图片异常
        }
    }

    @Test
    public void insertCoordinate() throws IOException {
        try {
//            Coordinate item = new Coordinate();
//            item.setId(1);
//            item.setH(56);
//            item.setImgUrl("muban-1-F01");
//            item.setName("F01");
//            item.setTId(1);
//            item.setType(1);
//            item.setValue(2);
//            item.setW(64);
//            item.setX(268);
//            item.setY(433);
//            mainService.saveCoordinate(item);
//            List<Coordinate> coordinateDo = mainService.findCoordinateByTId(1);

            String jsonStr = "[{\"value\":104,\"x\":221,\"y\":137,\"w\":25,\"h\":22},{\"value\":66,\"x\":219,\"y\":176,\"w\":28,\"h\":22},{\"value\":82,\"x\":220,\"y\":212,\"w\":28,\"h\":22},{\"value\":332,\"x\":214,\"y\":35,\"w\":35,\"h\":15},}\"value\":431,\"x\":175,\"y\":35,\"w\":35,\"h\":22}]";
//            String jsonSt1r =JSON.toJSONString(mainService.findCoordinateByTId(1));
            List<Coordinate> arrays = JSON.parseArray(jsonStr, Coordinate.class);

            int len = arrays.size()+1;
            for (int i = 1; i < len; i++) {
                Coordinate item = arrays.get(i-1);
                //  item.setId(1);
                int type = item.getValue()>300?2:1;
                item.setType(type);
                item.setTId(1);

                item.setImgUrl("muban-1-F"+i);
                item.setName("F"+i);
//                mainService.saveCoordinate(item);
            }
            List<Coordinate> coordinateDo = mainService.findCoordinateByTId(1);


        } catch (Exception e) {
            e.printStackTrace();//获取图片异常
        }
    }
}