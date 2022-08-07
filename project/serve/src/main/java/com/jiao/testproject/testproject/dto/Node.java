package com.jiao.testproject.testproject.dto;

import lombok.Data;

import java.util.Set;
@Data
public class Node{
 private String nodeNo;
 private String parentNodeNo;
 private Set<Node> childNodes;
 }