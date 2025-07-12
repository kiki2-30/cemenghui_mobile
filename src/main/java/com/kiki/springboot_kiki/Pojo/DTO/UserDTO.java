package com.kiki.springboot_kiki.Pojo.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserDTO {
    /**
     * @NotBlank: 验证该字段既不能是 null，也不能是只包含空白字符（如空格、制表符）的空字符串。
     * 比 @NotNull 更严格，通常用于字符串。
     * @Size: 验证字段的长度。
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在 3 到 20 个字符之间")
    private String username;

    /**
     * @Size: 为密码设置长度限制是基本的安全要求。
     * @Pattern: 密码必须包含字母和数字
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 30, message = "密码长度必须在 8 到 30 个字符之间")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$", 
             message = "密码必须至少包含一个字母和一个数字")
    private String password;

    /**
     * @Email: 验证字段必须是合法的电子邮件格式。
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * @Pattern: 验证中国手机号格式
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 真实姓名
     */
    @NotBlank(message = "真实姓名不能为空")
    @Size(min = 2, max = 20, message = "真实姓名长度必须在 2 到 20 个字符之间")
    private String realName;

    /**
     * 公司名称（可选）
     */
    private String company;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getRealName() {
        return realName;
    }

    public String getCompany() {
        return company;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", realName='" + realName + '\'' +
                ", company='" + company + '\'' +
                '}';
    }
}
