package com.certusnet.xproject.common.mybatis;

import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;

import com.penglecode.mybatis.ex.ExSqlSession;
import com.penglecode.mybatis.ex.spring.ExSqlSessionTemplate;

/**
 * 该类是{@link #EnhancedSqlSession}的一个实现类,其批量操作底层实际使用的是JDBC的batch特性
 * 
 * @author	  	pengpeng
 * @date	  	2014年6月18日 下午6:26:20
 * @version  	1.0
 */
public class EnhancedExSqlSessionTemplate extends AbstractEnhancedSqlSessionTemplate<ExSqlSession> {

	public EnhancedExSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		super(sqlSessionFactory);
	}

	public EnhancedExSqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType) {
		super(sqlSessionFactory, executorType);
	}
	
    protected ExSqlSession createSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
    	return new ExSqlSessionTemplate(sqlSessionFactory);
    }
    
    protected ExSqlSession createSqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType) {
    	return new ExSqlSessionTemplate(sqlSessionFactory, executorType);
    }

	public <T> int[] batchInsert(String statementKey, List<T> paramObjList) {
		return getSqlSessionTemplate().batchInsert(statementKey, paramObjList);
	}

	public <T> int[] batchInsert(String statementKey, List<T> paramObjList, int flushBatchSize) {
		return getSqlSessionTemplate().batchInsert(statementKey, paramObjList, flushBatchSize);
	}

	public <T> int[] batchUpdate(String statementKey, List<T> paramObjList) {
		return getSqlSessionTemplate().batchUpdate(statementKey, paramObjList);
	}

	public <T> int[] batchUpdate(String statementKey, List<T> paramObjList, int flushBatchSize) {
		return getSqlSessionTemplate().batchUpdate(statementKey, paramObjList, flushBatchSize);
	}

	public <T> int[] batchDelete(String statementKey, List<T> paramObjList) {
		return getSqlSessionTemplate().batchDelete(statementKey, paramObjList);
	}

	public <T> int[] batchDelete(String statementKey, List<T> paramObjList, int flushBatchSize) {
		return getSqlSessionTemplate().batchDelete(statementKey, paramObjList, flushBatchSize);
	}
	
}
