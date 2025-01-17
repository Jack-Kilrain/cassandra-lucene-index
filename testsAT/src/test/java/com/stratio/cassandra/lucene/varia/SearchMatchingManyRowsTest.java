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
package com.stratio.cassandra.lucene.varia;

import static com.stratio.cassandra.lucene.builder.Builder.all;
import static com.stratio.cassandra.lucene.builder.Builder.field;

import java.util.ArrayList;
import java.util.List;

import com.stratio.cassandra.lucene.BaseTest;
import com.stratio.cassandra.lucene.util.CassandraUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Test the retrieval of large volumes of rows, specially above 65535 rows.
 *
 * @author Andres de la Pena <adelapena@stratio.com>
 */

public class SearchMatchingManyRowsTest extends BaseTest {

    private static CassandraUtils utils;

    @BeforeAll
    public static void before() {
        utils = CassandraUtils.builder("search_matching_many_rows")
            .withPartitionKey("pk")
            .withClusteringKey("ck")
            .withColumn("pk", "int")
            .withColumn("ck", "int")
            .withColumn("rc", "int")
            .build()
            .createKeyspace()
            .createTable()
            .createIndex();

        int numPartitions = 666;
        int partitionSize = 100;
        String[] names = new String[]{"pk", "ck", "rc"};
        for (Integer pk = 0; pk < numPartitions; pk++) {
            List<Object[]> values = new ArrayList<>();
            for (Integer ck = 0; ck <= partitionSize; ck++) {
                values.add(new Object[]{pk, ck, pk * partitionSize + ck});
            }
            utils.insert(names, values);
        }
        utils.refresh();
    }

    @AfterAll
    public static void after() {
        CassandraUtils.dropKeyspaceIfNotNull(utils);
    }

    @Test
    public void testQuery() {
        utils.query(all()).fetchSize(500).limit(65536).check(65536);
    }

    @Test
    public void testFilter() {
        utils.filter(all()).fetchSize(500).limit(65536).check(65536);
    }

    @Test
    public void testSort() {
        utils.sort(field("rc")).fetchSize(500).limit(65536).check(65536);
    }
}
