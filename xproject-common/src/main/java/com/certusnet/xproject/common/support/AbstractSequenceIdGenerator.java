package com.certusnet.xproject.common.support;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 仿oracle sequence式的获取全局唯一主键的主键生成器
 * 
 * @author	  	pengpeng
 * @date	  	2014年10月17日 下午8:13:17
 * @version  	1.0
 */
@SuppressWarnings("resource")
public abstract class AbstractSequenceIdGenerator<T> implements IdGenerator<T> {

	private static final Logger logger = LoggerFactory.getLogger(AbstractSequenceIdGenerator.class);
	
	private DataSource dataSource;
	
	/** 数据库中单独的用于定义各个表或业务所需主键的sequence定义表 */
	private String sequenceTableName;
	
	protected DataSource getDataSource() {
		return dataSource;
	}

	protected void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	protected String getSequenceTableName() {
		return sequenceTableName;
	}

	protected void setSequenceTableName(String sequenceTableName) {
		this.sequenceTableName = sequenceTableName;
	}

	/**
	 * 一次批量从数据库中获取一个的序列值
	 * @param sequenceName
	 * @return
	 * @throws Exception
	 */
	protected Long getNextSequenceValue(String sequenceName) {
		Assert.hasText(sequenceName, "Parameter 'sequenceName' must be required!");
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			String sql = "update " + sequenceTableName + " set curval = LAST_INSERT_ID(curval + increment) where name = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, sequenceName);
			int result = pstmt.executeUpdate();
			Assert.state(result == 1, String.format("Increasing sequence[%s] value has not been updated!", sequenceName));
			sql = "select LAST_INSERT_ID()";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			Long value = null;
			while(rs.next()){
				value = rs.getLong(1);
				break;
			}
			conn.commit();
			return value;
		} catch(Exception e) {
			logger.error("Generating sequence key occurred an error: " + e.getMessage(), e);
			if(conn != null){
				try {
					conn.rollback();
				} catch (SQLException ex) {}
			}
			throw new KeyGeneratorException(e.getMessage(), e);
		} finally {
			if(rs != null){
				try {
					rs.close();
				} catch (Exception e) {}
			}
			if(pstmt != null){
				try {
					pstmt.close();
				} catch (Exception e) {}
			}
			if(conn != null){
				try {
					conn.close();
				} catch (Exception e) {}
			}
		}
	}
	
	/**
	 * 一次批量从数据库中获取多个连续的序列值
	 * @param sequenceName
	 * @param batchSize
	 * @return
	 * @throws SQLException
	 */
	protected List<Long> getNextSequenceValues(String sequenceName, int batchSize) {
		Assert.hasText(sequenceName, "Parameter 'sequenceName' must be required!");
		Assert.state(batchSize > 0, "Parameter 'batchSize' must be > 0!");
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			//获取自增步长
			String sql = "select increment from " + sequenceTableName + " where name = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, sequenceName);
			rs = pstmt.executeQuery();
			Integer increment = null;
			if (rs.next()) {
				increment = rs.getInt(1);
			}
			Assert.notNull(increment, String.format("No sequence found which named with '%s' in database table[%s]", sequenceName, sequenceTableName));
			Integer totalIncrement = increment * batchSize;
			//更新批量自增后的curval
			sql = "update " + sequenceTableName + " set curval = LAST_INSERT_ID(curval + ?) where name = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, totalIncrement);
			pstmt.setString(2, sequenceName);
			int result = pstmt.executeUpdate();
			Assert.state(result == 1, String.format("Increasing sequence[%s] value has not been updated!", sequenceName));
			//本次批量获取的序列中的最大值
			sql = "select LAST_INSERT_ID()";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			List<Long> batchSequences = new ArrayList<Long>();
			if (rs.next()) {
				Long maxCurval = rs.getLong(1);
				for (int i = 1; i <= batchSize; i++) {
					batchSequences.add(maxCurval - (increment * (batchSize - i)));
				}
			}
			conn.commit();
			return batchSequences;
		} catch(Exception e) {
			logger.error("Generating sequence key occurred an error: " + e.getMessage(), e);
			if(conn != null){
				try {
					conn.rollback();
				} catch (SQLException ex) {}
			}
			throw new KeyGeneratorException(e.getMessage(), e);
		} finally {
			if(rs != null){
				try {
					rs.close();
				} catch (Exception e) {}
			}
			if(pstmt != null){
				try {
					pstmt.close();
				} catch (Exception e) {}
			}
			if(conn != null){
				try {
					conn.close();
				} catch (Exception e) {}
			}
		}
	}
	
}
