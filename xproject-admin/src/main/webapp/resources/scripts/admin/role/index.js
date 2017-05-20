var ADMIN_ROLE_TYPE_SYSTEM = 0;
Vue.onDocumentReady(function() {
	new Vue({
		el: '#app',
		data: {
			roleQuering: false,
			submiting: false,
			loadingRoleList: false,
			roleQueryForm: {
				roleName: '',
				roleCode: '',
				pageSize: 10,
				currentPage: 1
			},
			roleQuerySort: {
				prop: 'createTime',
				order: 'descending'
			},
			roleQueryTotal: 0,
			roleList: [],
			editDialogVisible: false,
			viewConfigDialogVisible: false,
			viewConfigActiveTabName: 'roleDetail',
			defaultProps: {
				children: 'children',
		        label: 'label'
			},
			roleResourceDataList: [],
			checkedResourceIdList: [],
			loadingRoleResourceDataList: false,
			currentActionType: 'add',
			roleEditForm: {
				roleId: '',
				roleName: '',
				roleCode: '',
				description: ''
			},
			roleViewConfigForm: {
				roleId: '',
				roleName: '',
				roleCode: '',
				description: '',
				roleType: '',
				roleTypeName: '',
				createBy: '',
				createTime: '',
				updateBy: '',
				updateTime: ''
			},
			roleEditRules: {
				roleName: [{
					required: true,
					message: '请输入角色名称!'
				}],
				roleCode: [{
					required: true,
					message: '请输入角色代码!'
				},{
					validator: regex,
					regex: /^[a-zA-Z0-9_]{1,50}$/,
					message: '角色代码由不超过50个字母、数字、下划线组成!'
				}]
			}
		},
		computed: {
			showRoleQueryPager: function(){
				if(this.roleQuering || this.roleQueryTotal == 0){
					return false;
				}else{
					return true;
				}
			},
			editDialogTitle: function(){
				if(this.currentActionType == 'add'){
					return '新建角色';
				}else if(this.currentActionType == 'edit'){
					return '修改角色';
				}else{
					return '';
				}
			},
			viewConfigDialogTitle: function(){
				if(this.currentActionType == 'view'){
					return '查看角色详情';
				}else if(this.currentActionType == 'conf'){
					return '角色资源配置';
				}else{
					return '';
				}
			}
		},
		created: function(){
			this.queryRoleList(0);
		},
		methods: {
			getRoleTypeTagType: function(roleType){
				if(roleType == 0){
					return "success";
				}else{
					return "primary";
				}
			},
			queryRoleList: function(loading){
				this.roleQueryForm.currentPage = 1;
				this.doQueryRoleList(loading);
			},
			doQueryRoleList: function(loading){
				var _this = this;
				this.roleQuering = true;
				var url = ADMIN_CONTEXT_PATH + '/admin/role/list';
				if(loading){
					this.loadingRoleList = true;
				}
				setTimeout(function(){
					var params = Object.assign({}, _this.roleQueryForm, convertToOrderBy(_this.roleQuerySort));
					axios.get(url, {
						params: params
					}).then(function(response){
						var result = response.data;
						if(result.success){
							_this.roleList = result.data;
							_this.roleQueryTotal = result.totalRowCount;
						}else{
							_this.roleList = [];
							_this.roleQueryTotal = 0;
						}
						if(loading){
							_this.loadingRoleList = false;
						}
						_this.roleQuering = false;
					}).catch(function(error){
						_this.$message.error('请求出错!');
						_this.loadingRoleList = false;
						_this.roleQuering = false;
					});
				}, loading);
			},
			onRoleQuerySortChange: function(parameter){
				if(this.roleQuerySort.prop != parameter.prop || this.roleQuerySort.order != parameter.order){
					//alert(parameter.prop + ' , ' + parameter.order);
					this.roleQuerySort.prop = parameter.prop;
					this.roleQuerySort.order = parameter.order;
					this.queryRoleList(1000);
				}
			},
			resetQueryForm: function(){
				this.$refs.roleQueryForm.resetFields();
			},
			onRoleQueryPageSizeChange: function(event, pageSize){
		    	this.roleQueryForm.pageSize = pageSize;
		    	this.queryRoleList(1000);
		    },
		    onRoleQueryCurrentPageChange: function(currentPage){
		    	this.roleQueryForm.currentPage = currentPage;
		    	this.doQueryRoleList(1000);
		    },
		    handleRoleCmd: function(cmd, row){
		    	if(cmd == 'edit'){
		    		this.openRoleEditDialog(cmd, row);
		    	}else if(cmd == 'view'){
		    		this.openRoleViewConfigDialog(cmd, row);
		    	}else if(cmd == 'del'){
		    		this.deleteRole(row);
		    	}else if(cmd == 'conf'){
		    		this.openRoleViewConfigDialog(cmd, row);
		    	}
		    },
		    openRoleEditDialog: function(cmd, row){
		    	this.currentActionType = cmd;
		    	this.editDialogVisible = true;
		    	var _this = this;
				Vue.nextTick(function(){
					if(cmd == 'edit'){
						_this.roleEditForm.roleId = row.roleId;
						_this.roleEditForm.roleName = row.roleName;
						_this.roleEditForm.roleCode = row.roleCode;
						_this.roleEditForm.description = row.description;
			    	}
				});
		    },
		    openRoleViewConfigDialog: function(cmd, row){
		    	this.currentActionType = cmd;
		    	this.viewConfigDialogVisible = true;
		    	var _this = this;
				Vue.nextTick(function(){
					_this.roleViewConfigForm.roleId = row.roleId;
					_this.roleViewConfigForm.roleName = row.roleName;
					_this.roleViewConfigForm.roleCode = row.roleCode;
					_this.roleViewConfigForm.description = row.description;
					_this.roleViewConfigForm.roleType = row.roleType;
					_this.roleViewConfigForm.roleTypeName = row.roleTypeName;
					_this.roleViewConfigForm.createBy = row.createBy;
					_this.roleViewConfigForm.createTime = row.createTime;
					_this.roleViewConfigForm.updateBy = row.updateBy;
					_this.roleViewConfigForm.updateTime = row.updateTime;
		    		if(cmd == 'conf'){
		    			_this.viewConfigActiveTabName = 'roleResourceConfig';
		    			_this.loadRoleResourceDataList(1500, row.roleId);
		    		}else{
		    			_this.viewConfigActiveTabName = 'roleDetail';
		    			_this.loadRoleResourceDataList(0, row.roleId);
		    		}
				});
		    },
		    saveRole: function(){
		    	var url = '';
				if(this.currentActionType == 'add'){
					url = ADMIN_CONTEXT_PATH + '/admin/role/add/submit';
				}else if(this.currentActionType == 'edit'){
					url = ADMIN_CONTEXT_PATH + '/admin/role/edit/submit';
				}
				if(url){
					var _this = this;
					this.$refs.roleEditForm.validate(function(valid){
						if(valid && !_this.submiting){
							_this.submiting = true;
							setTimeout(function(){
								axios.post(url, _this.roleEditForm).then(function(response){
									_this.submiting = false;
									var result = response.data;
									if(result.success){
										_this.$message.success('保存成功!');
										_this.closeEditDialog();
										_this.queryRoleList(1000);
									}else{
										_this.$message.error(result.message);
									}
								}).catch(function(error){
									_this.submiting = false;
									_this.$message.error('请求出错!');
								});
							}, 1500);
						}else{
							return false;
						}
					});
				}
		    },
		    deleteRole: function(row){
		    	if(ADMIN_ROLE_TYPE_SYSTEM == row.roleType){
					this.$message.error('系统角色不能删除!');
				}else{
					var url = ADMIN_CONTEXT_PATH + '/admin/role/del?id=' + row.roleId;
					var _this = this;
					this.$confirm('你确定要删除该角色?', '提示', {
						confirmButtonText: '确定',
				        cancelButtonText: '取消',
				        type: 'warning',
				        callback: function(action, instance){
							if(action == 'confirm'){
								axios.get(url).then(function(response){
									var result = response.data;
									if(result.success){
										_this.$message.success('删除成功!');
										_this.queryRoleList(1000);
									}else{
										_this.$message.error(result.message);
									}
								}).catch(function(error){
									_this.$message.error('请求出错!');
								});
							}
						}
					});
				}
		    },
		    saveRoleResourceConfig: function(){
		    	var roleId = this.roleViewConfigForm.roleId;
		    	var checkedKeys = this.$refs.roleResourceViewTree.getCheckedKeys();
		    	var checkedResourceIds = checkedKeys.sort().join(",");
		    	if(checkedResourceIds == this.checkedResourceIdList.sort().join(",")){
		    		this.$message.warning('配置没有改变,无需保存!');
		    	}else{
		    		var url = ADMIN_CONTEXT_PATH + "/admin/role/config/submit";
		    		if(!this.submiting){
		    			this.submiting = true;
		    			var _this = this;
						setTimeout(function(){
							axios.post(url, {
								roleId: roleId,
								resourceIds: checkedResourceIds
							}).then(function(response){
								_this.submiting = false;
								var result = response.data;
								if(result.success){
									_this.$message.success('保存成功!');
									_this.loadRoleResourceDataList(1500, roleId);
								}else{
									_this.$message.error(result.message);
								}
							}).catch(function(error){
								_this.submiting = false;
								_this.$message.error('请求出错!');
							});
						}, 1500);
					}
		    	}
		    },
		    closeEditDialog: function(){
		    	this.editDialogVisible = false;
				this.$refs.roleEditForm.resetFields();
				this.roleEditForm.roleId = ''; //手动reset
			},
			loadRoleResourceDataList: function(loading, roleId){
				var _this = this;
				var url = ADMIN_CONTEXT_PATH + '/admin/role/resources?roleId=' + roleId;
				if(loading){
					this.loadingRoleResourceDataList = true;
				}
				setTimeout(function(){
					axios.get(url).then(function(response){
						var result = response.data;
						if(result.success){
							_this.roleResourceDataList = result.data.allResourceList;
							_this.checkedResourceIdList = result.data.checkedResourceIdList;
							Vue.nextTick(function(){
								_this.$refs.roleResourceViewTree.setCheckedKeys(result.data.checkedResourceIdList);
							});
						}else{
							_this.roleResourceDataList = [];
							_this.checkedResourceIdList = [];
						}
						if(loading){
							_this.loadingRoleResourceDataList = false;
						}
					}).catch(function(error){
						_this.$message.error('请求出错!');
						_this.roleResourceDataList = [];
						_this.checkedResourceIdList = [];
						if(loading){
							_this.loadingRoleResourceDataList = false;
						}
					});
				}, loading);
			},
			renderRoleResourceTreeNode: function(h, param){
				var _this = this;
				return h('span', {
					'class': 'el-tree-custom-resource el-clearfix'
				},[
					h('span', {
						'class': 'custom-label'
					}, param.node.label),
					h('div', {
						'class': 'custom-action-type'
					}, [
						h('el-tag', {
							'class': 'el-tag-small',
							props: {
								type: _this.getTagType4ActionType(param.data.actionType)
							}
						}, param.data.actionTypeName)
					])
				]);
			},
			onRoleResourceTreeNodeCheckClick: function(data, node){
				if(this.currentActionType == 'conf'){
					if(!node.checked){
						this.recursiveCheckParent(node, !node.checked); //当前节点被选中时则选中其所有父节点
					}else{
						this.recursiveCheckChild(node, !node.checked); //当前节点被取消选中时则取消选中其所有子节点
					}
				}
			},
			recursiveCheckParent: function(node, checked){
				var parent = node.parent;
				if(parent){
					parent.checked = checked;
					this.recursiveCheckParent(parent, checked);
				}
			},
			recursiveCheckChild: function(node, checked){
				var childs = node.childNodes;
				if(childs.length){
					var _this = this;
					childs.forEach(function(child){
						child.checked = checked;
						_this.recursiveCheckChild(child, checked);
					});
				}
			},
			getTagType4ActionType: function(actionType){
				return actionType == 0 ? 'primary' : 'success'
			}
		}
	});
}, DEFAULT_VUE_RENDER_TIMEOUT);