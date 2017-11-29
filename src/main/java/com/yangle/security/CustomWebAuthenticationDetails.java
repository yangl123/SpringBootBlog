package com.yangle.security;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yangle on 2017/10/11.
 */
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {
    private static final long serialVersionUID = 6975601077710753878L;
    private final String code;
    private  final String kapacode;

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        code = request.getParameter("code");
        kapacode= (String) request.getSession().getAttribute("vrifyCode");
    }

    public String getCode() {
        return code;
    }

    public String getKapacode() {
        return kapacode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("; code: ").append(this.getCode());
        sb.append("; kapacode: ").append(this.getKapacode());
        return sb.toString();
    }
}
