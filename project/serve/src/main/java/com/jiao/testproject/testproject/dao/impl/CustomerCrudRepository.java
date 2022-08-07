package com.jiao.testproject.testproject.dao.impl;
import org.hibernate.query.internal.NativeQueryImpl;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import java.util.List;


@Repository
public class CustomerCrudRepository {

    @PersistenceUnit
    private EntityManagerFactory emf;

    /**查询列表
     * @param sql
     * @param paramValues
     * @param clazz
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List sqlObjectList(String sql , Object[] paramValues, Class clazz){
        //用于管理系统中的实体，连接java bean 和 数据库实体，通过调用实体管理器的相关方法可以把实体持久化到数据库中，同时也可以把数据库中的记录打包成实体对象。
        EntityManager em =emf.createEntityManager();
        //em.getTransaction().begin();
        // 执行 原生sql
        Query query = em.createNativeQuery(sql);
        query.unwrap(NativeQueryImpl.class);
        if (paramValues != null) {
            for (int i = 0; i < paramValues.length; i++) {
                query.setParameter(i+1, paramValues[i]);
            }
        }
        List list = query.getResultList();
        em.close();
        return list;
    }


}
