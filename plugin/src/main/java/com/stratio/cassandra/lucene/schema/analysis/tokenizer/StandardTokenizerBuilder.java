/*
 * Copyright (C) 2014 Stratio (http://stratio.com)
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
package com.stratio.cassandra.lucene.schema.analysis.tokenizer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.lucene.analysis.standard.StandardTokenizer;

/**
 * A {@link TokenizerBuilder} for building {@link org.apache.lucene.analysis.standard.StandardTokenizer}
 *
 * @author Eduardo Alonso {@literal <eduardoalonso@stratio.com>}
 */
public class StandardTokenizerBuilder extends TokenizerBuilder<StandardTokenizer> {

    static final Integer DEFAULT_MAX_TOKEN_LENGTH = 255;

    /** If a token length is bigger that this, token is split at max token length intervals. */
    @JsonProperty("max_token_length")
    final Integer maxTokenLength;

    /**
     * Builds a new {@link StandardTokenizerBuilder} using the specified bufferSize, delimiter, replacement and skip.
     *
     * @param maxTokenLength if a token length is bigger that this, token is split at max token length intervals.
     */
    @JsonCreator
    public StandardTokenizerBuilder(@JsonProperty("max_token_length") Integer maxTokenLength) {
        this.maxTokenLength = getOrDefault(maxTokenLength, DEFAULT_MAX_TOKEN_LENGTH);
    }

    /** {@inheritDoc} */
    @Override
    public StandardTokenizer buildTokenizer() {
        StandardTokenizer tokenizer = new StandardTokenizer();
        tokenizer.setMaxTokenLength(maxTokenLength);
        return tokenizer;
    }
}