package framework.factory.support;

import framework.factory.BeanFactory;
import framework.ioc.BeanDefinition;
import framework.registry.support.DefaultSingletonBeanRegistry;

public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {
    @Override
    public Object getBean(String beanName) {
        // 先从一级缓存中获取
        Object singletonObject = getSingleton(beanName);
        if (null != singletonObject) {
            return singletonObject;
        }
        // 如果一级缓存中不存在，则进行创建bean实例
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        if (null == beanDefinition || null == beanDefinition.getClazzName()) {
            return null;
        }
        // 根据bean信息，判断该bean的类型（单例 or 原型）
        if (beanDefinition.isSingleton()) {
            // 根据beanDefinition创建bean
            singletonObject = creatBean(beanDefinition);
            // 如果是单例bean再创建成功之后需要将其存入一级缓存中
            addSingleton(beanName, singletonObject);
        } else if (beanDefinition.isProtoType()) {
            // 根据beanDefinition创建bean
            singletonObject = creatBean(beanDefinition);
        } else {
            // todo 其它类型
        }
        return singletonObject;
    }

    protected abstract Object creatBean(BeanDefinition beanDefinition);

    protected abstract BeanDefinition getBeanDefinition(String beanName);
}
