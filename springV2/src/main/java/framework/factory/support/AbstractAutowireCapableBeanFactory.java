package framework.factory.support;

import framework.ioc.BeanDefinition;
import framework.ioc.PropertyValue;
import framework.ioc.RuntimeBeanReference;
import framework.ioc.TypedStringValue;
import framework.strategy.TypeHandler;
import framework.strategy.TypeRegister;
import framework.utils.ReflectUtil;

import java.util.List;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {
    @Override
    protected Object creatBean(BeanDefinition beanDefinition) {
        // 1.new 对象
        Class<?> clazzType = beanDefinition.getClazzType();
        Object bean = createInstanceBean(clazzType);
        // 2.依赖注入
        populateBean(bean, beanDefinition);
        // 3.初始化
        initMethod(bean, beanDefinition);
        return bean;
    }

    private void initMethod(Object bean, BeanDefinition beanDefinition) {
        // todo 判断bean是否实现了Aware接口

        // todo 判断是否实现了InitializingBean，如果实现了

        // 指定了init-method 属性
        String initMethod = beanDefinition.getInitMethod();
        if (null == initMethod) {
            return;
        }
        ReflectUtil.invokeMethod(bean, initMethod);
    }

    private void populateBean(Object bean, BeanDefinition beanDefinition) {
        try {
            List<PropertyValue> propertyValues = beanDefinition.getPropertyValues();
            for (PropertyValue propertyValue : propertyValues) {
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();
                Object valueToUse = null;
                if (value instanceof TypedStringValue) {
                    TypedStringValue stringValue = (TypedStringValue) value;
                    String propertyName = stringValue.getValue();
                    Class<?> targetType = stringValue.getTargetType();
                    TypeRegister typeRegister = stringValue.getTypeRegister();
                    TypeHandler handler = typeRegister.getHandler(targetType);
                    valueToUse = handler.handler(propertyName);
                } else if (value instanceof RuntimeBeanReference) {
                    RuntimeBeanReference beanReference = (RuntimeBeanReference) value;
                    valueToUse = getBean(beanReference.getRef());
                }
                ReflectUtil.setProperty(bean, name, valueToUse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object createInstanceBean(Class<?> clazz) {
        return ReflectUtil.createObject(clazz, null);
    }
}
