package com.lee.ssxy.search.service;

import com.lee.ssxy.model.search.SkuEs;
import com.lee.ssxy.vo.search.SkuEsQueryVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SkuApiService {
     List<SkuEs> findHotSkuInfoList();


    void upperSku(Long skuId);

    void lowerSku(Long skuId);

    Page<SkuEs> search(Pageable pageable, SkuEsQueryVo skuEsQueryVo);

    void incrHotScore(Long skuId);
}
