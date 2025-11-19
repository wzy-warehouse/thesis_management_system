<template>
  <el-dialog
    v-model="dialogVisible"
    title="删除提示"
    width="500"
    :before-close="handleClose"
    append-to-body
  >
    <span>你正在删除论文，该论文会被移动到回收站，有效期30天，30天后将自动删除，确定删除吗？</span>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="emit('close-delete-paper')">取消</el-button>
        <el-button type="primary" @click="confirmDelete"> 确定 </el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script name="DeletePaper" setup lang="ts">
import { $api } from '@/api/api'
import { ElMessage } from 'element-plus'
import { inject, ref } from 'vue'

const props = defineProps<{
  paperId: number
  folderId: number
}>()

const emit = defineEmits<{
  (e: 'close-delete-paper'): void
  (e: 'delete-paper-success', paperId: number): void
}>()

const deletePaperSuccess = inject<(paperId: number) => void>('delete-paper-success')

const dialogVisible = ref(true)

function confirmDelete() {
  $api.paper.deleteById(props.paperId, props.folderId).then((res) => {
    if (res.code === 200) {
      ElMessage.success(res.message)
      emit('close-delete-paper')
      if (deletePaperSuccess) {
        deletePaperSuccess(props.paperId)
      }
    } else {
      ElMessage.error(res.message)
    }
  })
}

// 关闭
function handleClose() {
  emit('close-delete-paper')
}
</script>
<style scoped></style>
