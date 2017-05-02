var ACTION_TYPES = {
	0: "菜单",
	1: "按钮"
};
var ADMIN_RESOURCE_TYPE_SYSTEM = 0;

Vue.onDocumentReady(function() {
	new Vue({
		el: '#app',
		data: {
			fixedTreeHeightStyle: {
				height: window.parent.MIN_MAIN_FRAME_HEIGHT ? (window.parent.MIN_MAIN_FRAME_HEIGHT - 120) + 'px !important' : 'auto',
				overflowY: 'auto',
				overflowX: 'hidden'
			},
			defaultProps: {
				children: 'children',
		        label: 'label'
			},
			resourceTreeDataList: [],
			loadingResourceTreeDataList: false,
			editDialogVisible: false,
			viewDialogVisible: false,
			actionType: 'add',
			submiting: false,
			resourceEditForm: {
				resourceId: '',
				parentResourceId: '',
				parentResourceName: '',
				resourceName: '',
				permissionExpression: '',
				actionType: '',
				resourceUrl: '',
				siblingsIndex: '',
				resourceIcon: 'fa-circle-o'
			},
			resourceViewForm: {
				parentResourceName: '',
				resourceName: '',
				permissionExpression: '',
				actionType: '',
				resourceUrl: '',
				siblingsIndex: '',
				resourceIcon: ''
			},
			resourceEditRules: {
				parentResourceId: [{
					required: true,
					message: '未选择父级资源!'
				}],
				resourceName: [{
					required: true,
					message: '请输资源名称!'
				}],
				permissionExpression: [{
					validator: regex,
					regex: /^\w+(\:\w+){1,2}$/,
					message: '请输入合法的权限表达式!'
				}],
				actionType: [{
					required: true,
					message: '请选择资源功能类型!'
				}],
				siblingsIndex: [{
					required: true,
					message: '请输资源排序号!'
				},{
					validator: regex,
					regex: /^\d+$/,
					message: '排序号必须是整数!'
				}]
			}
		},
		computed: {
			editDialogTitle: function(){
				if(this.actionType == 'add'){
					return '新建资源';
				}else if(this.actionType == 'edit'){
					return '修改资源';
				}else{
					return '';
				}
			}
		},
		created: function(){
			this.refreshResourceTree(0);
		},
		methods: {
			renderResourceTreeNode: function(h, param){
				var _this = this;
				return h('span', {
					'class': 'el-tree-custom-resource el-clearfix'
				},[
					h('span', {
						'class': 'custom-label'
					}, param.node.label),
					h('div', {
						'class': 'custom-options'
					},[
						h('el-dropdown', {
							'class': 'el-dropdown-small',
							props: {
								type: 'primary',
								splitButton: true,
								trigger: 'click',
								menuAlign: 'start'
							},
							on: {
								click: function(){
									_this.viewResource(param);
								},
								command: function(cmd){
									if(cmd == 'add'){
										_this.addResource(param);
									}else if(cmd == 'edit'){
										_this.editResource(param);
									}else if(cmd == 'del'){
										_this.delResource(param);
									}
								}
							}
						},[ 
							'查看详情',
							h('el-dropdown-menu', {
								'class': 'el-dropdown-menu-small',
								slot: 'dropdown'
							}, [
								h('el-dropdown-item', {
									props: {
										command: 'add'
									}
								}, '新增资源'),
								h('el-dropdown-item', {
									props: {
										command: 'edit'
									}
								}, '修改资源'),
								h('el-dropdown-item', {
									props: {
										divided: true,
										command: 'del'
									}
								}, '删除资源')
							])
						])
					]),
					h('div', {
						'class': 'custom-permission-exp'
					}, param.data.permissionExpression),
					h('div', {
						'class': 'custom-resource-url',
						domProps: {
							innerHTML: _this.formatResourceUrl(param.data.resourceUrl)
						}
					}),
					h('div', {
						'class': 'custom-action-type'
					}, [
						h('el-tag', {
							'class': 'el-tag-action-type',
							props: {
								type: _this.getTagType4ActionType(param.data.actionType)
							}
						}, _this.formatActionType(param.data.actionType))
					]),
					h('div', {
						'class': 'custom-siblings-index'
					}, param.data.siblingsIndex)
				]);
			},
			getTagType4ActionType: function(actionType){
				return actionType == 0 ? 'primary' : 'success'
			},
			formatActionType: function(actionType){
				return ACTION_TYPES[actionType];
			},
			formatResourceUrl: function(resourceUrl){
				if(resourceUrl){
					return resourceUrl.replace(/\r{0,}\n/g,"<br/>");
				}
				return '';
			},
			refreshResourceTree: function(loading){
				var _this = this;
				var url = ADMIN_CONTEXT_PATH + '/admin/resource/available';
				if(loading){
					this.loadingResourceTreeDataList = true;
				}
				setTimeout(function(){
					axios.get(url).then(function(response){
						var result = response.data;
						if(result.success){
							_this.resourceTreeDataList = result.data;
						}else{
							_this.resourceTreeDataList = [];
						}
						if(loading){
							_this.loadingResourceTreeDataList = false;
						}
					}).catch(function(error){
						_this.$message.error('请求出错!');
					});
				}, loading);
			},
			addResource: function(param){
				this.actionType = 'add';
				this.resourceEditForm.resourceId = '';
				this.resourceEditForm.parentResourceId = param.data.id;
				this.resourceEditForm.parentResourceName = param.data.label;
				this.editDialogVisible = true;
			},
			editResource: function(param){
				this.actionType = 'edit';
				this.resourceEditForm.resourceId = param.data.id;
				this.resourceEditForm.resourceName = param.data.label;
				this.resourceEditForm.parentResourceId = param.data.parentResourceId;
				this.resourceEditForm.parentResourceName = param.data.parentResourceName;
				this.resourceEditForm.permissionExpression = param.data.permissionExpression;
				this.resourceEditForm.actionType = param.data.actionType;
				this.resourceEditForm.resourceUrl = param.data.resourceUrl;
				this.resourceEditForm.siblingsIndex = param.data.siblingsIndex;
				this.resourceEditForm.resourceIcon = param.data.resourceIcon;
				this.editDialogVisible = true;
			},
			viewResource: function(param){
				this.resourceViewForm.parentResourceName = param.data.parentResourceName;
				this.resourceViewForm.resourceName = param.data.label;
				this.resourceViewForm.permissionExpression = param.data.permissionExpression;
				this.resourceViewForm.actionType = param.data.actionType;
				this.resourceViewForm.resourceUrl = param.data.resourceUrl;
				this.resourceViewForm.siblingsIndex = param.data.siblingsIndex;
				this.resourceViewForm.resourceIcon = param.data.resourceIcon;
				this.viewDialogVisible = true;
			},
			saveResource: function(){
				var url = '';
				if(this.actionType == 'add'){
					url = ADMIN_CONTEXT_PATH + '/admin/resource/add/submit';
				}else if(this.actionType == 'edit'){
					url = ADMIN_CONTEXT_PATH + '/admin/resource/edit/submit';
				}
				if(url){
					var _this = this;
					this.$refs.resourceEditForm.validate(function(valid){
						if(valid && !_this.submiting){
							_this.submiting = true;
							setTimeout(function(){
								axios.post(url, _this.resourceEditForm).then(function(response){
									_this.submiting = false;
									var result = response.data;
									if(result.success){
										_this.$message.success('保存成功!');
										_this.closeEditDialog();
										_this.refreshResourceTree(1000);
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
			delResource: function(param){
				if(ADMIN_RESOURCE_TYPE_SYSTEM == param.data.resourceType){
					this.$message.error('系统资源不能删除!');
				}else{
					var url = ADMIN_CONTEXT_PATH + '/admin/resource/del?id=' + param.data.id;
					var _this = this;
					this.$confirm('你确定要删除该资源?', '提示', {
						confirmButtonText: '确定',
				        cancelButtonText: '取消',
				        type: 'warning',
				        callback: function(action, instance){
							if(action == 'confirm'){
								axios.get(url).then(function(response){
									var result = response.data;
									if(result.success){
										_this.$message.success('删除成功!');
										_this.refreshResourceTree(1000);
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
			closeEditDialog: function(){
				if(this.editDialogVisible){
					this.editDialogVisible = false;
				}
				this.$refs.resourceEditForm.resetFields();
			}
		}
	});
}, DEFAULT_VUE_RENDER_TIMEOUT);