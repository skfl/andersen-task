package com.andersentask.bookshop.request.mappers;

import com.andersentask.bookshop.request.dtos.RequestDTO;
import com.andersentask.bookshop.request.entities.Request;

import java.util.List;

public class RequestMapper {

    public static RequestDTO entityToDto(Request request) {
        return RequestDTO.builder()
                .id(request.getId())
                .user(request.getUser())
                .requestedBook(request.getRequestedBook())
                .createdAt(request.getCreatedAt())
                .createdAt(request.getCreatedAt())
                .build();
    }

    public static Request dtoToEntity(RequestDTO dto) {
        return Request.builder()
                .id(dto.getId())
                .user(dto.getUser())
                .requestedBook(dto.getRequestedBook())
                .createdAt(dto.getCreatedAt())
                .createdAt(dto.getCreatedAt())
                .build();
    }

    public static List<RequestDTO> entityListToDtoList(List<Request> requests) {
        return requests.stream()
                .map(RequestMapper::entityToDto)
                .toList();
    }

    public static List<Request> dtoListToEntityList(List<RequestDTO> dtos) {
        return dtos.stream()
                .map(RequestMapper::dtoToEntity)
                .toList();
    }
}
