package org.rcbg.afku.CryptoPass.responses;

import org.rcbg.afku.CryptoPass.dto.SafeFetchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseFactory {

    public static ResponseEntity<SafeFetchResponse> createSafeFetchResponse(String uri, HttpStatus status, SafeFetchResponseDto data){
        MetaData metaData = new MetaData();
        metaData.setUri(uri);
        metaData.setContentType("object");
        metaData.setStatusCode(status.value());
        SafeFetchResponse response = new SafeFetchResponse();
        response.setMetaData(metaData);
        response.setData(data);
        return new ResponseEntity<>(response, new HttpHeaders(), status);
    }

    public static ResponseEntity<FullFetchResponse> createFullFetchResponse(String uri, HttpStatus status, FullFetchResponse data){
        MetaData metaData = new MetaData();
        metaData.setUri(uri);
        metaData.setContentType("object");
        metaData.setStatusCode(status.value());
        FullFetchResponse response = new FullFetchResponse();
        response.setMetaData(metaData);
        response.setData(data);
        return new ResponseEntity<>(response, new HttpHeaders(), status);
    }

    public static ResponseEntity<SafePaginationResponse> createSafePaginationResponse(String uri, HttpStatus status, Page<SafeFetchResponseDto> data){
        MetaData metaData = new MetaData();
        metaData.setUri(uri);
        metaData.setContentType("list");
        metaData.setStatusCode(status.value());
        PaginationData paginationData = new PaginationData(data);
        SafePaginationResponse response = new SafePaginationResponse();
        response.setMetaData(metaData);
        response.setData(data.getContent());
        response.setPagination(paginationData);
        return new ResponseEntity<>(response, new HttpHeaders(), status);
    }

    public static ResponseEntity<ErrorResponse> createSafeFetchResponse(String uri, HttpStatus status, List<String> messages){
        MetaData metaData = new MetaData();
        metaData.setUri(uri);
        metaData.setContentType("object");
        metaData.setStatusCode(status.value());
        ErrorResponse response = new ErrorResponse();
        response.setMetaData(metaData);
        response.setMessages(messages);
        return new ResponseEntity<>(response, new HttpHeaders(), status);
    }
}
