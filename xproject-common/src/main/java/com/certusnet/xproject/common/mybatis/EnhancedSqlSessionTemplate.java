package com.certusnet.xproject.common.mybatis;

import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;

/**
 * 该类是{@link #EnhancedSqlSession}的一个实现类,其批量操作仅仅是for循环插入,数据量大时性能较差
 * 
 * @author	  	pengpeng
 * @date	  	2014年6月18日 下午6:26:20
 * @version  	1.0
 */
public class EnhancedSqlSessionTemplate extends AbstractEnhancedSqlSessionTemplate<SqlSession> {

	public EnhancedSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		super(sqlSessionFactory);
	}

	public EnhancedSqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType) {
		super(sqlSessionFactory, executorType);
	}
	
    protected SqlSession createSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
    	return new SqlSessionTemplate(sqlSessionFactory);
    }
    
    protected SqlSession createSqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType) {
    	return new SqlSessionTemplate(sqlSessionFactory, executorType);
    }
	
	public <T> int[] batchInsert(String statementKey, List<T> paramObjList) {
		checkBatchArgs(statementKey, paramObjList);
		int len = paramObjList.size();
		int[] results = new int[len];
		for(int i = 0; i < len; i++){
			results[i] = getSqlSessionTemplate().insert(statementKey, paramObjList.get(i));
		}
		return results;
	}

	public <T> int[] batchInsert(String statementKey, List<T> paramObjList, int flushBatchSize) {
		checkBatchArgs(statementKey, paramObjList);
		int len = paramObjList.size();
		int[] results = new int[len];
		for(int i = 0; i < len; i++){
			results[i] = getSqlSessionTemplate().insert(statementKey, paramObjList.get(i));
		}
		return results;
	}

	public <T> int[] batchUpdate(String statementKey, List<T> paramObjList) {
		int len = paramObjList.size();
		int[] results = new int[len];
		for(int i = 0; i < len; i++){
			results[i] = getSqlSessionTemplate().update(statementKey, paramObjList.get(i));
		}
		return results;
	}

	public <T> int[] batchUpdate(String statementKey, List<T> paramObjList, int flushBatchSize) {
		int len = paramObjList.size();
		int[] results = new int[len];
		for(int i = 0; i < len; i++){
			results[i] = getSqlSessionTemplate().update(statementKey, paramObjList.get(i));
		}
		return results;
	}

	public <T> int[] batchDelete(String statementKey, List<T> paramObjList) {
		int len = paramObjList.size();
		int[] results = new int[len];
		for(int i = 0; i < len; i++){
			results[i] = getSqlSessionTemplate().delete(statementKey, paramObjList.get(i));
		}
		return results;
	}

	public <T> int[] batchDelete(String statementKey, List<T> paramObjList, int flushBatchSize) {
		int len = paramObjList.size();
		int[] results = new int[len];
		for(int i = 0; i < len; i++){
			results[i] = getSqlSessionTemplate().delete(statementKey, paramObjList.get(i));
		}
		return results;
	}
	
	protected <T> void checkBatchArgs(String statementKey, List<T> paramObjList){
		if(statementKey == null || statementKey.trim().equals("")){
			throw new IllegalArgumentException("parameter 'statementKey' can't be null or empty!");
		}
		if(paramObjList == null || paramObjList.isEmpty()){
			throw new IllegalArgumentException("parameter 'paramObjList' can't be null or empty!");
		}
	}
	
}
