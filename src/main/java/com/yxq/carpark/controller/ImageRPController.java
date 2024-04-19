package com.yxq.carpark.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yxq.carpark.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sun.istack.logging.Logger;
import com.yxq.carpark.dto.FormData;
import com.yxq.carpark.entity.ParkInfo;
import com.yxq.carpark.entity.Result;
import com.yxq.carpark.serviceImpl.PlateRecogniseImpl;

@Controller
public class ImageRPController {

	private static final Logger logger = Logger.getLogger(ImageRPController.class);

	@Autowired
	private ParkinfoService parkinfoservice;
	@Autowired
	private ParkspaceService parkspaceService;
	@Autowired
	private DepotcardService depotcardService;
	@Autowired
	private UserService userService;
	@Autowired
	private IllegalInfoService illegalInfoService;
	@Autowired
	private ParkinfoallService parkinfoallService;
	@Autowired
	private IncomeService incomeService;
	@Autowired
	private CouponService couponService;
	@Autowired
	private BaiduService baiduService;

	@RequestMapping(value = "/fileUpload")
	public String fileLpload() {

		return "upload";
	}
	@ResponseBody
	@RequestMapping(value = "/fileUpload2")
	public Result upload2(@RequestParam("file") MultipartFile file) {

	//	System.out.println(parkId);
		if (file.isEmpty() || file==null) {
			List<String> responses = new ArrayList<String>();
			responses.add("文件不存在");
			logger.info("文件不存在");
			return new Result(-1, responses, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		}
		String fileName = file.getOriginalFilename();
		@SuppressWarnings("unused")
		String suffixName = fileName.substring(fileName.lastIndexOf("."));
		String filePath = "C:\\springUpload\\image\\";
		// fileName = UUID.randomUUID() + suffixName;
		File dest = new File(filePath + fileName);
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}
		try {
			file.transferTo(dest);
			PlateRecognise plateRecognise = new PlateRecogniseImpl();
			String img = filePath + fileName;
			logger.info(img);
			List<String> res = plateRecognise.plateRecognise(filePath + fileName);
			if (res.size() < 1 || res.contains("")) {
				List<String> responses = new ArrayList<String>();
				responses.add("ʶ文件解析异常");
				return new Result(-1, responses, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			}
			Result result = new Result(201, plateRecognise.plateRecognise(filePath + fileName),
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			logger.info(result.toString());
			return result;
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<String> responses = new ArrayList<String>();
		responses.add("上传成功");
	//	return parkInfo;
		return new Result(-1, responses, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}


	@PostMapping(value = "/fileUpload1")
	public String upload(@RequestParam("file") MultipartFile file,@RequestParam("id")int id,HttpServletResponse response,HttpServletRequest request) {
		int parkId=id;
		ParkInfo parkInfo=new ParkInfo();
		FormData formData=new FormData();
		System.out.println(parkId);

		String fileName = file.getOriginalFilename();
		@SuppressWarnings("unused")
		String suffixName = fileName.substring(fileName.lastIndexOf("."));
		String filePath = "C:\\springUpload\\image\\";
		// fileName = UUID.randomUUID() + suffixName;
		File dest = new File(filePath + fileName);
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}

			try {
				file.transferTo(dest);
				String img = filePath + fileName;
				logger.info(img);

				String carNum = baiduService.licensePlate(img);
				Result result = new Result(201, Collections.emptyList(),
						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				logger.info(result.toString());
				if (depotcardService.findCardnumByCarnum(carNum)!=null) {
					formData.setCardNum(depotcardService.findCardnumByCarnum(carNum));
					formData.setCarNum(carNum);
					formData.setParkNum(parkId);
					formData.setParkTem(0);
				}else {
					formData.setCardNum("");
					formData.setCarNum(carNum);
					formData.setParkNum(parkId);
					formData.setParkTem(1);
				}

				parkinfoservice.saveParkinfo(formData);
				parkspaceService.changeStatus(parkId, 1);
				//return "index";
				return "redirect:/index/toindex";
				//return Msg.success();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return "redirect:/index/toindex";
	}


	@RequestMapping(value = "/fileUpload3")
	public String upload3(@RequestParam("file") MultipartFile file,@RequestParam("id")int id) throws Exception, IOException {
		int parkId=id;
		ParkInfo parkInfo=new ParkInfo();
		FormData formData=new FormData();
		System.out.println(parkId);

		String fileName = file.getOriginalFilename();
		@SuppressWarnings("unused")
		String suffixName = fileName.substring(fileName.lastIndexOf("."));
		String filePath = "C:\\springUpload\\image\\";
		// fileName = UUID.randomUUID() + suffixName;
		File dest = new File(filePath + fileName);
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}

			file.transferTo(dest);
			PlateRecognise plateRecognise = new PlateRecogniseImpl();
			String img = filePath + fileName;
			logger.info(img);
			List<String> res = plateRecognise.plateRecognise(filePath + fileName);
			String carNum=res.get(0);
			Result result = new Result(201, plateRecognise.plateRecognise(filePath + fileName),
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			logger.info(result.toString());
			if (depotcardService.findCardnumByCarnum(carNum)!=null) {
				formData.setCardNum(depotcardService.findCardnumByCarnum(carNum));
				formData.setCarNum(carNum);
				formData.setParkNum(parkId);
				formData.setParkTem(0);
			}else {
				formData.setCardNum("");
				formData.setCarNum(carNum);
				formData.setParkNum(parkId);
				formData.setParkTem(1);
			}

			parkinfoservice.saveParkinfo(formData);
			parkspaceService.changeStatus(parkId, 1);
			//return "index";
			return "redirect:/index/toindex";
			//return Msg.success();

	}


	@RequestMapping(method = RequestMethod.GET, value = "/plateRecognise")
	public List<String> plateRecognise(@RequestParam("imgPath") String imgPath) {
		PlateRecognise plateRecognise = new PlateRecogniseImpl();
		return plateRecognise.plateRecognise(imgPath);
	}
}
