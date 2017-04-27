package com.certusnet.xproject.common.mybatis;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.certusnet.xproject.common.support.Pager;
import com.certusnet.xproject.common.util.CollectionUtils;

/**
 * 抽象的增强SqlSessionTemplate基类,增加了几个便于开发的方法,如分页查询方法等,批量insert,update,delete操作等
 * 
 * @param <S>
 * @author  pengpeng
 * @date 	 2015年4月2日 上午11:07:43
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractEnhancedSqlSessionTemplate<S extends SqlSession> implements EnhancedSqlSession {

	private final S delegate;

    public AbstractEnhancedSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
    	delegate = createSqlSessionTemplate(sqlSessionFactory);
    }
    
    public AbstractEnhancedSqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType) {
    	delegate = createSqlSessionTemplate(sqlSessionFactory, executorType);
    }
	
    protected S getSqlSessionTemplate() {
    	return delegate;
    }
    
    protected abstract S createSqlSessionTemplate(SqlSessionFactory sqlSessionFactory);
    
    protected abstract S createSqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType);
    
    public void clearCache() {
		delegate.clearCache();
	}

	public void close() {
		delegate.close();
	}

	public void commit() {
		delegate.commit();
	}

	public void commit(boolean force) {
		delegate.commit(force);
	}

	public int delete(String statement) {
		return delegate.delete(statement);
	}

	public int delete(String statement, Object parameter) {
		return delegate.delete(statement, parameter);
	}

	public List<BatchResult> flushStatements() {
		return delegate.flushStatements();
	}

	public Configuration getConfiguration() {
		return delegate.getConfiguration();
	}

	public Connection getConnection() {
		return delegate.getConnection();
	}

	public <T> T getMapper(Class<T> type) {
		return delegate.getMapper(type);
	}

	public int insert(String statement) {
		return delegate.insert(statement);
	}

	public int insert(String statement, Object parameter) {
		return delegate.insert(statement, parameter);
	}

	public void rollback() {
		delegate.rollback();
	}

	public void rollback(boolean force) {
		delegate.rollback(force);
	}

	public void select(String statement, ResultHandler handler) {
		delegate.select(statement, handler);
	}

	public void select(String statement, Object parameter, ResultHandler handler) {
		delegate.select(statement, parameter, handler);
	}

	public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
		delegate.select(statement, parameter, rowBounds, handler);
	}

	public <E> List<E> selectList(String statement) {
		return delegate.selectList(statement);
	}

	public <E> List<E> selectList(String statement, Object parameter) {
		return delegate.selectList(statement, parameter);
	}

	public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
		return delegate.selectList(statement, parameter, rowBounds);
	}

	public <K, V> Map<K, V> selectMap(String statement, String parameter) {
		return delegate.selectMap(statement, parameter);
	}

	public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
		return delegate.selectMap(statement, parameter, mapKey);
	}

	public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
		return delegate.selectMap(statement, parameter, mapKey, rowBounds);
	}

	public <T> T selectOne(String statement) {
		return delegate.selectOne(statement);
	}

	public <T> T selectOne(String statement, Object parameter) {
		return delegate.selectOne(statement, parameter);
	}

	public int update(String statement) {
		return delegate.update(statement);
	}

	public int update(String statement, Object parameter) {
		return delegate.update(statement, parameter);
	}
	
	public <T> T selectOne(String statementKey, Object paramObj, ModelHandler<T> modelHandler) {
        ModelResultHandler<T> resultHandler = new ModelResultHandler<T>(modelHandler);
        delegate.select(statementKey, paramObj, resultHandler);
        List<T> list = resultHandler.getResultList();
        if (list == null || list.isEmpty()) {
            return null;
        } else if (list.size() == 1) {
            return list.get(0);
        }
        throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
    }

	public <T> List<T> selectList(String statementKey, Object paramObj, ModelHandler<T> modelHandler) {
        ModelResultHandler<T> resultHandler = new ModelResultHandler<T>(modelHandler);
        delegate.select(statementKey, paramObj, resultHandler);
        return resultHandler.getResultList();
    }

	public <T> List<T> selectList(String statementKey, Object paramObj, Pager pager) {
    	Number totalNum = delegate.selectOne(statementKey + DEFAULT_PAGING_COUNT_STATEMENT_KEY_SUFFIX, paramObj);
    	Integer totalRowCount = totalNum.intValue();
    	List<T> resultList = null;
    	if(totalRowCount > 0){
    		int offset = (pager.getCurrentPage() - 1) * pager.getPageSize();
    		resultList = delegate.selectList(statementKey, paramObj, new RowBounds(offset, pager.getPageSize()));
            if(CollectionUtils.isEmpty(resultList)){
            	totalRowCount = 0;
            }else if(resultList.size() > totalRowCount){
            	totalRowCount = resultList.size();
            }
    	}
    	pager.setTotalRowCount(totalRowCount);
        return resultList;
    }

	public <T> List<T> selectList(String statementKey, Object paramObj, ModelHandler<T> modelHandler, Pager pager) {
    	Number totalNum = delegate.selectOne(statementKey + DEFAULT_PAGING_COUNT_STATEMENT_KEY_SUFFIX, paramObj);
        Integer totalRowCount = totalNum.intValue();
        List<T> resultList = null;
        if(totalRowCount > 0){
        	int offset = (pager.getCurrentPage() - 1) * pager.getPageSize();
            ModelResultHandler<T> resultHandler = new ModelResultHandler<T>(modelHandler);
            delegate.select(statementKey, paramObj, new RowBounds(offset, pager.getPageSize()), resultHandler);
            resultList = resultHandler.getResultList();
            if(CollectionUtils.isEmpty(resultList)){
            	totalRowCount = 0;
            }else if(resultList.size() > totalRowCount){
            	totalRowCount = resultList.size();
            }
        }
        pager.setTotalRowCount(totalRowCount);
		return resultList;
    }

	public <T> Cursor<T> selectCursor(String statement) {
		return delegate.selectCursor(statement);
	}

	public <T> Cursor<T> selectCursor(String statement, Object parameter) {
		return delegate.selectCursor(statement, parameter);
	}

	public <T> Cursor<T> selectCursor(String statement, Object parameter, RowBounds rowBounds) {
		return delegate.selectCursor(statement, parameter, rowBounds);
	}

}
