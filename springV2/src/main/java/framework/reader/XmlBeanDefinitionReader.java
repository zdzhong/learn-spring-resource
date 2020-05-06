package framework.reader;

import framework.registry.BeanDefinitionRegistry;
import framework.utils.DocumentUtil;
import org.dom4j.Document;

import java.io.InputStream;

/**
 * 用来读取xml
 */
public class XmlBeanDefinitionReader {

    private BeanDefinitionRegistry beanDefinitionRegistry;

    public XmlBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    public void loadBeanDefinitions(InputStream inputStream) {
        Document document = DocumentUtil.createDocument(inputStream);
        XmlBeanDefinitionDocumentReader documentReader = new XmlBeanDefinitionDocumentReader(beanDefinitionRegistry);
        documentReader.registerBeanDefinitions(document.getRootElement());
    }
}
