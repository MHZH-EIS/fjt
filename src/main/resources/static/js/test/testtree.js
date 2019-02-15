define(function () {
  return function () {
    var resourceTree = $("#standard-resource-tree");
    var resourcePanel = $("#standard-resource-panel");
    var currentRoelId = 0;


    //实例化权限树
    resourceTree.tree({
      url: "/resource/standard/listtree",
      checkbox: true,
      cascadeCheck: false
    });

    $("#standard-resource-save").on("click", function () {
      if (currentRoelId) {
        // 获取需要关联的资源节点
        var nodes = resourceTree.tree('getChecked', ['checked', 'indeterminate']);
        // 获取节点的ID列表
        var resourceId = [];
        $.each(nodes, function () {
          resourceId.push(this.id);
        });
        var prams = "roleId=" + currentRoelId + "&resourceId=" + resourceId.join("&resourceId=")
        // 发送请求保存关系
        $.post("/system/role/resource/save", prams, function (rsp) {
          if (rsp.success) {
            dg.datagrid("reload");
            $.messager.alert("系统提醒", "保存成功！");
          }
        });
      } else {
        $.messager.alert("系统提醒", "请先选择角色");
      }
    });
  }
});