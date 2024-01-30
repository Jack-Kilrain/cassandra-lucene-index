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
package com.stratio.cassandra.lucene.story;

import static com.stratio.cassandra.lucene.builder.Builder.wildcard;
import static com.stratio.cassandra.lucene.story.DataHelper.data1;
import static com.stratio.cassandra.lucene.story.DataHelper.data10;
import static com.stratio.cassandra.lucene.story.DataHelper.data11;
import static com.stratio.cassandra.lucene.story.DataHelper.data12;
import static com.stratio.cassandra.lucene.story.DataHelper.data13;
import static com.stratio.cassandra.lucene.story.DataHelper.data14;
import static com.stratio.cassandra.lucene.story.DataHelper.data15;
import static com.stratio.cassandra.lucene.story.DataHelper.data16;
import static com.stratio.cassandra.lucene.story.DataHelper.data17;
import static com.stratio.cassandra.lucene.story.DataHelper.data18;
import static com.stratio.cassandra.lucene.story.DataHelper.data19;
import static com.stratio.cassandra.lucene.story.DataHelper.data2;
import static com.stratio.cassandra.lucene.story.DataHelper.data20;
import static com.stratio.cassandra.lucene.story.DataHelper.data3;
import static com.stratio.cassandra.lucene.story.DataHelper.data4;
import static com.stratio.cassandra.lucene.story.DataHelper.data5;
import static com.stratio.cassandra.lucene.story.DataHelper.data6;
import static com.stratio.cassandra.lucene.story.DataHelper.data7;
import static com.stratio.cassandra.lucene.story.DataHelper.data8;
import static com.stratio.cassandra.lucene.story.DataHelper.data9;

import com.stratio.cassandra.lucene.BaseTest;
import com.stratio.cassandra.lucene.util.CassandraUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ComplexKeyIndexHandlingTest extends BaseTest {

    private CassandraUtils utils;

    @BeforeEach
    public void before() {
        utils = CassandraUtils.builder("complex_key_index_handling")
            .withPartitionKey("integer_1", "ascii_1")
            .withClusteringKey("double_1")
            .withColumn("ascii_1", "ascii")
            .withColumn("bigint_1", "bigint")
            .withColumn("blob_1", "blob")
            .withColumn("boolean_1", "boolean")
            .withColumn("decimal_1", "decimal")
            .withColumn("date_1", "timestamp")
            .withColumn("double_1", "double")
            .withColumn("float_1", "float")
            .withColumn("integer_1", "int")
            .withColumn("inet_1", "inet")
            .withColumn("text_1", "text")
            .withColumn("varchar_1", "varchar")
            .withColumn("uuid_1", "uuid")
            .withColumn("timeuuid_1", "timeuuid")
            .withColumn("list_1", "list<text>")
            .withColumn("set_1", "set<text>")
            .withColumn("map_1", "map<text,text>")
            .build()
            .createKeyspace()
            .createTable();
    }

    @AfterEach
    public void after() {
        CassandraUtils.dropKeyspaceIfNotNull(utils);
    }

    @Test
    public void testCreateIndexAfterInsertions() {
        utils.insert(data1,
                     data2,
                     data3,
                     data4,
                     data5,
                     data6,
                     data7,
                     data8,
                     data9,
                     data10,
                     data11,
                     data12,
                     data13,
                     data14,
                     data15,
                     data16,
                     data17,
                     data18,
                     data19,
                     data20)
            .createIndex()
            .refresh()
            .filter(wildcard("ascii_1", "*"))
            .check(20);
    }

    @Test
    public void testCreateIndexDuringInsertions1() {
        utils.insert(data1,
                     data2,
                     data3,
                     data4,
                     data5,
                     data6,
                     data7,
                     data8,
                     data9,
                     data10,
                     data11,
                     data12,
                     data13,
                     data14,
                     data15)
            .createIndex()
            .refresh()
            .insert(data16, data17, data18, data19, data20)
            .refresh()
            .filter(wildcard("ascii_1", "*"))
            .check(20);
    }

    @Test
    public void testCreateIndexDuringInsertions2() {
        utils.insert(data1,
                     data2,
                     data3,
                     data4,
                     data6,
                     data7,
                     data8,
                     data9,
                     data11,
                     data12,
                     data13,
                     data14,
                     data16,
                     data17,
                     data18,
                     data19)
            .createIndex()
            .refresh()
            .insert(data5, data10, data15, data20)
            .refresh()
            .filter(wildcard("ascii_1", "*"))
            .check(20);
    }

    @Test
    public void testCreateIndexDuringInsertions3() {
        utils.insert(data2,
                     data3,
                     data4,
                     data5,
                     data6,
                     data8,
                     data9,
                     data10,
                     data11,
                     data12,
                     data14,
                     data15,
                     data16,
                     data17,
                     data18,
                     data20)
            .createIndex()
            .refresh()
            .insert(data1, data7, data13, data19)
            .refresh()
            .filter(wildcard("ascii_1", "*"))
            .check(20);
    }

    @Test
    public void testRecreateIndexAfterInsertions() {
        utils.createIndex()
            .refresh()
            .insert(data1,
                    data2,
                    data3,
                    data4,
                    data5,
                    data6,
                    data7,
                    data8,
                    data9,
                    data10,
                    data11,
                    data12,
                    data13,
                    data14,
                    data15,
                    data16,
                    data17,
                    data18,
                    data19,
                    data20)
            .refresh()
            .dropIndex()
            .createIndex()
            .refresh()
            .filter(wildcard("ascii_1", "*"))
            .check(20);
    }
}