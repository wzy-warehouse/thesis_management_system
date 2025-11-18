import type { AddPaperRequest } from '@/types/folder/AddPaperRequest'
import type { FolderCreateRequest } from '@/types/folder/FolderCreateRequest'
import type { FolderCreateResponse } from '@/types/folder/FolderCreateResponse'
import type { Response } from '@/types/Response'
import type { Tree } from '@/types/tree/TreeDefine'
import httpInstance from '@/utils/request/http'

/**
 * 查询文件夹
 * @param parentId
 * @returns
 */
export const queryFolder = (parentId: number): Promise<Response<Tree[]>> => {
  return httpInstance.post('/folder/query-folder', null, {
    params: {
      parentId,
    },
  })
}

/**
 * 创建文件夹
 * @param createData
 * @returns
 */
export const create = (
  createData: FolderCreateRequest,
): Promise<Response<FolderCreateResponse>> => {
  return httpInstance.post('/folder/create', null, {
    params: createData,
  })
}

/**
 * 重命名文件夹
 * @param name
 * @param id
 * @returns
 */
export const renameFolder = (name: string, id: number): Promise<Response<void>> => {
  return httpInstance.post('/folder/rename', null, {
    params: {
      name,
      id,
    },
  })
}

/**
 * 添加论文
 * @param addPaperData
 * @returns
 */
export const addPaper = (addPaperData: AddPaperRequest): Promise<Response<void>> => {
  return httpInstance.post('/folder/add-paper', null, {
    params: addPaperData,
  })
}
