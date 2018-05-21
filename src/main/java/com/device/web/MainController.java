package com.device.web;

import com.device.common.enums.UserTypeEnum;
import com.device.config.CheckConfig;
import com.device.entity.BaseResult;
import com.device.entity.Coordinate;
import com.device.entity.Template;
import com.device.entity.User;
import com.device.entity.dto.*;
import com.device.entity.vo.CheckVO;
import com.device.entity.vo.WebCanImageVO;
import com.device.rpc.compareImage;
import com.device.rpc.serialport;
import com.device.service.MainService;
import com.device.service.UserService;
import com.device.utils.ImageUtils;
import com.device.utils.SpringContextUtil;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.opencv.core.Core;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 管理员/操作员
 * 原则：尽量不传图片（传图片url），尽量java端做数据存储，c++只做算法和下位机通信模块
 */

@RequestMapping("main")
@Controller
public class MainController {
    static
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @Resource
    MainService mainService;
    @Resource
    UserService userService;
    @Resource
    ImageUtils imageUtils;
    @Resource
    serialport serial;

    //region --------------------------公共操作--------------------------
    @RequestMapping("/admin")
    public String admin(HttpServletRequest request, @ModelAttribute("errorMsg") String errorMsg, Model model) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !UserTypeEnum.isAdmin(user.getUserTypeName())) {
            return "redirect:/";
        }

        model.addAttribute("user", user);
        model.addAttribute("errorMsg", errorMsg);
        return "main/admin";
    }

    @RequestMapping("/operator")
    public String operator(HttpServletRequest request, @ModelAttribute("errorMsg") String errorMsg, Model model) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || UserTypeEnum.isAdmin(user.getUserTypeName())) {
            return "redirect:/";
        }

        model.addAttribute("user", user);
        model.addAttribute("errorMsg", errorMsg);
        model.addAttribute("templates", this.getTemplateList());
        return "main/operator";
    }

    //todo 展示摄像头实时信息 获取摄像头实时信息？我调驱动还是直接拿结果


    //  todo 截取 摄像头图片，新建模板时需要，输入无，输出：截图路径


    //endregion

    //region --------------------------操作员操作--------------------------

    //模板列表，首页调用
    public List<Template> getTemplateList() {
        return mainService.getTemplateList();
    }

    //模板详情，点选择后
    @RequestMapping("/getTemplate")
    @ResponseBody
    public BaseResult<Template> getTemplate(Template template) {
        Template templateDo = null;
        try {
            templateDo = mainService.findTemplateById(template.getId());
            if (templateDo == null) {
                return new BaseResult<>("不存在该模板", false);
            }
        } catch (Exception e) {
            return new BaseResult<>("不存在该模板", false);
        }

        return new BaseResult<>(templateDo, true);
    }

    // 轮询检测，是否按下检测
    @RequestMapping("/trainingCheck")
    @ResponseBody
    public BaseResult<Integer> trainingCheck() {
        Integer result = 0;
        try {
            //读取参数
            int num = serial.getData(66);
//            int num = 1;
            if (num == 1) {//1 已按下
                Thread.sleep(500);
                if (serial.getData(66) == 1) {//的确是按下
                    return new BaseResult<>(1, "开始检测", true);//1
                } else {
                    return new BaseResult<>(2, "等待检测", true);//0
                }
//                return new BaseResult<>(1, "开始检测", true);//1
            } else if (num == 2) {//2未按下
                return new BaseResult<>(2, "等待检测", true);//0
            } else { //0 故障，查询10次是否仍是0，不是则返回未按下
                int j = 9;
                while (j != 0) {
                    if (serial.getData(66)!=0) {
                    return new BaseResult<>(2, "等待检测", true);//0
                    }
                    j--;
                }
                return new BaseResult<>(0, "设备出现故障，请操作员检查设备", true);//2
            }
        } catch (Exception e) {
            return new BaseResult<>("检测异常", false);
        }
//        return new BaseResult<>(result, true);
    }

    // 检测，轮询，入参 模板id，样图（或者不用传，截图已拿到）。返回 检测id，成功/失败，错误图片集合（检测点序号，路径）
    @RequestMapping("/check")
    @ResponseBody
    public BaseResult<CheckResultDTO> check(CheckVO checkVO) {
        CheckResultDTO checkResult = null;
        try {
            Template templateDo = mainService.findTemplateById(checkVO.getId());
            List<Coordinate> coordinateDo = mainService.findCoordinateByTId(checkVO.getId());
            checkResult = mainService.check(TemplateDTO.convertToTemplateDTO(templateDo, coordinateDo, new TemplateDTO()));
            if (checkResult == null) {
                return new BaseResult<>("检测异常", false);
            }
        } catch (Exception e) {
            return new BaseResult<>("检测异常", false);
        }

        return new BaseResult<>(checkResult, true);
    }

    //查看，返回错误图片 集合，入参（我存储检测结果到DB？）
    @RequestMapping("/getCheckInfo")
    @ResponseBody
    public BaseResult<List<Coordinate>> getCheckInfo(long id) {
        List<Coordinate> coordinates = null;
        try {
            coordinates = mainService.getCheckInfo(id);
            if (coordinates == null) {
                return new BaseResult<>("查看检测信息失败", false);
            }
        } catch (Exception e) {
            return new BaseResult<>("查看检测信息失败", false);
        }

        return new BaseResult<>(coordinates, true);
    }

    //todo 停止的逻辑

    // 操作信息 不存储，前端展示控制，通过检测结果做展示
    public void getOperationLog() {
    }
    //endregion

    //region --------------------------管理员操作--------------------------
    // 新建模板，即返回 截取摄像头图片
    public void newTemplate() {
    }
    //todo 编辑模板？是编辑学习后的，就删除？还是？

    // del 删除所有矩形，是在 B截取检测点 的时候可以操作的，前端控制，无后端利逻辑

    // A 前端控制，存储 在坐标集合，类型参考点
    // B 前端控制，存储 在坐标集合，类型检测点
    // C 分割函数，传入 检测点图片，模式类别，处理的系数。输出 结果图片url，名字
    @RequestMapping("/cutImage")
    @ResponseBody
    public BaseResult<CutImageDTO> cutImage(CutImageDTO cutImageDTO) {
        try {
            cutImageDTO = mainService.cutImage(cutImageDTO);
            if (cutImageDTO == null || cutImageDTO.getCurrentImgUrl().equalsIgnoreCase("")) {
                return new BaseResult<>("分割图片失败，无分割结果图片", false);
            }
        } catch (Exception e) {
            return new BaseResult<>("分割图片失败", false);
        }

        return new BaseResult<>(cutImageDTO, true);
    }

    // D 创建模板，输入 参考点图片id+图片集合，检测点 结果图片+id集合，输出 结果ok/fail。 todo
    @RequestMapping("/createTemplate")
    @ResponseBody
    public BaseResult<Boolean> createTemplate(TemplateDTO templateDTO) {
        Boolean result = false;
        try {
            //  保存模板
            // long tId = mainService.save(templateDTO);
        } catch (Exception e) {
            return new BaseResult<>("创建模板异常", false);
        }

        return new BaseResult<>(result);
    }

    // E /user/list

    // F /user/edit

    //endregion

    //region --------------------------测试--------------------------
    //    生成截图
    @RequestMapping("/testWebCam")
    @ResponseBody
    public BaseResult<WebCamResultDTO> testWebCam(HttpServletRequest request) {
        WebCamResultDTO result = null;
        String fileName = "";
        String filePath = imageUtils.getGalleryPath();

        try {
//            LocalDateTime now = LocalDateTime.now();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
//            String imgName = now.format(formatter);
            String imgName = "muban-1-realtime";
            fileName = imgName + ".jpeg";

            String imgStr = request.getParameter("image");

            if (null != imgStr) {
                imgStr = imgStr.substring(imgStr.indexOf(",") + 1);
            }

            Boolean flag = imageUtils.generateImage(imgStr, filePath, fileName);
            if (!flag) {
                return new BaseResult<>("图片生成异常", false);
            }
            result = new WebCamResultDTO(imgName);
        } catch (Exception e) {
            return new BaseResult<>("图片生成异常", false);
        }

        return new BaseResult<>(result, true);
    }

    //    测试实时摄像头 获取图片
    @GetMapping("/{imgName}")
    public void getImage(@PathVariable("imgName") String imgName, HttpServletResponse response) {
        response.setContentType("image/jpeg");
        String filePath = imageUtils.getGalleryPath() + imgName + ".jpg";

        Path path = Paths.get(filePath);
        try {
            byte[] bytes = Files.readAllBytes(path);
            response.getOutputStream().write(bytes);
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();//获取图片异常
        }
    }

    //  todo多余了   测试实时摄像头 获取图片
    @RequestMapping("/testWebCamImage")
    @ResponseBody
    public BaseResult<WebCanImageVO> getWebCamImage() {
        WebCanImageVO result = new WebCanImageVO();
        try {
            File file = ResourceUtils.getFile("classpath:ts24.lib");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

//            Resource resource = (Resource) new ClassPathResource("countries.xml");
//            File file1 = resource.getFile();
            SpringContextUtil.getBean(CheckConfig.class).setChecked(true); //设值为true
        } catch (Exception e) {
        }

        return new BaseResult<>(result, true);
    }

    /*- 修改检测获取数据逻辑  sxf 20180427  start-*/
    @RequestMapping("/testCheck1")
    @ResponseBody
    public BaseResult<CheckResultDTO> testCheck1(HttpServletRequest request, TemplateDTO templateDTO) {
        CheckResultDTO result = null;
        try {
            Template templateDo = mainService.findTemplateById(templateDTO.getId());
            List<Coordinate> coordinateDo = mainService.findCoordinateByTId((int) templateDTO.getId());
            result = mainService.check(TemplateDTO.convertToTemplateDTO(templateDo, coordinateDo, templateDTO));

            User user = (User) request.getSession().getAttribute("user");
            if (result.isResult()) {
                int qua = user.getQuaNum() + 1;
                user.setQuaNum(qua);
                result.setQuaNum(qua);
                result.setUnQuaNum(user.getUnQuaNum());
            } else {
                int unQua = user.getUnQuaNum() + 1;
                user.setUnQuaNum(unQua);
                result.setQuaNum(user.getQuaNum());
                result.setUnQuaNum(unQua);
            }
            userService.updateQuaAndUnQua(user);
            request.getSession().setAttribute("user", user);

            if (result == null) {
                return new BaseResult<>("检测异常", false);
            }
        } catch (Exception e) {
            return new BaseResult<>("检测异常", false);
        }

        return new BaseResult<>(result, true);
    }

    @RequestMapping("/testCheck")
    @ResponseBody
    public BaseResult<CheckResultDTO> testCheck(HttpServletRequest request, TemplateDTO templateDTO) {
        CheckResultDTO result = null;

        try {
            Template templateDo = mainService.findTemplateById(templateDTO.getId());
            List<Coordinate> coordinateDo = mainService.findCoordinateByTId((int) templateDTO.getId());
            result = mainService.check(TemplateDTO.convertToTemplateDTO(templateDo, coordinateDo, templateDTO));

            User user = (User) request.getSession().getAttribute("user");
           // List<Coordinate> RefsmodelCoordinate = templateDTO.getRefs();
           // Coordinate modelCoordinate=  coordinateDo.get(2);
            List<CheckResultItemDTO> FalseReultList = new ArrayList<>();
            List<CheckResultItemDTO> TrueReultList = new ArrayList<>();
           //遍历 List<Coordinate> coordinateDo
            for ( Coordinate modelCoordinate:coordinateDo){
                CheckResultItemDTO item = new CheckResultItemDTO();
                int x = modelCoordinate.getX();
                int y = modelCoordinate.getY();
                int width = modelCoordinate.getW();
                int height = modelCoordinate.getH();
                float Vaulej = modelCoordinate.getValue();
                String pathj = modelCoordinate.getPathj();
                String pathg = modelCoordinate.getPathg();
                int type = modelCoordinate.getType();
                //对比图片，返回精度值
                double compareValue =  compareImage.getValue( pathj, pathg, Vaulej, x, y, width, height, type);

                //匹配
                if (compareValue >=0 && compareValue < 0.01) {
                    item.setCoordinate(modelCoordinate);
                    TrueReultList.add(item);
                } else {
                    item.setCoordinate(modelCoordinate);
                    //将错误结果坐标返回;
                    item.setCoordinate(modelCoordinate);
                    FalseReultList.add(item);
                }

            }

            //存在一条错误坐标数据 即为匹配失败
            if(FalseReultList.size()>0){
                result.setData(FalseReultList);
                int unQua = user.getUnQuaNum() + 1;
                user.setUnQuaNum(unQua);
                result.setQuaNum(user.getQuaNum());
                result.setUnQuaNum(unQua);
                result.setResult(false);
            }else {
                result.setData(TrueReultList);
                int qua = user.getQuaNum() + 1;
                user.setQuaNum(qua);
                result.setQuaNum(qua);
                result.setUnQuaNum(user.getUnQuaNum());
                result.setResult(true);
            }

            userService.updateQuaAndUnQua(user);
            request.getSession().setAttribute("user", user);
            if (result == null) {
                return new BaseResult<>("检测异常", false);
            }
        } catch (Exception e) {
            return new BaseResult<>("检测异常", false);
        }

        return new BaseResult<>(result, true);
    }


    /*- 修改检测获取数据逻辑  sxf 20180427  end-*/
    /*- 标签打印  sxf 20180427  start-*/
    @RequestMapping("/labelPrint")
    @ResponseBody
    public void labelPrint(HttpServletRequest request) {
      //  String code = request.getSession().getAttribute("code").toString();
        String code ="  LZ-FA-060-01";
        mainService.labelPrint(code);
    }

    /*- 标签打印  sxf 20180427  end-*/
    @RequestMapping("/testCutImage")
    @ResponseBody
    public BaseResult<CutImageDTO> testCutImage(CutImageDTO cutImageDTO) {
        try {
            //把url替换后返回
            cutImageDTO = mainService.cutImage(cutImageDTO);
            if (cutImageDTO == null || cutImageDTO.getResultImgUrl().equalsIgnoreCase("")) {
                return new BaseResult<>("分割图片失败，无分割结果图片", false);
            }
        } catch (Exception e) {
            return new BaseResult<>("分割图片异常", false);
        }

        return new BaseResult<>(cutImageDTO, true);
    }
    //endregion


    //region --------------------------todo del--------------------------
    //    实时摄像头 页面跳转
    @RequestMapping("/toWebCam")
    public String toWebCam() {
        return "test/testWebCam";
    }
    //endregion
}
