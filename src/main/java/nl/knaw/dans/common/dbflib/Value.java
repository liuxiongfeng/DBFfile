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
package nl.knaw.dans.common.dbflib;


/**
 * Represents a value that can be stored in a record. <code>Value</code>s can be created by
 * instantiating sub-classes of this type. These subclasses are by convention called according to
 * the Java type they store, e.g., a <code>BooleanValue</code> stores a Java <code>Boolean</code>.
 * <p>
 * This interface has methods to retrieve the wrapped value in two formats: as an object of the Java
 * type that the concrete subclass refers to (e.g., <code>Boolean.TRUE</code>) or as the raw bytes
 * that are stored in the DBF file (e.g., <code>{(byte)'Y'}</code>). The raw value may depend on the
 * exact specifications (length, decimal count) of the field that is read from or written to. That
 * is why a {@link Field} instance must be specified when retrieving it.
 *
 * @author Jan van Mansum
 */
public abstract class Value
{
    private final byte[] originalRaw;
    private final Field originalField;
    protected Object typed;

    /**
     * Constructs a <tt>Value</tt> object with the specified raw value. The subclass must take care
     * of converting the raw value to a Java object by implementing {@link #doGetTypedValue(byte[]) }
     * .
     *
     * @param rawValue the bytes that constitute the raw value
     */
    protected Value(final Field originalField, final byte[] rawValue)
    {
        this.originalField = originalField;
        this.originalRaw = rawValue;
    }

    /**
     * Constructs a <tt>Value</tt> object with the specified typed value, i.e. Java object. The
     * subclass must take care of converting the typed value to a byte array by implementing
     * {@link #doGetRawValue(nl.knaw.dans.common.dbflib.Field) }.
     *
     * @param aTypedValue the value as a Java object
     */
    protected Value(final Object aTypedValue)
    {
        typed = aTypedValue;
        originalRaw = null;
        originalField = null;
    }

    /**
     * Returns the value as a Java object of the appropriate type. For instance a numeric value is
     * returned as a {@link Number} object. Each subclass of <code>Value</code> is linked to a
     * specific Java class, e.g. {@link NumberValue} is linked to {@link Number},
     * {@link BooleanValue} to {@link Boolean}, etc.
     *
     * @return the value as a Java object
     */
    final Object getTypedValue()
    {
        if (typed == null)
        {
            typed = doGetTypedValue(originalRaw);
        }

        return typed;
    }

    /**
     * Returns the value as a byte array. The byte array contains a representation of the value as
     * stored in the DBF file. This representation also depends on the specifications of the field
     * (i.e. type, length and decimal count) the value is to be stored in.
     *
     * If this value was read from the database and the field specifications permit it, the original
     * byte array is returned.
     *
     * @param aField the field for which to return the raw representation of this value
     * @return a byte array representation of this value
     * @throws DbfLibException if the value cannot be stored in the specified field
     */
    final byte[] getRawValue(final Field aField)
                      throws DbfLibException
    {
        if (originalRaw != null && originalField.equals(aField))
        {
            return originalRaw;
        }

        aField.validateTypedValue(typed);

        return doGetRawValue(aField);
    }

    /**
     * Converts the raw bytes to a Java object. The class of Java object to create is determined by
     * the subclass of <tt>Value</tt>.
     *
     * @return the value as a Java object
     */
    protected abstract Object doGetTypedValue(byte[] rawValue);

    /**
     * Converts the typed value to a byte array, according to the field specifications provided.
     *
     * @param aField the field specifications
     * @return a byte array containing the raw value
     * @throws nl.knaw.dans.common.dbflib.ValueTooLargeException if the value is too large for the
     *             field
     */
    protected abstract byte[] doGetRawValue(Field aField)
                                     throws ValueTooLargeException;
}
