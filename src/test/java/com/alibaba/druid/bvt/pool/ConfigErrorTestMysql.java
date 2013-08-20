/*
 * Copyright 1999-2011 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.druid.bvt.pool;

import java.lang.reflect.Field;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.logging.Log;

public class ConfigErrorTestMysql extends TestCase {

    private DruidDataSource dataSource;

    protected void setUp() throws Exception {
        dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql:xxx");
        dataSource.setTestOnBorrow(false);
        dataSource.setInitialSize(0);
    }

    protected void tearDown() throws Exception {
        dataSource.close();
    }

    public void test_success() throws Exception {
        dataSource.setTestWhileIdle(true);

        Field field = DruidDataSource.class.getDeclaredField("LOG");
        dataSource.setValidationQuery("select 1");
        field.setAccessible(true);
        Log LOG = (Log) field.get(null);

        LOG.resetStat();

        Assert.assertEquals(0, LOG.getWarnCount());
        dataSource.init();
        Assert.assertEquals(0, LOG.getWarnCount());
    }

    public void test_warn() throws Exception {
        dataSource.setTestWhileIdle(false);

        Field field = DruidDataSource.class.getDeclaredField("LOG");
        field.setAccessible(true);
        Log LOG = (Log) field.get(null);

        LOG.resetStat();

        Assert.assertEquals(0, LOG.getWarnCount());
        dataSource.init();
        Assert.assertEquals(0, LOG.getWarnCount());
    }
}
