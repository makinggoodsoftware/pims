package com.mgs.pims.core.linker.mixer;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class PimsMixersProvider implements ApplicationContextAware{
    private ApplicationContext applicationContext;

    public Object from(Class managerType) {
        return applicationContext.getBean(managerType);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
