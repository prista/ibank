package com.prista.netbanking.web;

import com.prista.netbanking.dao.api.IUserProfileDao;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class WebContextRunner {

    public static void main(final String[] args)
            throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("webapp-context.xml");
        System.out.println(context);

        final IUserProfileDao bean = context.getBean(IUserProfileDao.class);

        System.out.println(bean);

    }

}
