import type { PaperInfoResponse } from '@/types/paper/PaperInfoResponse'
import type { PaperListItem } from '@/types/paper/PaperListItem'
import type { PaperUpdateRequest } from '@/types/paper/PaperUpdateRequest'
import type { QueryPaper } from '@/types/paper/QueryPaper'
import type { Response } from '@/types/Response'
import httpInstance from '@/utils/request/http'

/**
 * 搜索符合条件论文列表
 * @param folderId 文件夹id，当文件夹id为0时默认搜索所有文件
 * @param keyword 关键字
 * @returns
 */
export const searchList = (folderId: number, keyword: string): Promise<Response<PaperListItem>> => {
  return httpInstance.post('/paper/search-list', null, {
    params: {
      folderId,
      keyword,
    },
  })
}

/**
 * 查询论文基本信息
 * @param paperDatas 查询数据
 * @returns
 */
export const queryPaperBaseInfo = (
  paperDatas: QueryPaper,
): Promise<Response<PaperInfoResponse>> => {
  return httpInstance.post('/paper/query-base-info', paperDatas)
}

/**
 * 通过AI获取论文信息
 * @param paperId
 * @returns
 */
export const chat = (paperId: number): Promise<Response<string>> => {
  return httpInstance.post('/paper/chat', null, {
    params: {
      paperId,
    },
  })
}

/**
 * 根据id查询论文
 * @param id
 * @returns
 */
export const queryById = (id: number): Promise<Response<PaperInfoResponse>> => {
  return httpInstance.get(`/paper/${id}`)
}

/**
 * 修改论文信息
 * @param updateData 修改 数据
 * @returns
 */
export const update = (updateData: PaperUpdateRequest): Promise<Response<void>> => {
  return httpInstance.put('/paper/update', updateData)
}

/**
 * 按照id删除论文
 * @param id
 * @param folderId
 * @returns
 */
export const deleteById = (id: number, folderId: number): Promise<Response<void>> => {
  return httpInstance.delete(`/paper/delete/${id}/${folderId}`)
}

/**
 * 批量删除论文
 * @param ids
 * @param folderId
 * @returns
 */
export const batchDeleteById = (ids: number[], folderId: number): Promise<Response<void>> => {
  return httpInstance.delete('/paper/batch-delete', {
    data: {
      paperIds: ids,
      folderId,
    },
  })
}
