package g2pc.ref.farmer.regsvc.serviceimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import g2pc.core.lib.dto.common.header.RequestHeaderDTO;
import g2pc.core.lib.dto.common.header.ResponseHeaderDTO;
import g2pc.core.lib.dto.common.message.request.QueryDTO;
import g2pc.core.lib.dto.common.message.response.DataDTO;
import g2pc.ref.farmer.regsvc.dto.request.QueryParamsFarmerDTO;
import g2pc.ref.farmer.regsvc.dto.response.RegRecordFarmerDTO;
import g2pc.ref.farmer.regsvc.entity.FarmerInfoEntity;
import g2pc.ref.farmer.regsvc.repository.FarmerInfoRepository;
import g2pc.ref.farmer.regsvc.service.FarmerResponseBuilderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class FarmerResponseBuilderServiceImpl implements FarmerResponseBuilderService {

    @Autowired
    private FarmerInfoRepository farmerInfoRepository;

    /**
     * Get farmer records information from DB
     *
     * @param farmerInfoEntity required
     * @return Farmer records
     */
    @Override
    public RegRecordFarmerDTO getRegRecordFarmerDTO(FarmerInfoEntity farmerInfoEntity) {
        RegRecordFarmerDTO dto = new RegRecordFarmerDTO();
        dto.setFarmerId(farmerInfoEntity.getFarmerId());
        dto.setFarmerName(farmerInfoEntity.getFarmerName());
        dto.setSeason(farmerInfoEntity.getSeason());
        dto.setPaymentStatus(farmerInfoEntity.getPaymentStatus());
        dto.setPaymentDate(farmerInfoEntity.getPaymentDate());
        dto.setPaymentAmount(farmerInfoEntity.getPaymentAmount());
        return dto;
    }

    /**
     * Get farmer records information string
     *
     * @param queryDTOList required
     * @return List of farmer records
     */
    @Override
    public List<String> getRegFarmerRecords(List<QueryDTO> queryDTOList) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> regFarmerRecordsList = new ArrayList<>();
        for (QueryDTO queryDTO : queryDTOList) {
            String queryParams = objectMapper.writeValueAsString(queryDTO.getQueryParams());
            QueryParamsFarmerDTO queryParamsFarmerDTO = objectMapper.readValue(queryParams, QueryParamsFarmerDTO.class);
            String farmerId = queryParamsFarmerDTO.getFarmerId();
            String season = queryParamsFarmerDTO.getSeason();
            Optional<FarmerInfoEntity> optional = farmerInfoRepository.findBySeasonAndFarmerId(season, farmerId);
            if (optional.isPresent()) {
                RegRecordFarmerDTO regRecordFarmerDTO = getRegRecordFarmerDTO(optional.get());
                regFarmerRecordsList.add(objectMapper.writeValueAsString(regRecordFarmerDTO));
            } else {
                regFarmerRecordsList.add(StringUtils.EMPTY);
            }
        }
        return regFarmerRecordsList;
    }
}
