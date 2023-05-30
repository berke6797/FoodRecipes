# RECIPE APP
<font color="#faebd7">The Recipe Book project is a Spring Boot application where administrators can add recipes, and users can add them to their favorites, leave comments, and rate them. Users can register and log in to the system. They can rate recipes, make comments, and even add recipes to their favorites.</font>

## TECHNOLOGIES
<font color="#faebd7">This project , written in Java, utilizes technologies such as _OpenFeing_, _RabbitMQ_, _Redis_,and _Zipkin_</font><br></br>
<font color="#faebd7">The project uses _MongoDB_ and _PostgreSQL_ as the database technologies</font>

---
<font color ="#99A98f">There are 5 services in the project. These services are: auth service,comment service,mail service,recipe service, user service</font>

<font color = "#d7c0ae">AUTH SERVICE</font>

<font color= "#99A98f">The Auth Service is primarily a service designed for user-related operations. In this service, users can register, activate their accounts, log in, and reset their passwords via email in case of forgetting.</font><br></br>

#### <font color= "#EEE3CB">api/v1/register</font><br></br>
<font color="#c7bca1">This method is used to perform user registration, and after that, the returned activation code should be used to activate the account before the user can log in. An activation code is sent to the registered user's email for account activation before logging in.</font>
![](SwaggerScreenShot/auth-service/register-auth.png)

#### <font color="#EEE3CB">api/v1/activate-status</font><br></br>
<font color="c7bca1">This method is responsible for activating the account only.</font>
![](SwaggerScreenShot/auth-service/activate-status.png)

#### <font color= "#EEE3CB">api/v1/login</font><br></br>
<font color="c7bca1">This method is used to perform the login process for activated accounts. After successful login, the returned token is used to perform the necessary operations.</font>
![](SwaggerScreenShot/auth-service/login.png)

#### <font color= "#EEE3CB">api/v1/forgot-password-from-auth</font><br></br>
<font color="c7bca1">This method is used to reset the password for a user account using their username and email. As a result, a randomly generated password is set, and it is sent to the user's email account for password reset.</font>
![](SwaggerScreenShot/auth-service/forgot-password.png)
![](SwaggerScreenShot/auth-service/forgot-password-mail.png)

<font color = "#d7c0ae">USER SERVICE</font><br></br>
<font color= "#99A98f">The User Service is responsible for allowing users to view, modify, and delete their user information.</font><br></br>

#### <font color= "#EEE3CB">api/v1/user/update-user-for-auth-and-address</font><br></br>
<font color="c7bca1">This method is used to update the address information of a registered and activated user.</font>
![](SwaggerScreenShot/user-service/update-user.png)

#### <font color= "#EEE3CB">api/v1/user/change-password-from-user</font><br></br>
<font color="c7bca1">This method allows the user to change their password.</font>
![](SwaggerScreenShot/user-service/change-password.png)

#### <font color= "#EEE3CB">api/v1/user/change-password-from-user</font><br></br>
<font color="c7bca1">This method allows the user to change their password.</font>
