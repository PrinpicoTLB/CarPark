<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%
	pageContext.setAttribute("APP_PATH", request.getContextPath());
%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
	<!-- 引入 echarts.js -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/echarts.min.js"></script>
	<!-- 引入jquery.js -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.2.1.min.js"></script>
</head>
<body>
<a id="exportIncomeA" href=""></a>
	<button class="btn btn-default" onclick="exportIncome()" type="button">导出收入</button>
	<button class="btn btn-default" onclick="setCharge()" type="button">设置收费</button>
<div style="display: flex; flex-direction: column; background-color: #2c343c; width: 97%; height: 1000px; margin-left: 20px; margin-top: 15px;">
	<div style="display: flex; justify-content: space-around; margin-bottom: 20px;">
		<div id="outIncomeChart" style="width: 40%; height: 450px;"></div>
		<div id="sourceIncomeChart" style="width: 40%; height: 450px;"></div>
	</div>
	<div id="dailyIncomeChart" style="width: 100%; height: 500px;"></div>
</div>

</body>
<script type="text/javascript">
	function createChart(chartId, title, dataType, dataKeys, dataNames, url, name) {
		var myChart = echarts.init(document.getElementById(chartId));
		myChart.showLoading();

		var data = [];
		for (var i = 0; i < dataKeys.length; i++) {
			data.push(1);
		}


		var colors = ['#c4ccd3', '#c2c4e2', '#e2c4c2', '#c2e2c4', '#c2d1e2', '#d1c2e2', '#e2e0c2'];

		$.ajax({
			type: "post",
			url: url,
			data: {},
			dataType: "json",
			success: function(result) {
				if(result){
					for (var i = 0; i < dataKeys.length; i++) {
						data[i] = result[dataKeys[i]];
					}
				}
				myChart.hideLoading();
				myChart.setOption({
					backgroundColor: '#2c343c',
					title: {
						text: title,
						left: 'center',
						top: 20,
						textStyle: {
							color: '#ccc'
						}
					},
					tooltip: {
						trigger: 'item',
						formatter: "{a} <br/>{b} : {c} ({d}%)"
					},
					series: [
						{
							name: name,
							type: dataType,
							radius: '55%',
							center: ['50%', '50%'],
							data: data.map(function(value, index) {
								return {
									value: value,
									name: dataNames[index],
									// 按数据项索引分配颜色
									itemStyle: {
										normal: {
											color: colors[index % colors.length],
											shadowBlur: 50,
											shadowColor: 'rgba(0, 0, 0, 0.5)'
										}
									}
								};
							}).sort(function (a, b) { return a.value - b.value; }),
							roseType: 'radius',
							label: {
								normal: {
									textStyle: {
										color: 'rgba(255, 255, 255, 0.3)'
									}
								}
							},
							labelLine: {
								normal: {
									lineStyle: {
										color: 'rgba(255, 255, 255, 0.3)'
									},
									smooth: 0.2,
									length: 10,
									length2: 20
								}
							},
							animationType: 'scale',
							animationEasing: 'elasticOut',
							animationDelay: function (idx) {
								return Math.random() * 200;
							}
						}
					]
				});
			}
		});
	}
	// 条形图
	function createBarChart(chartId, title, url, name) {
		var myChart = echarts.init(document.getElementById(chartId));
		myChart.showLoading();

		$.ajax({
			type: "post",
			url: url,
			data: {},
			dataType: "json",
			success: function(result) {
				console.log("result: ", result)
				if (result) {
					// 准备X轴和Y轴的数据
					var xData = [];
					var yData = [];
					result.forEach(function(item) {
						xData.push(item.date);
						yData.push(item.income);
					});

					myChart.hideLoading();
					myChart.setOption({
						backgroundColor: '#2c343c',
						title: {
							text: title,
							left: 'center',
							top: 20,
							textStyle: {
								color: '#ccc'
							}
						},
						tooltip: {
							trigger: 'axis',
							axisPointer: {
								type: 'shadow'
							}
						},
						xAxis: {
							type: 'category',
							data: xData,
							axisTick: {
								alignWithLabel: true
							},
							axisLine: {
								lineStyle: {
									color: '#ccc'
								}
							},
							axisLabel: {
								textStyle: {
									color: '#ccc'
								}
							}
						},
						yAxis: {
							type: 'value',
							axisLine: {
								lineStyle: {
									color: '#ccc'
								}
							},
							axisLabel: {
								textStyle: {
									color: '#ccc'
								}
							}
						},
						series: [{
							name: name,
							type: 'bar',
							data: yData,
							barWidth: '60%',
							itemStyle: {
								normal: {
									color: function(params) {
										return '#c4ccd3';
									},
									shadowBlur: 10,
									shadowColor: 'rgba(0, 0, 0, 0.5)'
								}
							},
							animationEasing: 'elasticOut',
							animationDelay: function (idx) {
								return idx * 10;
							}
						}]
					});
				}
			}
		});
	}
	createChart('outIncomeChart', '过去七日出库收入分析', 'pie', ['weixin', 'zhifubao', 'cash', 'card'], ['微信收入', '支付宝收入', '现金收入', '充值卡扣费'], "${pageContext.request.contextPath}/index/incomeCharts2", '收入方式');
	createChart('sourceIncomeChart', '过去七日收入来源分析', 'pie', ['card', 'car'], ['充值', '出库'], "${pageContext.request.contextPath}/index/incomeSourceCharts", '收入来源');
    createBarChart("dailyIncomeChart", "过去七日每日收入", "${pageContext.request.contextPath}/index/incomeWeekCharts", "收入")
function exportIncome()
{
	var html = "<label>开始时间：</label><div style=\"width: 30%;\">"
		+ "<div class=\"input-group\">"
		+ "<input type=\"text\" value=\"\" placeholder=\"开始时间\" id=\"datetimepickerStart\"/>"
		+ "</div>"
		+ "</div>"
		+ "<label>结束时间：</label><div style=\"width: 30%;\">"
		+ "<div class=\"input-group\">"
		+ "<input type=\"text\" value=\"\" placeholder=\"结束时间\" id=\"datetimepickerEnd\"/>"
		+ "</div></div>";
		$("#myModalLabel").html("导出收入");
		$("#checkSubmit").html("导出");
		$("#checkSubmit").attr("onclick","exportIncomeSubmit()");
		$(".modal-body").append(html);
		$("#myModal").modal('show');
		$('#datetimepickerStart').datetimepicker({
		    format: 'yyyy-mm-dd hh:ii'
		});
		$('#datetimepickerEnd').datetimepicker({
		    format: 'yyyy-mm-dd hh:ii'
		});
}
function exportIncomeSubmit()
{
	var datetimepickerStart=$("#datetimepickerStart").val();
	var datetimepickerEnd=$("#datetimepickerEnd").val();
	$("#myModal").modal('hide');
	window.location="${APP_PATH }/index/exportIncome?datetimepickerStart="+datetimepickerStart+"&&datetimepickerEnd="+datetimepickerEnd;
}
function setCharge() {
	$.ajax({
		type: 'get',
		url: '/depot-system/index/findSystem',
		success: function(data) {
			// console.log("当前收费:", data);
			displayChargeData(data);
		},
		error: function() {
			// alert('获取当前收费数据失败');
			displayChargeData(null);
		}
	});
}

function displayChargeData(data) {
	var hourmoney = data.code == 100 ? data.extend.depotInfo.hourmoney : '';
	var monthcard = data.code == 100 ? data.extend.depotInfo.monthcard : '';
	var yearcard = data.code == 100 ? data.extend.depotInfo.yearcard : '';
	var illegal = data.code == 100 ? data.extend.depotInfo.illegal : '';

	var html = "<label>时收费</label><div style=\"width: 30%;\">"
			+ "<div class=\"input-group\">"
			+ "<input id=\"hourmoney\" name=\"hourmoney\" type=\"text\" class=\"form-control\" placeholder=\"" + hourmoney + "\" unselectable=\"on\">"
			+ "</div>"
			+ "</div>"
			+"<label>月收费</label><div style=\"width: 30%;\">"
			+ "<div class=\"input-group\">"
			+ "<input id=\"monthcard\" name=\"monthcard\" type=\"text\" class=\"form-control\" placeholder=\"" + monthcard + "\" unselectable=\"on\">"
			+ "</div>"
			+ "</div>"
			+ "<label>年收费：</label><div style=\"width: 30%;\">"
			+ "<div class=\"input-group\">"
			+ "<input id=\"yearcard\" name=\"yearcard\" type=\"text\" class=\"form-control\" placeholder=\"" + yearcard + "\" unselectable=\"on\">"
			+ "</div></div>"
			+"<label>违规收费</label><div style=\"width: 30%;\">"
			+ "<div class=\"input-group\">"
			+ "<input id=\"illegal\" name=\"illegal\" type=\"text\" class=\"form-control\" placeholder=\"" + illegal + "\" unselectable=\"on\">"
			+ "</div>"
			+ "</div>";
	$("#myModalLabel").html("设置收费（空为不修改）");
	$("#checkSubmit").html("设置");
	$("#checkSubmit").attr("onclick","setChargeSubmit()");
	$(".modal-body").append(html);
	$("#myModal").modal('show');
}
function setChargeSubmit()
{
	$.ajax({
		type:'post',
		url:'/depot-system/index/setSystem',
		datatype:'json',
		data:$("#checkForm").serializeArray(),
		success:function(data){
			if(data.code==100){
				alert("设置成功!");
				$("#myModal").modal('hide');
			}else{
				alert("设置失败!");
			}
		}
	})
}
</script>
</html>
