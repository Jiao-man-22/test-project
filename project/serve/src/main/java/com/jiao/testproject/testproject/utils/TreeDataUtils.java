package com.jiao.testproject.testproject.utils;


import com.jiao.testproject.testproject.dto.FileViewVo;
import com.jiao.testproject.testproject.dto.FolderDto;
import com.jiao.testproject.testproject.dto.SideDtoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TreeDataUtils {
    private static final Logger log = LoggerFactory.getLogger(TreeDataUtils.class);
/*
    public List<Tree<Integer>> listToTreeByHutool(List list) {
        // 获取数据
*/
/*        List<AppMenuWithBLOBs> menuList = getMenuList();*//*

        // 构建node列表
        List<TreeNode<Integer>> nodeList = list.stream().map(menu -> {
            // 名称
            String name = getMenuFiled(menu.getBasicJson(), "name");
            // 排序
            Integer sort = 0;
            try {
                sort = Integer.valueOf(getMenuFiled(menu.getExpendJson(), "sort_number"));
            } catch (Exception e) {
                sort = 0;
            }
            // TreeNode构造方法: public TreeNode(T id, T parentId, String name, Comparable<?> weight)
            // 参数：id，父id，名称，权重（这里传的是sort）值越小越在前面
            TreeNode<Integer> treeNode = new TreeNode<Integer>(menu.getAmeId(), menu.getParentId(), name, sort);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("appMenu", menu);
            // 拓展字段，类型为Map<String, Object>，这里将整个对象传进去
            treeNode.setExtra(hashMap);
            return treeNode;
        }).collect(Collectors.toList());
        // 构造树，将前面的node列表和菜单id传入
        return TreeUtil.build(nodeList, -1);
    }
*/

    /*递归实现树*/
    public static <T> List<T> getTreeDataByIterator(List<T> folderDtoList){
        ArrayList<T> reFolderDtos = new ArrayList<>();
        for (T fd: folderDtoList) {
            //表示顶级目录
            if (fd instanceof  FolderDto){
                if (((FolderDto)fd).getParent_name().equals("")){
                    reFolderDtos.add(findChildren(fd,folderDtoList));
                }
                //表示顶级目录
                if (((FolderDto)fd).getParent_name().equals("VirtaulDisk")){
                    reFolderDtos.add(findChildren(fd,folderDtoList));
                }
            }

            if (fd instanceof  FileViewVo){
                if (((FileViewVo)fd).getName().contains("-VirtaulDisk")){
                    reFolderDtos.add(findChildren(fd,folderDtoList));
                }
            }
        }
        return reFolderDtos;
    }



    /*寻找子目录*/
    public static <T> T findChildren(T p_folder, List<T> folderDtoList) {
        for (T child : folderDtoList) {

            if (child instanceof FolderDto){
                if (((FolderDto)p_folder).getName().equals(((FolderDto)child).getParent_name())) {
                    if (((FolderDto)p_folder).getChlid_list() == null) {
                        ((FolderDto)p_folder).setChlid_list(new ArrayList<>());
                    }
                    ((FolderDto)p_folder).getChlid_list().add((FolderDto) findChildren(child, folderDtoList));
                }
            }

            if (child instanceof FileViewVo){
                if (((FileViewVo)p_folder).getName().equals(((FileViewVo)child).getParentName())) {
                    ((FileViewVo)child).setPid(((FileViewVo)p_folder).getId());
                    if (((FileViewVo)p_folder).getChildrenList() == null) {
                        ((FileViewVo)p_folder).setChildrenList(new ArrayList<>());
                    }
                    ((FileViewVo)p_folder).getChildrenList().add((FileViewVo) findChildren(child, folderDtoList));
                }
            }
        }
        return p_folder;
    }


/*    *//**
     * 获取菜单的树状结构
     *
     * @return
     *//*
    private List<FolderDto> fileTrees(List<FolderDto> filelist) {
        List<FolderDto> retreelist = new ArrayList<>();
        for (FolderDto f : filelist) {
            //获取根节点
            if (f.getParent_name().equals("")) {
                retreelist.add(f);
            }
            for (FolderDto child : filelist) {
                if (child.getParent_name().equals(f.getName())) {
                    f.addChild(child);
                }
            }
        }
        return retreelist;
    }
   菜单的实体类中加addChild方法
    public void addChild(FolderDto folderDto) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(menu);
    }




    //解析树结构
    public static List parseTreeData(List<Object> obj){
        //将树转化为Map结构 //抽象写法，先将树转为String
        String result = obj.toString();
        JSONObject jsonObject = JSONObject.parseObject(result);
        //获取Map形式的数据。
        Map<String, Object> objectMap = JSONObject.toJavaObject(jsonObject, Map.class);
        //该处需要给该树的根节点添加id和pid（例如id=UUID.randomUUID().toString()，pid=0）
        objectMap.put("pid", "0");
        objectMap.put("id", UUID.randomUUID().toString());
        //广度遍历解析树
        List<Map<String, Object>> platList = breadthFirst(objectMap);
        for (Map<String, Object> plat : platList) {
            System.out.println(plat.toString());
        }
        return platList;
    }

    /***
     * 树转化为List 广度优先遍历
     * @param root
     * @return
     */
    public static List<Map<String, Object>> breadthFirst(Map<String, Object> root) {
        List<Map<String, Object>> lists = new ArrayList<>();
        if (root == null) {
            return lists;
        }
        Queue<Map<String, Object>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Map<String, Object> tree = queue.poll();
            String pid = (String) tree.get("id");
            List<Map<String, Object>> children = (List<Map<String, Object>>) tree.get("children");
            for (Map<String, Object> child : children) {
                child.put("pid", pid);
                child.put("id", UUID.randomUUID().toString());
                queue.offer(child);
            }
            Map<String, Object> plat = new HashMap<>();
            plat.put("name", tree.get("name"));
            plat.put("pid", tree.get("pid"));
            plat.put("id", tree.get("id"));
            lists.add(plat);
        }
        return lists;
    }

    /*追加一个 id  根据名字
     * 将list集合数据重新排序 以 id 为渲染key
     * @param list<dto>
     * @return list<dto> sorted by id*/
    public static List<FileViewVo> IteratorCollectAndAddId(List<FolderDto> list){

        HashMap<String, List<FileViewVo>> nameKeyMap = new HashMap<>();

        List<FileViewVo> resultList = new ArrayList<>();

        List<String> parentNameList = list.stream().map(item -> item.getParent_name()).collect(Collectors.toList());
        //新建容器
        parentNameList.forEach(x->{
            nameKeyMap.put(x,new ArrayList<>());
        });

        parentNameList.stream().forEach((x)->{
            //按名字分成 不同的list
            list.stream().forEach(y->{
                if (y.getParent_name().equals("")){
                    FileViewVo genFileViewVo = new FileViewVo();
                    resultList.add(genFileViewVo);
                    nameKeyMap.put("root",resultList);
                }
                if (x.equals(y.getParent_name())){
                    FileViewVo fileViewVo = new FileViewVo();
                    setProperty(fileViewVo,y);
                   nameKeyMap.get(x).add(fileViewVo);
                }
            });
        });

        //追加id
        setId(nameKeyMap);
        List<List<FileViewVo>> collect = nameKeyMap.values().stream().collect(Collectors.toList());
        List<FileViewVo> fileViewVos = collect.get(0);
        //使用流合并
        Stream.of(fileViewVos,resultList).forEach(resultList::addAll);
        return  resultList;
    }

    static private void setProperty(FileViewVo fv,FolderDto fd){
        fv.setName(fd.getName());
        fv.setPath(fd.getAbsolutePath());
        fv.setRole(fd.getRole());
        fv.setShare(1);
        fv.setUpload(DateUtils.getSysTime());
    }

    static void setId(Map map){
        //非 null 判定
        if (map == null || map.isEmpty()){
            log.info("map is "+ map);
            return;
        }
        Integer id=0;
        //优先给gen目录赋id
        if (map.get("root")!= null && map.get("root") instanceof List){
            List root = (List)map.get("root");
            if (root.get(0) != null && root.get(0) instanceof FileViewVo){
               FileViewVo fv=(FileViewVo)root.get(0);
               fv.setId(++id);
            }
        }

        FileViewVo root = (FileViewVo)((List) map.get("root")).get(0);

        //遍历map
        Set<Map.Entry<String, Object>> entrySet = map.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            //第一个跳过
            if (entry.getKey().equals("root")){
                    continue;
            }
            //取出其他list 并且 给 根目录的下一个节点设置 id
//            List<FileViewVo> collect = entry.getValue() instanceof List?(List) entry.getValue():null;
            if (entry.getKey().equals(root.getName())){
                for (Object o : (List) entry.getValue()) {
                    if (o instanceof  FileViewVo){
                        ((FileViewVo) o).setId(++id);
                    }
                }
            }
            //给 FileViewVo添加id
            List<FileViewVo> value = (List<FileViewVo>)entry.getValue();
            for (FileViewVo fv:value) {
                fv.setId(++id);
            }
        }
    }

    /**
     * @param map
     * @return list
     */
    public static List<SideDtoVo> mapToTreeData(Map<String,List<SideDtoVo>> map){
        List<SideDtoVo> returnSideDtoVo = new ArrayList<>();
        SideDtoVo myFile = new SideDtoVo();
        myFile.setRole(0);
        SideDtoVo myShare = new SideDtoVo();
        myShare.setRole(0);
        SideDtoVo myRecycleBin= new SideDtoVo();
        myRecycleBin.setRole(0);
        AtomicReference<Integer> id= new AtomicReference<>(1);
        myFile.setId(id.getAndSet(id.get() + 1));
        myFile.setLabel("我的文件");
        myShare.setId(id.getAndSet(id.get() + 1));
        myShare.setLabel("我的分享");
        myRecycleBin.setId(id.getAndSet(id.get() + 1));
        myRecycleBin.setLabel("我的回收站");
        for (Map.Entry<String, List<SideDtoVo>> entry : map.entrySet()) {
            String mapKey = entry.getKey();
            if (mapKey.equals(myFile.getLabel())){
                List<SideDtoVo> mapValue = entry.getValue();
                mapValue.stream().forEach(x->{
                    x.setId(id.getAndSet(id.get() + 1));
                    x.setPid(myFile.getId());
                    x.setLabel(x.getName());
                });
                myFile.setChildren(entry.getValue());
            }
            if (mapKey.equals(myShare.getLabel())){
                List<SideDtoVo> mapValue = entry.getValue();
                mapValue.stream().forEach(x->{
                    x.setId(id.getAndSet(id.get() + 1));
                    x.setPid(myFile.getId());
                    x.setLabel(x.getName());
                });
                myShare.setChildren(entry.getValue());
            }
            if (mapKey.equals(myRecycleBin.getLabel())){
                List<SideDtoVo> mapValue = entry.getValue();
                mapValue.stream().forEach(x->{
                    x.setId(id.getAndSet(id.get() + 1));
                    x.setPid(myFile.getId());
                    x.setLabel(x.getName());
                });
                myRecycleBin.setChildren(entry.getValue());
            }
        }
        returnSideDtoVo.add(myFile);
        returnSideDtoVo.add(myShare);
        returnSideDtoVo.add(myRecycleBin);
        return returnSideDtoVo;
    }

    public static void addIdToTree(List<FileViewVo> sourceList) {

        //0 ：element-ui tree控件可能有问题
        AtomicInteger id = new AtomicInteger(1);

        //追加id
        sourceList.stream().forEach(x -> {
            if (x.getParentName().equals("VirtaulDisk")) {
                x.setId(id.getAndIncrement());
            }
            //判断父子关系id
            sourceList.stream().anyMatch(y -> {
                if (x.getParentName().equals(y.getName())) {
                    x.setId(id.getAndIncrement());
                    x.setPid(y.getId());
                    return true;
                }
                return false;
            });
        });
    }



}
