### bug:

1.用户界面的违规管理中无记录 (已修复)

2.管理员界面在停车位管理中为车辆添加违规无反应 (已修复)

3.管理员界面中用户管理可以删除自己 请将按钮取消 (已完成)

4.登陆页面的注册提示位置有偏移 请将其改为一个与登录按钮风格相同的注册按钮 放置在登录按钮的下方（高优先级）(已完成)

5.对车辆进行出库时 出现undefined提示 并且无收费页面跳出 历史记录正常产生

6.停车的费用计算似乎有问题 车辆停入十秒后出库产生10元费用 请您检查一下 (默认按小时/10元计费，未满向上+1，具体参数见Constants.HOURMONEY)

7.邮箱管理中，按标题查询功能无法使用 (已完成 作者压根没写......)

8.邮箱管理中，查看状态筛选无法使用 (已完成)

9.收入管理中 收入方式的筛选按钮无反应   (已完成)


### 需求：

修改项目登录页面的背景图片
新背景图片链接为
https://static.vecteezy.com/system/resources/previews/001/437/687/original/empty-car-parking-isometric-design-free-vector.jpg

请在管理员界面的导出功能模块中进行以下设置：
1.设置收费时 在对应收费栏中虚影显示当前设置的金额   (已完成)
2.设计两行 第一行有两个饼状图 按近七天收入方式和收入来源画饼图
	 第二行有一个条形图 显示近七天停车场的每天的收入 (已完成)

### 新增：
IDEA编码问题导致程序的多数提示为乱码，似乎可以通过单独调整每个文件的编码方式解决，不知是否有更好的解决方法，担心项目内同时存在UTF和GBK编码的文件。
已手动转为UTF-8中文

据使用情况，出场收费似乎只有一种逻辑即时长除以小时数，请问现有的收费逻辑是什么样的？
见上述6

数据库中card类型的deductedtime在什么情况下会被修改？
在第一次添加时

项目的三层架构是否为 表示层：JSP+Bootstrap 业务逻辑层：前台用户系统+后台管理系统 数据访问层:MyBatis+Redis+MySQL
是的，可以这么理解

Redis的作用？
访问数据库前会先访问redis中缓存内容，如果存在，直接返回



   


