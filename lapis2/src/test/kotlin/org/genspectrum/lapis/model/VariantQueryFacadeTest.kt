package org.genspectrum.lapis.model

import org.genspectrum.lapis.silo.AminoAcidInsertionContains
import org.genspectrum.lapis.silo.AminoAcidSymbolEquals
import org.genspectrum.lapis.silo.And
import org.genspectrum.lapis.silo.HasAminoAcidMutation
import org.genspectrum.lapis.silo.HasNucleotideMutation
import org.genspectrum.lapis.silo.Maybe
import org.genspectrum.lapis.silo.NOf
import org.genspectrum.lapis.silo.Not
import org.genspectrum.lapis.silo.NucleotideInsertionContains
import org.genspectrum.lapis.silo.NucleotideSymbolEquals
import org.genspectrum.lapis.silo.Or
import org.genspectrum.lapis.silo.PangoLineageEquals
import org.genspectrum.lapis.silo.StringEquals
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class VariantQueryFacadeTest {
    private lateinit var underTest: VariantQueryFacade

    @BeforeEach
    fun setup() {
        underTest = VariantQueryFacade()
    }

    @Test
    fun `given a complex variant query then map should return the corresponding SiloQuery`() {
        val variantQuery = "300G & (400- | 500B) & !600 & MAYBE(700B | 800-) & [3-of: 123A, 234T, 345G] & A.1.2.3*"

        val result = underTest.map(variantQuery)

        val expectedResult =
            And(
                listOf(
                    And(
                        listOf(
                            And(
                                listOf(
                                    And(
                                        listOf(
                                            And(
                                                listOf(
                                                    NucleotideSymbolEquals(null, 300, "G"),
                                                    Or(
                                                        listOf(
                                                            NucleotideSymbolEquals(null, 400, "-"),
                                                            NucleotideSymbolEquals(null, 500, "B"),
                                                        ),
                                                    ),
                                                ),
                                            ),
                                            Not(HasNucleotideMutation(null, 600)),
                                        ),
                                    ),
                                    Maybe(
                                        Or(
                                            listOf(
                                                NucleotideSymbolEquals(null, 700, "B"),
                                                NucleotideSymbolEquals(null, 800, "-"),
                                            ),
                                        ),
                                    ),
                                ),
                            ),
                            NOf(
                                3,
                                matchExactly = false,
                                listOf(
                                    NucleotideSymbolEquals(null, 123, "A"),
                                    NucleotideSymbolEquals(null, 234, "T"),
                                    NucleotideSymbolEquals(null, 345, "G"),
                                ),
                            ),
                        ),
                    ),
                    PangoLineageEquals(PANGO_LINEAGE_COLUMN, "A.1.2.3", true),
                ),
            )

        assertThat(result, equalTo(expectedResult))
    }

    @Test
    fun `given a variantQuery with a single entry then map should return the corresponding SiloQuery`() {
        val variantQuery = "300G"

        val result = underTest.map(variantQuery)

        val expectedResult = NucleotideSymbolEquals(null, 300, "G")
        assertThat(result, equalTo(expectedResult))
    }

    @Test
    fun `given a variantQuery with mutation with position only then should return HasNucleotideMutation filter`() {
        val variantQuery = "400"

        val result = underTest.map(variantQuery)

        assertThat(result, equalTo(HasNucleotideMutation(null, 400)))
    }

    @Test
    fun `given a variantQuery with an 'And' expression then map should return the corresponding SiloQuery`() {
        val variantQuery = "300G & 400-"

        val result = underTest.map(variantQuery)

        val expectedResult = And(
            listOf(
                NucleotideSymbolEquals(null, 300, "G"),
                NucleotideSymbolEquals(null, 400, "-"),
            ),
        )
        assertThat(result, equalTo(expectedResult))
    }

    @Test
    fun `given a variantQuery with two 'And' expression then map should return the corresponding SiloQuery`() {
        val variantQuery = "300G & 400- & 500B"

        val result = underTest.map(variantQuery)

        val expectedResult = And(
            listOf(
                And(
                    listOf(
                        NucleotideSymbolEquals(null, 300, "G"),
                        NucleotideSymbolEquals(null, 400, "-"),
                    ),
                ),
                NucleotideSymbolEquals(null, 500, "B"),
            ),
        )
        assertThat(result, equalTo(expectedResult))
    }

    @Test
    fun `given a variantQuery with a 'Not' expression then map should return the corresponding SiloQuery`() {
        val variantQuery = "!300G"

        val result = underTest.map(variantQuery)

        val expectedResult = Not(NucleotideSymbolEquals(null, 300, "G"))
        assertThat(result, equalTo(expectedResult))
    }

    @Test
    fun `given a variant variantQuery with an 'Or' expression then map should return the corresponding SiloQuery`() {
        val variantQuery = "300G | 400-"

        val result = underTest.map(variantQuery)

        val expectedResult = Or(
            listOf(
                NucleotideSymbolEquals(null, 300, "G"),
                NucleotideSymbolEquals(null, 400, "-"),
            ),
        )
        assertThat(result, equalTo(expectedResult))
    }

    @Test
    fun `given a variantQuery with an bracket expression then map should return the corresponding SiloQuery`() {
        val variantQuery = "300C & (400A | 500G)"

        val result = underTest.map(variantQuery)

        val expectedResult = And(
            listOf(
                NucleotideSymbolEquals(null, 300, "C"),
                Or(
                    listOf(
                        NucleotideSymbolEquals(null, 400, "A"),
                        NucleotideSymbolEquals(null, 500, "G"),
                    ),
                ),
            ),
        )
        assertThat(result, equalTo(expectedResult))
    }

    @Test
    fun `given a variantQuery with a 'Maybe' expression then map should return the corresponding SiloQuery`() {
        val variantQuery = "MAYBE(300G)"

        val result = underTest.map(variantQuery)

        val expectedResult = Maybe(NucleotideSymbolEquals(null, 300, "G"))
        assertThat(result, equalTo(expectedResult))
    }

    @Test
    fun `given a variantQuery with a 'Pangolineage' expression then map should return the corresponding SiloQuery`() {
        val variantQuery = "A.1.2.3"

        val result = underTest.map(variantQuery)

        val expectedResult = PangoLineageEquals(PANGO_LINEAGE_COLUMN, "A.1.2.3", false)
        assertThat(result, equalTo(expectedResult))
    }

    @Test
    @Suppress("ktlint:standard:max-line-length")
    fun `given a variantQuery with a 'Pangolineage' expression (including sublineages) then map should return the corresponding SiloQuery`() {
        val variantQuery = "A.1.2.3*"

        val result = underTest.map(variantQuery)

        val expectedResult = PangoLineageEquals(PANGO_LINEAGE_COLUMN, "A.1.2.3", true)
        assertThat(result, equalTo(expectedResult))
    }

    @Test
    @Suppress("ktlint:standard:max-line-length")
    fun `given a variantQuery with a 'NextcladePangolineage' expression then map should return the corresponding SiloQuery`() {
        val variantQuery = "nextcladePangoLineage:A.1.2.3*"

        val result = underTest.map(variantQuery)

        val expectedResult = PangoLineageEquals(NEXTCLADE_PANGO_LINEAGE_COLUMN, "A.1.2.3", true)
        assertThat(result, equalTo(expectedResult))
    }

    @Test
    fun `given a variantQuery with a 'Nof' expression then map should return the corresponding SiloQuery`() {
        val variantQuery = "[3-of: 123A, 234T, 345G]"

        val result = underTest.map(variantQuery)

        val expectedResult = NOf(
            3,
            false,
            listOf(
                NucleotideSymbolEquals(null, 123, "A"),
                NucleotideSymbolEquals(null, 234, "T"),
                NucleotideSymbolEquals(null, 345, "G"),
            ),
        )
        assertThat(result, equalTo(expectedResult))
    }

    @Test
    fun `given a variantQuery with a exact 'Nof' expression then map should return the corresponding SiloQuery`() {
        val variantQuery = "[exactly-3-of: 123A, 234T, 345G]"

        val result = underTest.map(variantQuery)

        val expectedResult = NOf(
            3,
            true,
            listOf(
                NucleotideSymbolEquals(null, 123, "A"),
                NucleotideSymbolEquals(null, 234, "T"),
                NucleotideSymbolEquals(null, 345, "G"),
            ),
        )
        assertThat(result, equalTo(expectedResult))
    }

    @Test
    fun `given a variantQuery with a 'Insertion' expression then returns SILO query`() {
        val variantQuery = "ins_1234:GAG"

        val result = underTest.map(variantQuery)

        assertThat(result, equalTo(NucleotideInsertionContains(1234, "GAG")))
    }

    @Test
    fun `given a variantQuery with a 'Insertion' expression with lower case letters then returns SILO query`() {
        val variantQuery = "ins_1234:gAG"

        val result = underTest.map(variantQuery)

        assertThat(result, equalTo(NucleotideInsertionContains(1234, "GAG")))
    }

    @Test
    fun `given a variantQuery with a 'Insertion' with wildcard expression then returns SILO query`() {
        val variantQuery = "ins_1234:G?A?G"

        val result = underTest.map(variantQuery)

        assertThat(result, equalTo(NucleotideInsertionContains(1234, "G.*A.*G")))
    }

    @Test
    fun `given amino acid mutation expression then should map to AminoAcidSymbolEquals`() {
        val variantQuery = "S:N501Y"

        val result = underTest.map(variantQuery)

        assertThat(result, equalTo(AminoAcidSymbolEquals("S", 501, "Y")))
    }

    @Test
    fun `given amino acid mutation expression with lower case letters then should map to AminoAcidSymbolEquals`() {
        val variantQuery = "S:n501y"

        val result = underTest.map(variantQuery)

        assertThat(result, equalTo(AminoAcidSymbolEquals("S", 501, "Y")))
    }

    @Test
    fun `given amino acid mutation expression with gene lower case letters then should map to AminoAcidSymbolEquals`() {
        val variantQuery = "orf1a:N501Y"

        val result = underTest.map(variantQuery)

        assertThat(result, equalTo(AminoAcidSymbolEquals("ORF1A", 501, "Y")))
    }

    @Test
    fun `given amino acid mutation expression without first symbol then should map to AminoAcidSymbolEquals`() {
        val variantQuery = "S:501Y"

        val result = underTest.map(variantQuery)

        assertThat(result, equalTo(AminoAcidSymbolEquals("S", 501, "Y")))
    }

    @Test
    fun `given amino acid mutation expression without second symbol then should return HasAminoAcidMutation`() {
        val variantQuery = "S:N501"

        val result = underTest.map(variantQuery)

        assertThat(result, equalTo(HasAminoAcidMutation("S", 501)))
    }

    @Test
    fun `given amino acid mutation expression without any symbol then should return HasAminoAcidMutation`() {
        val variantQuery = "S:501"

        val result = underTest.map(variantQuery)

        assertThat(result, equalTo(HasAminoAcidMutation("S", 501)))
    }

    @Test
    fun `given a valid variantQuery with a 'AA insertion' expression then returns SILO query`() {
        val variantQuery = "ins_S:501:EPE"

        val result = underTest.map(variantQuery)

        assertThat(result, equalTo(AminoAcidInsertionContains(501, "EPE", "S")))
    }

    @Test
    fun `given a valid variantQuery with a 'AA insertion' expression with lower case then returns SILO query`() {
        val variantQuery = "ins_S:501:ePe"

        val result = underTest.map(variantQuery)

        assertThat(result, equalTo(AminoAcidInsertionContains(501, "EPE", "S")))
    }

    @Test
    fun `given a valid variantQuery with a 'AA insertion' with wildcard then returns SILO query`() {
        val variantQuery = "ins_S:501:E?E?"

        val result = underTest.map(variantQuery)

        assertThat(result, equalTo(AminoAcidInsertionContains(501, "E.*E.*", "S")))
    }

    @Test
    fun `given a valid variantQuery with a 'NextstrainCladeLineage' expression then returns SILO query`() {
        val variantQuery = "nextstrainClade:22B"

        val result = underTest.map(variantQuery)

        assertThat(result, equalTo(StringEquals(NEXTSTRAIN_CLADE_COLUMN, "22B")))
    }

    @Test
    @Suppress("ktlint:standard:max-line-length")
    fun `given a valid variantQuery with a 'NextstrainCladeLineage' expression in lower case then returns SILO query`() {
        val variantQuery = "nextstrainClade:22b"

        val result = underTest.map(variantQuery)

        assertThat(result, equalTo(StringEquals(NEXTSTRAIN_CLADE_COLUMN, "22B")))
    }

    @Test
    fun `given a valid variantQuery with a 'NextstrainCladeLineage' recombinant expression then returns SILO query`() {
        val variantQuery = "nextstrainClade:RECOMBINANT"

        val result = underTest.map(variantQuery)

        assertThat(result, equalTo(StringEquals(NEXTSTRAIN_CLADE_COLUMN, "recombinant")))
    }

    @Test
    fun `given a valid variantQuery with a single letter 'GisaidCladeLineage' expression then returns SILO query`() {
        val variantQuery = "gisaid:X"

        val result = underTest.map(variantQuery)

        assertThat(result, equalTo(StringEquals(GISAID_CLADE_COLUMN, "X")))
    }

    @Test
    @Suppress("ktlint:standard:max-line-length")
    fun `given a valid variantQuery with a single letter 'GisaidCladeLineage' expression with lower case then returns SILO query`() {
        val variantQuery = "gisaid:x"

        val result = underTest.map(variantQuery)

        assertThat(result, equalTo(StringEquals(GISAID_CLADE_COLUMN, "X")))
    }

    @Test
    fun `given a valid variantQuery with a 'GisaidCladeLineage' expression then returns SILO query`() {
        val variantQuery = "gisaid:AB"

        val result = underTest.map(variantQuery)

        assertThat(result, equalTo(StringEquals(GISAID_CLADE_COLUMN, "AB")))
    }
}
