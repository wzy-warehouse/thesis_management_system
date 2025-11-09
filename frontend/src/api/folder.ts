import type { AddPaperRequest } from '@/types/folder/AddPaperRequest'
import type { FolderCreateRequest } from '@/types/folder/FolderCreateRequest'
import type { FolderCreateResponse } from '@/types/folder/FolderCreateResponse'
import type { Response } from '@/types/Response'
import httpInstance from '@/utils/request/http'

/**
 * 创建文件见
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
 * 添加论文
 * @param addPaperData
 * @returns
 */
export const addPaper = (addPaperData: AddPaperRequest): Promise<Response<void>> => {
  return httpInstance.post('/folder/add-paper', null, {
    params: addPaperData,
  })
}
