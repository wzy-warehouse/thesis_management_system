<template>
  <div>
    <el-drawer
      v-model="showDraw"
      size="240"
      title="重命名"
      direction="ltr"
      :before-close="cancelForm"
    >
      <el-form
        :model="form"
        :rules="useRenameFolderValidator"
        ref="renameFolderFormRef"
        label-width="80px"
      >
        <el-form-item label="目录名称" prop="name">
          <el-input v-model="form.name" autocomplete="off" />
        </el-form-item>
      </el-form>
      <div>
        <el-button @click="cancelForm">取消</el-button>
        <el-button type="primary" @click="renameFolder"> 修改 </el-button>
      </div>
    </el-drawer>
  </div>
</template>
<script name="RenameFolder" setup lang="ts">
import { $api } from '@/api/api'
import { useRenameFolderValidator } from '@/hooks/folder/useRenameFolderValidator'
import type { Tree } from '@/types/tree/TreeDefine'
import { ElMessage, type ElForm } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'

const props = defineProps<{
  clickRenameTreeNode: Tree | null
}>()

const emits = defineEmits<{
  (e: 'handle-node2-null', type: 'add' | 'rename' | 'delete'): void
  (e: 'handle-rename-success', newName: string): void
}>()

// 显示抽屉
const showDraw = true

// 表单
const form = reactive({
  name: '',
  id: 0,
})

// 表单对象
const renameFolderFormRef = ref<InstanceType<typeof ElForm>>()

// 取消
function cancelForm() {
  emits('handle-node2-null', 'rename')
}

// 重命名
async function renameFolder() {
  const isValid = await renameFolderFormRef.value?.validate()
  if (!isValid) return

  // 重命名
  $api.folder.renameFolder(form.name, form.id).then((res) => {
    if (res.code === 200) {
      // 动态重命名
      emits('handle-rename-success', form.name)
      emits('handle-node2-null', 'rename')
    } else {
      ElMessage.error(res.message)
    }
  })
}

onMounted(() => {
  if (props.clickRenameTreeNode != null) {
    form.id = props.clickRenameTreeNode.id
    form.name = props.clickRenameTreeNode.label
  }
})
</script>
<style scoped></style>
