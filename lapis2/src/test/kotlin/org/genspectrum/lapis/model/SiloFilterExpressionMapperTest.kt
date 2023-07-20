package org.genspectrum.lapis.model

import org.genspectrum.lapis.config.SequenceFilterFieldType
import org.genspectrum.lapis.config.SequenceFilterFields
import org.genspectrum.lapis.request.AminoAcidMutation
import org.genspectrum.lapis.request.CommonSequenceFilters
import org.genspectrum.lapis.request.NucleotideMutation
import org.genspectrum.lapis.request.OrderByField
import org.genspectrum.lapis.silo.AminoAcidSymbolEquals
import org.genspectrum.lapis.silo.And
import org.genspectrum.lapis.silo.DateBetween
import org.genspectrum.lapis.silo.FloatBetween
import org.genspectrum.lapis.silo.FloatEquals
import org.genspectrum.lapis.silo.HasAminoAcidMutation
import org.genspectrum.lapis.silo.HasNucleotideMutation
import org.genspectrum.lapis.silo.IntBetween
import org.genspectrum.lapis.silo.IntEquals
import org.genspectrum.lapis.silo.NucleotideSymbolEquals
import org.genspectrum.lapis.silo.PangoLineageEquals
import org.genspectrum.lapis.silo.SiloFilterExpression
import org.genspectrum.lapis.silo.StringEquals
import org.genspectrum.lapis.silo.True
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDate

class SiloFilterExpressionMapperTest {
    private val sequenceFilterFields = SequenceFilterFields(
        mapOf(
            "date" to SequenceFilterFieldType.Date,
            "dateTo" to SequenceFilterFieldType.DateTo("date"),
            "dateFrom" to SequenceFilterFieldType.DateFrom("date"),
            "pangoLineage" to SequenceFilterFieldType.PangoLineage,
            "some_metadata" to SequenceFilterFieldType.String,
            "other_metadata" to SequenceFilterFieldType.String,
            "variantQuery" to SequenceFilterFieldType.VariantQuery,
            "intField" to SequenceFilterFieldType.Int,
            "intFieldTo" to SequenceFilterFieldType.IntTo("intField"),
            "intFieldFrom" to SequenceFilterFieldType.IntFrom("intField"),
            "floatField" to SequenceFilterFieldType.Float,
            "floatFieldTo" to SequenceFilterFieldType.FloatTo("floatField"),
            "floatFieldFrom" to SequenceFilterFieldType.FloatFrom("floatField"),
        ),
    )

    private lateinit var underTest: SiloFilterExpressionMapper

    @BeforeEach
    fun setup() {
        underTest = SiloFilterExpressionMapper(sequenceFilterFields, VariantQueryFacade())
    }

    @Test
    fun `given invalid filter key then throws exception`() {
        val filterParameter = getSequenceFilters(mapOf("invalid query key" to "some value"))

        val exception = assertThrows<IllegalArgumentException> { underTest.map(filterParameter) }

        assertThat(
            exception.message,
            containsString("'invalid query key' is not a valid sequence filter key. Valid keys are:"),
        )
    }

    @Test
    fun `given empty filter parameters then returns a match-all filter`() {
        val filterParameter = getSequenceFilters(emptyMap())

        val result = underTest.map(filterParameter)

        assertThat(result, equalTo(True))
    }

    @ParameterizedTest(name = "FilterParameter: {0}, SiloQuery: {1}")
    @MethodSource("getFilterParametersWithExpectedSiloQuery")
    fun `given filter parameters then maps to expected FilterExpression`(
        filterParameter: Map<String, String>,
        expectedResult: SiloFilterExpression,
    ) {
        val result = underTest.map(getSequenceFilters(filterParameter))

        assertThat(result, equalTo(expectedResult))
    }

    @Test
    fun `given invalid date then should throw an exception`() {
        val filterParameter = getSequenceFilters(mapOf("date" to "this is not a date"))

        val exception = assertThrows<IllegalArgumentException> { underTest.map(filterParameter) }
        assertThat(exception.message, containsString("date 'this is not a date' is not a valid date"))
    }

    @Test
    fun `given invalid dateTo then should throw an exception`() {
        val filterParameter = getSequenceFilters(mapOf("dateTo" to "this is not a date"))

        val exception = assertThrows<IllegalArgumentException> { underTest.map(filterParameter) }
        assertThat(exception.message, containsString("dateTo 'this is not a date' is not a valid date"))
    }

    @Test
    fun `given invalid dateFrom then should throw an exception`() {
        val filterParameter = getSequenceFilters(mapOf("dateFrom" to "this is not a date either"))

        val exception = assertThrows<IllegalArgumentException> { underTest.map(filterParameter) }
        assertThat(exception.message, containsString("dateFrom 'this is not a date either' is not a valid date"))
    }

    @Test
    fun `given date and dateFrom then should throw an exception`() {
        val filterParameter = getSequenceFilters(
            mapOf(
                "date" to "2021-06-03",
                "dateFrom" to "2021-06-03",
            ),
        )
        val exception = assertThrows<IllegalArgumentException> { underTest.map(filterParameter) }
        assertThat(
            exception.message,
            containsString("Cannot filter by exact date field 'date' and by date range field 'dateFrom'."),
        )
    }

    @Test
    fun `given date and dateTo then should throw an exception`() {
        val filterParameter = getSequenceFilters(
            mapOf(
                "date" to "2021-06-03",
                "dateTo" to "2021-06-03",
            ),
        )
        val exception = assertThrows<IllegalArgumentException> { underTest.map(filterParameter) }
        assertThat(
            exception.message,
            containsString("Cannot filter by exact date field 'date' and by date range field 'dateTo'."),
        )
    }

    @Test
    fun `given int field with non-int value then should throw an exception`() {
        val filterParameter = getSequenceFilters(
            mapOf(
                "intField" to "not a number",
            ),
        )
        val exception = assertThrows<IllegalArgumentException> { underTest.map(filterParameter) }
        assertThat(
            exception.message,
            containsString("intField 'not a number' is not a valid integer"),
        )
    }

    @Test
    fun `given intTo field with non-int value then should throw an exception`() {
        val filterParameter = getSequenceFilters(
            mapOf(
                "intFieldTo" to "not a number",
            ),
        )
        val exception = assertThrows<IllegalArgumentException> { underTest.map(filterParameter) }
        assertThat(
            exception.message,
            containsString("intFieldTo 'not a number' is not a valid integer"),
        )
    }

    @Test
    fun `given float field with non-float value then should throw an exception`() {
        val filterParameter = getSequenceFilters(
            mapOf(
                "floatField" to "not a number",
            ),
        )
        val exception = assertThrows<IllegalArgumentException> { underTest.map(filterParameter) }
        assertThat(
            exception.message,
            containsString("floatField 'not a number' is not a valid float"),
        )
    }

    @Test
    fun `given floatTo field with non-float value then should throw an exception`() {
        val filterParameter = getSequenceFilters(
            mapOf(
                "floatFieldTo" to "not a number",
            ),
        )
        val exception = assertThrows<IllegalArgumentException> { underTest.map(filterParameter) }
        assertThat(
            exception.message,
            containsString("floatFieldTo 'not a number' is not a valid float"),
        )
    }

    @Test
    fun `given int and intFrom then should throw an exception`() {
        val filterParameter = getSequenceFilters(
            mapOf(
                "intField" to "42",
                "intFieldFrom" to "43",
            ),
        )
        val exception = assertThrows<IllegalArgumentException> { underTest.map(filterParameter) }
        assertThat(
            exception.message,
            containsString("Cannot filter by exact int field 'intField' and by int range field 'intFieldFrom'."),
        )
    }

    @Test
    fun `given int and intTo then should throw an exception`() {
        val filterParameter = getSequenceFilters(
            mapOf(
                "intFieldTo" to "43",
                "intField" to "42",
            ),
        )
        val exception = assertThrows<IllegalArgumentException> { underTest.map(filterParameter) }
        assertThat(
            exception.message,
            containsString("Cannot filter by exact int field 'intField' and by int range field 'intFieldTo'."),
        )
    }

    @Test
    fun `given float and floatFrom then should throw an exception`() {
        val filterParameter = getSequenceFilters(
            mapOf(
                "floatField" to "42.1",
                "floatFieldFrom" to "42.3",
            ),
        )
        val exception = assertThrows<IllegalArgumentException> { underTest.map(filterParameter) }
        assertThat(
            exception.message,
            containsString(
                "Cannot filter by exact float field 'floatField' and by float range field 'floatFieldFrom'.",
            ),
        )
    }

    @Test
    fun `given float and floatTo then should throw an exception`() {
        val filterParameter = getSequenceFilters(
            mapOf(
                "floatFieldTo" to "42.3",
                "floatField" to "42.1",
            ),
        )
        val exception = assertThrows<IllegalArgumentException> { underTest.map(filterParameter) }
        assertThat(
            exception.message,
            containsString("Cannot filter by exact float field 'floatField' and by float range field 'floatFieldTo'."),
        )
    }

    @Test
    fun `given nucleotide mutation with symbol then is mapped to NucleotideSymbolEquals`() {
        val filterParameter = DummySequenceFilters(
            emptyMap(),
            listOf(NucleotideMutation(null, 123, "B"), NucleotideMutation("sequenceName", 999, "A")),
            emptyList(),
        )

        val result = underTest.map(filterParameter)

        val expected =
            And(listOf(NucleotideSymbolEquals(null, 123, "B"), NucleotideSymbolEquals("sequenceName", 999, "A")))
        assertThat(result, equalTo(expected))
    }

    @Test
    fun `given nucleotide mutation without symbol then is mapped to HasNucleotideMutation`() {
        val filterParameter = DummySequenceFilters(
            emptyMap(),
            listOf(NucleotideMutation(null, 123, null), NucleotideMutation("sequenceName", 999, null)),
            emptyList(),
        )

        val result = underTest.map(filterParameter)

        val expected =
            And(listOf(HasNucleotideMutation(null, 123), HasNucleotideMutation("sequenceName", 999)))
        assertThat(result, equalTo(expected))
    }

    @Test
    fun `given amino acid mutation with symbol then is mapped to AASymbolEquals`() {
        val filterParameter = DummySequenceFilters(
            emptyMap(),
            emptyList(),
            listOf(AminoAcidMutation("geneName1", 123, "B"), AminoAcidMutation("geneName2", 999, "A")),
        )

        val result = underTest.map(filterParameter)

        val expected =
            And(listOf(AminoAcidSymbolEquals("geneName1", 123, "B"), AminoAcidSymbolEquals("geneName2", 999, "A")))
        assertThat(result, equalTo(expected))
    }

    @Test
    fun `given amino acid mutation without symbol then is mapped to HasAAMutation`() {
        val filterParameter = DummySequenceFilters(
            emptyMap(),
            emptyList(),
            listOf(AminoAcidMutation("geneName1", 123, null), AminoAcidMutation("geneName2", 999, null)),
        )

        val result = underTest.map(filterParameter)

        val expected =
            And(listOf(HasAminoAcidMutation("geneName1", 123), HasAminoAcidMutation("geneName2", 999)))
        assertThat(result, equalTo(expected))
    }

    @Test
    fun `given a query with an empty variantQuery then it should throw an error`() {
        val filterParameter = getSequenceFilters(mapOf("variantQuery" to ""))

        val exception = assertThrows<IllegalArgumentException> { underTest.map(filterParameter) }
        assertThat(exception.message, containsString("variantQuery must not be empty"))
    }

    @Test
    fun `given a query with a variantQuery alongside nucleotide mutations then it should throw an error`() {
        val filterParameter = DummySequenceFilters(
            mapOf("variantQuery" to "A123T"),
            listOf(NucleotideMutation(null, 123, null)),
            emptyList(),
        )

        val exception = assertThrows<IllegalArgumentException> { underTest.map(filterParameter) }
        assertThat(
            exception.message,
            containsString("variantQuery filter cannot be used with other variant filters such as: "),
        )
    }

    @Test
    fun `given a query with a variantQuery alongside amino acid mutations then it should throw an error`() {
        val filterParameter = DummySequenceFilters(
            mapOf("variantQuery" to "A123T"),
            emptyList(),
            listOf(AminoAcidMutation("gene", 123, null)),
        )

        val exception = assertThrows<IllegalArgumentException> { underTest.map(filterParameter) }
        assertThat(
            exception.message,
            containsString("variantQuery filter cannot be used with other variant filters such as: "),
        )
    }

    @Test
    fun `given a query with a variantQuery alongside pangoLineage filter then it should throw an error`() {
        val filterParameter = getSequenceFilters(
            mapOf(
                "pangoLineage" to "A.1.2.3",
                "variantQuery" to "A123T",
            ),
        )

        val exception = assertThrows<IllegalArgumentException> { underTest.map(filterParameter) }
        assertThat(
            exception.message,
            containsString("variantQuery filter cannot be used with other variant filters such as: "),
        )
    }

    companion object {
        @JvmStatic
        fun getFilterParametersWithExpectedSiloQuery() = listOf(
            Arguments.of(
                mapOf(
                    "some_metadata" to "ABC",
                    "other_metadata" to "def",
                ),
                And(
                    listOf(
                        StringEquals("some_metadata", "ABC"),
                        StringEquals("other_metadata", "def"),
                    ),
                ),
            ),
            Arguments.of(
                mapOf("pangoLineage" to "A.1.2.3"),
                And(listOf(PangoLineageEquals("pangoLineage", "A.1.2.3", includeSublineages = false))),
            ),
            Arguments.of(
                mapOf("pangoLineage" to "A.1.2.3*"),
                And(listOf(PangoLineageEquals("pangoLineage", "A.1.2.3", includeSublineages = true))),
            ),
            Arguments.of(
                mapOf("pangoLineage" to "A.1.2.3.*"),
                And(listOf(PangoLineageEquals("pangoLineage", "A.1.2.3", includeSublineages = true))),
            ),
            Arguments.of(
                mapOf(
                    "pangoLineage" to "A.1.2.3",
                    "some_metadata" to "ABC",
                    "other_metadata" to "DEF",
                ),
                And(
                    listOf(
                        PangoLineageEquals("pangoLineage", "A.1.2.3", includeSublineages = false),
                        StringEquals("some_metadata", "ABC"),
                        StringEquals("other_metadata", "DEF"),
                    ),
                ),
            ),
            Arguments.of(
                mapOf(
                    "date" to "2021-06-03",
                ),
                And(listOf(DateBetween("date", from = LocalDate.of(2021, 6, 3), to = LocalDate.of(2021, 6, 3)))),
            ),
            Arguments.of(
                mapOf(
                    "dateTo" to "2021-06-03",
                ),
                And(listOf(DateBetween("date", from = null, to = LocalDate.of(2021, 6, 3)))),
            ),
            Arguments.of(
                mapOf(
                    "dateFrom" to "2021-03-28",
                ),
                And(listOf(DateBetween("date", from = LocalDate.of(2021, 3, 28), to = null))),
            ),
            Arguments.of(
                mapOf(
                    "dateFrom" to "2021-03-28",
                    "dateTo" to "2021-06-03",
                ),
                And(listOf(DateBetween("date", from = LocalDate.of(2021, 3, 28), to = LocalDate.of(2021, 6, 3)))),
            ),
            Arguments.of(
                mapOf(
                    "dateTo" to "2021-06-03",
                    "some_metadata" to "ABC",
                ),
                And(
                    listOf(
                        DateBetween("date", from = null, to = LocalDate.of(2021, 6, 3)),
                        StringEquals("some_metadata", "ABC"),
                    ),
                ),
            ),
            Arguments.of(
                mapOf(
                    "variantQuery" to "300G & 400A",
                ),
                And(
                    listOf(
                        And(
                            listOf(
                                NucleotideSymbolEquals(null, 300, "G"),
                                NucleotideSymbolEquals(null, 400, "A"),
                            ),
                        ),
                    ),
                ),
            ),
            Arguments.of(
                mapOf(
                    "variantQuery" to "300G",
                    "some_metadata" to "ABC",
                ),
                And(
                    listOf(
                        NucleotideSymbolEquals(null, 300, "G"),
                        StringEquals("some_metadata", "ABC"),
                    ),
                ),
            ),
            Arguments.of(
                mapOf(
                    "intField" to "42",
                ),
                And(listOf(IntEquals("intField", 42))),
            ),
            Arguments.of(
                mapOf(
                    "intFieldFrom" to "42",
                ),
                And(listOf(IntBetween("intField", 42, null))),
            ),
            Arguments.of(
                mapOf(
                    "intFieldTo" to "42",
                ),
                And(listOf(IntBetween("intField", null, 42))),
            ),
            Arguments.of(
                mapOf(
                    "floatField" to "42.45",
                ),
                And(listOf(FloatEquals("floatField", 42.45))),
            ),
            Arguments.of(
                mapOf(
                    "floatFieldFrom" to "42.45",
                ),
                And(listOf(FloatBetween("floatField", 42.45, null))),
            ),
            Arguments.of(
                mapOf(
                    "floatFieldTo" to "42.45",
                ),
                And(listOf(FloatBetween("floatField", null, 42.45))),
            ),
        )
    }

    private fun getSequenceFilters(sequenceFilters: Map<String, String>) =
        DummySequenceFilters(sequenceFilters, emptyList(), emptyList())

    data class DummySequenceFilters(
        override val sequenceFilters: Map<String, String>,
        override val nucleotideMutations: List<NucleotideMutation>,
        override val aaMutations: List<AminoAcidMutation>,
        override val orderByFields: List<OrderByField> = emptyList(),
        override val limit: Int? = null,
        override val offset: Int? = null,
    ) : CommonSequenceFilters
}
