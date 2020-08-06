package com.imperva.stepping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SteppingTestingToolkit {
    private String algoName;
    private Algo algo;
    private Object syncObj = new Object();
    private ContainerRegistrar containerRegistrar = new ContainerRegistrar();


    private volatile List<String> subjects = new ArrayList<>();//* todo need?
    HashMap<String, Data> subjectsStatus = new HashMap<>();
    private StepConfig stepConfig;


    public SteppingTestingToolkit withAlgo(String algoName, Algo algo) {

        this.algoName = algoName;
        this.algo = algo;
        return this;
    }


    public SteppingTestingToolkit withSubject(String subject) {
        subjects.add(subject);
        for (String sub : subjects) {
            subjectsStatus.put(sub, null);
        }
        return this;
    }

    public SteppingTestingToolkit withStep(Step step) {

        containerRegistrar.add(step);
        return this;
    }

    public SteppingTestingToolkit withContainerRegistrar(ContainerRegistrar containerRegistrar) {
        this.containerRegistrar = containerRegistrar;
        return this;
    }

    public SteppingTestingToolkit withStepConfig(StepConfig stepConfig) {//* todo need  for each step
        this.stepConfig = stepConfig;
        return this;
    }

    public TestingResults test() {
        AlgoDecoratorTesting algoDecoratorTesting = new AlgoDecoratorTesting(algo, containerRegistrar, stepConfig, subjects, this::testingCallbackListener);

        new Stepping()
                .registerAndControl(algoName, algoDecoratorTesting)
                .go();

        waitTillDone();

        return new TestingResults(subjectsStatus);
    }

    private void waitTillDone() {
        synchronized (syncObj) {
            try {
                syncObj.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    boolean testingCallbackListener(Data d, String s) {

        subjectsStatus.put(s, d);

        boolean allArrived = isAllArrived();

        if (allArrived) {
            releaseWait();
            return true;
        }
        return false;
    }

    private void releaseWait() {
        System.out.println("Notifying");
        synchronized (syncObj) {
            syncObj.notifyAll();
        }
    }

    private boolean isAllArrived() {
        boolean allArrived = true;
        for (Map.Entry<String, Data> entry : subjectsStatus.entrySet()) {
            if (entry.getValue() == null) {
                allArrived = false;
                break;
            }
        }
        return allArrived;
    }
}
