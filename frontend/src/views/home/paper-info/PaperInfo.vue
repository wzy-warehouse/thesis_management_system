<template>
  <SearchAndUpload
    :current-folder-id="currentFolderId"
    @handle-paper-id-change="handlePaperIdChange"
  />

  <ShowInfo :table-datas="tableDatas" />
</template>
<script name="PaperInfo" setup lang="ts">
import SearchAndUpload from '@/components/home/right/paper-info/SearchAndUpload.vue'
import { provide, ref, watch, type Ref } from 'vue'
import ShowInfo from '../../../components/home/right/paper-info/ShowInfo.vue'
import type { QueryPaper } from '@/types/paper/QueryPaper'
import type { PaperInfoResponseData } from '@/types/paper/PaperInfoResponse'
import { Utils } from '@/utils/utils'
import { $api } from '@/api/api'

const props = defineProps<{
  currentFolderId: number
}>()

const queryPaper: Ref<QueryPaper> = ref({
  folderId: 0,
  paperId: 0,
  pageNum: 1,
  pageSize: 10,
  totalPage: 0,
})

// 表格数据
const tableDatas: Ref<PaperInfoResponseData[]> = ref([])

// 改变论文id
function handlePaperIdChange(id: number) {
  queryPaper.value.paperId = id
}

// 改变文件夹id
watch(
  () => props.currentFolderId,
  (id) => {
    queryPaper.value.folderId = id
  },
)

function updatePageNum(num: number) {
  queryPaper.value.pageNum = num
}

function updatePageSize(size: number) {
  queryPaper.value.pageSize = size
}

// 传递给孙子组件
provide('update-page-num', updatePageNum)
provide('update-page-size', updatePageSize)
provide('query-paper', queryPaper)

// 当文件夹id或者论文id改变时候触发
watch(
  () => [queryPaper.value.paperId, queryPaper.value.folderId],
  Utils.debounce(
    () => {
      // 查询论文信息
      $api.paper.queryPaperBaseInfo(queryPaper.value).then((res) => {
        tableDatas.value = res.data.data
        queryPaper.value.totalPage = res.data.totalPage
      })
    },
    300,
    true,
  ),
)
</script>
<style scoped></style>
