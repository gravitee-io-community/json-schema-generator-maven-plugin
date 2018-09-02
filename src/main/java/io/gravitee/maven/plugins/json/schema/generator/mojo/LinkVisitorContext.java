/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.maven.plugins.json.schema.generator.mojo;

import com.fasterxml.jackson.databind.JavaType;

/**
 * Transform recursive object references into links.
 *
 * @author <a href="mailto:karsten@simless.com">Karsten Ohme
 *         (karsten@simless.com)</a>
 */
public class LinkVisitorContext extends VisitorContext {

    @Override
    public String addSeenSchemaUri(JavaType aSeenSchema) {
        return getSeenSchemaUri(aSeenSchema);
    }

    @Override
    public String getSeenSchemaUri(JavaType aSeenSchema) {
        return isModel(aSeenSchema) ? javaTypeToUrn(aSeenSchema) : null;
    }

    protected boolean isModel(JavaType type) {
        return type.getRawClass() != String.class
                && !isBoxedPrimitive(type)
                && !type.isPrimitive()
                && !type.isEnumType()
                && !type.isMapLikeType()
                && !type.isCollectionLikeType();
    }

    protected static boolean isBoxedPrimitive(JavaType type) {
        return type.getRawClass() == Boolean.class
                || type.getRawClass() == Byte.class
                || type.getRawClass() == Long.class
                || type.getRawClass() == Integer.class
                || type.getRawClass() == Short.class
                || type.getRawClass() == Float.class
                || type.getRawClass() == Double.class;
    }

}
