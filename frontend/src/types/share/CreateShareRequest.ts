export interface CreateShareRequest {
  paperId: number
  permission: 1 | 2
  expireDays: 1 | 7 | 30 | null
}
