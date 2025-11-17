<template>
  <div class="bar-bg">
    <TreeList :tree-data="treeData" />
  </div>
</template>

<script name="BarPart" setup lang="ts">
import { ref, onMounted } from 'vue'
import type { Tree } from '@/types/tree/TreeDefine'
import { $api } from '@/api/api'
import TreeList from './tree/TreeList.vue'

// 树形数据（初始为空，将从后台加载根节点）
const treeData = ref<Tree[]>([])

// 组件挂载时，加载根目录
onMounted(() => {
  $api.folder.queryFolder(0).then((res) => {
    treeData.value = res.data
  })
})
</script>

<style scoped>
.bar-bg {
  width: 100%;
  height: calc(100vh - 170px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}
</style>
