// �ں��ʵĵط�����ȫ�ֱ�������������У��
document.addEventListener('DOMContentLoaded', function () {

    let validUsername = '';
    let validPassword = '';
    let validPhoneOrEmail = '';
    let validName = '';
    // ��������֤ͼ���ӳ���ϵ
    const inputIconMap = {
        username: 'usernameIcon',
        name: 'nameIcon',
        password: 'passwordIcon',
        phoneOrEmail: 'phoneOrEmailIcon',
    };

    // �����������֤����
    const inputRegexMap = {
        username: /^(?:[\u4e00-\u9fa5a-zA-Z0-9_]{1,8})$/,
        name: /^[\u4e00-\u9fa5]{2,4}$/,
        password: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/,
        phoneOrEmail: /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$|^\d{11}$/,
    };

    // �����ʹ�����Ϣ��ӳ���ϵ
    const inputErrorMap = {
        username: 'usernameError',
        name: 'nameError',
        password: 'passwordError',
        phoneOrEmail: 'phoneOrEmailError',
    };

    // ��ʾ������Ϣ
    function showError(inputName, errorMessage) {
        const errorElement = document.getElementById(inputErrorMap[inputName]);
        errorElement.textContent = errorMessage;
        errorElement.style.color = '#f00';
    }

    // ���ش�����Ϣ
    function hideError(inputName) {
        const errorElement = document.getElementById(inputErrorMap[inputName]);
        errorElement.textContent = '';
    }

    // ����ע�ᰴť״̬
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
        console.log('isValid', isValid)
        // У�����������Ƿ���������������ע�ᰴť״̬
        document.getElementById('registerBtn').disabled = !isValid;
    }

    // �����û��������ʧȥ�����¼�
    const usernameElement = document.getElementById('username');
    usernameElement.addEventListener('blur', async function () {
        const username = usernameElement.value;
        console.log(username)

        if (inputRegexMap.username.test(username)) {
            const isExist = await checkUsernameExist(username);
            console.log('isExist', isExist)
            if (isExist) {
                showError('username', '�û��Ѵ��ڡ�');
                //document.getElementById('usernameIcon').className = 'validation-icon invalid';
                validUsername = '';
                console.log(validUsername);
            } else {
                hideError('username');
                //document.getElementById('usernameIcon').className = 'validation-icon valid';
                validUsername = username;
                console.log('validUsername', validUsername);
            }
        } else {
            showError('username', "ֻ������ĸ�����ֺ��»��ߣ��ҳ����� 4 �� 16 ���ַ�֮��")
            //document.getElementById("usernameIcon").className = 'validation-icon invalid';
            validUsername = '';
            console.log(validUsername);
        }
    });

    // �������������ʧȥ�����¼�
    const nameElement = document.getElementById('name');
    nameElement.addEventListener('blur', function () {
        const name = nameElement.value;
        console.log(name)
        if (inputRegexMap.name.test(name)) {
            hideError('username');
            //document.getElementById('usernameIcon').className = 'validation-icon valid';
            validName = name;
            console.log('name', validName);
        } else {
            showError('name', "�����ʽ�Ƿ���ȷ")
            //document.getElementById("usernameIcon").className = 'validation-icon invalid';
            validName = '';
            console.log(name);
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

    // ����û����Ƿ����
    async function checkUsernameExist(username) {
        //  ���˷������󣬼���û����Ƿ����
        try {
            const response = await fetchWithTimeout("/depot-system/index/user/checkUsername?username=" + encodeURIComponent(username), { method: 'GET' })
            if (!response.ok) {
                alert('�����쳣��������һ��')
                throw new Error('�����쳣��������һ��');
            }
            //const response = await fetch("/userName?username=" + encodeURIComponent(username));
            const jsonReadear = await response.json();
            console.log("response:", jsonReadear);  // ��ӡ������Ӧ��
            //console.log("json:", jsonReadear.code);
            if (jsonReadear.code === 100) {
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


    // ������������������¼�������������ʾ��Ϣ
    const passwordElement = document.getElementById('password');
    const eyeIcon = document.getElementById('togglePassword');
    const eye = document.getElementById('eye');
    const eyeSlash = document.getElementById('eye-slash');

    eyeIcon.addEventListener('click', () => {
        const type = passwordElement.getAttribute('type') === 'password' ? 'text' : 'password';
        passwordElement.setAttribute('type', type);

        // �л��۾�ͼ�����ʾ״̬
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
            showError('password', '�������������� 8 ���ַ�,������д��Сд������.');
            //document.getElementById('passwordIcon').className = 'validation-icon invalid';
            validPassword = '';
            console.log(validPassword + '����');
        }
    });

    // �����ֻ��Ż����������ʧȥ�����¼�
    const phoneOrEmailElement = document.getElementById('phoneOrEmail');
    phoneOrEmailElement.addEventListener('blur', async function () {
        const phoneOrEmail = phoneOrEmailElement.value;

        if (inputRegexMap.phoneOrEmail.test(phoneOrEmail)) {
            const isExist = await checkPhoneOrEmailExist(phoneOrEmail);

            if (isExist) {
                showError('phoneOrEmail', '�û��Ѵ���.');
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
            showError('phoneOrEmail', '��������ȷ���ֻ��Ż������ʽ.');
            //document.getElementById('phoneOrEmailIcon').className = 'validation-icon invalid';
            validPhoneOrEmail = '';
            console.log(validPhoneOrEmail);
        }
    });
    // ����ֻ��Ż������Ƿ���ע��
    async function checkPhoneOrEmailExist(phoneOrEmail) {
        //  ���˷������󣬼���ֻ��Ż������Ƿ���ע��
        const phoneRegex = /^1\d{10}$/;
        const qqEmailRegex = /^[1-9]\d{4,10}@qq\.com$/;
        let response;
        try {
            if (phoneRegex.test(phoneOrEmail)) {
                response = await fetchWithTimeout("/depot-system/index/user/phone?tel=" + phoneOrEmail, { method: 'GET' });
            }
            if (response && !response.ok) {
                alert('�����쳣��������һ��')
                throw new Error('�����쳣��������һ��');
            }
            //const response = await fetch("/userName?username=" + encodeURIComponent(username));
            const jsonReadear = await response.json();
            console.log("response:", jsonReadear.code);  // ��ӡ������Ӧ��
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



    // ����ע�ᰴť����¼�
    document.getElementById('registerBtn').addEventListener('click', async function () {
        event.preventDefault();  // ��ֹ����Ĭ���ύ��Ϊ
        // ���˷���ע������ǰ�ٴ���֤����
        // ��ȡ��ǰ�������ȷ����
        var validSex = document.getElementById("sex").value;
        const formData = {
            username: validUsername,
            password: validPassword,
            tel: validPhoneOrEmail,
            name: validName,
            sex: validSex,
        };

        // ���˷���ע������
        //const registerResult = await registerUser(formData);
        const resp = await fetch( "/depot-system/index/user/register", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });
        const response = await resp.text();
        console.log("response: " + response)
        window.location.reload();
        // then(response => response.json())
        //     .then(data => {
        //         console.log("data: ", data)
        //         if (data.code === 200) {
        //             alert(data.msg);
        //             window.location.reload(); // ˢ��ҳ��
        //         } else {
        //             alert(data.msg);
        //         }
        //     })
        //     .catch((error) => {
        //         console.error('Error:', error);
        //     });

    });

    // // ���˷���ע������
    // async function registerUser(formData) {
    //
    // }

    // ��ʼ��ʱ����ע�ᰴť״̬
    updateRegisterButtonState();

});
