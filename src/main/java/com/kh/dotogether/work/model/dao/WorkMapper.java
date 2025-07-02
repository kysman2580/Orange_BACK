package com.kh.dotogether.work.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import com.kh.dotogether.work.model.dto.WorkDTO;
import com.kh.dotogether.work.model.vo.Work;

@Mapper
public interface WorkMapper {
	
	List<WorkDTO> findWorkList(Work work);
	
	int addWork(Work work);
	
	WorkDTO findWorkByWorkId(String workId);
	
	int updateWorkStatus(Work work);
	
	int updateWorkDetail(Work work);
	
	int deleteWorkByWorkNo(String workId);
	
}
