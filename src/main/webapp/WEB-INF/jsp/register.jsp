<%--
  Created by IntelliJ IDEA.
  User: dell
  Date: 4/13/2024
  Time: 10:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%
  pageContext.setAttribute("APP_PATH", request.getContextPath());
%>
<!DOCTYPE html>

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link href="${APP_PATH }/css/register.css"
        rel="stylesheet" media="screen">
  <title>停车管理系统- 注册</title>
</head>

<body>

<!-- Logo 标志 -->
<div class="logo-container">
  <img src="${APP_PATH }/assets/svg/logo.svg" alt="Logo">
</div>

<div class="register-container">
  <h2>创建您的账号</h2>
  <form id="registerForm">
    <label for="username">用户名:</label>
    <div class="input-container">
      <input type="text" id="username" class="input-text" name="username" placeholder="请输入长度在8以内的中英文字符"required>
      <div class="validation-icon" id="usernameIcon"></div>
      <div class="error-message" id="usernameError"></div>
    </div>

    <label for="name">真实姓名:</label>
    <div class="input-container">
      <input type="text" id="name" class="input-text" name="name" required>
      <div class="validation-icon" id="nameIcon"></div>
      <div class="error-message" id="nameError"></div>
    </div>

    <label for="phoneOrEmail">手机号:</label>
    <div class="input-container">
      <input type="text" id="phoneOrEmail" class="input-text" name="phoneOrEmail" required>
      <div class="validation-icon" id="phoneOrEmailIcon"></div>
      <div class="error-message" id="phoneOrEmailError"></div>
    </div>

    <label for="password">密码:</label>
    <div class="input-container">
      <input type="password" id="password" class="input-text" name="password" placeholder="密码必须包含至少 8 个字符,包括大写、小写和数字" required>
      <span class="eye-icon" id="togglePassword">
          <i class="fas fa-eye" id="eye"></i>
          <i class="fas fa-eye-slash" id="eye-slash"></i>
        </span>
      <div class="validation-icon" id="passwordIcon">
      </div>
      <div class="error-message" id="passwordError"></div>
    </div>

    <label for="sex">性别:</label>
    <div class="input-container">
      <select type="text" id="sex" class="input-text" name="sex" value="男">
        <option value="男">男</option>
        <option value="女">女</option>
      </select>
    </div>

    <button type="submit" id="registerBtn" class="button-confirm" disabled>注册</button>

  </form>

  <p class="login-link">已经有帐户? <a href="/depot-system/login" id="loginLink">登录</a></p>
</div>

<script src="${APP_PATH }/js/register.js"></script>
</body>

</html>
