package ioc;

import java.util.ArrayList;
import java.util.List;

public class BeanDefinition {
    // bean标签的class
    private String clazzName;
    // bean标签的id和name
    private String beanName;
    // bean标签的init-method
    private String initMethod;

    private String scope;

    private static final String SCOPE_SINGLETON = "singleton";

    private static final String SCOPE_PROTOTYPE = "prototype";

    private List<PropertyValue> propertyValues = new ArrayList<>();
    private Class<?> clazzType;

    public BeanDefinition(String clazzName, String beanName, Class<?> clazzType) {
        this.clazzName = clazzName;
        this.beanName = beanName;
        this.clazzType = clazzType;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getInitMethod() {
        return initMethod;
    }

    public void setInitMethod(String initMethod) {
        this.initMethod = initMethod;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public static String getScopeSingleton() {
        return SCOPE_SINGLETON;
    }

    public static String getScopePrototype() {
        return SCOPE_PROTOTYPE;
    }

    public List<PropertyValue> getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(List<PropertyValue> propertyValues) {
        this.propertyValues = propertyValues;
    }

    public boolean isSingleton() {
        return SCOPE_SINGLETON.equals(scope);
    }

    public boolean isProtoType() {
        return SCOPE_PROTOTYPE.equals(scope);
    }

    public void addPropertyValue(PropertyValue pv) {
        propertyValues.add(pv);
    }

    public Class<?> getClazzType() {
        return clazzType;
    }

    public void setClazzType(Class<?> clazzType) {
        this.clazzType = clazzType;
    }
}
