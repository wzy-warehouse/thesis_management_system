// hooks/useTreeRightClick.ts
import { ref, onMounted, onUnmounted } from 'vue'
import type { Tree } from '@/types/tree/TreeDefine'

export function useTreeRightClick() {
  // 菜单状态
  const menuVisible = ref(false)
  const menuX = ref(0)
  const menuY = ref(0)
  const currentNode = ref<Tree | null>(null)

  // 关闭菜单（点击空白处或执行操作后）
  const closeMenu = () => {
    menuVisible.value = false
    currentNode.value = null
  }

  // 监听全局点击，关闭菜单
  const handleGlobalClick = () => {
    closeMenu()
  }

  // 处理右键点击事件
  const handleContextMenu = (e: MouseEvent, node: Tree) => {
    e.preventDefault()
    e.stopPropagation()

    // 计算菜单位置
    menuX.value = e.clientX
    menuY.value = e.clientY
    currentNode.value = node
    menuVisible.value = true
  }

  // 挂载时监听全局点击
  onMounted(() => {
    document.addEventListener('click', handleGlobalClick)
  })

  // 卸载时移除监听
  onUnmounted(() => {
    document.removeEventListener('click', handleGlobalClick)
  })

  return {
    menuVisible,
    menuX,
    menuY,
    currentNode,
    handleContextMenu,
    closeMenu,
  }
}
