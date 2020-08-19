/*
 * Copyright 2020 Square Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.cash.tempest.internal

import app.cash.tempest.Ignore
import java.lang.reflect.Method
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaGetter
import kotlin.reflect.jvm.javaMethod

internal val KClass<*>.primaryConstructorParameters: Map<String, KParameter>
  get() = primaryConstructor?.parameters?.associateBy { requireNotNull(it.name) } ?: emptyMap()

internal data class ClassMember(
  val returnType: KType,
  val javaMethod: Method
)

internal val KClass<*>.declaredMembers: List<ClassMember>
  get() {
    return declaredMemberProperties.map { ClassMember(it.returnType, it.javaGetter!!) } +
        declaredMemberFunctions.map { ClassMember(it.returnType, it.javaMethod!!) }
  }

internal val KProperty<*>.shouldIgnore: Boolean
  get() = findAnnotation<Ignore>() != null || findAnnotation<Transient>() != null