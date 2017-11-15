package org.openmrs.module.wellness.fragment.controller.patient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.Visit;
import org.openmrs.VisitType;
import org.openmrs.api.context.Context;
import org.openmrs.module.appointmentscheduling.Appointment;
import org.openmrs.module.appointmentscheduling.AppointmentBlock;
import org.openmrs.module.appointmentscheduling.AppointmentType;
import org.openmrs.module.appointmentscheduling.TimeSlot;
import org.openmrs.module.appointmentscheduling.api.AppointmentService;
import org.openmrs.module.wellness.CustomAppointment;
import org.openmrs.module.wellness.CustomAppointmentBlocks;
import org.openmrs.module.wellness.api.KenyaEmrService;
import org.openmrs.module.wellness.metadata.CommonMetadata;
import org.openmrs.module.wellness.util.EmrUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.web.WebConstants;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ScheduleAppointmentFragmentController {
    protected final Log log = LogFactory.getLog(getClass());

    public void controller(FragmentModel model, @RequestParam(value = "patientId", required = false) String patientId
                           ){
        AppointmentService service = Context.getService(AppointmentService.class);
        List<Appointment> patientAppointments = new ArrayList<Appointment>();
        List<CustomAppointment> customAppointments = new ArrayList<CustomAppointment>();
        List<CustomAppointmentBlocks> customAppointmentBlocks = new ArrayList<CustomAppointmentBlocks>();
        String reason = "";

        List<Appointment> allAppointments = service.getAllAppointments();
        for(Appointment appointment : allAppointments){
            if(appointment.getPatient().equals(Context.getPatientService().getPatient(Integer.parseInt(patientId)))){
                patientAppointments.add(appointment);
            }
        }
        for(Appointment appointment:patientAppointments) {
            CustomAppointment customAppointment = new CustomAppointment();
            customAppointment.setProvider(appointment.getTimeSlot().getAppointmentBlock().getProvider());
            customAppointment.setAppointmentType(appointment.getAppointmentType());
            customAppointment.setAppointmentDate(EmrUtils.formatDates(appointment.getTimeSlot().getStartDate()));
            customAppointment.setTimeSlots(EmrUtils.formatTimeFromDate(appointment.getTimeSlot().getStartDate())+"-"+EmrUtils.formatTimeFromDate(appointment.getTimeSlot().getEndDate()));
            customAppointment.setStatus(appointment.getStatus().getName());
            if(appointment.getReason() != null){
                reason = appointment.getReason();
            }
            customAppointment.setNotes(reason);
            customAppointments.add(customAppointment);
        }
        //looping through the appointment blocks
        List<AppointmentBlock> allAppointmentBlocks = service.getAllAppointmentBlocks();
        List<AppointmentType> appointmentTypeList;
        for(AppointmentBlock block: allAppointmentBlocks){
            CustomAppointmentBlocks customBlocks = new CustomAppointmentBlocks();

            if(block != null) {
                appointmentTypeList = new ArrayList<AppointmentType>(block.getTypes());
                customBlocks.setBlockId(block.getAppointmentBlockId());
                customBlocks.setAppointmentType(appointmentTypeList.get(0));
                customBlocks.setProvider(block.getProvider());
                customBlocks.setAvailableDate(EmrUtils.formatDates(block.getStartDate()));
                customBlocks.setTimeSlots(EmrUtils.formatTimeFromDate(block.getStartDate())+"-"+EmrUtils.formatTimeFromDate(block.getEndDate()));
            }

            customAppointmentBlocks.add(customBlocks);

        }

        model.addAttribute("appointmentTypes", service.getAllAppointmentTypes());
        model.addAttribute("provider", service.getAllProvidersSorted(false));
        model.addAttribute("appointments", customAppointments);
        model.addAttribute("appointmentBlocks", customAppointmentBlocks);
    }



    public void post(HttpServletRequest request,
                     @RequestParam(value = "flow", required = false) String flow,
                     @RequestParam(value = "patientId", required = false) String patientId,
                     @RequestParam(value = "notes", required = false) String notes,
                     @RequestParam(value = "timeSlots", required = false) Integer timeSlots,
                     @RequestParam(value = "type", required = false) Integer type)
                    {
                HttpSession httpSession = request.getSession();
                AppointmentService appointmentService = Context.getService(AppointmentService.class);

                Appointment appointment = new Appointment();
                                //get the patient to be associate
                Patient patient = Context.getPatientService().getPatient(Integer.parseInt(patientId));



        if (flow != null && flow.equals("walk")) {
            appointment.setStatus(Appointment.AppointmentStatus.WALKIN);// this is normal client visit in the facility
            appointment.setDateCreated(new Date());

            //Start a new visit
            VisitType defaultVisitType = Context.getVisitService().getVisitTypeByUuid(CommonMetadata._VisitType.OUTPATIENT);
            Visit visit = new Visit(patient, defaultVisitType, new Date());
            visit.setLocation(Context.getService(KenyaEmrService.class).getDefaultLocation());
            visit = Context.getVisitService().saveVisit(visit);
            appointment.setVisit(visit);
        }
        else {
            //create a time slot for this appointment
            try {
                TimeSlot slot = new TimeSlot();
                AppointmentBlock appointmentBlock = appointmentService.getAppointmentBlock(timeSlots);
                List<TimeSlot> slotsToThisBlock = appointmentService.getTimeSlotsInAppointmentBlock(appointmentBlock);
                if (slotsToThisBlock.size() > 0) {
                    slot = slotsToThisBlock.get(0);
                }

                //tie the time slots to the appointment

                appointment.setDateCreated(new Date());
                appointment.setStatus(Appointment.AppointmentStatus.SCHEDULED);
                appointment.setAppointmentType(appointmentService.getAppointmentType(type));
                appointment.setTimeSlot(slot);
                appointment.setPatient(patient);
                appointment.setReason(notes);
                appointmentService.saveAppointment(appointment);
                httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Appointment Created");
            }
            catch (Exception e){
                log.warn("Provide all input "+e.toString());
            }

        }

    }

}
