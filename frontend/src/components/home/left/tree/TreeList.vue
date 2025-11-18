<template>
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

  <!-- 引入右键菜单组件 -->
  <TreeRightMenu
    :x="menuX"
    :y="menuY"
    :visible="menuVisible"
    :current-node="currentNode"
    @handle-node="handleNode"
  />

  <!-- 新建子目录 -->
  <CreateNewFolder
    v-if="clickAddTreeNode != null"
    :click-add-tree-node="clickAddTreeNode"
    @handle-node2-null="handleNode2Null"
    @handle-add-success="handleAddSuccess"
  />

  <!-- 重命名目录 -->
  <RenameFolder
    v-if="clickRenameTreeNode != null"
    :click-rename-tree-node="clickRenameTreeNode"
    @handle-node2-null="handleNode2Null"
    @handle-rename-success="handleRenameSuccess"
  />
</template>
<script name="TreeList" setup lang="ts">
import { $api } from '@/api/api'
import { useTreeRightClick } from '@/hooks/folder/useTreeRightClick'
import type { Tree } from '@/types/tree/TreeDefine'
import { Folder } from '@element-plus/icons-vue'
import { inject, ref, type Ref } from 'vue'
import TreeRightMenu from './TreeRightMenu.vue'
import CreateNewFolder from './CreateNewFolder.vue'
import RenameFolder from './RenameFolder.vue'

const props = defineProps<{
  treeData: Tree[]
}>()

// 获取祖先组件传递的数据
const contentChange = inject<(routerPath: string) => void>('content-change')
const foldeIdChange = inject<(folderId: number) => void>('folder-id-change')

// 点击的节点
const clickAddTreeNode: Ref<Tree | null> = ref(null)
const clickRenameTreeNode: Ref<Tree | null> = ref(null)
const clickDeleteTreeNode: Ref<Tree | null> = ref(null)

// 节点右键点击事件
const { menuVisible, menuX, menuY, currentNode, handleContextMenu } = useTreeRightClick()

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

// 点击节点的目录时触发
const handleClick = (node: Tree) => {
  contentChange?.('paper-info')
  foldeIdChange?.(node.id)
}

// 处理菜单操作
const handleNode = (node: Tree, type: 'add' | 'delete' | 'rename') => {
  menuVisible.value = false
  switch (type) {
    case 'add':
      clickAddTreeNode.value = node
      break
    case 'delete':
      clickDeleteTreeNode.value = node
      break
    case 'rename':
      clickRenameTreeNode.value = node
      break
  }
}

// 菜单节点置为null
const handleNode2Null = (type: 'add' | 'delete' | 'rename') => {
  menuVisible.value = false
  switch (type) {
    case 'add':
      clickAddTreeNode.value = null
      break
    case 'delete':
      clickDeleteTreeNode.value = null
      break
    case 'rename':
      clickRenameTreeNode.value = null
      break
  }
}

// 添加节点成功
function handleAddSuccess(newNode: Tree) {
  const parentNode = clickAddTreeNode.value
  if (!parentNode) return

  // 初始化父节点的children
  if (!parentNode.children) {
    parentNode.children = []
  }

  // 将新节点插入父节点的children数组
  parentNode.children.push(newNode)

  // 更新父节点的hasChildren
  if (!parentNode.hasChildren) {
    parentNode.hasChildren = true
  }
}

// 重命名成功
function handleRenameSuccess(newName: string) {
  const targetNode = clickRenameTreeNode.value // 要重命名的节点
  if (!targetNode) return

  // 直接修改节点的label
  targetNode.label = newName
}

// 删除成功
function handleDeleteSuccess() {
  const targetNode = clickDeleteTreeNode.value // 要删除的节点
  if (!targetNode) return

  // 查找父节
  const parentNode = findParentNode(props.treeData, targetNode.id)
  if (!parentNode || !parentNode.children) return

  // 从父节点的children中移除目标节点
  parentNode.children = parentNode.children.filter((child) => child.id !== targetNode.id)

  // 若父节点没有子节点了，更新hasChildren为false
  if (parentNode.children.length === 0) {
    parentNode.hasChildren = false
  }
}

// 递归查找目标节点的父节点
function findParentNode(nodes: Tree[], targetId: number): Tree | null {
  for (const node of nodes) {
    // 若当前节点的children包含目标节点，当前节点即为父节点
    if (node.children?.some((child) => child.id === targetId)) {
      return node
    }
    // 递归查找子节点
    if (node.children) {
      const parent = findParentNode(node.children, targetId)
      if (parent) return parent
    }
  }
  return null
}
</script>
<style scoped>
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
