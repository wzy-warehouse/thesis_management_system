import type { LoginRequest } from '@/types/user/LoginRequest'
import {
  autoLogin,
  changePassword,
  checkLogin,
  checkRemember,
  create as createUser,
  login,
} from './user'
import type { changePasswordRequest } from '@/types/user/ChangePasswordRequest'
import type { CreateUserRequest } from '@/types/user/CreateUserRequest'
import { deleteById, queryById, queryPaperBaseInfo, searchList, update } from './paper'
import type { PaperUpdateRequest } from '@/types/paper/PaperUpdateRequest'
import type { FolderCreateRequest } from '@/types/folder/FolderCreateRequest'
import { addPaper, create as createFolder, deleteFolder, queryFolder, renameFolder } from './folder'
import type { AddPaperRequest } from '@/types/folder/AddPaperRequest'
import type { CreateShareRequest } from '@/types/share/CreateShareRequest'
import { create as createShare } from './share'
import { generate } from './citation'
import { getSm2PublicKey } from './crypto'
import type { QueryPaper } from '@/types/paper/QueryPaper'
import { deleteByFolderId } from './recycle-bin'

export const $api = {
  // 用户模块
  user: {
    // 登录
    login: (loginData: LoginRequest) => login(loginData),

    // 检查登录状态
    checkLogin: () => checkLogin(),

    // 检查记住登录状态
    checkRemember: (token: string) => checkRemember(token),

    // 自动登录
    autoLogin: (token: string) => autoLogin(token),

    // 修改密码
    changePassword: (changeDatas: changePasswordRequest) => changePassword(changeDatas),

    // 创建新用户
    create: (createData: CreateUserRequest) => createUser(createData),
  },

  // 论文模块
  paper: {
    // 按照关键字搜索论文
    searchList: (folderId: number, keyword: string) => searchList(folderId, keyword),

    queryPaperBaseInfo: (paperDatas: QueryPaper) => queryPaperBaseInfo(paperDatas),

    // 获取论文
    queryById: (id: number) => queryById(id),

    // 更新论文
    update: (updateDatas: PaperUpdateRequest) => update(updateDatas),

    // 按照id删除论文
    deleteById: (id: number, folderId: number) => deleteById(id, folderId),
  },

  // 文件夹模块
  folder: {
    // 获取文件夹
    queryFolder: (parentId: number) => queryFolder(parentId),

    // 创建文件夹
    create: (createData: FolderCreateRequest) => createFolder(createData),

    // 重命名文件夹
    renameFolder: (name: string, id: number) => renameFolder(name, id),

    // 删除文件夹
    deleteFolder: (id: number) => deleteFolder(id),

    // 添加论文
    addPaper: (addPaperData: AddPaperRequest) => addPaper(addPaperData),
  },

  // 回收站模块
  recycleBin: {
    // 按照文件夹id删除论文
    deleteByFolderId: (folderId: number) => deleteByFolderId(folderId),
  },

  // 分享模块
  share: {
    create: (createData: CreateShareRequest) => createShare(createData),
  },

  // 参考文献模块
  citation: {
    // 生成参考文献
    generate: (paperId: number, formatId?: number) => generate(paperId, formatId),
  },

  // 加密模块
  crypto: {
    // 获取sm2公钥
    getSm2PublicKey: () => getSm2PublicKey(),
  },
}
