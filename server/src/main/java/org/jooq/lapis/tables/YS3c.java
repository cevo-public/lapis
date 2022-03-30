/*
 * This file is generated by jOOQ.
 */
package org.jooq.lapis.tables;


import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row5;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;
import org.jooq.lapis.DefaultSchema;
import org.jooq.lapis.Keys;
import org.jooq.lapis.tables.records.YS3cRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class YS3c extends TableImpl<YS3cRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>y_s3c</code>
     */
    public static final YS3c Y_S3C = new YS3c();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<YS3cRecord> getRecordType() {
        return YS3cRecord.class;
    }

    /**
     * The column <code>y_s3c.gisaid_epi_isl</code>.
     */
    public final TableField<YS3cRecord, String> GISAID_EPI_ISL = createField(DSL.name("gisaid_epi_isl"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>y_s3c.sra_accession</code>.
     */
    public final TableField<YS3cRecord, String> SRA_ACCESSION = createField(DSL.name("sra_accession"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>y_s3c.hospitalized</code>.
     */
    public final TableField<YS3cRecord, Boolean> HOSPITALIZED = createField(DSL.name("hospitalized"), SQLDataType.BOOLEAN, this, "");

    /**
     * The column <code>y_s3c.died</code>.
     */
    public final TableField<YS3cRecord, Boolean> DIED = createField(DSL.name("died"), SQLDataType.BOOLEAN, this, "");

    /**
     * The column <code>y_s3c.fully_vaccinated</code>.
     */
    public final TableField<YS3cRecord, Boolean> FULLY_VACCINATED = createField(DSL.name("fully_vaccinated"), SQLDataType.BOOLEAN, this, "");

    private YS3c(Name alias, Table<YS3cRecord> aliased) {
        this(alias, aliased, null);
    }

    private YS3c(Name alias, Table<YS3cRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>y_s3c</code> table reference
     */
    public YS3c(String alias) {
        this(DSL.name(alias), Y_S3C);
    }

    /**
     * Create an aliased <code>y_s3c</code> table reference
     */
    public YS3c(Name alias) {
        this(alias, Y_S3C);
    }

    /**
     * Create a <code>y_s3c</code> table reference
     */
    public YS3c() {
        this(DSL.name("y_s3c"), null);
    }

    public <O extends Record> YS3c(Table<O> child, ForeignKey<O, YS3cRecord> key) {
        super(child, key, Y_S3C);
    }

    @Override
    public Schema getSchema() {
        return DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public List<UniqueKey<YS3cRecord>> getKeys() {
        return Arrays.<UniqueKey<YS3cRecord>>asList(Keys.Y_S3C_GISAID_EPI_ISL_KEY, Keys.Y_S3C_SRA_ACCESSION_KEY);
    }

    @Override
    public YS3c as(String alias) {
        return new YS3c(DSL.name(alias), this);
    }

    @Override
    public YS3c as(Name alias) {
        return new YS3c(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public YS3c rename(String name) {
        return new YS3c(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public YS3c rename(Name name) {
        return new YS3c(name, null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<String, String, Boolean, Boolean, Boolean> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}
