package com.prista.netbanking.service;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringRunner {

    public static void main(final String[] args) {

        final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                "test-context.xml");
        System.out.println(context);

        final String[] beanDefinitionNames = context.getBeanDefinitionNames();

        System.out.println("Beans in context:");
        for (final String beanName : beanDefinitionNames) {
            System.out.println(beanName);
        }
        final IUserProfileService bean = context.getBean(IUserProfileService.class);
        System.out.println(bean);
    }

}
