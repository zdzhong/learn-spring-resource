package framework.factory;

/**
 * Spring 容器工厂的老大
 */
public interface BeanFactory {
    Object getBean(String beanName);
}
