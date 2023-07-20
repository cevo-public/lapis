import { expect } from 'chai';
import { lapisClient } from './common';
import fs from 'fs';
import { SequenceFilters } from './lapisClient';

describe('The /nucleotideMutationProportions endpoint', () => {
  let mutationWithLessThan10PercentProportion = 'C19220T';
  let mutationWithMoreThan50PercentProportion = 'G28280C';

  it('should return mutation proportions for Switzerland', async () => {
    const result = await lapisClient.postNucleotideMutations({
      sequenceFiltersWithMinProportion: { country: 'Switzerland' },
    });

    expect(result).to.have.length(362);

    const rareMutationProportion = result.find(
      mutationData => mutationData.position === mutationWithLessThan10PercentProportion
    );
    expect(rareMutationProportion?.count).to.equal(8);
    expect(rareMutationProportion?.proportion).to.be.approximately(0.0816, 0.0001);

    const commonMutationProportion = result.find(
      mutationProportion => mutationProportion.position === mutationWithMoreThan50PercentProportion
    );
    expect(commonMutationProportion?.count).to.equal(51);
    expect(commonMutationProportion?.proportion).to.be.approximately(0.5204, 0.0001);
  });

  it('should return mutation proportions for Switzerland with minProportion 0.5', async () => {
    const result = await lapisClient.postNucleotideMutations({
      sequenceFiltersWithMinProportion: {
        country: 'Switzerland',
        minProportion: 0.5,
      },
    });

    expect(result).to.have.length(108);

    const mutationsAboveThreshold = result.map(mutationData => mutationData.position);
    expect(mutationsAboveThreshold).to.contain(mutationWithMoreThan50PercentProportion);
    expect(mutationsAboveThreshold).to.not.contain(mutationWithLessThan10PercentProportion);
  });

  it('should order by specified fields', async () => {
    const ascendingOrderedResult = await lapisClient.postNucleotideMutations({
      sequenceFiltersWithMinProportion: {
        orderBy: [{ field: 'position', type: 'ascending' }],
      },
    });

    expect(ascendingOrderedResult[0]).to.have.property('position', 'A1-');

    const descendingOrderedResult = await lapisClient.postNucleotideMutations({
      sequenceFiltersWithMinProportion: {
        orderBy: [{ field: 'position', type: 'descending' }],
      },
    });

    expect(descendingOrderedResult[0]).to.have.property('position', 'T9-');
  });

  it('should apply limit and offset', async () => {
    const resultWithLimit = await lapisClient.postNucleotideMutations({
      sequenceFiltersWithMinProportion: {
        orderBy: [{ field: 'position', type: 'ascending' }],
        limit: 2,
      },
    });

    expect(resultWithLimit).to.have.length(2);
    expect(resultWithLimit[1]).to.have.property('position', 'A11201G');

    const resultWithLimitAndOffset = await lapisClient.postNucleotideMutations({
      sequenceFiltersWithMinProportion: {
        orderBy: [{ field: 'position', type: 'ascending' }],
        limit: 2,
        offset: 1,
      },
    });

    expect(resultWithLimitAndOffset).to.have.length(2);
    expect(resultWithLimitAndOffset[0]).to.deep.equal(resultWithLimit[1]);
  });
});
