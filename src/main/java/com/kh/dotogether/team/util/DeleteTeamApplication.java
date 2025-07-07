package com.kh.dotogether.team.util;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kh.dotogether.team.model.dao.TeamMapper;
import com.kh.dotogether.team.model.dto.ApplicantDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeleteTeamApplication {

	private final TeamMapper teamMapper;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteTeamApplication(ApplicantDTO applicantInfo) {
        teamMapper.deleteTeamApplication(applicantInfo);
    }
}
