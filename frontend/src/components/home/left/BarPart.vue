<template>
  <div class="bar-bg">
    <el-tree
      class="tree"
      :data="treeData"
      :load="loadChildren"
      @node-click="handleClick"
      @node-contextmenu="handleNodeContextMenu"
      lazy
    >
      <!-- 自定义节点内容：图标 + 文字 -->
      <template #default="{ node }">
        <el-icon class="tree-icon">
          <Folder />
        </el-icon>
        <span class="tree-label">{{ node.label }}</span>
      </template>
    </el-tree>
  </div>

  <!-- 引入右键菜单组件 -->
  <TreeRightMenu
    :x="menuX"
    :y="menuY"
    :visible="menuVisible"
    :current-node="currentNode"
    @handleAdd="handleAddNode"
    @handleRename="handleRenameNode"
    @handleDelete="handleDeleteNode"
  />
</template>

<script name="BarPart" setup lang="ts">
import { ref, onMounted } from 'vue'
import type { Tree } from '@/types/tree/TreeDefine'
import { Folder } from '@element-plus/icons-vue'
import { $api } from '@/api/api'
import TreeRightMenu from './tree/TreeRightMenu.vue'
import { useTreeRightClick } from '@/hooks/folder/useTreeRightClick'

// 接收父组件传递的emit
const emit = defineEmits(['content-change', 'folder-id-change'])

const { menuVisible, menuX, menuY, currentNode, handleContextMenu } = useTreeRightClick()

// 树形数据（初始为空，将从后台加载根节点）
const treeData = ref<Tree[]>([])

// 组件挂载时，加载根目录
onMounted(() => {
  $api.folder.queryFolder(0).then((res) => {
    treeData.value = res.data
  })
})

// 加载子节点的回调函数
const loadChildren = async (node: { data: Tree }, resolve: (data: Tree[]) => void) => {
  // 当前节点
  const currentNode = node.data

  // 如果有下级目录就获取，没有就不获取
  if (!currentNode.queryState && currentNode.hasChildren) {
    $api.folder.queryFolder(currentNode.id).then((res) => {
      currentNode.children = res.data
      currentNode.queryState = true
      resolve(res.data)
    })
  } else {
    currentNode.queryState = true
    return resolve([])
  }
}

// 右键节点触发
const handleNodeContextMenu = (e: MouseEvent, node: Tree) => {
  handleContextMenu(e, node)
}

// 处理菜单操作
const handleAddNode = (node: Tree) => {
  console.log('新增子目录：', node)
  // 调用接口新增节点...
  menuVisible.value = false // 操作后关闭菜单
}

const handleRenameNode = (node: Tree) => {
  console.log('重命名：', node)
  // 调用接口重命名...
  menuVisible.value = false
}

const handleDeleteNode = (node: Tree) => {
  console.log('删除：', node)
  // 调用接口删除...
  menuVisible.value = false
}

// 点击节点的目录时触发
const handleClick = (node: Tree) => {
  emit('content-change', 'paper-info')
  emit('folder-id-change', node.id)
}
</script>

<style scoped>
.bar-bg {
  width: 100%;
  height: calc(100vh - 170px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.tree {
  background-color: transparent;
}

.tree-label {
  color: #fff !important;
}

.tree-icon {
  margin-right: 8px;
  color: #fff !important;
}

::v-deep .el-tree-node__content {
  background-color: transparent !important;
}

::v-deep .el-tree-node__content:hover {
  background-color: transparent !important;
}

::v-deep .el-tree-node.is-current > .el-tree-node__content {
  background-color: transparent !important;
}

::v-deep .el-tree-node__expand-icon {
  color: rgba(255, 255, 255, 0.7);
}
</style>
