package com.example.campus

import com.example.campus.api.IService
import com.example.campus.view.Constance
import java.lang.reflect.InvocationTargetException
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by kangzhibo on 2022/9/2
 * @author kangzhibo
 */
object ServiceManager {
    private val concurrentHashMap = ConcurrentHashMap<Class<out IService?>, IService>()

    @JvmStatic
    fun <T : IService?> getService(clazz: Class<T>): T? {
        @Suppress("UNCHECKED_CAST")
        var impl: T? = concurrentHashMap[clazz] as T
        if (impl == null) {
            try {
                impl = getGeneratedImplementation(clazz)
                impl?.apply {
                    concurrentHashMap[clazz] = this
                }
                impl?.onInit()
            } catch (e: Exception) {
                println(e)
            }
        }
        return impl
    }


    private fun <T> getGeneratedImplementation(clazz: Class<T>): T? {
        val className = clazz.simpleName.apply {
            substring(1, length - 1)
        }
        val fullName: String =
            Constance.SERVICE_PKG_NAME + "." + className + Constance.SERVICE_SUF_FIX
        return try {
            @Suppress("UNCHECKED_CAST")
            val aClass = Class.forName(fullName) as? Class<T>
            val cons = aClass?.getDeclaredConstructor()
            cons?.isAccessible = true
            cons?.newInstance()
        } catch (e: ClassNotFoundException) {
            throw RuntimeException("ClassNotFoundException:$fullName")
        } catch (e: IllegalAccessException) {
            throw RuntimeException("IllegalAccessException:$fullName")
        } catch (e: InstantiationException) {
            throw RuntimeException("InstantiationException:$fullName")
        } catch (e: NoSuchMethodException) {
            throw RuntimeException("NoSuchMethodException:$fullName")
        } catch (e: InvocationTargetException) {
            throw RuntimeException("InvocationTargetException:$fullName")
        }
    }
}