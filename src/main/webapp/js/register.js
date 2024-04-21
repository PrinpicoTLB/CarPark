
document.addEventListener('DOMContentLoaded', function () {

    let validUsername = '';
    let validPassword = '';
    let validPhoneOrEmail = '';
    let validName = '';

    const inputIconMap = {
        username: 'usernameIcon',
        name: 'nameIcon',
        password: 'passwordIcon',
        phoneOrEmail: 'phoneOrEmailIcon',
    };

    const inputRegexMap = {
        username: /^(?:[\u4e00-\u9fa5a-zA-Z0-9_]{1,8})$/,
        name: /^[\u4e00-\u9fa5]{2,4}$/,
        password: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/,
        phoneOrEmail: /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$|^\d{11}$/,
    };


    const inputErrorMap = {
        username: 'usernameError',
        name: 'nameError',
        password: 'passwordError',
        phoneOrEmail: 'phoneOrEmailError',
    };


    function showError(inputName, errorMessage) {
        const errorElement = document.getElementById(inputErrorMap[inputName]);
        errorElement.textContent = errorMessage;
        errorElement.style.color = '#f00';
    }

    function hideError(inputName) {
        const errorElement = document.getElementById(inputErrorMap[inputName]);
        errorElement.textContent = '';
    }

    function updateRegisterButtonState() {
        // console.log(validPassword + validUsername + validPhoneOrEmail)
        if (validPassword === '' || validUsername === '' || validPhoneOrEmail === '') {
            document.getElementById('registerBtn').disabled = true;
            return;
        }

        const isValidUsername = inputRegexMap.username.test(validUsername);
        const isValidPassword = inputRegexMap.password.test(validPassword);
        const isValidPhoneOrEmail = inputRegexMap.phoneOrEmail.test(validPhoneOrEmail);

        const isValid = isValidUsername && isValidPassword && isValidPhoneOrEmail;
        //console.log('isValid', isValid)
        document.getElementById('registerBtn').disabled = !isValid;
    }

    const usernameElement = document.getElementById('username');
    usernameElement.addEventListener('blur', async function () {
        const username = usernameElement.value;
       // console.log(username)

        if (inputRegexMap.username.test(username)) {
            const isExist = await checkUsernameExist(username);
           // console.log('isExist', isExist)
            if (isExist) {
                usernameElement.value = "";
                showError('username', '用户已存在.');
                //document.getElementById('usernameIcon').className = 'validation-icon invalid';
                validUsername = '';
              //  console.log(validUsername);
            } else {
                hideError('username');
                //document.getElementById('usernameIcon').className = 'validation-icon valid';
                validUsername = username;
               // console.log('validUsername', validUsername);
            }
        } else {
            showError('username', "长度在8以内的中英文字符")
            //document.getElementById("usernameIcon").className = 'validation-icon invalid';
            validUsername = '';
           // console.log(validUsername);
        }
    });

    const nameElement = document.getElementById('name');
    nameElement.addEventListener('blur', function () {
        const name = nameElement.value;
       // console.log(name)
        if (inputRegexMap.name.test(name)) {
            hideError('name');
            //document.getElementById('usernameIcon').className = 'validation-icon valid';
            validName = name;
        //    console.log('name', validName);
        } else {
            nameElement.value = "";
            showError('name', "请检查格式是否正确")
            //document.getElementById("usernameIcon").className = 'validation-icon invalid';
            validName = '';
        //    console.log(name);
        }
    });



    const fetchWithTimeout = (url, options, timeout = 3000) => {
        return Promise.race([
            fetch(url, options),
            new Promise((_, reject) =>
                setTimeout(() => reject(new Error('Timeout')), timeout)
            )
        ]);
    };

    async function checkUsernameExist(username) {
        try {
            const response = await fetchWithTimeout("/depot-system/index/user/checkUsername?username=" + encodeURIComponent(username), { method: 'GET' })
            if (!response.ok) {
                alert('网络异常，请再试一次')
                throw new Error('网络异常，请再试一次');
            }
            //const response = await fetch("/userName?username=" + encodeURIComponent(username));
            const jsonReadear = await response.json();
            console.log("response:", jsonReadear);
            console.log("code:", jsonReadear.code);
            if (jsonReadear.code === 200) {
                const isExist = false;
                return isExist;
            } else {
                const isExist = true;
                return isExist;
            }

        } catch (error) {
            console.error('Error:', error);
        }
    }


    const passwordElement = document.getElementById('password');
    const eyeIcon = document.getElementById('togglePassword');
    const eye = document.getElementById('eye');
    const eyeSlash = document.getElementById('eye-slash');

    eyeIcon.addEventListener('click', () => {
        const type = passwordElement.getAttribute('type') === 'password' ? 'text' : 'password';
        passwordElement.setAttribute('type', type);

        eye.style.display = type === 'password' ? 'inline' : 'none';
        eyeSlash.style.display = type === 'password' ? 'none' : 'inline';
    });
    passwordElement.addEventListener('blur', async function () {
        const password = passwordElement.value;

        if (inputRegexMap.password.test(password)) {
            hideError('password');
            //document.getElementById('passwordIcon').className = 'validation-icon valid';
            validPassword = password;
            updateRegisterButtonState();
            console.log(validPassword);

        } else {
            showError('password', '密码必须包含至少 8 个字符,包括大写、小写和数字.');
            //document.getElementById('passwordIcon').className = 'validation-icon invalid';
            validPassword = '';
            console.log(validPassword + '错误');
        }
    });

    const phoneOrEmailElement = document.getElementById('phoneOrEmail');
    phoneOrEmailElement.addEventListener('blur', async function () {
        const phoneOrEmail = phoneOrEmailElement.value;

        if (inputRegexMap.phoneOrEmail.test(phoneOrEmail)) {
            const isExist = await checkPhoneOrEmailExist(phoneOrEmail);

            if (isExist) {
                phoneOrEmailElement.value = "";
                showError('phoneOrEmail', '手机号已存在.');
                //document.getElementById('phoneOrEmailIcon').className = 'validation-icon invalid';
                validPhoneOrEmail = '';
                console.log(validPhoneOrEmail);
            } else {
                hideError('phoneOrEmail');
                //document.getElementById('phoneOrEmailIcon').className = 'validation-icon valid';
                validPhoneOrEmail = phoneOrEmail;
                console.log(validPhoneOrEmail);
            }
        } else {
            showError('phoneOrEmail', '请输入正确的手机号.');
            //document.getElementById('phoneOrEmailIcon').className = 'validation-icon invalid';
            validPhoneOrEmail = '';
            console.log(validPhoneOrEmail);
        }
    });

    async function checkPhoneOrEmailExist(phoneOrEmail) {

        const phoneRegex = /^1\d{10}$/;
        const qqEmailRegex = /^[1-9]\d{4,10}@qq\.com$/;
        let response;
        try {
            if (phoneRegex.test(phoneOrEmail)) {
                response = await fetchWithTimeout("/depot-system/index/user/phone?tel=" + phoneOrEmail, { method: 'GET' });
            }
            if (response && !response.ok) {
                alert('请求失败，请重试')
                throw new Error('请求失败，请重试');
            }
            //const response = await fetch("/userName?username=" + encodeURIComponent(username));
            const jsonReadear = await response.json();
            console.log("response:", jsonReadear.code);
            //console.log("json:", jsonReadear.code);
            if (jsonReadear.code === 200) {
                const isExist = false;
                return isExist;
            } else {
                const isExist = true;
                return isExist;
            }
        } catch (error) {
            console.error('Error:', error);
        }
    }




    document.getElementById('registerBtn').addEventListener('click', async function () {
        event.preventDefault();

        var validSex = document.getElementById("sex").value;
        const formData = {
            username: validUsername,
            password: validPassword,
            tel: validPhoneOrEmail,
            name: validName,
            sex: validSex,
        };

        //const registerResult = await registerUser(formData);
        const resp = await fetch( "/depot-system/index/user/register", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });
        const response = await resp.json();
        if (response.code === 100) {
            alert("注册成功");
            window.location.replace("/depot-system/login");
        }else {
            alert("注册失败");
            window.location.reload();
        }
        //console.log("response: " + response)
        // window.location.reload();
        // then(response => response.json())
        //     .then(data => {
        //         console.log("data: ", data)
        //         if (data.code === 200) {
        //             alert(data.msg);
        //             window.location.reload();
        //         } else {
        //             alert(data.msg);
        //         }
        //     })
        //     .catch((error) => {
        //         console.error('Error:', error);
        //     });

    });

    // async function registerUser(formData) {
    //
    // }

    updateRegisterButtonState();

});
