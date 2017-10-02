package org.openmrs.module.wellness.calculation.library.nutrition;

import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyacore.calculation.Calculations;
import org.openmrs.module.wellness.Dictionary;

import java.util.Collection;
import java.util.Map;

public class BodyFatCalcultion extends AbstractPatientCalculation {
    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> map, PatientCalculationContext context) {
        CalculationResultMap ret = new CalculationResultMap();
        CalculationResultMap lastObs = Calculations.lastObs(Dictionary.getConcept("1343AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"), cohort, context);
        if(lastObs != null){
            ret.putAll(lastObs);
        }
        return ret;
    }
}
