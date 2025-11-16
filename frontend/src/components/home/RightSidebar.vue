<template>
  <div class="right-bg">
    <RouterView :current-folder-id="currentFolderId" />
  </div>
</template>
<script name="RightSidebar" setup lang="ts">
import { watch } from 'vue'
import { RouterView, useRouter } from 'vue-router'

const props = defineProps<{
  currentPath: string
  currentFolderId: number
}>()

// 获取路由实例
const router = useRouter()

// 监听 currentPath 变化，动态导航到对应路由
watch(
  () => props.currentPath,
  (newPath) => {
    router.push({
      name: newPath,
    })
  },
  { immediate: true },
)
</script>
<style scoped>
.right-bg {
  width: calc(100% - 240px);
  height: 100%;
  position: fixed;
  top: 0;
  left: 240px;
  background-color: #f5f7fa;
}
</style>
