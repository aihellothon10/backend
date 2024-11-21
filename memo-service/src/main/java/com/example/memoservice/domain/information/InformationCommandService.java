package com.example.memoservice.domain.information;

import com.example.commonmodule.common.exception.DataNotFoundException;
import com.example.memoservice.domain.information.model.Information;
import com.example.memoservice.domain.information.repository.InformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InformationCommandService {

    private final InformationRepository informationRepository;
    private final VectorStore vectorStore;


    /**
     * 정보와 함께 Document도 저장
     * - Document의 내용은 수정 불가이기 때문에 삭제 후 생성해야함
     *
     * @return : 생성한 정보의 식별자
     */
    public Long createInformation(CreateInformationRequest request) {
        Document document = new Document("title : %s, content : %s".formatted(request.title(), request.content()));
        vectorStore.add(List.of(document));

        Information newInformation = new Information(document.getId(), request.title(), request.content());
        Information savedInformation = informationRepository.save(newInformation);
        return savedInformation.getInformationId();
    }

    public void createInformationBulk(List<CreateInformationRequest> requests) {
        requests.forEach(this::createInformation);
    }

    public void deleteInformation(Long informationId) {
        Information memo = findInformation(informationId);

        vectorStore.delete(List.of(memo.getDocumentId()));
        informationRepository.delete(memo);
    }

    private Information findInformation(Long informationId) {
        return informationRepository.findById(informationId)
                .orElseThrow(() -> new DataNotFoundException("Information Not Found"));
    }

}
