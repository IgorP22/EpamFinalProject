package com.podverbnyj.provider.web;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class ValidationTag extends TagSupport {
    private int x;
    private int y;

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int doStartTag(){
        int res = x+y;
        try {
            pageContext.getOut().println(x+"+"+y+"="+res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SKIP_BODY;
    }
}
