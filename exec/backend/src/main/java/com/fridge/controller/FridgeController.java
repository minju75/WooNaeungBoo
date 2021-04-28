package com.fridge.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fridge.model.Fridge;
import com.fridge.model.service.FridgeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "fridgeController v0.1")
@RestController
@RequestMapping("/fridge")
public class FridgeController {
   
   private static final String SUCCESS = "success";
   private static final String FAIL = "fail";
   
   @Autowired
   private FridgeService fridgeService;
   
   @Operation(summary = "냉장고 등록", description = "냉장고 정보를 입력하고 등록한다.")
   @PostMapping("/create")
   public ResponseEntity<Map<String, Object>> create(
         @RequestBody @Parameter(name = "냉장고 등록에 필요한 정보", required = true) Fridge fridge){
      Map<String, Object> resultMap = new HashMap<String, Object>();
      HttpStatus status = null;
      try {
         fridgeService.create(fridge);
         resultMap.put("message", SUCCESS);
         status = HttpStatus.ACCEPTED;
      } catch(Exception e) {
         resultMap.put("message", FAIL);
         status = HttpStatus.INTERNAL_SERVER_ERROR;
      }
      return new ResponseEntity<Map<String, Object>>(resultMap, status);
   }
   
   @Operation(summary = "내 냉장고 목록 보기", description = "내가 등록한 냉장고의 정보를 가져온다.")
   @GetMapping("/list/{user_id}")
   public ResponseEntity<Map<String, Object>> fridgeList(
         @PathVariable("user_id") int user_id){
      Map<String, Object> resultMap = new HashMap<String, Object>();
      HttpStatus status = null;
      try {
         System.out.println("&&&");
         Fridge [] fridgeList = fridgeService.fridgeList(user_id);
         resultMap.put("message", SUCCESS);
         resultMap.put("fridgeList", fridgeList);
         status = HttpStatus.ACCEPTED;
      }catch(Exception e) {
         resultMap.put("message", e.getMessage());
         status = HttpStatus.INTERNAL_SERVER_ERROR;
      }
      return new ResponseEntity<Map<String, Object>>(resultMap, status);
   }
   
   
}