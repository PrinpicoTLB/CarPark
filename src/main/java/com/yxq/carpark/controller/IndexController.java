package com.yxq.carpark.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.yxq.carpark.entity.*;
import com.yxq.carpark.vo.IncomeDataVo;
import javafx.scene.chart.Chart;
import javafx.scene.chart.PieChart;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.XDDFColor;
import org.apache.poi.xddf.usermodel.XDDFShapeProperties;
import org.apache.poi.xddf.usermodel.XDDFSolidFillProperties;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xddf.usermodel.text.XDDFRunProperties;
import org.apache.poi.xddf.usermodel.text.XDDFTextBody;
import org.apache.poi.xddf.usermodel.text.XDDFTextParagraph;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yxq.carpark.dto.ChargeData;
import com.yxq.carpark.dto.CouponData;
import com.yxq.carpark.dto.DepotcardManagerData;
import com.yxq.carpark.dto.EmailData;
import com.yxq.carpark.dto.IncomeData;
import com.yxq.carpark.dto.ParkinfoallData;
import com.yxq.carpark.service.CouponService;
import com.yxq.carpark.service.DepotInfoService;
import com.yxq.carpark.service.DepotcardService;
import com.yxq.carpark.service.EmailService;
import com.yxq.carpark.service.IllegalInfoService;
import com.yxq.carpark.service.IncomeService;
import com.yxq.carpark.service.ParkinfoallService;
import com.yxq.carpark.service.ParkspaceService;
import com.yxq.carpark.service.UserService;
import com.yxq.carpark.utils.Constants;
import com.yxq.carpark.utils.Msg;
import com.yxq.carpark.utils.PageUtil;


@Controller
@Slf4j
public class IndexController {

	@Autowired
	private UserService userService;
	@Autowired
	private ParkspaceService parkspaceService;
	@Autowired
	private DepotcardService depotcardService;
	@Autowired
	private ParkinfoallService parkinfoallService;
	@Autowired
	private IllegalInfoService illegalInfoService;
	@Autowired
	private IncomeService incomeService;
	@Autowired
	private CouponService couponService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private DepotInfoService depotInfoService;

	@RequestMapping("/index/toindex")
	public String toIndex(Model model,HttpSession session,@RequestParam(value="tag",required=false) Integer tag,@RequestParam(value="page",required=false) Integer page)
	{
		if(tag==null)
		{
			tag=0;
		}
		if(page==null)
		{
			page=0;
		}
		if(page!=0)
		{
			page-=1;
		}
		PageUtil<ParkSpace> pageUtil=new PageUtil<ParkSpace>();
		pageUtil.setCurrent(page);
		pageUtil.setTag(tag);
		User user1=(User) session.getAttribute("user");
		List<ParkSpace> list=new ArrayList<ParkSpace>();
		int count=0;
		int countPage=0;
		if(user1!=null)
		{
			if(user1.getRole()==1)
			{
				if(tag==0)
				{
				list=parkspaceService.findAllParkspace(page*10,Constants.PAGESIZE);
				}
				else
				{
				list=parkspaceService.findParkspaceByTag(tag,page*10,Constants.PAGESIZE);
				}
				count=parkspaceService.findAllParkspaceCount(tag);
			}else if(user1.getRole()==2)
			{
				if(tag==0)
				{
				list=parkspaceService.findAllParkspace(page*10,Constants.PAGESIZE);
				}
				else
				{
				list=parkspaceService.findParkspaceByTag(tag,page*10,Constants.PAGESIZE);
				}
				count=parkspaceService.findAllParkspaceCount(tag);
			}else {

			}
			countPage=count/10;
			if(count%10!=0)
			{
				countPage+=1;
			}
			pageUtil.setCountPage(countPage);
			pageUtil.setCount(count);
			pageUtil.setPages(list);
			model.addAttribute("parkspaces", pageUtil);
			return "index";
		}else{
			return "login";
		}
	}

	@RequestMapping("/index/findAllUser")
	public String findAllUser(Model model, HttpSession session,@RequestParam(value="tag",required=false) Integer tag,@RequestParam(value="page",required=false) Integer page)
	{
		if(tag==null)
		{
			tag=0;
		}
		if(page==null)
		{
			page=0;
		}
		if(page!=0)
		{
			page-=1;
		}
		List<User> users=null;
		User user1 = (User) session.getAttribute("user");
		PageUtil<User> pageUtil=new PageUtil<User>();
		int count=0;
		int countPage=0;
		if (user1 != null) {
			if (user1.getRole() == 1) {
				users=userService.findUsersByRole(tag.intValue(),page*10,Constants.PAGESIZE);
				count=userService.findAllUserCount(tag);
			} else if (user1.getRole() == 2) {
				users=userService.findUsersByRoleMan(tag.intValue(),page*10,Constants.PAGESIZE);
				count=userService.findAllUserCountMan(tag);
			} else if (user1.getRole() == 3) {
				users=new ArrayList<User>();
				user1=userService.findUserById(user1.getId());
				users.add(user1);
				count=1;
			} else if (user1.getRole() == 4) {

			} else {

			}
		}
		System.out.println("count:" + count);
		countPage=count/10;
		if(count%10!=0)
		{
			countPage+=1;
		}
		pageUtil.setCountPage(countPage);
		pageUtil.setPage(page + 1);
		pageUtil.setCount(count);
		pageUtil.setPages(users);
		pageUtil.setCurrent(page);
		model.addAttribute("users", pageUtil);
		return "user";
	}

	@RequestMapping("/index/findAllDepot")
	public String findAllDepot(Model model, HttpSession session,@RequestParam(value="page",required=false) Integer page,@RequestParam(value="name",required=false) String name)
	{

		if(page==null)
		{
			page=0;
		}
		if(page!=0)
		{
			page--;
		}
		if(name==null)
		{
			name="";
		}
		List<ParkinfoallData> parkinfoallDatas=null;
		PageUtil<ParkinfoallData> pageUtil=new PageUtil<ParkinfoallData>();
		User user1 = (User) session.getAttribute("user");
		int count=0;
		int countPage=0;
		if (user1 != null) {
			if (user1.getRole() == 1) {
				parkinfoallDatas=parkinfoallService.findAllParkinfoallByLike(page*10,Constants.PAGESIZE,name);
				count=parkinfoallService.findAllParkinfoallCount(name);
			} else if (user1.getRole() == 2) {
				parkinfoallDatas=parkinfoallService.findAllParkinfoallByLike(page*10,Constants.PAGESIZE,name);
				count=parkinfoallService.findAllParkinfoallCount(name);
			} else if (user1.getRole() == 3) {
				Depotcard depotcard=depotcardService.findByCardid(user1.getCardid());
				parkinfoallDatas=parkinfoallService.findByCardNumByPage(page*10,Constants.PAGESIZE,depotcard.getCardnum(),name);
				List<ParkinfoallData> parkinfoallDatas1=parkinfoallService.findByCardNum(depotcard.getCardnum(),name);
				count=parkinfoallDatas1.size();
			} else if (user1.getRole() == 4) {

			} else {

			}
		}
		countPage=count/10;
		if(count%10!=0)
		{
			countPage++;
		}
		pageUtil.setExtra(name);
		pageUtil.setPages(parkinfoallDatas);
		pageUtil.setCount(count);
		pageUtil.setCurrent(page);
		pageUtil.setCountPage(countPage);
		model.addAttribute("parkinfoallDatas", pageUtil);
		return "depot";
	}

	@RequestMapping("/index/findAllIllegalinfo")
	public String findAllIllegalinfo(Model model, HttpSession session,@RequestParam(value="page",required=false) Integer page,@RequestParam(value="name",required=false)String name)
	{
		System.out.println("用户名:" + name);
		if(page==null)
		{
			page=0;
		}
		if(page!=0)
		{
			page--;
		}
		if(name==null)
		{
			name="";
		}
		List<IllegalInfo> illegalInfo=null;
		PageUtil<IllegalInfo> pageUtil=new PageUtil<IllegalInfo>();
		User user1 = (User) session.getAttribute("user");
		int count=0;
		int countPage=0;
		if (user1 != null) {
			if (user1.getRole() == 1) {
				illegalInfo=illegalInfoService.findAllIllegalInfo(page*10,Constants.PAGESIZE,name);
				count=illegalInfoService.findAllIllegalInfoCount(name);
			} else if (user1.getRole() == 2) {
				illegalInfo=illegalInfoService.findAllIllegalInfo(page*10,Constants.PAGESIZE,name);
				count=illegalInfoService.findAllIllegalInfoCount(name);
			} else if (user1.getRole() == 3) {
				// 获取用户卡号
				Depotcard depotcard = depotcardService.findByCardid(user1.getCardid());
				illegalInfo = illegalInfoService.findAllByCardName(depotcard.getCardnum());
				count=illegalInfo.size();
			}
		}
		System.out.println("illegalInfo :" + illegalInfo.size());
		System.out.println("illegalInfo count:" + count);
		countPage=count/10;
		if(count%10!=0)
		{
			countPage++;
		}
		pageUtil.setExtra(name);
		pageUtil.setPages(illegalInfo);
		pageUtil.setCount(count);
		pageUtil.setCountPage(countPage);
		pageUtil.setCurrent(page);
		model.addAttribute("illegalInfo", pageUtil);
		return "illegalinfo";
	}

	@RequestMapping("/index/toDepotcardIndex")
	public String findAllDepotcard(Model model, HttpSession session,@RequestParam(value="cardnum",required=false)String cardnum,@RequestParam(value="page",required=false) Integer page)
	{
		if(page==null)
		{
			page=0;
		}
		if(page!=0)
		{
			page--;
		}
		List<DepotcardManagerData> depotcardManagerDatas = null;
		PageUtil<DepotcardManagerData> pageUtil=new PageUtil<DepotcardManagerData>();
		int count =0;
		int countPage=0;
		User user1 = (User) session.getAttribute("user");
		log.info("停车卡管理: " + user1.toString());
		if(cardnum==null)
		{
			cardnum="";
		}
		if (user1 != null) {
			if (user1.getRole() == 1) {
				depotcardManagerDatas = depotcardService.findAllDepotcard(cardnum,page.intValue()*10,Constants.PAGESIZE);
				count=depotcardService.findAllDepotcardCount(cardnum);
			} else if (user1.getRole() == 2) {
				depotcardManagerDatas = depotcardService.findAllDepotcard(cardnum,page.intValue()*10,Constants.PAGESIZE);
				count=depotcardService.findAllDepotcardCount(cardnum);
			} else if (user1.getRole() == 3) {
				depotcardManagerDatas = depotcardService.findByCardId(user1.getCardid());
				log.info("停车卡数据：" + depotcardManagerDatas);
				count=depotcardManagerDatas.size();
			}
		}
		countPage=count/10;
		if(count%10>0)
		{
			countPage++;
		}
		pageUtil.setExtra(cardnum);
		pageUtil.setCurrent(page);
		pageUtil.setCount(count);
		pageUtil.setCountPage(countPage);
		pageUtil.setPages(depotcardManagerDatas);
		model.addAttribute("depotcardManagerDatas", pageUtil);
		return "depotcard";
	}

	@RequestMapping("/index/findAllCoupon")
	public String findAllCoupon(Model model, HttpSession session,@RequestParam(value="page",required=false) Integer page,@RequestParam(value="name",required=false) String name)
	{
		if(page==null)
		{
			page=0;
		}
		if(page!=0)
		{
			page--;
		}
		List<CouponData> list = null;
		PageUtil<CouponData> pageUtil=new PageUtil<CouponData>();
		int count =0;
		int countPage=0;
		User user1 = (User) session.getAttribute("user");
		if(name==null)
		{
			name="";
		}
		if (user1 != null) {
			if (user1.getRole() == 1) {
				list = couponService.findAllCoupon(page.intValue()*10,Constants.PAGESIZE,name);
				count=couponService.findAllDepotcardCount(name);
			} else if (user1.getRole() == 2) {
				list = couponService.findAllCoupon(page.intValue()*10,Constants.PAGESIZE,name);
				count=couponService.findAllDepotcardCount(name);
			} else if (user1.getRole() == 3) {
				Depotcard depotcard = depotcardService.findByCardid(user1.getCardid());
				if (depotcard != null) {
					list = couponService.findAllCouponByCardNum(depotcard.getCardnum(),name);
					count=list.size();
				}

			} else if (user1.getRole() == 4) {

			} else {

			}
		}
		countPage=count/10;
		if(count%10>0)
		{
			countPage++;
		}
		pageUtil.setExtra(name);
		pageUtil.setCurrent(page);
		pageUtil.setCount(count);
		pageUtil.setCountPage(countPage);
		pageUtil.setPages(list);
		model.addAttribute("couponDatas", pageUtil);
		return "coupon";
	}
	@RequestMapping("/index/findAllIncome")
	public String findAllIncome(Model model, HttpSession session,
								@RequestParam(value="page", required=false) Integer page,
								@RequestParam(value="startTime",required=false)String startTime,
								@RequestParam(value="endTime",required=false)String endTime,
								@RequestParam(value="content",required=false)String content,
								@RequestParam(value="num",required=false)Integer num,
								@RequestParam(value="tag",required=false)Integer tag)
	{
		if(page==null)
		{
			page=0;
		}
		if(page!=0)
		{
			page--;
		}
		if(content==null)
		{
			content="";
		}
		if(startTime==null)
		{
			startTime="";
		}
		if(endTime==null)
		{
			endTime="";
		}
		if(num==null)
		{
			num=9;
		}
		if(tag == null) {
			tag = 4;
		}
		List<IncomeData> incomes=null;
		List<IncomeDataVo> incomeDataVos = new ArrayList<>();
		List<IncomeData> incomes1=null;
		User user1 = (User) session.getAttribute("user");
		PageUtil<IncomeDataVo> pageUtil=new PageUtil<IncomeDataVo>();
		int count =0;
		int countPage=0;
		double countMoney=0;
		if (user1 != null) {
			if (user1.getRole() == 1) {
				incomes = incomeService.findAllIncome(page*10,Constants.PAGESIZE,content,startTime,endTime,num, tag);
				if(incomes != null) {
					System.out.println("incomes size: " + incomes.size());
					incomes.forEach(incomeData -> {
						IncomeDataVo incomeDataVo = new IncomeDataVo();
						BeanUtils.copyProperties(incomeData, incomeDataVo);
						String duration = formatDuration(incomeData.getDuration());
						incomeDataVo.setDuration(duration);
						incomeDataVos.add(incomeDataVo);
					});
				}
				incomes1 = incomeService.findAllIncome(content,startTime,endTime,num);

				if(incomes1.size()>0){
				for(IncomeData incomeData:incomes1)
				{
					countMoney+=incomeData.getMoney();
				}
				}
				count=incomeService.findAllIncomeCount(content,startTime,endTime,num);
				countPage=count/10;
				if(count%10!=0)
				{
					countPage++;
				}
				pageUtil.setCurrent(page);
				pageUtil.setCount(count);
				pageUtil.setCountPage(countPage);
				pageUtil.setPages(incomeDataVos);
			}
		}
		model.addAttribute("incomes", pageUtil);
		model.addAttribute("countMoney", countMoney);
		return "income";
	}

	public static String formatDuration(long minutes) {
		int days = (int) (minutes / (24 * 60)); // 计算全天数
		int hours = (int) ((minutes % (24 * 60)) / 60); // 计算剩余的小时数
		int remainingMinutes = (int) (minutes % 60); // 计算剩余的分钟数

		return String.format("%d天%d时%d分", days, hours, remainingMinutes);
	}
	@RequestMapping("/index/findAllEmail")
	public String findAllEmail(Model model, HttpSession session,@RequestParam(value="page", required=false) Integer page,@RequestParam(value="content", required=false) String content,@RequestParam(value="tag", required=false) Integer tag)
	{
		if(page==null)
		{
			page=0;
		}
		if(page!=0)
		{
			page--;
		}
		if(content==null)
		{
			content="";
		}
		if(tag==null)
		{
			tag=4;
		}
		List<EmailData> emails=null;
		PageUtil<EmailData> pageUtil=new PageUtil<EmailData>();
		int count =0;
		int countPage=0;
		User user1 = (User) session.getAttribute("user");

		if (user1.getRole()==1||user1.getRole()==2) {
			//emails=emailService.findByToId(1);
			emails = emailService.findByPage(page*10, Constants.PAGESIZE,1, content, user1.getRole(), tag);
		}else {
			// emails=emailService.findByToId(user1.getId());
			emails = emailService.findByPage(page*10, Constants.PAGESIZE,user1.getId(), content, user1.getRole(), tag);
		}
		//emails=emailService.findByToId(1);
		List<EmailData> emailDatas=new ArrayList<EmailData>();
		for(EmailData emailData:emails)
		{
			if(user1.getRole()==3)
			{
				emailData.setIsRead(emailData.getUserisread());
			}
			else{
				emailData.setIsRead(emailData.getManagerisread());
			}
			User sendUser=userService.findUserById(emailData.getSendid());
			if (sendUser == null) {
				continue;
			}
			if(emailData.getToid()!=0)
			{
			User toUser=userService.findUserById(emailData.getToid());
			emailData.setToUsername(toUser.getUsername());
			}else
			{
				emailData.setToUsername("");
			}
			if(user1.getId()==emailData.getSendid())
			{
				emailData.setIsSend(1);
			}
			emailData.setSendUsername(sendUser.getUsername());
			emailDatas.add(emailData);
		}
		count=emailService.findAllEmailCountByUser(user1.getId(),user1.getRole());
		countPage=count/10;
		if(count%10!=0)
			{
				countPage++;
			}
		pageUtil.setCurrent(page);
		pageUtil.setCount(count);
		pageUtil.setCountPage(countPage);
		pageUtil.setPages(emailDatas);
		model.addAttribute("emails", pageUtil);
		model.addAttribute("tag", tag);
		return "email";
	}

	@RequestMapping("/index/system")
	public String system(Model model, HttpSession session)
	{
		return "system";
	}

	@RequestMapping("/index/incomeCharts")
	public String incomeCharts()
	{
		return "incomeCharts";
	}

	@RequestMapping("/index/line")
	public String line()
	{
		return "indexcopy";
	}

	@ResponseBody
	@RequestMapping("/index/incomeCharts1")
	public IncomeCharts incomeCharts1()
	{
		int weixin=incomeService.findPayByType(0);
		int zhifubao=incomeService.findPayByType(1);
		int cash=incomeService.findPayByType(2);
		IncomeCharts incomeCharts=new IncomeCharts();
		incomeCharts.setCash(cash);
		incomeCharts.setWeixin(weixin);
		incomeCharts.setZhifubao(zhifubao);
		return incomeCharts;
	}

	@ResponseBody
	@RequestMapping("/index/incomeCharts2")
	public IncomeCharts incomeCharts2()
	{
		// 获取当前时间和7天前
		LocalDateTime now = LocalDateTime.now();
		String nowFormat = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		LocalDateTime weekAgo = now.minusDays(7);
		String weekAgoFormat = weekAgo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		int weixin=incomeService.findPayByType2(weekAgoFormat,nowFormat,0);
		int zhifubao=incomeService.findPayByType2(weekAgoFormat,nowFormat,1);
		int cash=incomeService.findPayByType2(weekAgoFormat,nowFormat,2);
		int card=incomeService.findPayByType2(weekAgoFormat,nowFormat,9);
		IncomeCharts incomeCharts=new IncomeCharts();
		incomeCharts.setCash(cash);
		incomeCharts.setWeixin(weixin);
		incomeCharts.setZhifubao(zhifubao);
		incomeCharts.setCard(card);
		return incomeCharts;
	}

	@ResponseBody
	@RequestMapping("/index/incomeSourceCharts")
	public IncomeCharts incomeSourceCharts()
	{
		// 获取当前时间和7天前
		LocalDateTime now = LocalDateTime.now();
		String nowFormat = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		LocalDateTime weekAgo = now.minusDays(7);
		String weekAgoFormat = weekAgo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		int card=incomeService.findSourceByType(weekAgoFormat,nowFormat,0);
		int car=incomeService.findSourceByType(weekAgoFormat,nowFormat,1);
		IncomeCharts incomeCharts=new IncomeCharts();
		incomeCharts.setCard(card);
		incomeCharts.setCar(car);
		return incomeCharts;
	}

	@ResponseBody
	@RequestMapping("/index/incomeWeekCharts")
	public List<IncomeDay> incomeWeekCharts()
	{
		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		List<IncomeDay> weekIncome = new ArrayList<>();

		for (int i = 6; i >= 0; i--) {
			LocalDate date = today.minusDays(i);
			LocalDateTime startOfDay = date.atStartOfDay();
			LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

			String start = startOfDay.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			String end = endOfDay.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

			int dailyIncome = incomeService.findDailyIncome(start, end);

			weekIncome.add(new IncomeDay(date.format(formatter), dailyIncome));
		}
		return weekIncome;
	}

	@ResponseBody
	@RequestMapping("/index/line1")
	public NowParkspace line1() {
		int ispark=parkspaceService.findNowParkspace(1);
		int nopark=100-ispark;
		NowParkspace nowParkspace=new NowParkspace();
		nowParkspace.setIspark(ispark);
		nowParkspace.setNopark(nopark);
		return nowParkspace;

	}

	@RequestMapping("/index/exit")
	public String exit(Model model, HttpSession session)
	{
		session.removeAttribute("user");
		return "login";
	}
	@RequestMapping("/index/exportIncome")
	public void exportIncome(@RequestParam(value="datetimepickerStart", required=false) String datetimepickerStart,
							 @RequestParam(value="datetimepickerEnd", required=false) String datetimepickerEnd, HttpServletResponse response) {
		if (datetimepickerStart == null) {
			datetimepickerStart = "";
		}
		if (datetimepickerEnd == null) {
			datetimepickerEnd = "";
		}
		System.out.println("datetimepickerEnd:" + datetimepickerEnd);
		List<IncomeData> list = incomeService.findAllIncome("", datetimepickerStart, datetimepickerEnd, 9);
		// 获取当前时间和7天前
		LocalDateTime now = LocalDateTime.now();
		String nowFormat = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		LocalDateTime weekAgo = now.minusDays(7);
		String weekAgoFormat = weekAgo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		List<IncomeData> pieDates = incomeService.findAllIncome("", weekAgoFormat, nowFormat, 9);
		Map<String, Double> paymentMethodData = collectDataByPaymentMethod(pieDates);
		Map<String, Double> sourceData = collectDataBySource(pieDates);
		Map<String, Double> dailyIncome = collectDailyIncome(pieDates);

		// 创建HSSFWorkbook对象(excel的文档对象)
//		HSSFWorkbook wb = new HSSFWorkbook();
//		// 建立新的sheet对象（excel的表单）
//		HSSFSheet sheet = wb.createSheet("收入详情");
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("收入详情");
		// 创建饼图
		int chartInitialRow = 0;
		createPieChart(wb, sheet, paymentMethodData, chartInitialRow, 0, "支付方式");
		createPieChart(wb, sheet, sourceData, chartInitialRow, 6, "收入来源");
		// 创建条形图
		// 在图表后计算开始新表格的行索引
		int barChartInitialRow = 22;
		createBarChart(wb, sheet, dailyIncome, barChartInitialRow, 0, "每日收入");
		// 准备插入表格数据的起始行
		int tableStartRow = barChartInitialRow + 22;
		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0至65535之间的任何一行
		XSSFRow row1 = sheet.createRow(tableStartRow);
		// 创建单元格（excel的单元格，位置为0至255之间的任何一个）
		XSSFCell cell = row1.createCell(0);
		// 设置单元格内容
		cell.setCellValue("收入详情");
		// 合并单元格CellRangeAddress构造参数为：起始行，结束行，起始列，结束列
		sheet.addMergedRegion(new CellRangeAddress(tableStartRow, tableStartRow, 0, 8));
		// 在sheet里创建第二行
		XSSFRow row2 = sheet.createRow(tableStartRow + 1);
		// 创建单元格并设置单元格内容
		row2.createCell(0).setCellValue("车牌号码");
		row2.createCell(1).setCellValue("停车卡号");
		row2.createCell(2).setCellValue("金额");
		row2.createCell(3).setCellValue("支付方式");
		row2.createCell(4).setCellValue("收入来源");
		row2.createCell(5).setCellValue("入账时间");
		row2.createCell(6).setCellValue("时长");
		row2.createCell(7).setCellValue("违规");

		// 在sheet里创建第三行及之后的数据
		int rowSize = tableStartRow + 2;
		for (IncomeData data : list) {
			XSSFRow row3 = sheet.createRow(rowSize++);
			row3.createCell(0).setCellValue(data.getCarnum());
			row3.createCell(1).setCellValue(data.getCardnum());
			row3.createCell(2).setCellValue(data.getMoney());
			row3.createCell(3).setCellValue(data.getMethod() == 0 ? "现金" : data.getMethod() == 1 ? "支付宝" : data.getMethod() == 2 ? "微信" : "其他");
			row3.createCell(4).setCellValue(data.getSource() == 0 ? "充值" : "消费");
			row3.createCell(5).setCellValue(data.getTime());
			row3.createCell(6).setCellValue(data.getDuration());
			row3.createCell(7).setCellValue(data.getIsillegal());
		}
		// 文件输出流
		OutputStream output;
		try {
			output = response.getOutputStream();
			response.reset();
			response.setHeader("Content-disposition", "attachment; filename=incomeDetail.xls");
			response.setContentType("application/vnd.ms-excel");
			wb.write(output);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 收集支付方式数据
	private Map<String, Double> collectDataByPaymentMethod(List<IncomeData> incomeData) {
		Map<String, Double> data = new HashMap<>();
		for (IncomeData item : incomeData) {
			String method;
			switch (item.getMethod()) {
				case 0:
					method = "现金";
					break;
				case 1:
					method = "支付宝";
					break;
				case 2:
					method = "微信";
					break;
				default:
					method = "其他";
			}
			data.put(method, data.getOrDefault(method, 0.0) + item.getMoney());
		}
		return data;
	}

	// 收集收入来源数据
	private Map<String, Double> collectDataBySource(List<IncomeData> incomeData) {
		Map<String, Double> data = new HashMap<>();
		for (IncomeData item : incomeData) {
			String source = item.getSource() == 0 ? "充值" : "消费";
			data.put(source, data.getOrDefault(source, 0.0) + item.getMoney());
		}
		return data;
	}

	// 3. 收集每日收入数据
	private Map<String, Double> collectDailyIncome(List<IncomeData> incomeData) {
		Map<String, Double> data = new HashMap<>();
		for (IncomeData item : incomeData) {
			String date = item.getTime().split(" ")[0];
			System.out.println("date:" + date);
			data.put(date, data.getOrDefault(date, 0.0) + item.getMoney());
		}
		return data;
	}

	// 饼图创建
	private void createPieChart(XSSFWorkbook workbook, XSSFSheet sheet, Map<String, Double> data, int row, int col, String title) {
		XSSFDrawing drawing = sheet.createDrawingPatriarch();
		XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, col, row, col + 4, row + 20);

		XSSFChart chart = drawing.createChart(anchor);
		XDDFChartLegend legend = chart.getOrAddLegend();
		legend.setPosition(LegendPosition.RIGHT);

		XDDFDataSource<String> categories = XDDFDataSourcesFactory.fromStringCellRange(sheet,
				new CellRangeAddress(row, row + data.size() - 1, col, col));
		XDDFNumericalDataSource<Double> values = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
				new CellRangeAddress(row, row + data.size() - 1, col + 1, col + 1));

		XDDFChartData dataChart = chart.createData(ChartTypes.PIE, null, null);
		XDDFChartData.Series series = dataChart.addSeries(categories, values);
		series.setTitle(title, null);

		chart.plot(dataChart);

		// 填充数据
		int rowData = row;
		for (Map.Entry<String, Double> entry : data.entrySet()) {
			Row r = sheet.createRow(rowData);
			r.createCell(col).setCellValue(entry.getKey());
			r.createCell(col + 1).setCellValue(entry.getValue());
			rowData++;
		}
	}

	// 条形图创建
	private void createBarChart(XSSFWorkbook workbook, XSSFSheet sheet, Map<String, Double> unsortedData, int row, int col, String title) {
		Map<String, Double> data = new TreeMap<>(unsortedData);

		XSSFDrawing drawing = sheet.createDrawingPatriarch();
		XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, col, row, col + 8, row + 20);

		XSSFChart chart = drawing.createChart(anchor);
		XDDFChartLegend legend = chart.getOrAddLegend();
		legend.setPosition(LegendPosition.TOP_RIGHT);

		XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
		XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
		leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

		List<String> categoriesList = new ArrayList<>(data.keySet());
		List<Double> valuesList = new ArrayList<>(data.values());

		int rowData = row;
		for (int i = 0; i < categoriesList.size(); i++) {
			Row r = sheet.createRow(rowData);
			r.createCell(col).setCellValue(categoriesList.get(i));
			r.createCell(col + 1).setCellValue(valuesList.get(i));
			rowData++;
		}

		XDDFDataSource<String> categories = XDDFDataSourcesFactory.fromStringCellRange(sheet,
				new CellRangeAddress(row, row + data.size() - 1, col, col));
		XDDFNumericalDataSource<Double> values = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
				new CellRangeAddress(row, row + data.size() - 1, col + 1, col + 1));

		XDDFBarChartData dataChart = (XDDFBarChartData) chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
		dataChart.setBarDirection(BarDirection.COL);

		XDDFChartData.Series series = dataChart.addSeries(categories, values);
		series.setTitle(title, null);

		XDDFSolidFillProperties fillProperties = new XDDFSolidFillProperties(XDDFColor.from(new byte[]{(byte) 0, (byte) 176, (byte) 240}));
		XDDFShapeProperties shapeProperties = new XDDFShapeProperties();
		shapeProperties.setFillProperties(fillProperties);
		series.setShapeProperties(shapeProperties);

		chart.plot(dataChart);

		bottomAxis.setMajorTickMark(AxisTickMark.NONE);
		leftAxis.setMinimum(0.0);
		leftAxis.setMaximum(1.1 * Collections.max(valuesList));
	}

	@RequestMapping("/index/findSystem")
	@ResponseBody
	public Msg findSystem()
	{
		DepotInfo depotInfo=depotInfoService.findById(1);
		return Msg.success().add("depotInfo", depotInfo);
	}

	@RequestMapping("/index/setSystem")
	@ResponseBody
	public Msg setSystem(ChargeData chargeData)
	{
		Integer hourmoney=chargeData.getHourmoney();
		Integer monthcard=chargeData.getMonthcard();
		Integer yearcard=chargeData.getYearcard();
		Integer illegal=chargeData.getIllegal();
		DepotInfo depotInfo=depotInfoService.findById(1);
		if(hourmoney==null||hourmoney==0)
		{
			chargeData.setHourmoney(depotInfo.getHourmoney());
		}
		if(monthcard==null||monthcard==0)
		{
			chargeData.setMonthcard(depotInfo.getMonthcard());
		}
		if(yearcard==null||yearcard==0)
		{
			chargeData.setYearcard(depotInfo.getYearcard());
		}
		if(illegal==null||illegal==0)
		{
			chargeData.setIllegal(depotInfo.getIllegal());
		}
		depotInfoService.update(chargeData);
		return Msg.success();
	}

}
