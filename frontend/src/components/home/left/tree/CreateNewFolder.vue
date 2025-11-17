<template>
  <div>
    <el-drawer
      v-model="showDraw"
      size="240"
      title="新建子目录"
      direction="ltr"
      :before-close="cancelForm"
    >
      <el-form
        :model="form"
        :rules="useCreateFolderValidator"
        ref="createFolderFormRef"
        label-width="80px"
      >
        <el-form-item label="目录名称" prop="name">
          <el-input v-model="form.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="父级目录">
          <el-input :value="clickAddTreeNode?.label" autocomplete="off" disabled />
        </el-form-item>
      </el-form>
      <div>
        <el-button @click="cancelForm">取消</el-button>
        <el-button type="primary" @click="createNewFolder"> 创建 </el-button>
      </div>
    </el-drawer>
  </div>
</template>
<script name="CreateNewFolder" setup lang="ts">
import { $api } from '@/api/api'
import { useCreateFolderValidator } from '@/hooks/folder/useCreateFolderValidator'
import type { Tree } from '@/types/tree/TreeDefine'
import { ElMessage, type ElForm } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'

const props = defineProps<{
  clickAddTreeNode: Tree | null
}>()

const emits = defineEmits<{
  (e: 'handle-node2-null', type: 'add' | 'rename' | 'delete'): void
  (e: 'handle-add-success', node: Tree): void
}>()

// 显示抽屉
const showDraw = true

// 表单
const form = reactive({
  name: '',
  parentId: 0,
})

// 表单对象
const createFolderFormRef = ref<InstanceType<typeof ElForm>>()

// 取消
function cancelForm() {
  emits('handle-node2-null', 'add')
}

// 创建
async function createNewFolder() {
  const isValid = await createFolderFormRef.value?.validate()
  if (!isValid) return

  // 创建目录
  $api.folder.create(form).then((res) => {
    if (res.code === 200) {
      // 添加目录动态树
      emits('handle-add-success', {
        id: res.data.folderId,
        label: form.name,
        hasChildren: false,
        queryState: true,
      })
      emits('handle-node2-null', 'add')
    } else {
      ElMessage.error(res.message)
    }
  })
}

onMounted(() => {
  if (props.clickAddTreeNode != null) {
    form.parentId = props.clickAddTreeNode.id
  }
})
</script>
<style scoped></style>
