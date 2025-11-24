<template>
  <div class="page-info-bg">
    <el-row>
      <el-col :span="8" class="search-component">
        <el-form ref="form" :model="searchForm">
          <el-autocomplete
            v-model="searchForm.searchText"
            :fetch-suggestions="querySearchAsync"
            clearable
            placeholder="检索论文标题、作者或关键词..."
            @select="emits('handlePaperIdChange', $event.id)"
          />
        </el-form>
      </el-col>

      <el-col :span="2" style="margin-left: 15px" v-if="currentFolderId > 0">
        <el-button type="success" @click="viewUpload = true">上传论文</el-button>
      </el-col>
    </el-row>
  </div>

  <!-- 上传组件 -->
  <UploadPaper
    v-if="viewUpload"
    @cancel-upload="cancelUpload"
    :current-folder-id="currentFolderId"
  />
</template>
<script name="SearchAndUpload" setup lang="ts">
import { $api } from '@/api/api'
import { inject, reactive, ref, watch } from 'vue'
import UploadPaper from './upload/UploadPaper.vue'

const props = defineProps<{
  currentFolderId: number
}>()

const emits = defineEmits<{
  (e: 'handlePaperIdChange', paperId: number): void
}>()

// 接收PaperInfo.vue组件中传递的query-paper-list
const queryPaperList = inject<() => void>('query-paper-list')

// 搜索表单
const searchForm = reactive({
  searchText: '',
})

// 上传组件
const viewUpload = ref(false)

// 文件夹id改变时候，清空搜索框和论文id
watch(
  () => props.currentFolderId,
  () => {
    searchForm.searchText = ''
  },
)

// 查询函数
let timeout: ReturnType<typeof setTimeout> // 定时器
const querySearchAsync = async (queryString: string, cb: (arg: unknown) => void) => {
  const results = (await $api.paper.searchList(props.currentFolderId, queryString)).data
  clearTimeout(timeout)
  timeout = setTimeout(() => {
    cb(results)
  }, 1000)
}

// 取消上传
function cancelUpload() {
  viewUpload.value = false

  // 更新文件列表
  if (queryPaperList) {
    queryPaperList()
  }
}
</script>
<style scoped>
.page-info-bg {
  width: 98%;
  height: 60px;
  margin: 0 auto;
  border-radius: 8px;
  background-color: #fff;
  box-shadow: 0px 2px 8px 0px rgba(0, 0, 0, 0.05);
  line-height: 60px;
}
</style>
