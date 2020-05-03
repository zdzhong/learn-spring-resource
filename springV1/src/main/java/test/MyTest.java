package test;

import entry.Blog;
import ioc.BeanDefinition;
import ioc.PropertyValue;
import ioc.RuntimeBeanReference;
import ioc.TypedStringValue;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Before;
import org.junit.Test;
import service.BlogService;
import strategy.TypeHandler;
import strategy.TypeRegister;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyTest {

    private Map<String, Object> singletonObjects = new HashMap<>();

    private Map<String, BeanDefinition> beanDefinitions = new HashMap<>();

    @Before
    public void init() {
        // XML解析，将BeanDefinition注册到beanDefinitions集合中
        String location = "beans.xml";
        // 获取流对象
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(location);
        // 创建文档对象
        Document document = createDocument(inputStream);

        // 按照spring定义的标签语义去解析Document文档
        parseBeanDefinitions(document.getRootElement());
    }

    @SuppressWarnings("unchecked")
    public void parseBeanDefinitions(Element rootElement) {
        // 获取<bean>和自定义标签（比如mvc:interceptors）
        List<Element> elements = rootElement.elements();
        for (Element element : elements) {
            // 获取标签名称
            String name = element.getName();
            if (name.equals("bean")) {
                // 解析默认标签，其实也就是bean标签
                parseDefaultElement(element);
            } else {
                // 解析自定义标签，比如aop:aspect标签
                parseCustomElement(element);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void parseDefaultElement(Element beanElement) {
        try {
            if (beanElement == null)
                return;
            // 获取id属性
            String id = beanElement.attributeValue("id");

            // 获取name属性
            String name = beanElement.attributeValue("name");
            // 获取class属性
            String clazzName = beanElement.attributeValue("class");
            if (clazzName == null || "".equals(clazzName)) {
                return;
            }

            // 获取init-method属性
            String initMethod = beanElement.attributeValue("init-method");
            // 获取scope属性
            String scope = beanElement.attributeValue("scope");
            scope = scope != null && !scope.equals("") ? scope : "singleton";

            // 获取beanName
            String beanName = id == null ? name : id;
            Class<?> clazzType = Class.forName(clazzName);
            beanName = beanName == null ? clazzType.getSimpleName() : beanName;
            // 创建BeanDefinition对象
            // 此次可以使用构建者模式进行优化
            BeanDefinition beanDefinition = new BeanDefinition(clazzName, beanName, clazzType);
            beanDefinition.setInitMethod(initMethod);
            beanDefinition.setScope(scope);
            // 获取property子标签集合
            List<Element> propertyElements = beanElement.elements();
            for (Element propertyElement : propertyElements) {
                parsePropertyElement(beanDefinition, propertyElement);
            }

            // 注册BeanDefinition信息
            this.beanDefinitions.put(beanName, beanDefinition);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void parsePropertyElement(BeanDefinition beanDefinition, Element propertyElement) {
        if (propertyElement == null)
            return;

        // 获取name属性
        String name = propertyElement.attributeValue("name");
        // 获取value属性
        String value = propertyElement.attributeValue("value");
        // 获取ref属性
        String ref = propertyElement.attributeValue("ref");

        // 如果value和ref都有值，则返回
        if (value != null && !value.equals("") && ref != null && !ref.equals("")) {
            return;
        }

         // PropertyValue就封装着一个property标签的信息
        PropertyValue pv = null;

        if (value != null && !value.equals("")) {
            // 因为spring配置文件中的value是String类型，而对象中的属性值是各种各样的，所以需要存储类型
            TypedStringValue typeStringValue = new TypedStringValue(value);

            Class<?> targetType = getTypeByFieldName(beanDefinition.getClazzName(), name);
            typeStringValue.setTargetType(targetType);

            pv = new PropertyValue(name, typeStringValue);
            beanDefinition.addPropertyValue(pv);
        } else if (ref != null && !ref.equals("")) {

            RuntimeBeanReference reference = new RuntimeBeanReference(ref);
            pv = new PropertyValue(name, reference);
            beanDefinition.addPropertyValue(pv);
        }
    }

    private Class<?> getTypeByFieldName(String beanClassName, String name) {
        try {
            Class<?> clazz = Class.forName(beanClassName);
            Field field = clazz.getDeclaredField(name);
            return field.getType();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void parseCustomElement(Element element) {
        // TODO Auto-generated method stub

    }

    private Document createDocument(InputStream inputStream) {
        Document document = null;
        try {
            SAXReader reader = new SAXReader();
            document = reader.read(inputStream);
            return document;
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void test() {
        BlogService blogService = (BlogService) getBean("blogService");
        Blog blog = new Blog();
        blog.setId(101);
        System.out.println(blogService.queryBlog(blog));
    }

    private Object getBean(String beanName) {
        // 先从一级缓存中获取
        Object singletonObject = singletonObjects.get(beanName);
        if (null != singletonObject) {
            return singletonObject;
        }
        // 如果一级缓存中不存在，则进行创建bean实例
        BeanDefinition beanDefinition = beanDefinitions.get(beanName);
        if (null == beanDefinition || null == beanDefinition.getClazzName()) {
            return null;
        }
        // 根据bean信息，判断该bean的类型（单例 or 原型）
        if (beanDefinition.isSingleton()) {
            // 根据beanDefinition创建bean
            singletonObject = creatBean(beanDefinition);
            // 如果是单例bean再创建成功之后需要将其存入一级缓存中
            singletonObjects.put(beanName, singletonObject);
        } else if (beanDefinition.isProtoType()) {
            // 根据beanDefinition创建bean
            singletonObject = creatBean(beanDefinition);
        } else {
            // todo 其它类型
        }
        return singletonObject;
    }

    private Object creatBean(BeanDefinition beanDefinition) {
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
        if (null != initMethod) {
            Class<?> clazz = bean.getClass();
            try {
                Method method = clazz.getMethod(initMethod);
                method.invoke(bean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
                setProperty(bean, name, valueToUse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setProperty(Object bean, String name, Object valueToUse) throws NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = bean.getClass();
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        field.set(bean, valueToUse);
    }

    public Object createInstanceBean(Class<?> clazz) {
        try {
            Constructor<?> declaredConstructor = clazz.getDeclaredConstructor();
            return declaredConstructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
