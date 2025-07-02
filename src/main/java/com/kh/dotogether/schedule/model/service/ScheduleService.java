package com.kh.dotogether.schedule.model.service;

import com.kh.dotogether.schedule.model.dto.ScheduleDTO;




public interface ScheduleService {

	void setSchedule(ScheduleDTO scheduleDTO);

	ScheduleDTO findScheduleById(Long scheduleNo);

	void updateSchedule(ScheduleDTO scheduleDTO);

	void deleteSchedule(Long scheduleNo);


}
