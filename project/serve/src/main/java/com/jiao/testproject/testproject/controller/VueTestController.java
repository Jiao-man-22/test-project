package com.jiao.testproject.testproject.controller;


import com.jiao.testproject.testproject.dto.AjaxResult;
import com.jiao.testproject.testproject.dto.TreeData;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName VueTestController
 * @Description TODO
 * @AUTHOR jiaorongjin
 * @Date 2022/8/4 13:03
 * @Version 1.0
 **/
//@CrossOrigin("*")
@RestController("/vue")
public class VueTestController {
    @RequestMapping("/getVueParam.json")
    public void getVueParam(){
        System.out.println("请求成功");
    }

    //菜单数据显示
    @RequestMapping("/getMenuData.json")
    public AjaxResult getMenuData(){
        List<TreeData> treeData = setDataSource();
        return AjaxResult.success(treeData);
    }

    private List<TreeData> setDataSource() {
        TreeData treeData0  = new TreeData();
        treeData0.setId("1");
        treeData0.setLabel("目录1");
        TreeData treeData1  = new TreeData();
        treeData1.setId("2");
        treeData1.setLabel("目录2");
        TreeData treeData2  = new TreeData();
        treeData2.setId("3");
        treeData2.setLabel("目录3");
        TreeData treeData3  = new TreeData();
        treeData3.setId("4");
        treeData3.setLabel("目录4");
        TreeData treeData4  = new TreeData();
        treeData4.setId("5");
        treeData4.setLabel("目录1-1");
        treeData4.setParentId("1");
        TreeData treeData5  = new TreeData();
        treeData5.setId("6");
        treeData5.setLabel("目录1-2");
        treeData5.setParentId("1");
        TreeData treeData6  = new TreeData();
        treeData6.setId("7");
        treeData6.setLabel("目录1-3");
        treeData6.setParentId("1");
        TreeData treeData7  = new TreeData();
        treeData7.setId("8");
        treeData7.setParentId("2");
        treeData7.setLabel("目录2-1");
        TreeData treeData8  = new TreeData();
        treeData8.setId("9");
        treeData8.setParentId("2");
        TreeData treeData9  = new TreeData();
        treeData9.setId("10");
        treeData9.setParentId("3");
        treeData9.setLabel("目录3-1");
        TreeData treeData10  = new TreeData();
        treeData0.setId("11");
        treeData10.setParentId("4");
        treeData10.setLabel("目录4-1");
        ArrayList<TreeData> dataList = new ArrayList<>();
        CollectionUtils.addAll(dataList,treeData0,treeData1,treeData2,treeData3,treeData4,treeData5
        ,treeData6,treeData7,treeData8,treeData9,treeData10);
        toTree(dataList);
        return dataList;
    }

    private <T extends TreeData,E extends String> void toTree(ArrayList<T> dataList) {

        HashMap<String, TreeData> stringTreeDataHashMap = new HashMap<>();
        for (T t : dataList) {
            if (t instanceof TreeData){
                String parentId =  t.getParentId();
                if (parentId == null){
                    stringTreeDataHashMap.put(t.getId(),t);
                }else{
                    findParentNode(t,stringTreeDataHashMap);
                }
            }
        }
        // 将map 里的list 拼接
        for (Map.Entry<String, TreeData> entry : stringTreeDataHashMap.entrySet()) {
            String mapKey = entry.getKey();
            TreeData treeData = entry.getValue();
            String parentId = treeData.getParentId();
            if (parentId != null && stringTreeDataHashMap.get(parentId) != null){
                TreeData treeDataParent = stringTreeDataHashMap.get(parentId);
                treeDataParent.getChildren().add(treeData);
                stringTreeDataHashMap.remove(mapKey);
            }
        }
    }

    private <T extends TreeData> void findParentNode(T t, Map<String,TreeData> map) {
        String parentId = t.getParentId();
        if (parentId!=null){
            if (map.containsKey(parentId)){
                TreeData treeData = map.get(parentId);
                treeData.getChildren().add(t);
            }else {
                map.put(t.getId(),t);
            }
        }

    }


}
