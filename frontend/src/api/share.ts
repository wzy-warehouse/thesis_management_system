import type { Response } from '@/types/Response'
import type { CreateShareRequest } from '@/types/share/CreateShareRequest'
import type { CreateShareResponse } from '@/types/share/CreateShareResponse'
import httpInstance from '@/utils/request/http'

/**
 * 生成分享链接
 * @param createData
 * @returns
 */
export const create = (createData: CreateShareRequest): Promise<Response<CreateShareResponse>> => {
  return httpInstance.post('/share/create', createData)
}
