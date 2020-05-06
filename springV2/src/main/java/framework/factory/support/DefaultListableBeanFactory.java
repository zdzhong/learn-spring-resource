package framework.factory.support;

import framework.factory.support.AbstractAutowireCapableBeanFactory;
import framework.ioc.BeanDefinition;
import framework.registry.BeanDefinitionRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Spring中真正的beanFactory实例
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry {

    private Map<String, BeanDefinition> beanDefinitions = new HashMap<>();

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitions.get(beanName);
    }

    @Override
    public List<BeanDefinition> getBeanDefinitions() {
        return null;
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitions.put(beanName, beanDefinition);
    }
}
