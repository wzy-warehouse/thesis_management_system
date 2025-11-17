<template>
  <div class="home-bg">
    <LeftSidebar />
    <RightSidebar :current-path="currentPath" :current-folder-id="currentFolderId" />
  </div>
</template>
<script name="HomePage" setup lang="ts">
import LeftSidebar from '@/components/home/LeftSidebar.vue'
import RightSidebar from '@/components/home/RightSidebar.vue'
import { provide, ref } from 'vue'

// 响应式变量：存储当前点击按钮的目标路径
const currentPath = ref<string>('paper-info')
const currentFolderId = ref<number>(0)

// 处理左侧传来的点击事件，更新右侧内容
const handleContentChange = (path: string) => {
  currentPath.value = path
}

// 更改文件夹id
const handleFolderIdChange = (id: number) => {
  currentFolderId.value = id
}

provide('content-change', handleContentChange)
provide('folder-id-change', handleFolderIdChange)
</script>
<style scoped>
.home-bg {
  width: 100%;
  height: 100vh;
}
</style>
