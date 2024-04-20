package com.yxq.carpark.controller;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yxq.carpark.dto.CouponData;
import com.yxq.carpark.dto.FormData;
import com.yxq.carpark.entity.Depotcard;
import com.yxq.carpark.entity.IllegalInfo;
import com.yxq.carpark.entity.Income;
import com.yxq.carpark.entity.ParkInfo;
import com.yxq.carpark.entity.Parkinfoall;
import com.yxq.carpark.entity.User;
import com.yxq.carpark.service.CouponService;
import com.yxq.carpark.service.DepotcardService;
import com.yxq.carpark.service.IllegalInfoService;
import com.yxq.carpark.service.IncomeService;
import com.yxq.carpark.service.ParkinfoService;
import com.yxq.carpark.service.ParkinfoallService;
import com.yxq.carpark.service.ParkspaceService;
import com.yxq.carpark.service.UserService;
import com.yxq.carpark.utils.Constants;
import com.yxq.carpark.utils.Msg;


@Controller
public class CheckController {

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

	static int i=0;
	@RequestMapping("/index/check/checkIn")
	@ResponseBody
	@Transactional
	public Msg checkIn(Model model, FormData data) {
		System.out.println("入库数据: " + data.toString());
		Depotcard depotcard=depotcardService.findByCardnum(data.getCardNum());
		if(data.getParkTem()!=1)
		{
		if(depotcard!=null)
		{
			if(depotcard.getIslose()==1)
			{
				return Msg.fail().add("va_msg", "充值卡已经挂失");
			}
		}else{
			return Msg.fail().add("va_msg", "充值卡不存在");
		}
		}
		parkinfoservice.saveParkinfo(data);
		parkspaceService.changeStatus(data.getId(), 1);
		return Msg.success();
	}

	@RequestMapping("/index/check/checkOut")
	@ResponseBody
	@Transactional
	public Msg checkOut(Model model, @RequestBody FormData data) {
		System.out.println("停车信息: " + data.toString());
		int pay_money=data.getPay_money();
		Date parkout=new Date();
		Parkinfoall parkinfoall=new Parkinfoall();
		ParkInfo parkInfo=parkinfoservice.findParkinfoByParknum(data.getParkNum());
		if(data.getPay_type()==9)
		{
			Depotcard depotcard=depotcardService.findByCardnum(data.getCardNum());
			IllegalInfo illegalInfo=illegalInfoService.findByCardnumParkin(data.getCardNum(),parkInfo.getParkin());
			Income income=new Income();
			List<CouponData> coupons=couponService.findAllCouponByCardNum(data.getCardNum(), "");
			if(coupons!=null&&coupons.size()>0)
			{
				couponService.deleteCoupon(coupons.get(0).getId());
			}
			depotcardService.addMoney(data.getCardNum(), 0);
			income.setMoney(pay_money);
			income.setMethod(data.getPayid());
			income.setCardnum(data.getCardNum());
			income.setCarnum(data.getCarNum());
			if(depotcard!=null)
			{
			income.setType(depotcard.getType());
			}
			if(illegalInfo!=null)
			{
				income.setIsillegal(1);
			}
			income.setSource(1);
			income.setTime(parkout);
			Date parkin=parkInfo.getParkin();
			long day=parkout.getTime()-parkin.getTime();
			long time=day/(1000*60);
			if(day%(1000*60)>0){
			time+=1;
			}
			income.setDuration(time);
			incomeService.save(income);
		}else{
			if(data.getPay_type()==9)
			{
				return Msg.fail().add("va_msg", "现金付费");
			}else if(data.getPay_type()==0)
			{
				Depotcard depotcard=depotcardService.findByCardnum(data.getCardNum());
				IllegalInfo illegalInfo=illegalInfoService.findByCardnumParkin(data.getCardNum(),parkInfo.getParkin());
				double money=depotcard.getMoney();
				List<CouponData> coupons=couponService.findAllCouponByCardNum(data.getCardNum(), "");
				if(coupons!=null&&coupons.size()>0)
				{
					money-=coupons.get(0).getMoney();
					couponService.deleteCoupon(coupons.get(0).getId());
				}
				money-=pay_money;
				depotcardService.addMoney(depotcard.getCardnum(),money);
				Income income=new Income();
				income.setMoney(pay_money);
				income.setMethod(data.getPayid());
				income.setCardnum(data.getCardNum());
				income.setCarnum(data.getCarNum());
				income.setType(depotcard.getType());
				if(illegalInfo!=null)
				{
					income.setIsillegal(1);
				}
				income.setSource(1);
				income.setTime(parkout);
				Date parkin=parkInfo.getParkin();
				long day=parkout.getTime()-parkin.getTime();
				long time=day/(1000*60);
				if(day%(1000*60)>0){
				time+=1;
				}
				income.setDuration(time);
				income.setTrueincome(1);
				incomeService.save(income);
			}else{

			}
		}
		parkinfoall.setCardnum(parkInfo.getCardnum());
		parkinfoall.setCarnum(parkInfo.getCarnum());
		parkinfoall.setParkin(parkInfo.getParkin());
		parkinfoall.setParknum(data.getParkNum());
		parkinfoall.setParkout(parkout);
		parkinfoall.setParktemp(parkInfo.getParktem());
		parkinfoallService.save(parkinfoall);
		parkspaceService.changeStatusByParkNum(data.getParkNum(),0);
		parkinfoservice.deleteParkinfoByParkNum(data.getParkNum());
		return Msg.success().add("va_msg", "出库成功");
	}

	@RequestMapping("/index/check/findParkinfoByParknum")
	@ResponseBody
	public Msg findParkinfoByParknum(@RequestParam("parkNum") int parknum) {
		ParkInfo parkInfo = parkinfoservice.findParkinfoByParknum(parknum);
		return Msg.success().add("parkInfo", parkInfo);
	}

	@RequestMapping("/index/check/findParkinfoByCardnum")
	@ResponseBody

	public Msg findParkinfoByCardnum(@RequestParam("cardnum") String cardnum) {
		ParkInfo parkInfo = parkinfoservice.findParkinfoByCardnum(cardnum);
		//System.out.println("ello"+parkInfo.getId());
		if(parkInfo!=null)
		{
			return Msg.success().add("parkInfo", parkInfo);
		}
		return Msg.fail();
	}

	@RequestMapping("/index/check/findParkinfoDetailByParknum")
	@ResponseBody
	public Msg findParkinfoDetailByParknum(@RequestParam("parkNum") int parknum)
	{
		ParkInfo parkInfo = parkinfoservice.findParkinfoByParknum(parknum);
		System.out.println("停车详情: " + parkInfo.toString());
		if(parkInfo==null)
		{
			return Msg.fail();
		}
		Date date=parkInfo.getParkin();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String parkin=formatter.format(date);
		System.out.println(parkInfo.toString());
		String cardnum=parkInfo.getCardnum();
		Depotcard depotcard=depotcardService.findByCardnum(cardnum);
		int cardid=0;
		User user =null;
		if(depotcard!=null)
		{
		cardid=depotcard.getId();
		user =userService.findUserByCardid(cardid);
		}
		return Msg.success().add("parkInfo", parkInfo).add("user", user).add("parkin", parkin);
	}

	@RequestMapping("/index/check/illegalSubmit")
	@ResponseBody
	public Msg illegalSubmit(@RequestBody FormData data,HttpSession httpSession)
	{
		User currentUser=(User) httpSession.getAttribute("user");
		System.out.println("接收违规数据:" +data.getCardNum());
		ParkInfo parkInfo=parkinfoservice.findParkinfoByCardnum2(data.getCardNum());
		System.out.println("获取的parkinfo:" + parkInfo.getParkin()+"hell");
		IllegalInfo info=new IllegalInfo();
		IllegalInfo illegalInfo=illegalInfoService.findByCardnumParkin(data.getCardNum(),parkInfo.getParkin());
		if(illegalInfo!=null)
		{
			System.out.println("违规数据已经存在: " + illegalInfo.toString());
			return Msg.fail().add("va_msg", "违规数据已经存在");
		}
		info.setCardnum(data.getCardNum());
		info.setCarnum(data.getCarNum());
		String cardnum=data.getCarNum();
		//String carnum=data.getCarNum();
		//Depotcard depotcard=depotcardService.findByCardnum(cardnum);
		//System.out.println(depotcard.getCardnum());
	//	int cardid=0;
		//User user =null;
		/*if(depotcard!=null)
		{
		int cardid=depotcard.getId();

		User user =userService.findUserByCardid(cardid);
		System.out.println(user.getId()+"1");
		info.setUid(user.getId());
		}else {
			info.setUid(i+1);
		}*/
		//info.setUsername(user.getUsername());
		info.setIllegalInfo(data.getIllegalInfo());

	//	info.setUsername(data.get);
	//	info.setUid(currentUser.getId());
		//info.setUid(user.getId());
		info.setUid(currentUser.getId());
		Date date=new Date();
		info.setTime(date);

		info.setParkin(parkInfo.getParkin());

		info.setDelete("N");

		try {

		illegalInfoService.save(info);

		} catch (Exception e) {
			e.printStackTrace();
			return Msg.fail().add("va_msg", "数据库异常，请重试");
		}
		return Msg.success().add("va_msg", "提交成功");
	}

	private String formatTime(long duration) {
		long seconds = duration / 1000;
		long days = seconds / (24 * 3600);
		long hours = (seconds % (24 * 3600)) / 3600;
		long minutes = (seconds % 3600) / 60;
		long secs = seconds % 60;

		String formattedDuration = days + "天" + hours + "时" + minutes + "分" + secs + "秒";
		return formattedDuration;
	}

	/* 是否已经支付
	 * type:0表示已经支付
	 * type:1表示尚未支付 */
	@RequestMapping("/index/check/ispay")
	@ResponseBody
	public Msg ispay(@RequestParam("parknum") Integer parknum)
	{
		ParkInfo parkInfo=parkinfoservice.findParkinfoByParknum(parknum.intValue());
		Date date=new Date();
		Date parkin;
		long time=0;
		long day=0;
		int illegalmoney=0;
		// 临时停车超过10分钟
		if(parkInfo==null)
		{
			return Msg.fail().add("type", 9);
		}
		// 是否有违规需要缴纳
		IllegalInfo illegalInfo=illegalInfoService.findByCarnum(parkInfo.getCarnum(),parkInfo.getParkin());
		if(illegalInfo!=null)
		{
			illegalmoney=Constants.ILLEGAL;
		}
		if(StringUtils.isEmpty(parkInfo.getCardnum()))
		{
			// 需要现金扫码支付,1小时10元
			parkin=parkInfo.getParkin();
			day=date.getTime()-parkin.getTime();
			time=day/(1000*60*60);
			if(day%(1000*60*60)>0){
			time+=1;
			}
			String parkTime = formatTime(time);
			return Msg.success().add("money_pay", time*Constants.TEMPMONEY+illegalmoney)
					.add("va_msg", "违规信息"+(illegalmoney>0? ",原因"+illegalInfo.getIllegalInfo():""))
					.add("park_time", parkTime);
		}
		String cardnum=parkInfo.getCardnum();
		Depotcard depotcard=depotcardService.findByCardnum(cardnum);

		if(depotcard!=null&&depotcard.getType()==1)
		{

			double balance=depotcard.getMoney();
			int money=0;
			List<CouponData> coupons=couponService.findAllCouponByCardNum(cardnum, "");
			if(coupons!=null&&coupons.size()>0)
			{
				money=coupons.get(0).getMoney();
			}
			parkin=parkInfo.getParkin();
			day=date.getTime()-parkin.getTime();
			time=day/(1000*60*60);
			if(day%(1000*60*60)>0){
			time+=1;
			}
			String parkTime = formatTime(day);
			System.out.println("本次停车时间:" + parkTime + ", time: " + day);
			if(balance+money-illegalmoney<time*Constants.HOURMONEY)
			{
				String igllInfo= illegalInfo == null ? "" : ",违规原因: " + illegalInfo.getIllegalInfo();
			return Msg.success().add("money_pay", time*Constants.HOURMONEY+illegalmoney-money-balance)
					.add("va_msg", "余额不足"+(illegalmoney>0 ? igllInfo:""))
					.add("park_time", parkTime);
			}else{
			return Msg.fail().add("type", 0).add("money_pay", time*Constants.HOURMONEY+illegalmoney-money)
					.add("va_msg", "账户支付自动成功")
					.add("park_time", parkTime);
			}
		}
		Date deductedtime=depotcard.getDeductedtime();
		if(depotcard.getType()>1)
		{
		day=date.getTime()-deductedtime.getTime();
		}
		if(depotcard.getType()==3){
			time=day/(1000*60*60*24*30);
		}
		if(depotcard.getType()==4){
			time=day/(1000*60*60*24*365);
		}
		// 会员尚未到期，直接返回
		if(time<1)
		{
			String parkTime = formatTime(day);
			return Msg.fail().add("type", 1).add("va_msg", "会员未到期，直接返回").add("park_time", parkTime);
		}else{
			// 检查停车时间是否足够久，如果不够可能需要现金扫码
			double balance=depotcard.getMoney();
			int money=0;
			List<CouponData> coupons=couponService.findAllCouponByCardNum(cardnum, "");
			if(coupons!=null&&coupons.size()>0)
			{
				money=coupons.get(0).getMoney();
			}
			parkin=parkInfo.getParkin();
			day=date.getTime()-parkin.getTime();
			time=day/(1000*60*60);
			if(day%(1000*60*60)>0){
			time+=1;
			}
			String parkTime = formatTime(day);
			if(balance+money-illegalmoney<time*Constants.HOURMONEY)
			{
				String igllInfo= illegalInfo == null ? "" : ",违规原因: " + illegalInfo.getIllegalInfo();
			return Msg.success().add("money_pay", time*Constants.HOURMONEY+illegalmoney-money-balance)
					.add("va_msg", "余额不足 "+(illegalmoney>0 ? igllInfo:""))
					.add("park_time", parkTime);
			}else{
			return Msg.fail().add("type", 0).add("va_msg", "账户支付自动成功").add("park_time", parkTime);
			}
		}
	}

}
