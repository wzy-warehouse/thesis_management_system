import axios, { type InternalAxiosRequestConfig, type AxiosResponse } from 'axios'
import config from '@/config/config.json'
import { useUserStore } from '@/stores/useUserStore'
import { cryptUtils } from '../safety/crypto'

// 扩展Axios内部配置类型
declare module 'axios' {
  interface InternalAxiosRequestConfig {
    __sm4Key?: string
    isNoEncryptUrl?: boolean
  }
}

// 无需加密的接口路径
const NO_ENCRYPT_URLS = ['/crypto/sm2/public-key']

// 无需token的接口路径
const NO_TOKEN_URLS = ['/crypto/sm2/public-key', '/user/login']

const httpInstance = axios.create({
  baseURL: config.apiBaseUrl,
  timeout: 15000, // 增加超时时间
  withCredentials: true,
})

// 请求拦截器
httpInstance.interceptors.request.use(
  async (config: InternalAxiosRequestConfig): Promise<InternalAxiosRequestConfig> => {
    const { url, method } = config
    const userStore = useUserStore()

    // 初始化headers
    config.headers = config.headers || {}

    // Token处理
    const isNoTokenUrl = NO_TOKEN_URLS.some((path) => url?.includes(path))
    if (!isNoTokenUrl && userStore.token) {
      config.headers.Authorization = `Bearer ${userStore.token}`
    }

    // 加密处理
    const isNoEncryptUrl = NO_ENCRYPT_URLS.some((path) => url?.includes(path))
    config.isNoEncryptUrl = isNoEncryptUrl

    if (!isNoEncryptUrl) {
      try {
        const sm4Key = cryptUtils.generateSm4Key()
        config.__sm4Key = sm4Key

        const sm4KeyEncrypted = await cryptUtils.sm2Encrypt(sm4Key)

        // GET请求：处理URL参数
        if (method?.toUpperCase() === 'GET' && config.params) {
          const encryptedParams = cryptUtils.sm4Encrypt(sm4Key, config.params)
          config.params = {
            encryptedData: encryptedParams,
            sm4KeyEncrypted: sm4KeyEncrypted,
          }
        }

        // POST/PUT/DELETE请求：处理请求体
        if (
          ['POST', 'PUT', 'DELETE', 'PATCH'].includes(method?.toUpperCase() || '') &&
          config.data
        ) {
          if (config.data instanceof FormData) {
            const encryptedFormData = cryptUtils.encryptFormData(sm4Key, config.data)

            // 创建新的FormData并添加加密密钥
            const newFormData = new FormData()

            // 复制所有加密后的字段
            for (const [key, value] of encryptedFormData.entries()) {
              newFormData.append(key, value)
            }

            // 添加加密的SM4密钥
            newFormData.append('sm4KeyEncrypted', sm4KeyEncrypted)
            config.data = newFormData
          } else {
            const encryptedData = cryptUtils.sm4Encrypt(sm4Key, config.data)
            config.data = {
              encryptedData: encryptedData,
              sm4KeyEncrypted: sm4KeyEncrypted,
            }
          }
        }
      } catch (error) {
        console.error('请求加密失败:', error)
        return Promise.reject(new Error('请求数据加密失败'))
      }
    }

    return config
  },
  (error) => {
    console.error('请求拦截器错误:', error)
    return Promise.reject(error)
  },
)

// 响应拦截器
httpInstance.interceptors.response.use(
  (response: AxiosResponse) => {
    const { config, data } = response

    // 非加密接口或无SM4密钥，直接返回
    if (config.isNoEncryptUrl || !config.__sm4Key) {
      return data
    }

    // 解密响应数据
    try {
      // 如果后端返回的是加密数据字符串
      if (typeof data === 'string') {
        return cryptUtils.sm4Decrypt(config.__sm4Key, data)
      }

      // 如果后端返回的是对象，检查是否有加密字段
      if (data && typeof data === 'object') {
        // 这里可以根据后端返回格式调整
        return cryptUtils.sm4Decrypt(config.__sm4Key, data.encryptedData || data)
      }

      return data
    } catch (error) {
      console.error('响应数据解密失败:', error)
      return Promise.reject(new Error('数据解密失败，请重试'))
    }
  },
  (error) => {
    console.error('响应拦截器错误:', error)

    // 处理解密相关的错误
    if (error.response?.status === 500 && error.response?.data?.msg?.includes('解密')) {
      return Promise.reject(new Error('服务器解密失败，请检查密钥配置'))
    }

    return Promise.reject(error)
  },
)

export default httpInstance
