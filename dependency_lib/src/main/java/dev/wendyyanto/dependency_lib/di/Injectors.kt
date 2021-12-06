package dev.wendyyanto.dependency_lib.di

import dev.wendyyanto.dependency_lib.annotation.Inject
import dev.wendyyanto.dependency_lib.annotation.Provides
import java.lang.reflect.Field
import java.lang.reflect.Method
import kotlin.reflect.KClass

object Injectors {

  private val methodToClassMap: MutableMap<Method, Class<*>> by lazy {
    mutableMapOf()
  }
  private val classToMethodMap: MutableMap<Class<*>, Method> by lazy {
    mutableMapOf()
  }
  private val methodDependencies: MutableMap<Method, List<Class<*>>> by lazy {
    mutableMapOf()
  }
  private val methodTree: MutableMap<Method, MutableSet<Method>> by lazy {
    mutableMapOf()
  }
  private val appDependencies: MutableMap<Class<*>, Any> by lazy {
    mutableMapOf()
  }

  fun <T : InjectorModule> injectApp(kClass: KClass<T>) {
    saveMethods(kClass)
    generateMethodTree()

    val appModuleInstance = kClass.java.newInstance()

    methodTree.keys.forEach { methodKey ->
      constructDependenciesByDFS(methodKey, appModuleInstance, appDependencies)
    }

    cleanUp()
  }

  fun <T : InjectorModule, R : Any> inject(kClass: KClass<T>, entryPointClass: R) {
    val dependencies: MutableMap<Class<*>, Any> = appDependencies.toMutableMap()

    saveMethods(kClass)
    generateMethodTree()

    val moduleInstance = kClass.java.newInstance()

    // Construct and inject dependencies
    entryPointClass.javaClass.fields.filter { field ->
      field.isAnnotationPresent(Inject::class.java)
    }.onEach { field ->
      constructAndCacheDependencies(field, dependencies, moduleInstance)
    }.forEach { field ->
      field.set(entryPointClass, dependencies[field.type])
    }

    cleanUp()
  }

  private fun <T: InjectorModule> saveMethods(kClass: KClass<T>) {
    kClass.java.declaredMethods
      .filter { method -> method.isAnnotationPresent(Provides::class.java) }
      .forEach(::saveMethod)
  }

  private fun <T : InjectorModule> constructAndCacheDependencies(
    field: Field,
    dependencies: MutableMap<Class<*>, Any>,
    moduleInstance: T
  ) {
    if (dependencies.containsKey(field.type)) {
      return
    }
    val rootMethod = classToMethodMap[field.type]
    val safeRootMethod =
      rootMethod ?: throw IllegalArgumentException("Should have root entry point")
    constructDependenciesByDFS(safeRootMethod, moduleInstance, dependencies)
  }

  private fun saveMethod(method: Method) {
    methodToClassMap[method] = method.returnType
    classToMethodMap[method.returnType] = method
    methodDependencies[method] = method.parameterTypes.toList()
  }

  private fun <T> constructDependenciesByDFS(
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
      constructDependenciesByDFS(methodDependency, moduleInstance, dependencies)
    }

    val parameters = methodDependencies
      .map { methodDependency -> methodToClassMap[methodDependency] }
      .map { clazz -> dependencies[clazz] }
      .toTypedArray()

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

  private fun cleanUp() {
    methodToClassMap.clear()
    classToMethodMap.clear()
    methodDependencies.clear()
    methodTree.clear()
  }
}