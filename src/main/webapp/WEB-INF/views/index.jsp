<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Регистрация</title>
    <style>
        .registration-container {
            max-width: 500px;
            margin: 50px auto;
            padding: 30px;
            border: 1px solid #ddd;
            border-radius: 8px;
            background-color: #f9f9f9;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .form-group {
            margin-bottom: 20px;
        }
        .form-label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #333;
        }
        .form-input {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 16px;
            box-sizing: border-box;
        }
        .form-input:focus {
            border-color: #007bff;
            outline: none;
            box-shadow: 0 0 5px rgba(0,123,255,0.3);
        }
        .submit-btn {
            width: 100%;
            padding: 12px;
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            cursor: pointer;
        }
        .submit-btn:hover {
            background-color: #218838;
        }
        .error-message {
            color: #d9534f;
            font-size: 14px;
            margin-top: 5px;
        }
        .success-message {
            color: #28a745;
            font-size: 14px;
            margin-top: 5px;
        }
        .login-link {
            text-align: center;
            margin-top: 20px;
        }
        .form-note {
            font-size: 12px;
            color: #666;
            margin-top: 5px;
        }
    </style>
</head>
<body>
<div class="registration-container">
    <h2 style="text-align: center; margin-bottom: 30px;">Регистрация нового пользователя</h2>

    <%
        String username = request.getParameter("username");
        String age = request.getParameter("age");
        String errorUsername = (String) request.getAttribute("error_username");
        String errorAge = (String) request.getAttribute("error_age");
        String errorPassword = (String) request.getAttribute("error_password");
        String errorConfirmPassword = (String) request.getAttribute("error_confirmPassword");
        String errorMessage = (String) request.getAttribute("error_message");
        String successMessage = (String) request.getAttribute("success_message");

        if (username == null) username = "";
        if (age == null) age = "";
    %>

    <form action="/security/registration" method="post">
        <div class="form-group">
            <label class="form-label" for="username">Имя пользователя *</label>
            <input type="text" id="username" name="username" class="form-input"
                   value="<%= username %>" required maxlength="50">
            <% if (errorUsername != null) { %>
            <div class="error-message"><%= errorUsername %></div>
            <% } %>
        </div>

        <div class="form-group">
            <label class="form-label" for="age">Возраст *</label>
            <input type="number" id="age" name="age" class="form-input"
                   value="<%= age %>" required min="1" max="120">
            <% if (errorAge != null) { %>
            <div class="error-message"><%= errorAge %></div>
            <% } %>
        </div>

        <div class="form-group">
            <label class="form-label" for="password">Пароль *</label>
            <input type="password" id="password" name="password" class="form-input"
                   required minlength="6">
            <div class="form-note">Минимум 6 символов</div>
            <% if (errorPassword != null) { %>
            <div class="error-message"><%= errorPassword %></div>
            <% } %>
        </div>

        <div class="form-group">
            <label class="form-label" for="confirmPassword">Подтверждение пароля *</label>
            <input type="password" id="confirmPassword" name="confirmPassword"
                   class="form-input" required>
            <% if (errorConfirmPassword != null) { %>
            <div class="error-message"><%= errorConfirmPassword %></div>
            <% } %>
        </div>

        <% if (successMessage != null) { %>
        <div class="success-message" style="text-align: center; margin-bottom: 20px;">
            <%= successMessage %>
        </div>
        <% } %>

        <% if (errorMessage != null) { %>
        <div class="error-message" style="text-align: center; margin-bottom: 20px;">
            <%= errorMessage %>
        </div>
        <% } %>

        <button type="submit" class="submit-btn">Зарегистрироваться</button>
    </form>

    <div class="login-link">
        <p>Уже есть аккаунт? <a href="${pageContext.request.contextPath}/security/login">Войти</a></p>
    </div>
</div>

<script>
    // Клиентская валидация паролей
    document.querySelector('form').addEventListener('submit', function(e) {
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirmPassword').value;

        if (password !== confirmPassword) {
            e.preventDefault();
            alert('Пароли не совпадают!');
            return false;
        }

        if (password.length < 6) {
            e.preventDefault();
            alert('Пароль должен содержать минимум 6 символов!');
            return false;
        }
    });

    // Очистка сообщений при начале ввода
    document.querySelectorAll('.form-input').forEach(input => {
        input.addEventListener('input', function() {
            const errorMessage = this.parentElement.querySelector('.error-message');
            if (errorMessage) {
                errorMessage.style.display = 'none';
            }
        });
    });
</script>
</body>
</html>