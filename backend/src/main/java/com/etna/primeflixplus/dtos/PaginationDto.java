package com.etna.primeflixplus.dtos;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class PaginationDto {

    @Value("0")
    Integer page;

    @Value("10")
    Integer size;
}
