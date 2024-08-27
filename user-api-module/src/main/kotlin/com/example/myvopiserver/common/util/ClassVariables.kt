package com.example.myvopiserver.common.util

import jakarta.annotation.PostConstruct
import jakarta.persistence.Column
import jakarta.persistence.JoinColumn
import org.springframework.stereotype.Component
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

// unused
@Component
class ClassVariables {

    private val moduleName = "user-api-module"
    private val projectName = "myvopiserver"
    private val rootPackageName = "com.example.myvopiserver"
    private val entityDirectoryName = "domain"

    @PostConstruct
    fun initBuild() {
        val path = File("").absolutePath.plus("\\${moduleName}\\src\\main\\kotlin\\com\\example\\${projectName}\\${entityDirectoryName}")
        val className = File(path).listFiles { file -> file.isFile && file.name.endsWith(".kt") && !file.name.contains("BaseTime") }
            ?.map { file -> file.nameWithoutExtension }
            ?: emptyList()
        className.forEach { name ->
            val clazz = Class.forName("${rootPackageName}.${entityDirectoryName}.${name}").kotlin
            build(clazz)
        }
    }

    companion object {

        val NamesByClass = mutableMapOf<KClass<*>, List<String>>()

        fun <T : Any> build(clazz: KClass<T>) {
            val properties = clazz.memberProperties
            val columns = properties.mapNotNull { it.javaField?.annotations }
                .flatMap { it.toList() }
                .filterIsInstance<Column>()
                .map { it.name }
            val joinColumns = properties.mapNotNull { it.javaField?.annotations }
                .flatMap { it.toList() }
                .filterIsInstance<JoinColumn>()
                .map { it.name }
            val allColumnNames = columns + joinColumns
            NamesByClass[clazz] = allColumnNames
        }
    }
}