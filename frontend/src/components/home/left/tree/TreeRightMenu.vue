<template>
  <div
    class="tree-right-menu"
    :style="{ left: `${x}px`, top: `${y}px`, display: visible ? 'block' : 'none' }"
    @click.stop
  >
    <ul>
      <li @click="$emit('handle-node', currentNode, 'add')">新建子目录</li>
      <li @click="$emit('handle-node', currentNode, 'rename')">重命名</li>
      <li @click="$emit('handle-node', currentNode, 'delete')">删除</li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { defineProps } from 'vue'
import type { Tree } from '@/types/tree/TreeDefine'

// 接收的参数：显示位置、是否可见、当前节点信息
defineProps<{
  x: number
  y: number
  visible: boolean
  currentNode: Tree | null
}>()
</script>

<style scoped>
.tree-right-menu {
  position: fixed;
  background: #333;
  color: #fff;
  border-radius: 4px;
  padding: 4px 0;
  min-width: 120px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
  z-index: 1000;
}

.tree-right-menu ul {
  list-style: none;
  margin: 0;
  padding: 0;
}

.tree-right-menu li {
  padding: 6px 16px;
  cursor: pointer;
}

.tree-right-menu li:hover {
  background: #555;
}
</style>
