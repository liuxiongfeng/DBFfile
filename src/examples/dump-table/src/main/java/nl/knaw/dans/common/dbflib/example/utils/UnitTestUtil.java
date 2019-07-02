/**
 * Copyright (C) 2009-2016 DANS - Data Archiving and  Networked Services (info@dans.knaw.nl)
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
package nl.knaw.dans.common.dbflib.example.utils;


import nl.knaw.dans.common.dbflib.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.*;

/**
 * Utility functions for testing databases.
 *
 * @author Jan van Mansum
 */
public class UnitTestUtil
{
    private UnitTestUtil()
    {
        // Disallow instantiation.
    }

    /**
     * Takes a record iterator and a field name and creates a list of the records sorted by the
     * field specified by the field name. The type of this field must implement
     * <tt>java.lang.Comparable</tt>.
     *
     * @param aRecordIterator a record iterator
     * @param aFieldName a field name
     *
     * @return a sorted list of records
     */
    public static List<Record> createSortedRecordList(final Iterator<Record> aRecordIterator, final String aFieldName)
    {
        final List<Record> recordList = new ArrayList<Record>();

        while (aRecordIterator.hasNext())
        {
            recordList.add(aRecordIterator.next());
        }

        Collections.sort(recordList,
                         new Comparator<Record>()
            {
                @SuppressWarnings("unchecked")
                public int compare(Record r1, Record r2)
                {
                    Comparable c1 = (Comparable) r1.getTypedValue(aFieldName);
                    Comparable c2 = (Comparable) r2.getTypedValue(aFieldName);

                    if (c1 == null)
                    {
                        if (c2 == null)
                        {
                            return 0;
                        }

                        return -1;
                    }

                    if (c2 == null)
                    {
                        return 1;
                    }

                    return c1.compareTo(c2);
                }
            });

        return recordList;
    }

    /**
     * Performs a test in which a DBF is copied by reading it and writing it using the Table class,
     * after which a byte by byte comparision of the . DBF files and .DBT files (.FPT files in
     * FoxPro) is done. Ranges of bytes to be ignored when comparing the files can be provided.
     * @throws IOException should not happen
     * @throws CorruptedTableException should not happen
     */
    public static String doCopy(String pathDemoFile, String outputDir,String createTableName, Version aVersion)
            throws IOException, DbfLibException {

        File orgFile = new File(pathDemoFile);

        if (!orgFile.exists()) {
            orgFile = new File(pathDemoFile);
        }

        File copyFile = new File(outputDir, createTableName + ".dbf");
        Table copyTable = null;
        Table orgTable = null;

        try {
            orgTable = new Table(orgFile);
            orgTable.open();

            List<Field> fields = orgTable.getFields();
            copyTable = new Table(copyFile, aVersion, fields);
            copyTable.open(IfNonExistent.CREATE);

            //循环复制源表
            /*Iterator<Record> recordIterator = orgTable.recordIterator();
            while (recordIterator.hasNext()) {
                copyTable.addRecord(recordIterator.next());
            }*/
        } finally {
            if (orgTable != null) {
                orgTable.close();
            }
            if (true) {
                copyTable.close();
            }

        }
        return outputDir + createTableName + ".dbf";
    }

    static void copyFile(File in, File outDir, String outFileName)
                  throws IOException
    {
        FileChannel inChannel = new FileInputStream(in).getChannel();
        outDir.mkdirs();

        File outFile = new File(outDir, outFileName);
        FileChannel outChannel = new FileOutputStream(outFile).getChannel();

        try
        {
            inChannel.transferTo(0,
                                 inChannel.size(),
                                 outChannel);
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            if (inChannel != null)
            {
                inChannel.close();
            }

            if (outChannel != null)
            {
                outChannel.close();
            }
        }
    }
}
