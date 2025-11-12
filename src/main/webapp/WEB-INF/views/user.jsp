<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.tms.model.User" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html>
<head>
    <title>Профиль пользователя</title>
    <style>
        .user-profile {
            max-width: 600px;
            margin: 20px auto;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 8px;
            background-color: #f9f9f9;
        }
        .user-field {
            margin: 15px 0;
            padding: 10px;
            border-bottom: 1px solid #eee;
        }
        .field-label {
            font-weight: bold;
            color: #333;
            min-width: 150px;
            display: inline-block;
        }
        .field-value {
            color: #666;
        }
        .user-not-found {
            text-align: center;
            color: #d9534f;
            font-size: 18px;
            margin: 50px 0;
        }
        .back-link {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 15px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }
        .back-link:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
<div class="container">
    <%
        User user = (User) request.getAttribute("user");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    %>

    <% if (user == null) { %>
    <div class="user-not-found">
        Пользователь не найден
    </div>
    <% } else { %>
    <div class="user-profile">
        <h1>Профиль пользователя</h1>

        <div class="user-field">
            <span class="field-label">ID:</span>
            <span class="field-value"><%= user.getId() %></span>
        </div>

        <div class="user-field">
            <span class="field-label">Имя пользователя:</span>
            <span class="field-value"><%= user.getUsername() != null ? user.getUsername() : "Не указано" %></span>
        </div>

        <div class="user-field">
            <span class="field-label">Возраст:</span>
            <span class="field-value"><%= user.getAge() %> лет</span>
        </div>

        <div class="user-field">
            <span class="field-label">Дата создания:</span>
            <span class="field-value">
                        <% if (user.getCreated() != null) { %>
                            <%= user.getCreated().format(formatter) %>
                        <% } else { %>
                            Не указана
                        <% } %>
                    </span>
        </div>

        <div class="user-field">
            <span class="field-label">Дата изменения:</span>
            <span class="field-value">
                        <% if (user.getUpdated() != null) { %>
                            <%= user.getUpdated().format(formatter) %>
                        <% } else { %>
                            Не указана
                        <% } %>
                    </span>
        </div>

        <a href="javascript:history.back()" class="back-link">Назад</a>
        <a href="/user" class="back-link">К списку пользователей</a>
    </div>
    <% } %>
</div>
</body>
</html>