package com.imperva.stepping;

import java.util.ArrayList;
import java.util.List;

//todo - very expensive - fix
public class EvenDistributionStrategy extends IDistributionStrategy {
    @Override
    public void distribute(List<IStepDecorator> iStepDecorators, Data data, String subjectType) {
        if (!(data.getValue() instanceof List))
            // todo- consider maybe we need an IdentifiableSteppingException in the future
            throw new SteppingException("EvenDistributionStrategy not supported");

        List dataToDistribute = ((List) data.getValue());
        int chunks = Math.floorDiv(dataToDistribute.size(), iStepDecorators.size());

        List<List> chopped = chopped(dataToDistribute, chunks);
        if (chopped.size() > iStepDecorators.size()) {
            chopped.get(chopped.size() - 2).addAll(chopped.get(chopped.size() - 1));
            chopped.remove(chopped.size() - 1);
        }
        Distribution[] arr  = new Distribution[iStepDecorators.size()];
        for (int u = 0; u < iStepDecorators.size(); u++) {
            arr[u] = new Distribution(iStepDecorators.get(u),new Data(chopped.get(u)), subjectType);
        }
        distribute(arr);
    }

    private  <T> List<List<T>> chopped(List<T> list, final int L) {
        List<List<T>> parts = new ArrayList<List<T>>();
        final int N = list.size();
        for (int i = 0; i < N; i += L) {
            parts.add(new ArrayList<T>(
                    list.subList(i, Math.min(N, i + L)))
            );
        }
        return parts;
    }
}
