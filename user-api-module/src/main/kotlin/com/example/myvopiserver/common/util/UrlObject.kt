package com.example.myvopiserver.common.util

import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.*

@Component
class UrlObject {

    private val moduleNameDir = "\\user-api-module"
    private val sourceDirName = "\\src\\main\\kotlin"
    private val projectName = "\\com\\example\\myvopiserver"
    private val rootPackageName = "com.example.myvopiserver"
    private val controllerDirName = "\\interfaces"

    @PostConstruct
    fun initBuild() {
        val classPath = File("").absolutePath.plus("${moduleNameDir}${sourceDirName}${projectName}${controllerDirName}")
        val className = File(classPath).listFiles { file -> file.isFile && file.name.endsWith(".kt") && file.name.contains("ApiController") }
            ?.map { file -> file.nameWithoutExtension }
            ?: emptyList()
        className.forEach { name ->
            val clazz = Class.forName("$rootPackageName.interfaces.$name").kotlin
            urlBuild(clazz)
        }
    }

    companion object {
        val RequestMatchersMap = mutableMapOf<String, List<String>>()

        fun <T : Any> urlBuild(clazz: KClass<T>) {
            val declaredMembers = clazz.declaredMembers
            val postMapping = declaredMembers.flatMap { it.findAnnotation<PostMapping>()?.path?.map { path -> path to "POST" } ?: emptyList() }
            val getMapping = declaredMembers.flatMap { it.findAnnotation<GetMapping>()?.path?.map { path -> path to "GET" } ?: emptyList() }
            val putMapping = declaredMembers.flatMap { it.findAnnotation<PutMapping>()?.path?.map { path -> path to "PUT" } ?: emptyList() }
            val deleteMapping = declaredMembers.flatMap { it.findAnnotation<DeleteMapping>()?.path?.map { path -> path to "DELETE" } ?: emptyList() }
            val resultMap = mutableMapOf<String, MutableList<String>>()
            postMapping.forEach { (path, method) -> resultMap.computeIfAbsent(path) { mutableListOf() }.add(method) }
            getMapping.forEach { (path, method) -> resultMap.computeIfAbsent(path) { mutableListOf() }.add(method) }
            putMapping.forEach { (path, method) -> resultMap.computeIfAbsent(path) { mutableListOf() }.add(method) }
            deleteMapping.forEach { (path, method) -> resultMap.computeIfAbsent(path) { mutableListOf() }.add(method) }
            resultMap.forEach { (key, value) ->
                RequestMatchersMap[key] = value.toList()
            }
        }
    }
}