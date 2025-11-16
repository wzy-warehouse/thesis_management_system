export interface Tree {
  id: number
  label: string
  queryState: boolean
  hasChildren: boolean
  children?: Tree[]
}
