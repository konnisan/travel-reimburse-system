/**
 * zhangcheng
 * 2024年12月18日
 * 胜意科技前端请求工具类
 */
import axios from 'axios'
import type {
    AxiosInstance,
    AxiosRequestConfig,
    AxiosResponse,
    InternalAxiosRequestConfig
} from 'axios'
import { ElMessage, ElLoading } from 'element-plus'

const TOKEN_KEY = 'VE_ACCESS_TOKEN'

/**
 * 自定义配置
 */
interface RequestConfig extends AxiosRequestConfig {
    loading?: boolean
}

/**
 * 后端统一返回结构
 */
export interface ApiResponse<T = any> {
    code: number
    data: T
    msg: string
}

/**
 * 默认请求配置
 */
const defaultConfig: AxiosRequestConfig = {
    baseURL: import.meta.env.VITE_API_URL ?? '',
    timeout: 60000,
    headers: {
        'Content-Type': 'application/json;charset=UTF-8',
        'Accept': 'application/json, text/plain, */*'
    },
    withCredentials: true,
    timeoutErrorMessage: '请求超时',
    responseEncoding: 'utf-8',
    validateStatus: (status: number) => status >= 200 && status < 300
}

/**
 * 请求类
 */
class Request {
    private readonly instance: AxiosInstance
    private loadingInstance: any = null

    constructor() {
        this.instance = axios.create(defaultConfig)
        this.setupInterceptors()
    }

    private setupInterceptors() {
        /**
         * 请求拦截
         */
        this.instance.interceptors.request.use(
            (config: InternalAxiosRequestConfig & { loading?: boolean }) => {
                if (config.loading) {
                    this.showLoading()
                }
                const token = localStorage.getItem(TOKEN_KEY) || localStorage.getItem('VETOKEN')
                if (token && config.headers) {
                    config.headers.Authorization = `Bearer ${token}`
                }
                return config
            },
            (error) => {
                this.hideLoading()
                ElMessage.error('请求失败')
                return Promise.reject(error)
            }
        )

        /**
         * 响应拦截
         */
        this.instance.interceptors.response.use(
            (response: AxiosResponse<ApiResponse>) => {
                this.hideLoading()
                const res = response.data
                // 成功：直接 return res.data
                if (res.code === 200) {
                    return res.data
                }
                // 失败提示
                ElMessage.error(res.msg || '请求失败')
                return Promise.reject(res)
            },
            (error) => {
                this.hideLoading()
                let msg = '网络异常'
                if (!error.response) msg = '服务器连接失败'
                else {
                    const status = error.response.status
                    switch (status) {
                        case 401:
                            msg = '登录已过期'
                            localStorage.removeItem(TOKEN_KEY)
                            localStorage.removeItem('VE_USER_INFO')
                            localStorage.removeItem('token')
                            window.dispatchEvent(new Event('auth:expired'))
                            break
                        case 403: msg = '权限不足'; break
                        case 404: msg = '接口不存在'; break
                        case 500: msg = '服务器错误'; break
                    }
                }
                if (error.message.includes('timeout')) msg = '请求超时'
                ElMessage.error(msg)
                return Promise.reject(error)
            }
        )
    }

    /**
     * 请求锁屏
     * @private
     */
    private showLoading() {
        this.loadingInstance = ElLoading.service({
            lock: true,
            text: '加载中...',
            background: 'rgba(0,0,0,0.05)'
        })
    }

    /**
     * 强求隐藏
     * @private
     */
    private hideLoading() {
        this.loadingInstance?.close()
    }

    /**
     * 核心请求方法
     * @param config
     */
    request<T = any>(config: RequestConfig): Promise<T> {
        return this.instance.request(config)
    }

    /**
     * 请求get方法
     * @param url
     * @param config
     */
    get<T = any>(url: string, config?: RequestConfig): Promise<T> {
        return this.instance.get(url, config)
    }

    /**
     * 强求post方法
     * @param url
     * @param data
     * @param config
     */
    post<T = any>(url: string, data?: any, config?: RequestConfig): Promise<T> {
        return this.instance.post(url, data, config)
    }

    /**
     * 强求put方法
     * @param url
     * @param data
     * @param config
     */
    put<T = any>(url: string, data?: any, config?: RequestConfig): Promise<T> {
        return this.instance.put(url, data, config)
    }

    /**
     * 强求delete方法
     * @param url
     * @param data
     * @param config
     */
    delete<T = any>(url: string, config?: RequestConfig): Promise<T> {
        return this.instance.delete(url, config)
    }
}

export default new Request()
