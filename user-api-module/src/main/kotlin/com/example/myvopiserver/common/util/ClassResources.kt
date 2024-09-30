package com.example.myvopiserver.common.util

import com.example.myvopiserver.common.util.extension.getLogger
import jakarta.annotation.PostConstruct
import jakarta.persistence.Column
import jakarta.persistence.JoinColumn
import jakarta.persistence.Table
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

@Component
class ClassResources(
    @Value("\${SOURCE_DIR}")
    private val sourceDirName: String,
    @Value("\${OS_SET}")
    private val os: String,
    @Value("\${ACTIVE_PROFILE}")
    private val activeProfile: String,
) {
    private val entityDirName = "/domain"
    private val controllerDirName = "/interfaces"
    private val rootPackageName = "com.example.myvopiserver"

    private val logger by getLogger()

    @PostConstruct
    fun initBuildClasses() {
        val classPath = File("").absolutePath.plus(
            getSourceDirDestinationByOs().plus(entityDirName)
        )
        val className = File(classPath).listFiles { file -> file.isFile && file.name.endsWith(".kt") && !file.name.contains("BaseTime") }
            ?.map { file -> file.nameWithoutExtension }
            ?: emptyList()
        className.forEach { name ->
            val clazz = Class.forName("$rootPackageName.domain.$name").kotlin
            classBuild(clazz)
        }
    }

    @PostConstruct
    fun initBuildUrls() {
        val classPath = File("").absolutePath.plus(
            getSourceDirDestinationByOs().plus(controllerDirName)
        )
        val className = File(classPath).listFiles { file -> file.isFile && file.name.endsWith(".kt") && file.name.contains("ApiController") }
            ?.map { file -> file.nameWithoutExtension }
            ?: emptyList()
        className.forEach { name ->
            val clazz = Class.forName("$rootPackageName.interfaces.$name").kotlin
            urlBuild(clazz)
        }
    }

    private fun getSourceDirDestinationByOs(): String {
        logger.info("Current OS = $os")
        return if(os == "win" && activeProfile == "dev") {
            "/user-api-module$sourceDirName"
        } else {
            sourceDirName
        }
    }

    companion object {
        val ColumnNamesByClass = mutableMapOf<KClass<*>, List<String>>()
        val TablesNamesByClass = mutableMapOf<KClass<*>, String?>()
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

        fun <T : Any> classBuild(clazz: KClass<T>) {
            val columnProperties = clazz.memberProperties
            val columns = columnProperties.mapNotNull { it.javaField?.annotations }
                .flatMap { it.toList() }
                .filterIsInstance<Column>()
                .map { it.name }
            val joinColumns = columnProperties.mapNotNull { it.javaField?.annotations }
                .flatMap { it.toList() }
                .filterIsInstance<JoinColumn>()
                .map { it.name }
            val allColumnNames = columns + joinColumns
            ColumnNamesByClass[clazz] = allColumnNames

            val tableAnnotation = clazz.annotations
                .filterIsInstance<Table>()
                .map { it.name }
                .firstOrNull()
            TablesNamesByClass[clazz] = tableAnnotation
        }
    }
}