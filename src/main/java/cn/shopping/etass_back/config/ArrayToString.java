package cn.shopping.etass_back.config;

public class ArrayToString {

        public static String formArrayToString(String[] array) {
            String str="";
            for (int i = 0; i < array.length; i++) {
                //需要区分是不是最后一个
                if(i==array.length-1){
                    str+="'" +array[i]+"'";
                }else{
                    str+="'"+array[i]+"'"+",";
                }
            }
            return str;
        }
//
//    public static void main(String[] args) {
//        String[] array={"123","456"};
//        String result=formArrayToString(array);
//        System.out.println(result);
//    }

}
