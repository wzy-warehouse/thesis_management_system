<template>
  <div class="delete-bg">
    <el-dialog
      v-model="dialogVisible"
      title="删除提示"
      width="500"
      :before-close="closeDialog"
      append-to-body
    >
      <span class="message">确定删除该目录吗？一旦删除将无法回复。</span>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="closeDialog">取消</el-button>
          <el-button type="danger" @click="deleteFolder"> 删除 </el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog
      v-model="dialogRecycleVisible"
      title="删除提示"
      width="500"
      :before-close="closeDialog"
      append-to-body
    >
      <span class="message"
        >删除失败，回收站中存在归属于该目录的论文，是否先删除回收站中归属于该目录的论文？</span
      >
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="closeRecycleDialog()">取消</el-button>
          <el-button type="primary" @click="deleteRecycle"> 确认 </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
<script name="DeleteFolder" setup lang="ts">
import { $api } from '@/api/api'
import type { Tree } from '@/types/tree/TreeDefine'
import { ElMessage } from 'element-plus'
import { onMounted, ref } from 'vue'

const props = defineProps<{
  clickDeleteTreeNode: Tree | null
}>()

const emits = defineEmits<{
  (e: 'handle-node2-null', type: 'add' | 'rename' | 'delete'): void
  (e: 'handle-delete-success'): void
}>()

const dialogVisible = ref(false)

const dialogRecycleVisible = ref(false)

onMounted(() => {
  // 验证是否可以删除
  if (!props.clickDeleteTreeNode?.hasChildren) {
    dialogVisible.value = true
  } else {
    ElMessage.error('当前目录下有子目录，请先删除子目录')
    emits('handle-node2-null', 'delete')
  }
})

// 删除目录
function deleteFolder() {
  if (!props.clickDeleteTreeNode) {
    ElMessage.error('未选择有效的目录节点')
    return
  }
  // 向后端发起请求
  $api.folder.deleteFolder(props.clickDeleteTreeNode.id).then((res) => {
    if (res.code === 200) {
      ElMessage.success('删除成功，刷新页面后生效。')
      emits('handle-delete-success')
      emits('handle-node2-null', 'delete')
    } else if (res.code === 409) {
      dialogVisible.value = false
      // 提示清空回收站
      dialogRecycleVisible.value = true
    } else {
      ElMessage.error(`删除失败：${res.message}`)
    }
  })
}

// 删除回收站中该目录下的论文
function deleteRecycle() {
  if (!props.clickDeleteTreeNode) {
    ElMessage.error('未选择有效的目录节点')
    return
  }
  $api.recycleBin.deleteByFolderId(props.clickDeleteTreeNode?.id).then((res) => {
    if (res.code === 200) {
      dialogRecycleVisible.value = false
      dialogVisible.value = true
      deleteFolder()
    } else {
      ElMessage.error(`删除失败：${res.message}`)
    }
  })
}

// 关闭弹窗
function closeDialog() {
  dialogVisible.value = false
  emits('handle-node2-null', 'delete')
}

function closeRecycleDialog() {
  dialogRecycleVisible.value = false
  emits('handle-node2-null', 'delete')
}
</script>
<style scoped>
.message {
  color: fff8f8;
  font-size: 16px;
}
</style>
