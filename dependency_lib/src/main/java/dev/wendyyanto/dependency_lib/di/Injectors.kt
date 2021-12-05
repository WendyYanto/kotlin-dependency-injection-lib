package dev.wendyyanto.dependency_lib.di

import dev.wendyyanto.dependency_lib.annotation.EntryPoint
import dev.wendyyanto.dependency_lib.annotation.Inject
import dev.wendyyanto.dependency_lib.annotation.Provides
import java.lang.reflect.Method
import kotlin.reflect.KClass

object Injectors {

  private val methodToClassMap: MutableMap<Method, Class<*>> = mutableMapOf()
  private val classToMethodMap: MutableMap<Class<*>, Method> = mutableMapOf()
  private val methodDependencies: MutableMap<Method, List<Class<*>>> = mutableMapOf()
  private val methodTree: MutableMap<Method, MutableSet<Method>> = mutableMapOf()

  fun <T : InjectorModule, R : Any> inject(kClass: KClass<T>, entryPointClazz: R) {
    val dependencies: MutableMap<Class<*>, Any> = mutableMapOf()

    kClass.java.declaredMethods.filter { method ->
      method.isAnnotationPresent(Provides::class.java)
    }.forEach {
      methodToClassMap[it] = it.returnType
      classToMethodMap[it.returnType] = it
      methodDependencies[it] = it.parameterTypes.toList()
    }

    generateMethodTree()

    val rootMethod = kClass.java.declaredMethods.firstOrNull { method ->
      method.isAnnotationPresent(EntryPoint::class.java)
    }

    // ToDo: to improve
    val safeRootMethod = rootMethod ?: throw IllegalArgumentException("Should have root entry point")

    val moduleInstance = kClass.java.newInstance()
    injectByDFS(safeRootMethod, moduleInstance, dependencies)

    // Injecting Dependencies
    entryPointClazz.javaClass.fields.filter { field ->
      field.isAnnotationPresent(Inject::class.java)
    }.forEach {
      it.set(entryPointClazz, dependencies[it.type])
    }
  }

  private fun <T> injectByDFS(
    method: Method,
    moduleInstance: T,
    dependencies: MutableMap<Class<*>, Any>
  ) {
    val methodDependencies = methodTree[method].orEmpty()

    if (methodDependencies.isEmpty()) {
      methodToClassMap[method]?.let { safeClass ->
        dependencies[safeClass] = method.invoke(moduleInstance)
      }
      return
    }

    methodDependencies.forEach { methodDependency ->
      injectByDFS(methodDependency, moduleInstance, dependencies)
    }

    val parameters = methodDependencies.map { method ->
      methodToClassMap[method]
    }.map { clazz ->
      dependencies[clazz]
    }.toTypedArray()

    methodToClassMap[method]?.let { safeClass ->
      dependencies[safeClass] = method.invoke(moduleInstance, *parameters)
    }
  }

  private fun generateMethodTree() {
    methodToClassMap.forEach {
      if (methodTree[it.key] == null) {
        methodTree[it.key] = mutableSetOf()
      }
      val safeMethods: MutableList<Method> = mutableListOf()
      methodDependencies[it.key]?.forEach { clazz ->
        classToMethodMap[clazz]?.let { safeMethod ->
          safeMethods.add(safeMethod)
        }
      }
      methodTree[it.key]?.addAll(safeMethods)
    }
  }
}