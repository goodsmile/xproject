package com.certusnet.xproject.common.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.certusnet.xproject.common.support.Pager;

/**
 * 增加了几个便于开发的方法,如分页查询方法、批量insert、update、delete等方法
 * 
 * @author	  	pengpeng
 * @date	  	2014年10月31日 上午9:08:05
 * @version  	1.0
 */
public interface EnhancedSqlSession extends SqlSession {

	/**
     * <p>
     * 默认分页总数查询statementKey的后缀
     * 例如分页查询数据的statementKey为'getXxxxList',则针对该分页的查询总记录数的statementKey一定要以<code>DEFAULT_PAGING_COUNT_STATEMENT_KEY_SUFFIX</code>结尾,例如：
     * <p/>
     * <select id="selectMyOrderList" parameterType="java.util.Map" statementType="PREPARED" resultType="OrderInfo">
     * select * from t_order_info a where a.user_id = #{userId}
     * </select>
     * 其分页count的查询statementKey的定义应如下：
     * <select id="selectMyOrderList_count" parameterType="java.util.Map" statementType="PREPARED" resultType="Integer">
     * select count(*) from t_order_info a where a.user_id = #{userId}
     * </select>
     */
    public static final String DEFAULT_PAGING_COUNT_STATEMENT_KEY_SUFFIX = "_count";
	
	/**
	 * 批量insert
	 * 
	 * @param statementKey
	 * @param paramObjList
	 * @return
	 */
	public <T> int[] batchInsert(String statementKey, List<T> paramObjList);

	/**
	 * 批量insert
	 * 
	 * @param statementKey
	 * @param paramObjList
	 * @param flushBatchSize	- 当量比较大的时候,每flushBatchSize笔插入记录调用下flushStatement方法
	 * @return
	 */
	public <T> int[] batchInsert(String statementKey, List<T> paramObjList, int flushBatchSize);

	/**
	 * 批量update
	 * 
	 * @param statementKey
	 * @param paramObjList
	 * @return
	 */
	public <T> int[] batchUpdate(String statementKey, List<T> paramObjList);

	/**
	 * 批量update
	 * 
	 * @param statementKey
	 * @param paramObjList
	 * @param flushBatchSize	- 当量比较大的时候,每flushBatchSize笔更新记录调用下flushStatement方法
	 * @return
	 */
	public <T> int[] batchUpdate(String statementKey, List<T> paramObjList, int flushBatchSize);

	/**
	 * 批量delete
	 * 
	 * @param statementKey
	 * @param paramObjList
	 * @return
	 */
	public <T> int[] batchDelete(String statementKey, List<T> paramObjList);

	/**
	 * 批量delete
	 * 
	 * @param statementKey
	 * @param paramObjList
	 * @param flushBatchSize	- 当量比较大的时候,每flushBatchSize笔删除记录调用下flushStatement方法
	 * @return
	 */
	public <T> int[] batchDelete(String statementKey, List<T> paramObjList, int flushBatchSize);
	
	/**
     * ModelHandler的作用：对key-value键值对型数据转换,例如：
     * 
     * return getSqlSessionTemplate().selectOne(MybatisUtils.getMapperKey(OrderInfo.class, "selectOrderInfoById"), orderId, new ModelHandler<OrderInfo>() {
     * 		public void handleModel(OrderInfo element) {
     * 			element.setOrderTypeName(OrderTypeEnum.getTypeName(element.getOrderType()));
     * 			...
     * 			...
     * 		}
     * });
     *
     * @param statementKey
     * @param paramObj
     * @param escapeFilter
     * @return
     */
	public <T> T selectOne(String statementKey, Object paramObj, ModelHandler<T> escapeFilter);
	
	/**
	 * 分页查询方法
	 * 
	 * ModelHandler的作用：对key-value键值对型数据转换,例如：
	 * 
     * return getSqlSessionTemplate().selectOne(MybatisUtils.getMapperKey(OrderInfo.class, "selectOrderInfoById"), orderId, new ModelHandler<OrderInfo>() {
     * 		public void handleModel(OrderInfo element) {
     * 			element.setOrderTypeName(OrderTypeEnum.getTypeName(element.getOrderType()));
     * 			...
     * 			...
     * 		}
     * });
     * 
	 * @param statementKey
	 * @param paramObj
	 * @param escapeFilter
	 * @return
	 */
	public <T> List<T> selectList(String statementKey, Object paramObj, ModelHandler<T> escapeFilter);
	
	/**
	 * 分页查询方法
	 * 
	 * @param statementKey
	 * @param paramObj
	 * @param pager
	 * @return
	 */
	public <T> List<T> selectList(String statementKey, Object paramObj, Pager pager);
	
	/**
	 * 分页查询方法
	 * 
	 * ModelHandler的作用：对key-value键值对型数据转换,例如：
	 * 
     * return getSqlSessionTemplate().selectOne(MybatisUtils.getMapperKey(OrderInfo.class, "selectOrderInfoById"), orderId, new ModelHandler<OrderInfo>() {
     * 		public void handleModel(OrderInfo element) {
     * 			element.setOrderTypeName(OrderTypeEnum.getTypeName(element.getOrderType()));
     * 			...
     * 			...
     * 		}
     * });
     * 
	 * @param statementKey
	 * @param paramObj
	 * @param escapeFilter
	 * @param pager
	 * @return
	 */
	public <T> List<T> selectList(String statementKey, Object paramObj, ModelHandler<T> escapeFilter, Pager pager);
	
}
