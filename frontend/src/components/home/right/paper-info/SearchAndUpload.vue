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

      <el-col :span="2" style="margin-left: 15px">
        <el-button type="success">上传论文</el-button>
      </el-col>
    </el-row>
  </div>
</template>
<script name="SearchAndUpload" setup lang="ts">
import { $api } from '@/api/api'
import { reactive, watch } from 'vue'

const props = defineProps<{
  currentFolderId: number
}>()

const emits = defineEmits<{
  (e: 'handlePaperIdChange', paperId: number): void
}>()

// 搜索表单
const searchForm = reactive({
  searchText: '',
})

// 文件夹id改变时候，清空搜索框和论文id
watch(
  () => props.currentFolderId,
  () => {
    searchForm.searchText = ''
    emits('handlePaperIdChange', 0)
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
