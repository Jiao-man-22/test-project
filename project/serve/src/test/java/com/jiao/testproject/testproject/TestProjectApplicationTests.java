package com.jiao.testproject.testproject;
import com.jiao.testproject.testproject.dao.impl.CustomerCrudRepository;
import com.jiao.testproject.testproject.dto.FileViewVo;
import com.jiao.testproject.testproject.dto.FolderDto;
import com.jiao.testproject.testproject.dto.Node;
import com.jiao.testproject.testproject.entity.FileEntity;
import com.jiao.testproject.testproject.test.CasTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@SpringBootTest
class TestProjectApplicationTests {
    @Resource
    CustomerCrudRepository customerCrudRepository;

    @Test
    void contextLoads() throws InstantiationException, IllegalAccessException {
/*      FileDto fileDto = new FileDto();
        FileEntity fileEntity = new FileEntity();
        fileDto.setFileid(1);
        fileDto.setFileName("file");
        fileDto.setFileSize("123");
        this.reflectField(fileDto,fileEntity);
        System.out.println(fileEntity.getFile_id());
        System.out.println(fileEntity.getFile_name());
        System.out.println(fileEntity.getFile_size());*/
        //String s = excludeString("E://file_disk//import/profile/import/2022/04/17");
        //System.out.println(getSysTime());
        //System.out.println(s);
        //openFile();
        //testStream();
        //yunsuan();
/*        Integer i=11;
        Integer i1=new Integer(11);
        Integer i2=new Integer(200);
        System.out.println(i == i1);
        System.out.println(i2 == i1);
        System.out.println(i2 == 11);*/
//        Integer integer = new Integer(100000);
//        System.out.println(i2 == 100000);
//        Integer i2= 100000;
//        System.out.println(integer == i2);
/*
        FolderDto folderDto = new FolderDto();
        FolderDto folderDto1 = new FolderDto();
        FolderDto folderDto2 = new FolderDto();
        FolderDto folderDto3 = new FolderDto();
        FolderDto folderDto4 = new FolderDto();
        FolderDto folderDto5 = new FolderDto();
        FolderDto folderDto6 = new FolderDto();*/
/*
        String x="0";
        File file = new File("E:\\VirtaulDisk\\zs_2-VirtaulDisk");
        calculateFolderLength(file,x);
        System.out.println(x);
        System.out.println(file.length());
*/
//        FileViewVo fileViewVo = new FileViewVo();
//        fileViewVo.setName("jiao");
//        this.setName(fileViewVo);
//        System.out.println(fileViewVo.getName());

//        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
//        ArrayList<Node> nodes = new ArrayList<>();
//        AtomicInteger i= new AtomicInteger(1);
//        Node nodeGen = new Node();
//        nodeGen.setNodeNo(String.valueOf(i.getAndIncrement()));
//        nodeGen.setParentNodeNo(null);
//        nodes.add(nodeGen);
//        for (int j = 0; j < 10; j++) {
//            Node node = new Node();
//            node.setNodeNo(String.valueOf(i.incrementAndGet()));
//            i.getAndDecrement();
//            node.setParentNodeNo(String.valueOf(i.getAndDecrement()));
//            nodes.add(node);
//        }
//        List<Node> nodes1 = listToTree(nodes);
//        HashSet<Object> objects = new HashSet<>();

          //testJPACrud();
        CasTest.testCas();



    }


    void reflectField(Object source, Object target) throws InstantiationException, IllegalAccessException {
        Class<?> class_s = source.getClass();
        java.lang.Class<?> class_t = target.getClass();
        Field[] field_s = class_s.getDeclaredFields();
        Field[] field_t = class_t.getDeclaredFields();
        for (int i = 0; i <= field_s.length; i++) {
            field_s[i].setAccessible(true);
            field_t[i].setAccessible(true);
            System.out.println("源对象属性值" + field_s[i].get(source));
            field_t[i].set(target, field_s[i].get(source));
        }
    }

    public static String getSysTime() {
        Date now = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd:HH:mm");
        return f.format(now);

    }

    public static String excludeString(String s){
        String rexg="profile/import/";
        String[] split = s.split(rexg);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(split[0]);
        stringBuilder.append(split[1]);
        String s1 = stringBuilder.toString();
        return s1;
    }

    public void openFile() {
        try {
           String  filePath="E:"+File.separator+"a.txt";
            File file = new File(filePath); // 创建文件对象，路径为filePath
            try {
                Desktop.getDesktop().open(file); // 启动已在本机桌面上注册的关联应用程序，打开文件对象file。
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) { // 异常处理
            System.err.println(e);
        }
    }

    public void testStream(){
        //List<String> list = Arrays.asList("a", "b", "c");
        // 创建一个顺序流
        //Stream<String> stream = list.stream();
        // 创建一个并行流
        //Stream<String> parallelStream = list.parallelStream();

/*        int[] array={1,3,5,6,8};
        IntStream stream1 = Arrays.stream(array);
        stream1.forEach(System.out::print);*/

        //List<Integer> list = Arrays.asList(7, 6, 9, 3, 8, 2, 1);
   /*       // 遍历输出符合条件的元素
        list.stream().filter(x -> x > 6).forEach(System.out::println);*/

        //Optional<Integer> findAny = list.parallelStream().filter(x -> x > 6).findAny();

        //boolean anyMatch = list.stream().anyMatch(x -> x > 6);
        //System.out.println("匹配第一个值：" + findFirst.get());
        //System.out.println("匹配任意一个值：" + findAny.get());
        //System.out.println("是否存在大于6的值：" + anyMatch);
/*        List<Person> personList = new ArrayList<Person>();
        personList.add(new Person("Tom", 8900, "male", "New York"));
        personList.add(new Person("Jack", 7000, "male", "Washington"));
        personList.add(new Person("Lily", 7800, "female", "Washington"));
        personList.add(new Person("Anni", 8200, "female", "New York"));
        personList.add(new Person("Owen", 9500, "male", "New York"));
        personList.add(new Person("Alisa", 7900, "female", "New York"));
        List<String> fiterList = personList.stream().filter(x -> x.getSalary() > 8000).map(Person::getName)
                .collect(Collectors.toList());
        System.out.print("高于8000的员工姓名：" + fiterList);*/
/*
        List<String> list = Arrays.asList("adnm", "admmt", "pot", "xbangd", "weoujgsd");
        Optional<String> max = list.stream().max(Comparator.comparing(String::length));
        System.out.println("最长的字符串：" + max.get());*/

        //List<Integer> list = Arrays.asList(7, 6, 9, 4, 11, 6);

/*        // 自然排序
        Optional<Integer> max = list.stream().max(Integer::compareTo);
        // 自定义排序
        Optional<Integer> max2 = list.stream().max(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        System.out.println("自然排序的最大值：" + max.get());
        System.out.println("自定义排序的最大值：" + max2.get());*/
  /*      List<Person> personList = new ArrayList<Person>();
        personList.add(new Person("Tom", 8900, "male", "New York"));
        personList.add(new Person("Jack", 7000, "male", "Washington"));
        personList.add(new Person("Lily", 7800, "female", "Washington"));
        personList.add(new Person("Anni", 8200,"female", "New York"));
        personList.add(new Person("Owen", 9500, "male", "New York"));
        personList.add(new Person("Alisa", 7900,  "female", "New York"));

        Optional<Person> max = personList.stream().max(Comparator.comparingInt(Person::getSalary));
        System.out.println("员工工资最大值：" + max.get().getSalary());*/
/*

        List<Integer> list = Arrays.asList(1, 3, 2, 8, 11, 4);
        // 求和方式1
        Optional<Integer> sum = list.stream().reduce((x, y) -> x + y);
        // 求和方式2
        Optional<Integer> sum2 = list.stream().reduce(Integer::sum);
        // 求和方式3
        Integer sum3 = list.stream().reduce(0, Integer::sum);

        // 求乘积
        Optional<Integer> product = list.stream().reduce((x, y) -> x * y);

        // 求最大值方式1
        Optional<Integer> max = list.stream().reduce((x, y) -> x > y ? x : y);
        // 求最大值写法2
        Integer max2 = list.stream().reduce(1, Integer::max);

        System.out.println("list求和：" + sum.get() + "," + sum2.get() + "," + sum3);
        System.out.println("list求积：" + product.get());
        System.out.println("list求和：" + max.get() + "," + max2);
*/
    }

    void yunsuan(){
        String s = Integer.toBinaryString(1024);
        int length = s.length();
        System.out.println(s);
        System.out.println(length);
    }

    public static List<FileViewVo> IteratorCollectAndAddId(List<FolderDto> list){

        list.stream().map(x->x.getParent_name()).distinct().collect(Collectors.toList()).forEach(x ->{
            list.stream().filter(y->{
                if(x.equals(y.getParent_name())) {
                    return true;
                }
                return false;
            }).collect(Collectors.toList());
        });
        return  null;
    }

    private <T> void calculateFolderLength(T folder,String x){

        if (folder instanceof  File){
            File[] files = ((File) folder).listFiles();
            for (File f: files) {
                if (f.isFile()){
                    x += f.length();
                    System.out.println(x);
                }
                if (f.isDirectory()){
                    calculateFolderLength(f,x);
                }
            }
        }
    }

    void setName(FileViewVo fvv){
        FileViewVo s =fvv;
        s.setName("rong");
        System.out.println(fvv.getName());
    }

             //数据源为sourceList  此时所有的ChildNodes里都是空的 并没有被拼成树状
    List<Node> listToTree(List <Node> sourceList){
                 //1.先准备一个数组 用于存储根节点(s)  准备一个Map用于存储节点的子元素列表

                 List<Node> rootNodes= new ArrayList<Node>();
                 Map<String, Set<Node>> childNodesMap= new HashMap<String, Set<Node>>();

                 //2.唯一的一层循环
                 for (Node node: sourceList) {
                         //3.查看Map中是否有子此Node的子元素列表 没有则创建
                         if (childNodesMap.get(node.getNodeNo()) == null) {
                                 childNodesMap.put(node.getNodeNo(), new HashSet<Node >());
                             }
                         //4.将childNodes赋值给此Node
                         node.setChildNodes(childNodesMap.get(node.getNodeNo()));
                         //5.如果此Node是根Node则放到根列表中去
                         if (node.getParentNodeNo() == null) {
                                 rootNodes.add(node);
                             }
                         if (node.getParentNodeNo() != null) {
                                  //6.查看此Node的ParentNode的 ChildNodes是否存在 没有则创建
                                 if (childNodesMap.get(node.getParentNodeNo()) == null) {
                                        childNodesMap.put(node.getParentNodeNo(), new HashSet<Node >());
                                    }
                                 //7.将此节点 添加到ParentNodes的ChildNodes中
                                 childNodesMap.get(node.getParentNodeNo()).add(node);
                             }
                     }
                 return rootNodes;
         }

    // 测试crud
    private void testJPACrud(){

        String sql="SELECT *  FROM filelist";
        List list = customerCrudRepository.sqlObjectList(sql, null, FileEntity.class);
        list.stream().forEach(x->{
            System.out.println(x);
        });

    }
}