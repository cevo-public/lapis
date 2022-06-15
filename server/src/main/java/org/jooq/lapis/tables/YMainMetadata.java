/*
 * This file is generated by jOOQ.
 */
package org.jooq.lapis.tables;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
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
import org.jooq.lapis.tables.records.YMainMetadataRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class YMainMetadata extends TableImpl<YMainMetadataRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>y_main_metadata</code>
     */
    public static final YMainMetadata Y_MAIN_METADATA = new YMainMetadata();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<YMainMetadataRecord> getRecordType() {
        return YMainMetadataRecord.class;
    }

    /**
     * The column <code>y_main_metadata.id</code>.
     */
    public final TableField<YMainMetadataRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>y_main_metadata.source</code>.
     */
    public final TableField<YMainMetadataRecord, String> SOURCE = createField(DSL.name("source"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>y_main_metadata.source_primary_key</code>.
     */
    public final TableField<YMainMetadataRecord, String> SOURCE_PRIMARY_KEY = createField(DSL.name("source_primary_key"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>y_main_metadata.accession</code>.
     */
    public final TableField<YMainMetadataRecord, String> ACCESSION = createField(DSL.name("accession"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>y_main_metadata.strain</code>.
     */
    public final TableField<YMainMetadataRecord, String> STRAIN = createField(DSL.name("strain"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>y_main_metadata.sra_accession</code>.
     */
    public final TableField<YMainMetadataRecord, String> SRA_ACCESSION = createField(DSL.name("sra_accession"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>y_main_metadata.date</code>.
     */
    public final TableField<YMainMetadataRecord, LocalDate> DATE = createField(DSL.name("date"), SQLDataType.LOCALDATE, this, "");

    /**
     * The column <code>y_main_metadata.year</code>.
     */
    public final TableField<YMainMetadataRecord, Integer> YEAR = createField(DSL.name("year"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>y_main_metadata.month</code>.
     */
    public final TableField<YMainMetadataRecord, Integer> MONTH = createField(DSL.name("month"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>y_main_metadata.day</code>.
     */
    public final TableField<YMainMetadataRecord, Integer> DAY = createField(DSL.name("day"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>y_main_metadata.date_submitted</code>.
     */
    public final TableField<YMainMetadataRecord, LocalDate> DATE_SUBMITTED = createField(DSL.name("date_submitted"), SQLDataType.LOCALDATE, this, "");

    /**
     * The column <code>y_main_metadata.region</code>.
     */
    public final TableField<YMainMetadataRecord, String> REGION = createField(DSL.name("region"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>y_main_metadata.country</code>.
     */
    public final TableField<YMainMetadataRecord, String> COUNTRY = createField(DSL.name("country"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>y_main_metadata.division</code>.
     */
    public final TableField<YMainMetadataRecord, String> DIVISION = createField(DSL.name("division"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>y_main_metadata.location</code>.
     */
    public final TableField<YMainMetadataRecord, String> LOCATION = createField(DSL.name("location"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>y_main_metadata.region_exposure</code>.
     */
    public final TableField<YMainMetadataRecord, String> REGION_EXPOSURE = createField(DSL.name("region_exposure"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>y_main_metadata.country_exposure</code>.
     */
    public final TableField<YMainMetadataRecord, String> COUNTRY_EXPOSURE = createField(DSL.name("country_exposure"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>y_main_metadata.division_exposure</code>.
     */
    public final TableField<YMainMetadataRecord, String> DIVISION_EXPOSURE = createField(DSL.name("division_exposure"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>y_main_metadata.host</code>.
     */
    public final TableField<YMainMetadataRecord, String> HOST = createField(DSL.name("host"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>y_main_metadata.age</code>.
     */
    public final TableField<YMainMetadataRecord, Integer> AGE = createField(DSL.name("age"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>y_main_metadata.sex</code>.
     */
    public final TableField<YMainMetadataRecord, String> SEX = createField(DSL.name("sex"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>y_main_metadata.hospitalized</code>.
     */
    public final TableField<YMainMetadataRecord, Boolean> HOSPITALIZED = createField(DSL.name("hospitalized"), SQLDataType.BOOLEAN, this, "");

    /**
     * The column <code>y_main_metadata.died</code>.
     */
    public final TableField<YMainMetadataRecord, Boolean> DIED = createField(DSL.name("died"), SQLDataType.BOOLEAN, this, "");

    /**
     * The column <code>y_main_metadata.fully_vaccinated</code>.
     */
    public final TableField<YMainMetadataRecord, Boolean> FULLY_VACCINATED = createField(DSL.name("fully_vaccinated"), SQLDataType.BOOLEAN, this, "");

    /**
     * The column <code>y_main_metadata.sampling_strategy</code>.
     */
    public final TableField<YMainMetadataRecord, String> SAMPLING_STRATEGY = createField(DSL.name("sampling_strategy"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>y_main_metadata.clade</code>.
     */
    public final TableField<YMainMetadataRecord, String> CLADE = createField(DSL.name("clade"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>y_main_metadata.authors</code>.
     */
    public final TableField<YMainMetadataRecord, String> AUTHORS = createField(DSL.name("authors"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>y_main_metadata.institution</code>.
     */
    public final TableField<YMainMetadataRecord, String> INSTITUTION = createField(DSL.name("institution"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>y_main_metadata.nextclade_qc_overall_score</code>.
     */
    public final TableField<YMainMetadataRecord, Double> NEXTCLADE_QC_OVERALL_SCORE = createField(DSL.name("nextclade_qc_overall_score"), SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>y_main_metadata.nextclade_qc_missing_data_score</code>.
     */
    public final TableField<YMainMetadataRecord, Double> NEXTCLADE_QC_MISSING_DATA_SCORE = createField(DSL.name("nextclade_qc_missing_data_score"), SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>y_main_metadata.nextclade_qc_mixed_sites_score</code>.
     */
    public final TableField<YMainMetadataRecord, Double> NEXTCLADE_QC_MIXED_SITES_SCORE = createField(DSL.name("nextclade_qc_mixed_sites_score"), SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>y_main_metadata.nextclade_qc_private_mutations_score</code>.
     */
    public final TableField<YMainMetadataRecord, Double> NEXTCLADE_QC_PRIVATE_MUTATIONS_SCORE = createField(DSL.name("nextclade_qc_private_mutations_score"), SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>y_main_metadata.nextclade_qc_snp_clusters_score</code>.
     */
    public final TableField<YMainMetadataRecord, Double> NEXTCLADE_QC_SNP_CLUSTERS_SCORE = createField(DSL.name("nextclade_qc_snp_clusters_score"), SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>y_main_metadata.nextclade_qc_frame_shifts_score</code>.
     */
    public final TableField<YMainMetadataRecord, Double> NEXTCLADE_QC_FRAME_SHIFTS_SCORE = createField(DSL.name("nextclade_qc_frame_shifts_score"), SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>y_main_metadata.nextclade_qc_stop_codons_score</code>.
     */
    public final TableField<YMainMetadataRecord, Double> NEXTCLADE_QC_STOP_CODONS_SCORE = createField(DSL.name("nextclade_qc_stop_codons_score"), SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>y_main_metadata.nextclade_alignment_score</code>.
     */
    public final TableField<YMainMetadataRecord, Double> NEXTCLADE_ALIGNMENT_SCORE = createField(DSL.name("nextclade_alignment_score"), SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>y_main_metadata.nextclade_alignment_start</code>.
     */
    public final TableField<YMainMetadataRecord, Integer> NEXTCLADE_ALIGNMENT_START = createField(DSL.name("nextclade_alignment_start"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>y_main_metadata.nextclade_alignment_end</code>.
     */
    public final TableField<YMainMetadataRecord, Integer> NEXTCLADE_ALIGNMENT_END = createField(DSL.name("nextclade_alignment_end"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>y_main_metadata.nextclade_total_substitutions</code>.
     */
    public final TableField<YMainMetadataRecord, Integer> NEXTCLADE_TOTAL_SUBSTITUTIONS = createField(DSL.name("nextclade_total_substitutions"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>y_main_metadata.nextclade_total_deletions</code>.
     */
    public final TableField<YMainMetadataRecord, Integer> NEXTCLADE_TOTAL_DELETIONS = createField(DSL.name("nextclade_total_deletions"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>y_main_metadata.nextclade_total_insertions</code>.
     */
    public final TableField<YMainMetadataRecord, Integer> NEXTCLADE_TOTAL_INSERTIONS = createField(DSL.name("nextclade_total_insertions"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>y_main_metadata.nextclade_total_frame_shifts</code>.
     */
    public final TableField<YMainMetadataRecord, Integer> NEXTCLADE_TOTAL_FRAME_SHIFTS = createField(DSL.name("nextclade_total_frame_shifts"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>y_main_metadata.nextclade_total_aminoacid_substitutions</code>.
     */
    public final TableField<YMainMetadataRecord, Integer> NEXTCLADE_TOTAL_AMINOACID_SUBSTITUTIONS = createField(DSL.name("nextclade_total_aminoacid_substitutions"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>y_main_metadata.nextclade_total_aminoacid_deletions</code>.
     */
    public final TableField<YMainMetadataRecord, Integer> NEXTCLADE_TOTAL_AMINOACID_DELETIONS = createField(DSL.name("nextclade_total_aminoacid_deletions"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>y_main_metadata.nextclade_total_aminoacid_insertions</code>.
     */
    public final TableField<YMainMetadataRecord, Integer> NEXTCLADE_TOTAL_AMINOACID_INSERTIONS = createField(DSL.name("nextclade_total_aminoacid_insertions"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>y_main_metadata.nextclade_total_missing</code>.
     */
    public final TableField<YMainMetadataRecord, Integer> NEXTCLADE_TOTAL_MISSING = createField(DSL.name("nextclade_total_missing"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>y_main_metadata.nextclade_total_non_acgtns</code>.
     */
    public final TableField<YMainMetadataRecord, Integer> NEXTCLADE_TOTAL_NON_ACGTNS = createField(DSL.name("nextclade_total_non_acgtns"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>y_main_metadata.nextclade_total_pcr_primer_changes</code>.
     */
    public final TableField<YMainMetadataRecord, Integer> NEXTCLADE_TOTAL_PCR_PRIMER_CHANGES = createField(DSL.name("nextclade_total_pcr_primer_changes"), SQLDataType.INTEGER, this, "");

    private YMainMetadata(Name alias, Table<YMainMetadataRecord> aliased) {
        this(alias, aliased, null);
    }

    private YMainMetadata(Name alias, Table<YMainMetadataRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>y_main_metadata</code> table reference
     */
    public YMainMetadata(String alias) {
        this(DSL.name(alias), Y_MAIN_METADATA);
    }

    /**
     * Create an aliased <code>y_main_metadata</code> table reference
     */
    public YMainMetadata(Name alias) {
        this(alias, Y_MAIN_METADATA);
    }

    /**
     * Create a <code>y_main_metadata</code> table reference
     */
    public YMainMetadata() {
        this(DSL.name("y_main_metadata"), null);
    }

    public <O extends Record> YMainMetadata(Table<O> child, ForeignKey<O, YMainMetadataRecord> key) {
        super(child, key, Y_MAIN_METADATA);
    }

    @Override
    public Schema getSchema() {
        return DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public UniqueKey<YMainMetadataRecord> getPrimaryKey() {
        return Keys.Y_MAIN_METADATA_STAGING_PKEY;
    }

    @Override
    public List<UniqueKey<YMainMetadataRecord>> getKeys() {
        return Arrays.<UniqueKey<YMainMetadataRecord>>asList(Keys.Y_MAIN_METADATA_STAGING_PKEY);
    }

    @Override
    public YMainMetadata as(String alias) {
        return new YMainMetadata(DSL.name(alias), this);
    }

    @Override
    public YMainMetadata as(Name alias) {
        return new YMainMetadata(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public YMainMetadata rename(String name) {
        return new YMainMetadata(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public YMainMetadata rename(Name name) {
        return new YMainMetadata(name, null);
    }
}
