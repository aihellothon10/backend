package com.example.memoservice.domain.information;

import com.example.commonmodule.common.exception.DataNotFoundException;
import com.example.memoservice.domain.information.model.Information;
import com.example.memoservice.domain.information.repository.InformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InformationQueryService {

    private final InformationRepository informationRepository;


    public Information getInformation(Long informationId) {
        return informationRepository.findById(informationId)
                .orElseThrow(() -> new DataNotFoundException("Information Not Found"));
    }

    public List<Information> getAllInformation() {
        return informationRepository.findAll();
    }

}
