package dao.impl;

import dao.BlogDao;
import entry.Blog;
import mybatis.framework.builder.SqlSessionFactoryBuilder;
import mybatis.framework.factory.SqlSessionFactory;
import mybatis.framework.io.Resources;
import mybatis.framework.sqlsession.SqlSession;

import java.io.InputStream;

public class BlogDaoImpl implements BlogDao {

    @Override
    public Blog queryBlog(Blog blog) {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        return sqlSession.selectOne("mapper.selectBlog", blog);
    }
}
